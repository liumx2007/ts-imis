package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: TbAttenceLocation
 * @Description: 操作类型
 * @date 2017/6/16
 */
@Getter
@Setter
public class TbAttenceLocation {


    private Integer pkid;
    private String name; // 考勤规则名称
    private String depName; //部门
    private String depId; // 部门ID
    private String tagName ; //标签名
    private String tagId; //  标签ID
    private String workingDay ; // 工作日
    private Float latitude; //  纬度
    private Float longitude ; //经度
    private Integer range; // 考勤范围
    private String address; // 考勤地址
    private Date created; // 创建时间
    private Date updated; //更新时间
    private String operator; // 操作人
    private String createUser; // 创建人
    private String province; //省份
    private String city; // 城市
    private String district; // 区
    private String outDate; // 排除日期
    private String inDate; //增加日期
    private String signinTime; //上班时间
    private String signoutTime; // 下班时间

}
