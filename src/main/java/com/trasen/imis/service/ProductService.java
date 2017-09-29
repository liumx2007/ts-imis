package com.trasen.imis.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.dao.TbProductMapper;
import com.trasen.imis.model.TbProModule;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/25
 */
@Component
public class ProductService {

    @Autowired
    private TbProductMapper tbProductMapper;

    public int saveOrUpdateProductList(){
        /*Map<String, String> wxParam = new HashMap<String, String>();
        wxParam.put("appid", appid);
        wxParam.put("touser", toUser);
        String parameterJson = JSONObject.toJSONString(wxParam);*/

        String product_imis = PropertiesUtils.getProperty("product_imis");
        if(product_imis==null){
            return 1;
        }
        String json=HttpUtil.connectURL(product_imis,"","POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        boolean boo=dataJson.getBoolean("success");
        if(boo){
            JSONArray dataJsonJSONArray= dataJson.getJSONArray("list");
            if(dataJsonJSONArray.size()>0){
                for (java.util.Iterator tor=dataJsonJSONArray.iterator();tor.hasNext();) {
                    JSONObject jsonObject = (JSONObject)tor.next();
                    Integer productId=jsonObject.getInteger("productId");
                    if(productId!=null){
                        TbProModule tbProduct=tbProductMapper.selectProductCount(String.valueOf(productId));
                        if(tbProduct==null){
                            TbProModule tbProductSave=new TbProModule();
                            tbProductSave.setModId(String.valueOf(productId));
                            tbProductSave.setModName(jsonObject.getString("productName"));
                            if(jsonObject.getString("type")!=null)tbProductSave.setProCode(jsonObject.getString("type"));
                            if(jsonObject.getString("productNo")!=null&&jsonObject.getString("productNo")!="") tbProductSave.setModNo(jsonObject.getString("productNo"));
                            if(jsonObject.getDate("createDate")!=null) {
                                tbProductSave.setCreated(jsonObject.getDate("createDate"));
                            }else{
                                tbProductSave.setCreated(new Date());
                            }
                            if(jsonObject.getInteger("versionCode")!=null){
                                tbProductSave.setVersion(jsonObject.getString("versionCode"));
                            } else{
                                tbProductSave.setVersion("1");
                            }
                            if(jsonObject.getInteger("latest")!=null){
                                tbProductSave.setIsVaild(jsonObject.getInteger("latest"));
                            }else{
                                tbProductSave.setIsVaild(0);
                            }
                            tbProductMapper.saveProduct(tbProductSave);
                        }
                    }
                }
            }
        }
       return 0;
    }
}
