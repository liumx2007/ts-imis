package com.trasen.imis.service;

import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.dao.TbUserMapper;
import com.trasen.imis.model.TbUser;
import com.trasen.imis.utils.PropertiesUtils;
import com.trasen.imis.utils.SignConvertUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/27.
 */
@Component
public class TbUserService {

    Logger logger = Logger.getLogger(TbUserService.class);

    @Autowired
    TbUserMapper tbUserMapper;

    public TbUser isLogin(String name,String pwd){
        TbUser tbUser = null;
        if(!StringUtil.isEmpty(name)&&!StringUtil.isEmpty(pwd)){
            TbUser user = tbUserMapper.getUser(name);
            if(user!=null&&user.getPassword()!=null&&pwd.equals(user.getPassword())){
                //登录成功,设置X-TOKEN
                Map<String, String> parameters = new HashedMap();
                parameters.put("name", name);
                parameters.put("pwd", pwd);
                parameters.put("showName", user.getDisplayName());
                parameters.put("userId", user.getPkid().toString());
                try {
                    String secret = PropertiesUtils.getProperty("CONTENT_SECRET");
                    String sign = SignConvertUtil.generateMD5Sign(secret, parameters);
                    String parameterJson = JSONObject.toJSONString(parameters);
                    String asB64 = Base64.getEncoder().encodeToString(parameterJson.getBytes());
                    String xtoken = sign+"."+asB64;
                    user.setXtoken(xtoken);
                } catch(NoSuchAlgorithmException e) {
                    logger.error(e.getMessage(), e);
                } catch(UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                }
                return user;
            }
        }
        return tbUser;
    }

    public TbUser getUser(String name,String pwd){
        TbUser tbUser = null;
        if(!StringUtil.isEmpty(name)&&!StringUtil.isEmpty(pwd)){
            TbUser user = tbUserMapper.getUser(name);
            if(user!=null&&user.getPassword()!=null&&pwd.equals(user.getPassword())){
                return user;
            }
        }
        return tbUser;
    }

    public int updatePassword(TbUser tbUser){
        return tbUserMapper.updatePassword(tbUser);
    }

}
