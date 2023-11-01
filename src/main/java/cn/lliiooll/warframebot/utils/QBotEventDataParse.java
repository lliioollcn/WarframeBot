package cn.lliiooll.warframebot.utils;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.lliiooll.warframebot.data.qbot.QBotAtMessage;
import cn.lliiooll.warframebot.event.qbot.QBotAtMessageEvent;
import cn.lliiooll.warframebot.event.qbot.QBotEvent;
import cn.lliiooll.warframebot.services.qbot.QBot;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class QBotEventDataParse {

    @SneakyThrows
    public static QBotEvent parse(QBot bot, String type, String id, JSONObject data) {
        if (type.equalsIgnoreCase("AT_MESSAGE_CREATE")) {
            QBotAtMessageEvent event = new QBotAtMessageEvent();
            event.setBot(bot);
            event.setId(id);
            event.setType(type);
            event.setMessage(new ObjectMapper().readValue(data.toString(), QBotAtMessage.class));
            return event;
        }
        return QBotEvent.builder().build();
    }
}
