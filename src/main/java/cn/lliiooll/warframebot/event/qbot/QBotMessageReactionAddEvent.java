package cn.lliiooll.warframebot.event.qbot;

import cn.lliiooll.warframebot.data.qbot.QBotAtMessage;
import cn.lliiooll.warframebot.data.qbot.QBotSendMessageResp;
import cn.lliiooll.warframebot.data.qbot.msg.QBotMessage;
import cn.lliiooll.warframebot.data.qbot.msg.QBotReplyMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QBotMessageReactionAddEvent extends QBotEvent {
    private QBotAtMessage message;

}
