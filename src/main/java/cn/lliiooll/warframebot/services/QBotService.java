package cn.lliiooll.warframebot.services;

import cn.hutool.json.JSONObject;
import cn.lliiooll.warframebot.services.qbot.QBot;
import cn.lliiooll.warframebot.services.qbot.QBotWebSocketHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QBotService {
    @PostConstruct
    public void init() {
        // 从q.qq.com获取
        QBot bot = QBot.builder()
                .id("id")
                .token("token")
                .secret("secret")
                .build();
        bot.handlerMessage(json -> {

        });
    }
}
