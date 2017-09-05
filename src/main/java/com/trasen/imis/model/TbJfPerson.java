package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/29
 */
@Setter
@Getter
public class TbJfPerson {
    private String pkid;
    private String workNum;
    private Integer score; //积分
    private Integer rank; //职级
    private String rankName;//职级名称
    private Date created;
    private String operator;
    private Date updated;
    private String name;
    private String openId;
    private String depId;
    private String depName;
    private String tagCode;

    private String company;//所属公司
    private Integer nextRank;
    private String nextRankName;//下一等级职称
    private Integer px;//排序
    private Integer prmScore;//晋级积分

    private String status;
}
