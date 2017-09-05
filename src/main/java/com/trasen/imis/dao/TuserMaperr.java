package com.trasen.imis.dao;

import com.trasen.imis.model.Tuser;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/28
 */
public interface TuserMaperr {
    public int insertGetId(Tuser tuser);

    public int updateTuser(Tuser tuser);

    public Tuser selectByperId(String perId);

    public int updatePersonByTuser(Tuser tuser);
}
