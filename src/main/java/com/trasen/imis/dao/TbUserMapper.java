package com.trasen.imis.dao;

import com.trasen.imis.model.TbUser;

/**
 * Created by zhangxiahui on 17/7/27.
 */
public interface TbUserMapper {

    TbUser getUser(String name);

    int updatePassword(TbUser tbUser);
}
