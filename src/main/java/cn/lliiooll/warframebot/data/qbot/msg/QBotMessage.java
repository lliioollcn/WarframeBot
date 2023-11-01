package cn.lliiooll.warframebot.data.qbot.msg;

public abstract class QBotMessage {
    public abstract String getType();

    public abstract Object content();
}
