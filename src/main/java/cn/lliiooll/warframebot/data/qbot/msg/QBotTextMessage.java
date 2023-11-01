package cn.lliiooll.warframebot.data.qbot.msg;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class QBotTextMessage extends QBotMessage {

    private StringBuilder content;

    public QBotTextMessage(String content) {
        this.content = new StringBuilder(content);
    }

    @Override
    public String getType() {
        return "content";
    }

    @Override
    public Object content() {
        return content.toString();
    }

    public QBotTextMessage at(String userId) {
        return append(String.format("<@%s>", userId));
    }

    public QBotTextMessage atAll() {
        return append("@everyone");
    }

    public QBotTextMessage channel(String channelId) {
        return append(String.format("<#%s>", channelId));
    }

    public QBotTextMessage emoji(String id) {
        return append(String.format("<emoji:%s>", id));
    }

    public QBotTextMessage append(String text) {
        content.append(text);
        return this;
    }
}
