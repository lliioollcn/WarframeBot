package cn.lliiooll.warframebot.data.qbot.msg;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
public class QBotReplyMessage extends QBotMessage {

    private String id;

    @Override
    public String getType() {
        return "message_reference";
    }

    @Override
    public Object content() {
        return new HashMap<>() {{
            put("message_id", id);
        }};
    }
}
