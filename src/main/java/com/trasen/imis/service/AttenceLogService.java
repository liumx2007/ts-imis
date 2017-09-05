package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.dao.TbAttenceLogMapper;
import com.trasen.imis.model.AttenceLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/6/27
 */
@Component
public class AttenceLogService {

    @Autowired
    private TbAttenceLogMapper tbAttenceLogMapper;

    public List<AttenceLogVo> getAttenceLogList(Map<String,Object> attenceLogMap, Page page){
        return tbAttenceLogMapper.getAttenceLogList(attenceLogMap,page);
    }
}
