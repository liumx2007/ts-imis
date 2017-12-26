package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.dao.ContractMapper;
import com.trasen.imis.dao.TbAttenceMapper;
import com.trasen.imis.model.TbContract;
import com.trasen.imis.utils.DateUtils;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/18
 */
@Component
public class ContractService {

    public final static Logger logger = Logger.getLogger(ContractService.class);

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    TbAttenceMapper tbAttenceMapper;

    public List<TbContract> getTbContractList(Map<String,String> param,Page page){
        if(Optional.ofNullable(param.get("depName")).isPresent()&&!Optional.ofNullable(param.get("depName")).get().equals("")){
            String deptCode=tbAttenceMapper.getDeptCode(param.get("depName"));
            param.put("deptCode",deptCode);
        }
        return contractMapper.getTbContractList(param,page);
    }

    public List<TbContract> getTbContractList(Map<String,String> param){
        if(Optional.ofNullable(param.get("depName")).isPresent()&&!Optional.ofNullable(param.get("depName")).get().equals("")){
            String deptCode=tbAttenceMapper.getDeptCode(param.get("depName"));
            param.put("deptCode",deptCode);
        }
        return contractMapper.getTbContractList(param);
    }

    public void insertContract(TbContract tbContract){
        contractMapper.insertContract(tbContract);
    }

    public int updateContract(TbContract tbContract){
        return contractMapper.updateContract(tbContract);
    }

    public int deleteContractforWorkNum(String workNum){
        return contractMapper.deleteContractforWorkNum(workNum);
    }

    public List<TbContract> getTbContratListByDate(Map<String,String> date){
        return contractMapper.getTbContratListByDate(date);
    }

    public int TbContratCount(String workNum){
        return contractMapper.TbContratCount(workNum);
    }

    public int updateSendStatus(Integer pkid){
        return contractMapper.updateSendStatus(pkid);
    }

    public TbContract getTbContract(String workNum){
        return contractMapper.getTbContract(workNum);
    }

    public void sendTbContract(){
        String appid = PropertiesUtils.getProperty("appid");

        String xq_templateId = PropertiesUtils.getProperty("xq_templateId");

        String qd_templateId = PropertiesUtils.getProperty("qd_templateId");
        String send_template_message=PropertiesUtils.getProperty("send_template_message");
        String toUser = PropertiesUtils.getProperty("toUser");
        String contract_date=PropertiesUtils.getProperty("contract_date");
        System.out.println("===============发送劳动合同");
        if (appid == null || xq_templateId == null|| qd_templateId==null || toUser == null || send_template_message == null||contract_date==null) {
            logger.info("参数错误:" + "参数错误");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String dateStr = sdf.format(date);
            String contractDate = null;
            String qdContractDate=null;
            Date counDate=null;
            Date qdCounDate=null;
            try {
                counDate = DateUtils.getBeforeTargetDate(dateStr,Integer.valueOf(contract_date));
                qdCounDate = DateUtils.getBeforeTargetDate(dateStr,-Integer.valueOf(contract_date));
            } catch (Exception e) {
                e.printStackTrace();
            }
            contractDate = sdf.format(counDate);
            qdContractDate = sdf.format(qdCounDate);
            Map<String, String> dateMap = new HashMap<String, String>();
            dateMap.put("dateStrat", dateStr);
            dateMap.put("dateEnd", contractDate);
            dateMap.put("dateEntry",qdContractDate);
            List<TbContract> tbContractList = contractMapper.getTbContratListByDate(dateMap);
            tbContractList.stream().forEach(con->{
                Map<String, String> wxParam = new HashMap<String, String>();

                int count=contractMapper.TbContratCount(con.getWorkNum());

                wxParam.put("appid", appid);
                wxParam.put("touser", toUser);
                if(count>=1){
                    wxParam.put("templateId", xq_templateId);
                    wxParam.put("first","你好，有劳动合同需要续签");
                    wxParam.put("keyword4",con.getEndDate());
                    wxParam.put("keyword5",count+"次");
                }else{
                    wxParam.put("templateId", qd_templateId);
                    wxParam.put("first","你好，有劳动合同需要签订");
                    wxParam.put("keyword4",con.getEntryDate());
                }
                wxParam.put("remark","请尽快处理");
                wxParam.put("keyword1",con.getName());
                wxParam.put("keyword2",con.getWorkNum());
                wxParam.put("keyword3",con.getDepName());

                String parameterJson = JSONObject.toJSONString(wxParam);
                String message = HttpUtil.connectURL(send_template_message, parameterJson, "POST");
                JSONObject dataJson = (JSONObject) JSONObject.parse(message);
                String result = dataJson.getString("msg");
                if(result.equals("success")){
                    contractMapper.updateSendStatus(con.getPkid());
                }
                logger.info("劳动合同发送:" + result);
            });
        }
    }
}
