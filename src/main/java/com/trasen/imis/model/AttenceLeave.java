package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/4
 */
@Getter
@Setter
public class AttenceLeave {
    private int pkid; //主键
    private String name; //姓名
    private String tagName; //部门
    private String tagId; //部门ID
    private String workNum; //工号
    private String position; //职务
    private String attenceDate;   //请假日
    private int type;            //请假类型（参考数?
    private String startTime;   //开始时间
    private String endTime;    //结束时间
    private String remark;    //备注
    private int leaveHours;  //请假时长
    private Date created;   //创建时间
    private Date updated;   //更新时间
    private String operator; //操作人
    private String createUser; //创建人
    private Long attId;
    private Integer attType;

}
