package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.dao.ContractMapper;
import com.trasen.imis.dao.TbAttenceMapper;
import com.trasen.imis.model.TbContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/18
 */
@Component
public class ContractService {
    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    TbAttenceMapper tbAttenceMapper;

    public List<TbContract> getTbContractList(Map<String,String> param,Page page){
        if(Optional.ofNullable(param.get("depName")).isPresent()&&!Optional.ofNullable(param.get("depName")).get().equals("")){
            String deptCode=tbAttenceMapper.getDeptCode(param.get("depName"));
            param.put("deptCode",deptCode);
        }
        return contractMapper.getTbContractList(param,page);
    }

    public List<TbContract> getTbContractList(Map<String,String> param){
        if(Optional.ofNullable(param.get("depName")).isPresent()&&!Optional.ofNullable(param.get("depName")).get().equals("")){
            String deptCode=tbAttenceMapper.getDeptCode(param.get("depName"));
            param.put("deptCode",deptCode);
        }
        return contractMapper.getTbContractList(param);
    }

    public void insertContract(TbContract tbContract){
        contractMapper.insertContract(tbContract);
    }

    public int updateContract(TbContract tbContract){
        return contractMapper.updateContract(tbContract);
    }

    public int deleteContractforWorkNum(String workNum){
        return contractMapper.deleteContractforWorkNum(workNum);
    }

    public String getTbContratListByDate(Map<String,String> date){
        return contractMapper.getTbContratListByDate(date);
    }
    public TbContract getTbContract(String workNum){
        return contractMapper.getTbContract(workNum);
    }
}
