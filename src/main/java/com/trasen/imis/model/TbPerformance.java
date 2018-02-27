package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangxiahui on 18/2/26.
 */
@Setter
@Getter
public class TbPerformance {

    private Integer pkid; //'自增主键'

    private String importDate;

    private String workNum;//'工号',

    private String name;//'姓名',

    private String score;//'分数',

    private String grade;//'级别',

    private Integer status;//'状态，0:为处理，1:已处理',

    private String created;//'创建时间',


}
