package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.common.SecurityCheck;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/10/31.
 */
@Controller
@RequestMapping("download")
public class DownloadController {

    private Logger logger = Logger.getLogger(DownloadController.class);

    @ResponseBody
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public void fileUpload(HttpServletRequest request, HttpServletResponse response, String filePath,Boolean isOnLine) throws Exception{
        //一个简单的鉴权
        String userSign = SecurityCheck.getCookieValue(request,"userSign");
        logger.info("userSign鉴权===获取到userSign[" + userSign + "]");
        if(userSign==null){
            return;
        }
        if(!SecurityCheck.checkUserSigner(userSign)){
            return;
        }

        String saveFileUrl = PropertiesUtils.getProperty("saveFileUrl");
        if(saveFileUrl==null){
            saveFileUrl = "/ucenter/cs/driverFile/imisFile/";
        }
        String picDir = PropertiesUtils.getProperty("savePicUrl");
        if(picDir==null){
            picDir = "/ucenter/cs/driverFile/imisPic/";
        }
        if(filePath.indexOf("imisFile")>0){
            filePath = saveFileUrl + filePath.split("/")[filePath.split("/").length-1];
        }
        if(filePath.indexOf("imisPic")>0){
            filePath = picDir + filePath.split("/")[filePath.split("/").length-1];
        }




        File f = new File(filePath);
        if (!f.exists()) {
            //response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }
}
