package cn.lliiooll.warframebot.data.qbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class QBotMessageReaction {


    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("emoji")
    private EmojiDTO emoji;
    @JsonProperty("guild_id")
    private String guildId;
    @JsonProperty("target")
    private TargetDTO target;
    @JsonProperty("user_id")
    private String userId;

    @NoArgsConstructor
    @Data
    public static class EmojiDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("type")
        private Integer type;
    }

    @NoArgsConstructor
    @Data
    public static class TargetDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("type")
        private String type;
    }
}
