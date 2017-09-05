package com.trasen.imis.model.resp;

import java.io.Serializable;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class BaseMessage implements Serializable {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType;
    private Long MsgId;
    /**
     * @return the toUserName
     */
    public String getToUserName() {
        return ToUserName;
    }
    /**
     * @param toUserName the toUserName to set
     */
    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }
    /**
     * @return the fromUserName
     */
    public String getFromUserName() {
        return FromUserName;
    }
    /**
     * @param fromUserName the fromUserName to set
     */
    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }
    /**
     * @return the createTime
     */
    public Long getCreateTime() {
        return CreateTime;
    }
    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }
    /**
     * @return the msgType
     */
    public String getMsgType() {
        return MsgType;
    }
    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(String msgType) {
        MsgType = msgType;
    }
    /**
     * @return the msgId
     */
    public Long getMsgId() {
        return MsgId;
    }
    /**
     * @param msgId the msgId to set
     */
    public void setMsgId(Long msgId) {
        MsgId = msgId;
    }
}