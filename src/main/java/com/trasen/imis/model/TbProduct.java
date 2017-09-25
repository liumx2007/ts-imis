package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/22
 */
@Getter
@Setter
public class TbProduct {
    private Integer pkid;
    private String name;
    private String no;
    private String depId;
    private Integer code;
    private Date created;
    private Integer version;
    private Integer isLatest;
}
