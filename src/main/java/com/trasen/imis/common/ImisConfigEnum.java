package com.trasen.imis.common;

/**
 * Created by zhangxiahui on 17/7/6.
 */

public enum ImisConfigEnum {

    /*** 考勤类型 */
    ATTENCE_LACK("0", "旷工");




    private String propGroup;
    private String propName;

    private ImisConfigEnum(final String propGroup,final String propName){
        this.propGroup = propGroup;
        this.propName = propName;
    }


    public String getPropGroup() {
        return propGroup;
    }

    public void setPropGroup(String propGroup) {
        this.propGroup = propGroup;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public static ImisConfigEnum valueOfIgnoreCase(final String propGroup, final String propName) {
        final ImisConfigEnum[] es = ImisConfigEnum.class.getEnumConstants();
        for (final ImisConfigEnum e : es) {
            if (e.getPropGroup().equalsIgnoreCase(propGroup) &&
                    e.getPropName().equalsIgnoreCase(propName)) {
                return e;
            }
        }
        return null;
    }


    public static String getName(String propGroup) {
        for (ImisConfigEnum c : ImisConfigEnum.values()) {
            if (c.getPropGroup().equalsIgnoreCase(propGroup)) {
                return c.getPropName();
            }
        }
        return null;
    }


}
