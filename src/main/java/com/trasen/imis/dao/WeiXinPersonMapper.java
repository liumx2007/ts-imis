package com.trasen.imis.dao;

import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.model.WinXinPerson;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/19
 */
public interface WeiXinPersonMapper {

    public WinXinPerson getPersonByOpenId(String openId);

    public int updatePerson(WinXinPerson winXinPerson);
    public int updatePersonBasic(WinXinPerson winXinPerson);

    public List<WinXinPerson> getPersonByName(String name);
    public int getBlackList(String name);

    Integer checkWeixinOpenId(String code);

    Integer updateT_Weixin_user(TbPersonnel tbPersonnel);
}
