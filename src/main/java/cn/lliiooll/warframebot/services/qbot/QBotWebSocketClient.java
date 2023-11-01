package cn.lliiooll.warframebot.services.qbot;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.lliiooll.warframebot.event.QBotEventBus;
import cn.lliiooll.warframebot.utils.QBotEventDataParse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class QBotWebSocketClient extends ChannelInitializer<SocketChannel> {

    private final String url;
    private final QBot bot;
    private boolean errorReconnect = false;
    private String sessionId = null;

    private int lastS = -1;
    private List<QBotWebSocketHandler> handlers = new ArrayList<>();
    private Bootstrap client;
    private Channel channel;
    private NioEventLoopGroup group;


    public QBotWebSocketClient(String url, QBot bot) {
        this.bot = bot;
        this.url = url;
        this.init();
    }

    @SneakyThrows
    private void init() {
        this.group = new NioEventLoopGroup(4);

        this.client = new Bootstrap();
        URI u = new URI(url);
        log.info("尝试连接到{}", u.getHost());
        this.channel = this.client.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler())
                .channel(NioSocketChannel.class)
                .handler(this)
                .connect(u.getHost(), 443)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        HttpHeaders headers = new DefaultHttpHeaders();
                        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(u, WebSocketVersion.V13, null, true, headers);
                        QBotWebSocketClientHandler clientHandler = (QBotWebSocketClientHandler) channel.pipeline().get("websocketHandler");
                        handshaker.handshake(future.channel());
                        clientHandler.setHandshaker(handshaker);

                    }
                })
                .channel();

    }

    private void reconnect() {
        log.info("尝试重连，是否错误: {}", errorReconnect);
        this.group.shutdownGracefully();
        this.channel.close();
        if (!errorReconnect) {
            sessionId = null;
            lastS = -1;
        }
        init();
    }

    public static QBotWebSocketClient create(String url, QBot bot) {
        return new QBotWebSocketClient(url, bot);
    }


    public void handler(QBotWebSocketHandler handler) {
        handlers.add(handler);
    }

    private long lastTime = -1;
    private long heartBeat = -1;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build()
                        .newHandler(ch.alloc()))
                .addLast(new IdleStateHandler(0, 5, 0))
                .addLast(new HttpClientCodec(), new HttpObjectAggregator(1024 * 1024 * 10))
                .addLast("websocketHandler", new QBotWebSocketClientHandler(this));
    }

    public static class QBotWebSocketClientHandler extends SimpleChannelInboundHandler {

        private final QBotWebSocketClient client;
        private WebSocketClientHandshaker handshaker;


        public QBotWebSocketClientHandler(QBotWebSocketClient client) {
            this.client = client;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!handshaker.isHandshakeComplete()) {
                handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
                if (handshaker.isHandshakeComplete()) {
                    log.info("握手完毕");
                    if (client.errorReconnect) {
                        if (client.sessionId != null) {
                            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(new HashMap<>() {{
                                put("op", 6);
                                put("d", new HashMap<>() {{
                                    put("token", client.bot.getAuthorization());
                                    put("session_id", client.sessionId);
                                    put("seq", client.lastS);
                                }});
                            }})));
                        }
                    } else {
                        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(new HashMap<>() {{
                            put("op", 2);
                            put("d", new HashMap<>() {{
                                put("token", client.bot.getAuthorization());
                                put("intents", 1 << 0 | 1 << 1 | 1 << 10 | 1 << 12 | 1 << 18 | 1 << 19 | 1 << 26 | 1 << 27 | 1 << 29 | 1 << 30)
                                ;
                            }});
                        }})));
                    }
                    client.errorReconnect = false;
                    client.watchDog();
                }
            } else if (msg instanceof WebSocketFrame frame) {
                if (frame instanceof TextWebSocketFrame tFrame) {
                    log.info("收到数据: {}", tFrame.text());
                    JSONObject json = JSONUtil.parseObj(tFrame.text());
                    int op = json.getInt("op");

                    if (json.getInt("s", -1) > 0) {
                        client.lastS = json.getInt("s");
                    }
                    if (op == 10) {
                        JSONObject data = json.getJSONObject("d");
                        client.heartBeat = data.getLong("heartbeat_interval");
                    } else if (op == 11) {
                        log.info("心跳成功");
                    } else if (op == 7) {
                        log.info("服务器要求重连");
                        client.reconnect();
                    } else if (op == 0) {
                        String type = json.getStr("t");
                        String id = json.getStr("id");
                        if (type.equalsIgnoreCase("READY")) {
                            JSONObject data = json.getJSONObject("d");
                            client.sessionId = data.getStr("session_id");
                        } else if (type.equalsIgnoreCase("RESUMED")) {
                            client.errorReconnect = false;
                            log.info("重连成功");
                        } else {
                            JSONObject data = json.getJSONObject("d");
                            QBotEventBus.post(QBotEventDataParse.parse(client.bot, type, id, data));
                        }

                    } else {
                        log.info("暂未支持的操作: {}", op);
                    }

                }
            }
        }

        public void setHandshaker(WebSocketClientHandshaker handshaker) {
            this.handshaker = handshaker;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            client.errorReconnect = true;
            client.reconnect();
        }
    }

    private boolean watchdog = false;

    @SneakyThrows
    private void watchDog() {
        if (watchdog) return;
        Thread.sleep(1000L);
        ThreadUtil.execute(() -> {
            try {
                while (true) {
                    if (channel.isActive()) {
                        if (heartBeat > 0 && System.currentTimeMillis() - lastTime >= heartBeat) {
                            log.info("心跳");
                            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(new HashMap<>() {{
                                put("op", 1);
                                put("d", lastS > 0 ? lastS : null);
                            }})));
                            lastTime = System.currentTimeMillis();
                        }
                    } else {
                        errorReconnect = true;
                        reconnect();
                        throw new RuntimeException();
                    }
                    Thread.sleep(1000L);
                }
            } catch (Throwable e) {
                watchdog = false;
                watchDog();

            }
        });
        watchdog = true;
    }


}
