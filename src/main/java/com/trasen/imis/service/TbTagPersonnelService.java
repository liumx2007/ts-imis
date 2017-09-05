package com.trasen.imis.service;

import com.trasen.imis.dao.TbTagPersonnelMapper;
import com.trasen.imis.model.TbTagPersonnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/11
 */
@Component
public class TbTagPersonnelService {
    @Autowired
    private TbTagPersonnelMapper tbTagPersonnelMapper;

    public List<TbTagPersonnel> getTaTagPersonnelList(String workNum){
        return tbTagPersonnelMapper.getTaTagPersonnelList(workNum);
    }
    public int deleteTaTagPersonnel(String pkid){
        return tbTagPersonnelMapper.deleteTaTagPersonnel(pkid);
    }
    public int deleteTaTagPersonnelForWorkNum(String workNum){
        return tbTagPersonnelMapper.deleteTaTagPersonnelForWorkNum(workNum);
    }

    public void saveTagPersonnel(List<Map<String,Object>> parms){
        tbTagPersonnelMapper.saveTagPersonnel(parms);
    }
    public String getTagNameforWorNum(String workNum){
        return tbTagPersonnelMapper.getTagNameforWorNum(workNum);
    }
}
