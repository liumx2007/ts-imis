package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/27
 */
@Getter
@Setter
public class TbHoliday {
    private Integer pkid;
    private String yearMonth;
    private String workDay;
    private String holiday;
}
