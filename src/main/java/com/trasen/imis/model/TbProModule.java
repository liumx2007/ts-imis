package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/22
 */
@Getter
@Setter
public class TbProModule {
    private String modId;
    private String modName;//模块名
    private String modNo;//模块编号
    private String proCode;//产品编号
    private String version;//版本
    private Integer isVaild;//是否有效，1:有效；0:无效
    private Date created;//创建时间
}
