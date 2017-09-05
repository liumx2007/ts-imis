package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbJfRecord;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/5
 */
public interface RecordApprovalMapper {
    List<TbJfRecord> getRecordApprovalList(Map<String,String> param, Page page);
}
