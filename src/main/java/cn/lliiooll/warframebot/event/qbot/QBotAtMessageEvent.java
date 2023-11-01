package cn.lliiooll.warframebot.event.qbot;

import cn.lliiooll.warframebot.data.qbot.QBotAtMessage;
import cn.lliiooll.warframebot.data.qbot.QBotSendMessageResp;
import cn.lliiooll.warframebot.data.qbot.msg.QBotMessage;
import cn.lliiooll.warframebot.data.qbot.msg.QBotReplyMessage;
import cn.lliiooll.warframebot.services.qbot.QBot;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QBotAtMessageEvent extends QBotEvent {
    private QBotAtMessage message;

    public QBotSendMessageResp reply(QBotMessage message) {
        return reply(new ArrayList<>() {{
            add(message);
        }});
    }

    public QBotSendMessageResp reply(List<QBotMessage> messages) {
        messages.add(new QBotReplyMessage(message.getId()));
        return send(messages);
    }

    public QBotSendMessageResp send(QBotMessage message) {
        return send(new ArrayList<>() {{
            add(message);
        }});
    }

    public QBotSendMessageResp send(List<QBotMessage> messages) {
        return getBot().sendMessage(message.getChannelId(), message.getId(), messages);
    }
}
