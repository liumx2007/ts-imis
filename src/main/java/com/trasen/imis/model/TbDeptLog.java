package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by zhangxiahui on 17/7/19.
 */
@Getter
@Setter
public class TbDeptLog {
    private Integer pkid;// '主键'
    private String oldDepId;// '原部门ID'
    private String oldDept;// '原部门'
    private String oldPosition;// '原岗位'
    private String newDepId;// '新部门ID'
    private String newDept;// '新部门'
    private String newPosition;// '新岗位'
    private String changeDate;// '调岗日期'
    private String workNum;// '工号'
    private String perId;// '人员ID'
    private String exeDate;// '执行时间'
    private Date created;// '创建时间'
    private String operator;// '操作人'
}
