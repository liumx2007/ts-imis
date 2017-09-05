package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/7/6.
 */
@Getter
@Setter
public class TbAttenceCount {

    private Long pkid;//'主键'

    private String name;// '姓名',

    private String workNum;

    private String tagName;// '部门',

    private String tagId;// '部门标签',

    private int shouldSignNum;//'应签到天数',

    private int factSignNum;//'实际签到天数',

    private int attNum;// '正常出勤',

    private int lateNum;// '迟到天数',

    private int backNum;// '早退天数',

    private int lackNum;// '旷工',

    private int annualLeave;//'年假',

    private int maternityLeave;// '产假',

    private int peternituLeave;// '陪产假',

    private int maritalLeave;// '婚假',

    private int funeralLeave;// '丧假',

    private int affairLeave;// '事假',

    private int sickLeave;// '病假',

    private int otherLeave;// '其他假',

    private String countDate;

    private Date created;

    private Date updated;

}
