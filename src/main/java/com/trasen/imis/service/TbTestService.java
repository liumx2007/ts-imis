package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.controller.vo.TestVo;
import com.trasen.imis.dao.TbTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhangxiahui on 17/6/6.
 */
@Component
public class TbTestService {

    @Autowired
    TbTestMapper tbTestMapper;

    public String getTestName(String id){
        return tbTestMapper.getTestName(id);
    }

    public List<TestVo> getList(Page page){
        return tbTestMapper.getList(page);
    }

}
