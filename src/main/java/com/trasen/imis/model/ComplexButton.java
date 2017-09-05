package com.trasen.imis.model;

import java.util.List;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class ComplexButton extends Button{
    private List<Button> sub_button;


    public List<Button> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<Button> sub_button) {
        this.sub_button = sub_button;
    }
}