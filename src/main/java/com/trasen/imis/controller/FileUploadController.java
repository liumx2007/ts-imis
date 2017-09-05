package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.utils.DateUtils;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhangxiahui on 17/7/21.
 */
@Controller
@RequestMapping("fileUpload")
public class FileUploadController {


    private Logger logger = Logger.getLogger(FileUploadController.class);

    @ResponseBody
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public Result fileUpload(HttpServletRequest request, HttpServletResponse response,String type) {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("上传失败");

        response.addHeader("key","value");
        String fileUrl = "";
        String timeStr = DateUtils.getTime("yyyyMMddHHmmss");
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession()
                .getServletContext());
        if(commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipart = multipartHttpServletRequest.getMultiFileMap().get("file").get(0);

            if(multipart!=null&&type!=null){
                String suffix = multipart.getOriginalFilename().split("\\.")[1];
                String name = multipart.getOriginalFilename().split("\\.")[0];
                String fileName = type+"_"+name+"-"+timeStr+"."+suffix;

                String saveFileUrl = PropertiesUtils.getProperty("saveFileUrl");
                if(saveFileUrl==null){
                    saveFileUrl = "/ucenter/cs/driverFile/imisFile/";
                }
                String saveFilePath = PropertiesUtils.getProperty("saveFilePath");
                if(saveFilePath==null){
                    saveFilePath = "http://123.207.46.33/imisFile/";
                }
                try {

                    File fileDir = new File(saveFileUrl);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }


                    File tmpFile = new File(saveFileUrl+ System.getProperty("file.separator") +fileName);
                    multipart.transferTo(tmpFile);

                    if(!tmpFile.isFile()) {
                        return result;
                    }

                    fileUrl = saveFilePath + fileName;
                    result.setSuccess(true);
                    result.setMessage("文件上传成功");
                    result.setObject(fileUrl);
                    logger.info("文件访问路径:" + fileUrl);
                } catch(IOException e) {
                    logger.error("文件上传异常:" + e.getMessage(), e);
                }
            }

        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public Result imageUpload(@RequestParam(value = "file", required = true) MultipartFile imgFile,String workNum) {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("上传失败");

        String timeStr = DateUtils.getTime("yyyyMMddHHmmss");

        String picUrl="";
        String imageName = workNum+"-"+timeStr+".png";
        if (imgFile.getSize() <= 2 * 1024 * 1024) {
            String picDir = PropertiesUtils.getProperty("savePicUrl");
            if(picDir==null){
                picDir = "/ucenter/cs/driverFile/imisPic/";
            }
            String picPath = PropertiesUtils.getProperty("savePicPath");
            if(picPath==null){
                picPath = "http://123.207.46.33/imisPic/";
            }
            try {
                File fileDir = new File(picDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                File tmpFile = new File(picDir+ System.getProperty("file.separator") +imageName);
                imgFile.transferTo(tmpFile);

                if(!tmpFile.isFile()) {
                    result.setSuccess(false);
                    result.setMessage(" 不是图片");
                }

                picUrl = picPath + imageName;
                logger.info("图片访问路径:" + picUrl);
                result.setSuccess(true);
                result.setMessage("图片上传成功");
                result.setObject(picUrl);
            } catch(IOException e) {
                logger.error("图片上传异常:" + e.getMessage(), e);
            }
        }
        return result;
    }

}
