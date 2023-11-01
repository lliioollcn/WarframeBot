package cn.lliiooll.warframebot.data.qbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QBotSendMessageResp {


    @JsonProperty("id")
    private String id;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("guild_id")
    private String guildId;
    @JsonProperty("content")
    private String content;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("tts")
    private Boolean tts;
    @JsonProperty("mention_everyone")
    private Boolean mentionEveryone;
    @JsonProperty("author")
    private AuthorDTO author;
    @JsonProperty("pinned")
    private Boolean pinned;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("flags")
    private Integer flags;
    @JsonProperty("seq_in_channel")
    private String seqInChannel;

    @NoArgsConstructor
    @Data
    public static class AuthorDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("username")
        private String username;
        @JsonProperty("avatar")
        private String avatar;
        @JsonProperty("bot")
        private Boolean bot;
    }
}
