package cn.lliiooll.warframebot.data.qbot.msg;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class QBotMarkdownMessage extends QBotMessage {

    private String content;

    @Override
    public String getType() {
        return "markdown";
    }

    @Override
    public Object content() {
        return content;
    }
}
