package com.trasen.imis.model.resp;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class TextMessage extends BaseMessage {
    private String Content;
    /**
     * @return the content
     */
    public String getContent() {
        return Content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String Content) {
        this.Content = Content;
    }
}