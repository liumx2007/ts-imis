package com.trasen.imis.service;

import com.trasen.imis.dao.TbAttenceLeaveMapper;
import com.trasen.imis.model.AttenceLeave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/4
 */
@Component
public class AttenceLeaveService {
    @Autowired
    private TbAttenceLeaveMapper tbAttenceLeaveMapper;

    @Autowired
    AttenceService attenceService;

    public int insertAttenceLeaveList(List<AttenceLeave> attenceLeaveList){
        if(attenceLeaveList!=null){
            for(AttenceLeave attenceLeave : attenceLeaveList){
                if(attenceLeave.getAttType()!=null&&attenceLeave.getAttId()!=null){
                    if(attenceLeave.getAttType()==1){
                        attenceService.deleAttence(attenceLeave.getAttId());
                    }else if(attenceLeave.getAttType()==2){
                        attenceService.deleLackAttence(attenceLeave.getAttId());
                    }
                }
            }
            return tbAttenceLeaveMapper.insertAttenceLeaveList(attenceLeaveList);
        }
        return 0;
    }
}
