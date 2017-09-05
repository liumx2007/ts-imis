package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.controller.vo.TestVo;

import java.util.List;

/**
 * Created by zhangxiahui on 17/6/6.
 */
public interface TbTestMapper {
    String getTestName(String id);

    List<TestVo> getList(Page page);
}
