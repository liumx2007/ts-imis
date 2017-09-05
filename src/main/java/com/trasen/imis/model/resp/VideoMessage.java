package com.trasen.imis.model.resp;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class VideoMessage extends BaseMessage {
    /**
     * 通过上传多媒体文件，得到的id
     */
    private String MediaId;
    /**
     * 视频消息的标题
     */
    private String Title;
    /**
     * 视频消息的描述
     */
    private String Description;
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
    /**
     * @return the title
     */
    public String getTitle() {
        return Title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        Title = title;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return Description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        Description = description;
    }
}