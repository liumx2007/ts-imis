package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhangxiahui on 17/7/5.
 */
@Getter
@Setter
public class TreeVo {

    private String label;

    private List<TreeVo> children;

    private Object data;
}
