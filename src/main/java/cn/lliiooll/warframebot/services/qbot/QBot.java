package cn.lliiooll.warframebot.services.qbot;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.lliiooll.warframebot.data.qbot.QBotSendMessageResp;
import cn.lliiooll.warframebot.data.qbot.msg.QBotMessage;
import cn.lliiooll.warframebot.data.qbot.msg.QBotReplyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Builder
@AllArgsConstructor
public class QBot {

    public static final String SANDBOX = "https://sandbox.api.sgroup.qq.com";
    public static final String RELEASE = "https://api.sgroup.qq.com";
    public static final boolean debug = true;

    @Getter
    private String id;
    @Getter
    private String token;
    @Getter
    private String secret;
    private QBotWebSocketClient client;

    public String getWssUrl() {
        return JSONUtil.parseObj(execute(QBotApi.GATEWAY, null)).getStr("url", "");
    }

    public void handlerMessage(QBotWebSocketHandler handler) {
        if (this.client == null) this.client = QBotWebSocketClient.create(getWssUrl(), this);
        this.client.handler(handler);
    }


    public String getAuthorization() {
        return String.format("Bot %s.%s", getId(), getToken());
    }

    @SneakyThrows
    public QBotSendMessageResp sendMessage(String channelId, String eventId, List<QBotMessage> messages) {
        return new ObjectMapper().readValue(execute(String.format(QBotApi.SEND_MESSAGE, channelId), new HashMap<>() {{
            //put("event_id", eventId);
            put("msg_id", eventId);
            messages.forEach(msg -> {
                put(msg.getType(), msg.content());
            });

        }}), QBotSendMessageResp.class);
    }

    private String execute(String url, Object body) {
        if (debug) url = SANDBOX + url;
        else url = RELEASE + url;
        HttpRequest req;
        if (body != null) {
            req = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(body));
        } else {
            req = HttpUtil.createGet(url);
        }
        String jstr = req.header("Authorization", getAuthorization())
                .contentType("application/json")
                .execute()
                .body();
        log.info("http: {}", jstr);
        if (JSONUtil.isTypeJSON(jstr)) {
            return jstr;
        }
        return "{}";
    }


    static class QBotApi {
        static String GATEWAY = "/gateway";
        static String SEND_MESSAGE = "/channels/%s/messages";

    }

}
