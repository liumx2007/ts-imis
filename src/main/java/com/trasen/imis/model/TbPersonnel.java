package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/6
 */

@Getter
@Setter
public class TbPersonnel {
    private String perId; //人员ID
    private String workNum; //工号
    private String name; //姓名
    private Integer sex; //性别，1:男；2:女
    private String depId; //部门ID
    private String depName; //部门名称
    private String phone; //手机号码
    private Date created; //创建时间
    private Integer isVaild; //是否有效，1:有效；2:无效
    private String operator; //操作人
    private String tagCode;
    private String position;
    private Date updated;
    private String entryDate;//入职时间
    private String picture;


    private String rank;
    private Integer reEntry;//'是否重复入职 1:是；0:否'
    private String regularDate;// '转正日期'
    private String quitDate;// '离职日期'
    private Integer trySalary;// '试用期工资'
    private Integer salary;// '薪资'
    private String salaryRemark;// '薪资福利说明'
    private String shbDate;// '社保购买时间'
    private String gjjDate;// '公积金购买时间'

    private String idCard;// '身份证号'
    private String birthday;// '生日'
    private Integer birthdayType;// '生日类型 1:阳历；2:农历 '
    private String householdRegiste;// '户籍'
    private String houseAddress;// '家庭地址'
    private String province;// '省'
    private String city;// '市'
    private String district;// '区'
    private String school;// '学校'
    private String major;//'专业'
    private String education;// '学历'
    private String graduateDate;// '毕业时间'
    private String linkman;// '紧急联系人'
    private String linkPhone;// '联系人电话'
    private String remark;// '备注'

    private String schoolFile; //毕业证明
    private String idCardFile; //身份证附件
    private String separationFile; //离职证明

    private String openId;




}
