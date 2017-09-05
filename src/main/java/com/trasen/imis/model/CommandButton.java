package com.trasen.imis.model;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class CommandButton extends Button{
    private String type;
    private String key;
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }
    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }


}
