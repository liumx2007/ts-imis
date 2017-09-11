package com.trasen.imis.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/6/19
 */
public class BaiDuUtil {

    /*
    *
    * 通过地址获取坐标点
    * */
    public String getCoordinateforAddress(String address) throws MalformedURLException {
        String baiduurl="http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=TTYEcxv5asPAMZ8ZBIMtuqIyXLOjrGhM";
        StringBuffer str = new StringBuffer();
        URL url = new URL(baiduurl);
        String Coordinate="";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                str.append(line);
            }
            in.close();
            if (str.equals("") || str == null) {
                System.err.println("百度服务无返回");
                return null;
            }
            System.out.println(str.toString());
            // dataJson=null;
            JSONObject dataJson =(JSONObject)JSONObject.parse(str.toString());
            JSONObject result=dataJson.getJSONObject("result");
            JSONObject location=result.getJSONObject("location");
            Coordinate=location.getDouble("lng")+","+location.getDouble("lat");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Coordinate;
    }

    /*
    *
    * 进行坐标点装换
    * */
    public static String getCoordinateForbd09ll(String coordinate) throws MalformedURLException {
        String baiduurl="http://api.map.baidu.com/geoconv/v1/?coords="+coordinate+"&from=1&to=5&ak=TTYEcxv5asPAMZ8ZBIMtuqIyXLOjrGhM";
        StringBuffer str = new StringBuffer();
        URL url = new URL(baiduurl);
        String CoordinateForbd09ll="";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                str.append(line);
            }
            in.close();
            if (str.equals("") || str == null) {
                System.err.println("百度服务无返回");
                return null;
            }
            System.out.println(str.toString());
            // dataJson=null;
            Float x=0.00f;
            Float y=0.00f;
            JSONObject dataJson =(JSONObject)JSONObject.parse(str.toString());
            JSONArray result=dataJson.getJSONArray("result");
            if(result.size()>0){
                	for (java.util.Iterator tor=result.iterator();tor.hasNext();) {
                	JSONObject coordinateJson = (JSONObject)tor.next();
                	x=coordinateJson.getFloat("x");
                     y= coordinateJson.getFloat("y");
                	}
            }

            CoordinateForbd09ll=x+","+y;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CoordinateForbd09ll;
    }

    /*
    * 利用百度地图，通过坐标点获取地址
    * */
    public static String getAddressForCoordinate(String coordinate){
        String locationCoordinate=null;
        try {
            String coordinabdo9ll=getCoordinateForbd09ll(coordinate);
            String[] coordinabdo9llArrayy=coordinabdo9ll.split(",");
            locationCoordinate=coordinabdo9llArrayy[1]+","+coordinabdo9llArrayy[0];

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String baiduurl="http://api.map.baidu.com/geocoder/v2/?location="+locationCoordinate+"&output=json&pois=0&ak=TTYEcxv5asPAMZ8ZBIMtuqIyXLOjrGhM";
        String address=null;
        StringBuffer str = new StringBuffer();
        try {
            URL url = new URL(baiduurl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                str.append(line);
            }
            in.close();
            if (str.equals("") || str == null) {
                System.err.println("百度服务无返回");
                return null;
            }
            System.out.println(str.toString());
            JSONObject dataJson =(JSONObject)JSONObject.parse(str.toString());
            JSONObject result=dataJson.getJSONObject("result");
            address=result.getString("formatted_address")+"["+result.getString("sematic_description")+"]";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    /*
    * 利用百度地图，通过坐标点获取地址
    * */
    public static List<Map<String,String>> getAddressForCoordinateList(String address, String coordinate){
        List<Map<String,String>> list = new ArrayList<>();
        String addressFujin = "";
        String locationCoordinate="";
        String coordinabdo9ll = "";
        try {
            coordinabdo9ll=getCoordinateForbd09ll(coordinate);
            String[] coordinabdo9llArrayy=coordinabdo9ll.split(",");
            locationCoordinate=coordinabdo9llArrayy[1]+","+coordinabdo9llArrayy[0];

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(address!=null){
            Map<String,String> first = new HashMap<>();
            first.put("address",address);
            first.put("GPSStr",coordinabdo9ll);

            list.add(first);
            addressFujin = address.substring(0,address.indexOf("["));
        }


        String radius = PropertiesUtils.getProperty("radius");
        if(radius==null){
            radius = "300";
        }


        String baiduurl="http://api.map.baidu.com/place/v2/search?query=医院&page_size=10&page_num=0&scope=1&location="+locationCoordinate+"&radius="+radius+"&output=json&ak=TTYEcxv5asPAMZ8ZBIMtuqIyXLOjrGhM";
        StringBuffer str = new StringBuffer();
        try {
            URL url = new URL(baiduurl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                str.append(line);
            }
            in.close();
            if (str.equals("") || str == null) {
                System.err.println("百度服务无返回");
                return list;
            }
            System.out.println(str.toString());
            JSONObject dataJson =(JSONObject)JSONObject.parse(str.toString());
            if(dataJson!=null){
                Integer total = dataJson.getInteger("total");
                if(total!=null&&total>0){
                    List<JSONObject> results = (List<JSONObject>)dataJson.get("results");
                    if(results!=null&&results.size()>0){
                        for(JSONObject jsonObject : results){
                            String name = jsonObject.getString("name");
                            JSONObject location = jsonObject.getJSONObject("location");
                            if(name!=null&&location!=null){
                                float lat = location.getFloat("lat");
                                float lng = location.getFloat("lng");
                                String GPSStr = lng+","+lat;
                                Map<String,String> map = new HashMap<>();
                                map.put("address",addressFujin+"["+name+"]");
                                map.put("GPSStr",GPSStr);
                                list.add(map);
                            }
                        }

                    }

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }




    public static void main(String[] args)  {
        String address= null;
        //公司:112.87425,28.21533


        address = new BaiDuUtil().getAddressForCoordinate("115.61130,33.17476");

        //115.61130,33.17476
        //113.11034,29.14915
        //112.85190,26.42187

        System.out.println(address);

        List<Map<String,String>> list = BaiDuUtil.getAddressForCoordinateList(address,"115.61130,33.17476");
        for(Map<String,String> map : list){
            System.out.println(map.get("address")+"=="+map.get("GPSStr"));
        }
    }

}
