package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/7/18.
 */
@Getter
@Setter
public class TbTalentPool {

    private Integer pkid;// '主键'
    private String name;// '姓名'
    private Integer sex;// '性别，1:男；2:女'
    private String phone;// '联系方式'
    private String workDate;// '就业年限'
    private String willJob;// '意向岗位'
    private String beGood;// '擅长技术'
    private String school;// '毕业学校'
    private String education;// '学历'
    private String certificate;// '相关证书'
    private String resumeFile;// '简历附件'
    private Integer isCome;// '是否来过公司'
    private String result;// '面试结果'
    private String reserve;// '储备岗位'
    private String remark;// '备注'
    private Date created;// '创建时间'
    private String operator;// '操作人'
    private Date updated;

}
