package com.trasen.imis.task;

import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.service.ContractService;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/20
 */
@Component
public class WeiXinPersonTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(WeiXinPersonTask.class);

    @Autowired
    private ContractService contractService;


    public String getSpecifiedDayAfter(Date date) {
        Calendar calendar = Calendar.getInstance();
        String contractDate = PropertiesUtils.getProperty("contractDate");
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));//今天的日期
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + Integer.valueOf(contractDate));//让日期加1
        System.out.println(calendar.get(Calendar.DATE));//加1之后的日期Top
        System.out.println(calendar.get(Calendar.DATE));
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return dayAfter;
    }

    /**
       * 日期加几月
       */


    public  String dateAddMonth(Date date) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();
        String contractMouth = PropertiesUtils.getProperty("contractMouth");
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, Integer.valueOf(contractMouth));// 日期加3个月
        // rightNow.add(Calendar.DAY_OF_YEAR,10);//日期加10天
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    @Override
    public void run() {
        String appid = PropertiesUtils.getProperty("appid");
        String templateId = PropertiesUtils.getProperty("templateId");
        String toUser = PropertiesUtils.getProperty("toUser");
        String openName = PropertiesUtils.getProperty("openName");
        String messageUrl=PropertiesUtils.getProperty("send_template_message");
        System.out.println("===============发送劳动合同");
        if (appid == null || templateId == null || toUser == null || openName == null||messageUrl==null) {
            logger.info("参数错误:" + "参数错误");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String dateStr = sdf.format(date);
            String contractDate = null;
            try {
                contractDate = dateAddMonth(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, String> dateMap = new HashMap<String, String>();
            dateMap.put("dateStrat", dateStr);
            dateMap.put("dateEnd", contractDate);
            String contractName = contractService.getTbContratListByDate(dateMap);
            if (contractName != null) {
                Map<String, String> wxParam = new HashMap<String, String>();
                wxParam.put("appid", appid);
                wxParam.put("touser", toUser);
                wxParam.put("templateId", templateId);
                wxParam.put("name", "尹中贵");
                wxParam.put("code", contractName);
                String parameterJson = JSONObject.toJSONString(wxParam);
                String message = HttpUtil.connectURL(messageUrl, parameterJson, "POST");
                JSONObject dataJson = (JSONObject) JSONObject.parse(message);
                String result = dataJson.getString("msg");
                logger.info("劳动合同发送:" + result);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date =new Date();
        String mytime = dateFormat.format(date);
        System.out.println(mytime);
    }


}
