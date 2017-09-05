package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/7/19.
 */
@Getter
@Setter
public class TbWorkHistory {

    private Integer pkid;// '主键'
    private String startDate;// '开始日期'
    private String endDate;// '结束日期'
    private String company;// '公司'
    private String dept;// '部门'
    private String post;// '职务'
    private Integer type;// '类型，1:公司员工；2:人才库'
    private String perId;// '关联人ID'
    private Date created;
    private Date updated;
    private String operator;
}
