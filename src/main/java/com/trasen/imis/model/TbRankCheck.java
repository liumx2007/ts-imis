package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/30
 */
@Getter
@Setter
public class TbRankCheck {
    private Integer pkid;
    private String workNum;//工号
    private Integer status;//状态，1:晋级申请，2:审批通过，3:审批不通过，4:直接晋级，0:未申请
    private String remark;//备注
    private Integer oldRank;//老职级
    private Integer newRank;//新职级
    private Date created;//创建时间
    private Date updated;//审批时间
    private String operator;//操作人、审核人

    private String name;
    private String depId;
    private String depName;
    private String tagCode;
    private String company;//所属公司
    private String rankName;
    private String score;
}
