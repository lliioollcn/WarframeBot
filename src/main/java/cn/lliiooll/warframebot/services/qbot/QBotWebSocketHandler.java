package cn.lliiooll.warframebot.services.qbot;

import cn.hutool.json.JSONObject;

@FunctionalInterface
public interface QBotWebSocketHandler {

    void onMessage(JSONObject json);
}
