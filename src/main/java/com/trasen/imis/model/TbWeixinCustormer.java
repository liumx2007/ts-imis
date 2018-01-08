package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2018/1/8
 */
@Getter
@Setter
public class TbWeixinCustormer {
    /**
     * 主键
     */
    private Integer pkid;

    /**
     * 微信公众号OpenID
     */
    private String openId;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户地址
     */
    private String address;

    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 操作人
     */
    private String operator;

    private Integer isVaild;
}
