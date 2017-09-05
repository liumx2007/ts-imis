package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/6/21.
 */
@Getter
@Setter
public class AttenceVo {

    private Integer pkid;

    private String name;

    private String tagName;

    private String workNum;

    private String tagId;

    private String position;

    private String attenceDate;

    private Date signinTime;

    private Date signoutTime;

    private Integer workHours;

    private Integer type;

    private String remark;

    private String week;

    private String openId;

    private String operator;

    private Long lateTime;

    private Long backTime;

    private String signinAddress;

    private String signoutAddress;

    //请假表字段
    private String startTime;   //开始时间
    private String endTime;    //结束时间
    private int leaveHours;  //请假时长

    private Integer attType;

    private String signinType;

    private String createUser;

}
