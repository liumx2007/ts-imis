package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/30
 */
@Setter
@Getter
public class TbJfRank {
    private Integer pkid;
    private String name;
    private Integer score;
    private Integer prmScore;
    private String type;
    private Integer px;
}
