package cn.lliiooll.warframebot.listener;

import cn.lliiooll.warframebot.data.qbot.QBotAtMessage;
import cn.lliiooll.warframebot.data.qbot.msg.QBotImageMessage;
import cn.lliiooll.warframebot.data.qbot.msg.QBotTextMessage;
import cn.lliiooll.warframebot.event.QBotEventBus;
import cn.lliiooll.warframebot.event.qbot.QBotAtMessageEvent;
import cn.lliiooll.warframebot.event.qbot.QBotEvent;
import com.google.common.eventbus.Subscribe;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
public class QBotMessageListener {

    @PostConstruct
    public void init(){
        QBotEventBus.register(this);
    }

    @Subscribe
    public void onMessage(QBotAtMessageEvent event){
        log.info("收到了消息: {}",event.getMessage().getContent());
        event.reply(new ArrayList<>(){{
            add(new QBotTextMessage("").at(event.getMessage().getAuthor().getId()).append("收到了").emoji("277"));
            add(new QBotImageMessage("https://q.qlogo.cn/openurl/3303657276/3303657276/100?rf=qz_hybrid&c=cXpfaHliacmlkQDMzMDM2NTcyNzY"));
        }});
    }
}
