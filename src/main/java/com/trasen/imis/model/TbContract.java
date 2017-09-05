package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/18
 */
@Getter
@Setter
public class TbContract {
    private Integer pkid;
    private String name; //'姓名',
    private String workNum;// '工号'
    private String conCode;// '合同编号'
    private String entryDate;// '入职时间'
    private String regularDate;// '转正日期'
    private Integer status;// '合同签订情况，1:已签；0:未签'
    private String startDate;// '合同开始时间'
    private String endDate;// '合同结束时间'
    private Integer years;// '合同年限'
    private Integer type;//  '合同类型'
    private Integer conNum;// '合同份数'
    private Integer xyNum;// '协议份数'
    private String conFile;// '合同附件'
    private String remark;// '备注'
    private Date updated;// '更新时间'
    private String operator;// '操作人'
    private Integer isVaild; //是否有效 1:有效，0：无效
    private String signDate; //合同签订时间
}
