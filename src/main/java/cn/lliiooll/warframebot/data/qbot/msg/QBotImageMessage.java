package cn.lliiooll.warframebot.data.qbot.msg;

import cn.lliiooll.warframebot.utils.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor
public class QBotImageMessage extends QBotMessage {

    private String url;

    public QBotImageMessage(String url) {
        this.url = url;
    }

    public QBotImageMessage(File imageFile) {
        this.url = ImageUtils.upload(imageFile);
    }

    @Override
    public String getType() {
        return "image";
    }

    @Override
    public Object content() {
        return url;
    }
}
