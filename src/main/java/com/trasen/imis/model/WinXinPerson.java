package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/19
 */
@Getter
@Setter
public class WinXinPerson {

    private String openId;
    private String perId; //人员ID
    private String workNum; //工号
    private String corporation; //公司
    private String name; //姓名
    private String depId; //部门ID
    private String depName; //部门名称
    private String phone; //手机号码
    private String entryDate;//入职时间
    private String regularDate;// '转正日期'
    private String position;
    private String tagName; //标签名

    private String idCard;// '身份证号'
    private String birthday;// '生日'
    private String houseAddress;// '家庭地址'
}
