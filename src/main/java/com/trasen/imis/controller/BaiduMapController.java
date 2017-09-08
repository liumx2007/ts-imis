package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.trasen.imis.utils.BaiDuUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: BaiduMapController
 * @Description: 操作类型
 * @date 2017/6/19
 */
@Controller
@RequestMapping(value="/baidumap")
public class BaiduMapController {

    /*
    * 通过坐标点，在百度地图上显示
    * */
    @RequestMapping(value="/getCoordinateBaiduMapView",method = RequestMethod.GET)
    public String getCoordinateBaiduMapView(@QueryParam("coordinate") String coordinate, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {

        coordinate=BaiDuUtil.getCoordinateForbd09ll(coordinate);
        String centerCoordinate=null;
        String[] coordinateArray=coordinate.split(";");
        if(coordinateArray.length==0){
            centerCoordinate=coordinate;
            //if(centerCoordin)
        }else{
            centerCoordinate=coordinateArray[0];
            coordinate=coordinate.replace(";","|");
        }
        String width = PropertiesUtils.getProperty("width");
        if(width==null){
            width = "400";
        }
        String height = PropertiesUtils.getProperty("height");
        if(height==null){
            height = "350";
        }
        String baiduurl="http://api.map.baidu.com/staticimage/v2?ak=TTYEcxv5asPAMZ8ZBIMtuqIyXLOjrGhM&width="+width+"&height="+height+"&center="+centerCoordinate+"&markers="+coordinate+"&zoom=14&markerStyles=s,A,0xff0000";
        try {
            InputStream is = new URL(baiduurl).openStream();

            // String line = null;
            byte[] b = new byte[1024];
            int len = -1;
            while((len = is.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    * 通过坐标点获取地址
    * */
    @RequestMapping(value="/getAddressforCoordinate",method = RequestMethod.GET)
    @ResponseBody
    public String getAddressforCoordinate(@QueryParam("coordinate") String coordinate){



        String  address=BaiDuUtil.getAddressForCoordinate(coordinate);

        String json = "{\"address\": \"" + address + "\"}";
        return json;
    }

    /*
    * 通过坐标点获取地址
    * */
    @RequestMapping(value="/getAddressForCoordinateList",method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAddressForCoordinateList(@QueryParam("coordinate") String coordinate){
        String  address=BaiDuUtil.getAddressForCoordinate(coordinate);
        List<String> list = BaiDuUtil.getAddressForCoordinateList(address,coordinate);
        return list;
    }



}
