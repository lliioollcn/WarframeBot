package cn.lliiooll.warframebot.event;

import cn.lliiooll.warframebot.event.qbot.QBotEvent;
import com.google.common.eventbus.EventBus;

public class QBotEventBus {

    private static final EventBus globalBus = new EventBus("GlobalBotBus");

    public static void post(QBotEvent event) {
        globalBus.post(event);
    }

    public static void register(Object listener) {
        globalBus.register(listener);
    }
}
