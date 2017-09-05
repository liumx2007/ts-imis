package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangxiahui on 17/7/5.
 */
@Getter
@Setter
public class TbTree {
    private String pkid;
    private String parent;
    private String name;
    private String code;
    private Integer level;
    private String type;
    private Integer  is_final;
}
