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
public class TbDept {
    private String depId;   //部门ID
    private String depName; //部门名称
    private String parentDepId; //上级部门ID
    private String parentDepName; //上级部门名称
    private Integer depLevel; //部门级别
    private String remark;   //备注
    private Date created;   //创建时间
    private Integer isVaild; // 是否有效，1:有效；2:无效
    private String operator; //操作人
}
