package com.trasen.imis.utils;


import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/26
 */
public class WorkDateutil {

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY ="24d703e1904b0c614d2be55e6764655a";

    //1.获取当天的详细信息
    public static void getRequest1(String dateString){
        String result =null;
        String url ="http://v.juhe.cn/calendar/day";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//您申请的appKey
        params.put("date",dateString);//指定日期,格式为YYYY-MM-DD,如月份和日期小于10,则取个位,如:2012-1-1

        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if(object.getInteger("error_code")==0){
                System.out.println(object.get("result"));
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2.获取当月近期假期
    public static JSONObject getRequest2(String monthString){
        String result =null;
        String url ="http://v.juhe.cn/calendar/month";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//您申请的appKey
        params.put("year-month",monthString);//指定月份,格式为YYYY-MM,如月份和日期小于10,则取个位,如:2012-1

        try {
            result =net(url, params, "GET");
            JSONObject object = (JSONObject) JSONObject.parse(result);

            if(object.getJSONObject("result")==null||object.getInteger("error_code")==0){
                return object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //3.获取当年的假期列表
    public static void getRequest3(String yearString){
        String result =null;
        String url ="http://v.juhe.cn/calendar/year";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//您申请的appKey
        params.put("year",yearString);//指定年份,格式为YYYY,如:2015

        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if(object.getInteger("error_code")==0){
                System.out.println(object.get("result"));
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void main(String[] args){
       // getRequest2("2017-5");

        /*String[] name="2017-1-2".split(",");

        System.out.println("周一".length()+"周一".split("")[1]);*/
        List<String> workList=getWorkDayForRue("周一,周二,周三","2017-7");
        System.out.println(workList.size());
    }

    public static List<String> getWorkDayForRue(String workingDay, String yearMonth){
        Calendar time= Calendar.getInstance();
        time.clear();
        String[] year_month=yearMonth.split("-");
        time.set(Calendar.YEAR,Integer.valueOf(year_month[0]));
        //year年
        time.set(Calendar.MONTH,Integer.valueOf(year_month[1])-1);
        //Calendar对象默认一月为0,month月
        int day=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        List<String> workdayList=new ArrayList<String>();
        String[] working_day=workingDay.split(",");
        for(int i=0;i<working_day.length;i++){
            String week=working_day[i].split("")[1];
            for(int k = 1; k <=day; k++){
                try {
                    Date date = sdf1.parse(yearMonth + "-" +k);
                    if(sdf2.format(date).indexOf(week)>=0){
                        workdayList.add(sdf1.format(date));
                    }
                } catch (ParseException e) {
                    //do nothing
                }
            }
        }
        return workdayList;
    }

}
