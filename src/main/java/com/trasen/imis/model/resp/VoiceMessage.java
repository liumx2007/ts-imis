package com.trasen.imis.model.resp;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class VoiceMessage extends BaseMessage {
    /**
     * 通过上传多媒体文件，得到的id
     */
    private String MediaId;
    /**
     * @return the mediaId
     */
    public String getMediaId() {
        return MediaId;
    }
    /**
     * @param mediaId the mediaId to set
     */
    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}