package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/20
 */
@Getter
@Setter
public class TbBlackList {
    private Integer pkid;
    private String name;
    private Integer type;
    private String remark;
    private Integer isVaild;
}
