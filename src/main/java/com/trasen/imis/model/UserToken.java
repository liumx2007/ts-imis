package com.trasen.imis.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangxiahui on 17/6/20.
 */
@Setter
@Getter
public class UserToken {
    /**
     * 获取到的凭证
     * */
    private String accessToken;

    /**
     * 凭证有效时间，单位：秒
     * */
    private Integer expiresIn;

    private String openid;

    private String scope;

    private String refreshToken;

}
