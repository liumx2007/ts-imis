package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/6/21.
 */
@Getter
@Setter
public class AttenceLogVo {

    private String openId;

    private Float longitude;

    private Float latitude;

    private Float accuracy;

    private Date attenceTime;

    private String attenceDate;

    private String attenceWeek;

    private String type;

    private String name;

    private String workNum;

    private String remark;

    private String address;

    private String tagId;
}
