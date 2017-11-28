package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/11/28.
 */
@Getter
@Setter
public class AttenceDetailVo {

    private String attenceDate;

    private String week;

    private String remark;

    private Date signinTime;

    private String signinAddress;


}
