package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/30
 */
@Setter
@Getter
public class TbJfRecord {
    private Integer pkid;
    private String workNum;//工号
    private Integer score;//积分
    private Integer type;//类型，1:人事加分，2:部门经理加分申请，3:系统自动加分
    private Integer status;//状态，1:有效分值，0:待确认分值
    private String remark;//加分备注
    private String createUser;//创建人
    private Date created;//创建时间
    private String operator;//操作人、审核人
    private Date updated;//更新时间
    private Integer isShow;//是否在微信端展示，1:展示，0:不展示
    private String checkRemark;//审核备注

    private String name;//姓名
    private String company;//公司
    private String depName;
    private Integer currentScore;//当前积分
    private String tagCode;

    private List<String> workNums;
}
