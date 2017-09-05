package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbContract;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/18
 */
public interface ContractMapper {

    public List<TbContract> getTbContractList(Map<String,String> param,Page page);
    public List<TbContract> getTbContractList(Map<String,String> param);

    public void insertContract(TbContract tbContract);

    public int updateContract(TbContract tbContract);

    public int deleteContractforWorkNum(String workNum);

    public String getTbContratListByDate(Map<String,String> date);

    public TbContract getTbContract(String workNum);

}
