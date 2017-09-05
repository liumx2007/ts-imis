package com.trasen.imis.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.model.AccessToken;
import com.trasen.imis.model.Menu;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class WeixinUtil {
    private static Logger log = Logger.getLogger(WeixinUtil.class);
    // 创建菜单
    public final static String create_menu_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject handleRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;

        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            SSLContext ctx = SSLContext.getInstance("SSL", "SunJSSE");
            TrustManager[] tm = {new MyX509TrustManager()};
            ctx.init(null, tm, new SecureRandom());
            SSLSocketFactory sf = ctx.getSocketFactory();
            conn.setSSLSocketFactory(sf);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod);
            conn.setUseCaches(false);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                conn.connect();
            }

            if (StringUtils.isNotEmpty(outputStr)) {
                OutputStream out = conn.getOutputStream();
                out.write(outputStr.getBytes("utf-8"));
                out.close();
            }

            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;

            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

            in.close();
            conn.disconnect();

            jsonObject = (JSONObject) JSONObject.parse(buffer.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.error("URL错误！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    /**
     * 创建菜单
     *
     * @author qincd
     * @date Nov 6, 2014 9:56:36 AM
     */
    public static boolean createMenu(Menu menu, String accessToken) {
        String requestUrl = create_menu_url.replace("ACCESS_TOKEN", accessToken);
        String menuJsonString = JSONObject.toJSONString(menu);
                //JSONObject.fromObject(menu).toString();
        JSONObject jsonObject = handleRequest(requestUrl, "POST", menuJsonString);
        String errorCode = jsonObject.getString("errcode");
        if (!"0".equals(errorCode)) {
            log.error(String.format("菜单创建失败！errorCode:%d,errorMsg:%s",jsonObject.getInteger("errcode"),jsonObject.getString("errmsg")));
            return false;
        }

        log.info("菜单创建成功！");

        return true;
    }
}
