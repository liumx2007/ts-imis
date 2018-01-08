package com.trasen.imis.service;

import com.trasen.imis.dao.TbPersonnelMapper;
import com.trasen.imis.dao.WeiXinPersonMapper;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.model.TbWeixinCustormer;
import com.trasen.imis.model.WinXinPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/19
 */
@Component
public class WinXinPersonService {

    @Autowired
    private WeiXinPersonMapper weiXinPersonMapper;

    @Autowired
    private TbPersonnelMapper tbPersonnelMapper;

    public WinXinPerson getPersonByOpenId(String openId){
        return weiXinPersonMapper.getPersonByOpenId(openId);
    }

    public int updatePersonAndBasic(WinXinPerson winXinPerson){
        weiXinPersonMapper.updatePerson(winXinPerson);
       int wxUpdate= weiXinPersonMapper.updatePersonBasic(winXinPerson);
       return wxUpdate;
    }

    public List<WinXinPerson> getPersonByName(String name){
        return weiXinPersonMapper.getPersonByName(name);
    }
    public int getBlackList(String name){
        return weiXinPersonMapper.getBlackList(name);
    }
    public Integer checkWeixinOpenId(String openId){
        return weiXinPersonMapper.checkWeixinOpenId(openId);
    }

    public TbPersonnel findPersonByWorkNum(String workNum){
        return tbPersonnelMapper.getPersonnelForWorkNum(workNum);
    }

    public int updateT_Weixin_user(TbPersonnel tbPersonnel){
        return weiXinPersonMapper.updateT_Weixin_user(tbPersonnel);
    }

    public TbWeixinCustormer selectWeixinCusByCode(String inviteCode){
        return weiXinPersonMapper.selectWeixinCusByCode(inviteCode);
    }

    public int updateWeixinCusOpenId(TbWeixinCustormer tbWeixinCustormer){
        return weiXinPersonMapper.updateWeixinCusOpenId(tbWeixinCustormer);
    }

}
