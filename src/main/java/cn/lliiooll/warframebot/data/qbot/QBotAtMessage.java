package cn.lliiooll.warframebot.data.qbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class QBotAtMessage {

    @JsonProperty("author")
    private AuthorDTO author;
    @JsonProperty("channel_id")
    private String channelId;
    @JsonProperty("content")
    private String content;
    @JsonProperty("guild_id")
    private String guildId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("member")
    private MemberDTO member;
    @JsonProperty("mentions")
    private List<MentionsDTO> mentions;
    @JsonProperty("seq")
    private Integer seq;
    @JsonProperty("seq_in_channel")
    private String seqInChannel;
    @JsonProperty("timestamp")
    private String timestamp;

    @NoArgsConstructor
    @Data
    public static class AuthorDTO {
        @JsonProperty("avatar")
        private String avatar;
        @JsonProperty("bot")
        private Boolean bot;
        @JsonProperty("id")
        private String id;
        @JsonProperty("username")
        private String username;
    }

    @NoArgsConstructor
    @Data
    public static class MemberDTO {
        @JsonProperty("joined_at")
        private String joinedAt;
        @JsonProperty("nick")
        private String nick;
        @JsonProperty("roles")
        private List<String> roles;
    }

    @NoArgsConstructor
    @Data
    public static class MentionsDTO {
        @JsonProperty("avatar")
        private String avatar;
        @JsonProperty("bot")
        private Boolean bot;
        @JsonProperty("id")
        private String id;
        @JsonProperty("username")
        private String username;
    }
}
