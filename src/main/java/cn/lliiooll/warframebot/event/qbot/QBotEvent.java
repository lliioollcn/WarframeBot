package cn.lliiooll.warframebot.event.qbot;

import cn.lliiooll.warframebot.services.qbot.QBot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QBotEvent{
    private String type;
    private String id;
    private QBot bot;
}
