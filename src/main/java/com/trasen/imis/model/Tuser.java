package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/28
 */
@Getter
@Setter
public class Tuser {
    private Integer pkid;
    private String name;
    private String password;
    private Integer status;
    private Integer permission;
    private String appId;
    private String displayName;
    private String perId;
    private Date created;
    private Date updated;
}
