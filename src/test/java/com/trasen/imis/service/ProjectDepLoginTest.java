package com.trasen.imis.service;/**
 * Created by zhangxiahui on 16/6/21.
 */

import org.apache.commons.codec.digest.HmacUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 给项目部demo
 * @author zhangxiahui
 * @version 1.0
 * @date 2016/06/21 下午3:49
 */
public class ProjectDepLoginTest {
    public static void main(String[] args) {
        String tenantId = "qiushi";
        String secret = "9dF74HlkgsAckjQfLA9OXSc46";
        String sign = "";
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("tenantId",tenantId);
            sign = ProjectDepLoginTest.generateMD5Sign(secret,parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sign);

        //$httpProvider.defaults.headers.common = { 'My-Header' : 'value' }
        //sessionStorage.setItem("key", "value");

        /*传统的Web应用，客户端的访问凭证都存储在浏览器的Cookie中。在统一认证的方案中，我们要求将凭证存放在Web Storage中。
        这两种方式主要的区别在于安全性。
         （1）存放在Cookie中，可以有效防止XSS攻击（前提是设置cookie的httponly和secure属性），但是有受到CSRF攻击的风险。
         （2）存放在Web Storage中，可以防止CSRF攻击，但是有受到XSS攻击的风险。
        相比之下，XSS攻击更容易防范；而CSRF攻击无处不在，防不胜防。*/




        String username = "admin" ;
        String currrentDate = "2017-01-20 15:30:00";//yyyy-MM-dd HH:mm:ss
        String seperator = "/";
        String message = username + seperator + currrentDate;
        message = HmacUtils.hmacSha1Hex("ts-imis", message);
        System.out.println("X-TOKEN:"+message);

        String asB64 = Base64.getEncoder().encodeToString("你好".getBytes());
        System.out.println(asB64);





        String json = new String(Base64.getUrlDecoder().decode(asB64));

        System.out.println(json);
        //登录内容管理时必须有以下两个cookie
        //Cookie tenantId = new Cookie("tenantId", tenantId);
        //Cookie contentSign = new Cookie("contentSign", sign);
    }

    public static String generateMD5Sign(String secret, Map<String, String> parameters) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(generateConcatSign(secret, parameters).getBytes("utf-8"));
        return byteToHex(bytes);
    }
    private static String generateConcatSign(String secret, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder().append(secret);
        Set<String> keys = parameters.keySet();
        for(String key : keys) {
            sb.append(key).append(parameters.get(key));
        }
        return sb.append(secret).toString();
    }

    private static String byteToHex(byte[] bytesIn) {
        StringBuilder sb = new StringBuilder();
        for(byte byteIn : bytesIn) {
            String bt = Integer.toHexString(byteIn & 0xff);
            if(bt.length() == 1)
                sb.append(0).append(bt);
            else
                sb.append(bt);
        }
        return sb.toString().toUpperCase();
    }
}
