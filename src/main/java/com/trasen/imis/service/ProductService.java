package com.trasen.imis.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.dao.TbProductMapper;
import com.trasen.imis.model.TbProduct;
import com.trasen.imis.utils.HttpUtil;
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
        String json=HttpUtil.connectURL("http://localhost:9090/contract/getProductList","","POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        boolean boo=dataJson.getBoolean("success");
        if(boo){
            JSONArray dataJsonJSONArray= dataJson.getJSONArray("list");
            if(dataJsonJSONArray.size()>0){
                for (java.util.Iterator tor=dataJsonJSONArray.iterator();tor.hasNext();) {
                    JSONObject jsonObject = (JSONObject)tor.next();
                    Integer productId=jsonObject.getInteger("productId");
                    if(productId!=null){
                        TbProduct tbProduct=tbProductMapper.selectProductCount(String.valueOf(productId));
                        if(tbProduct==null){
                            TbProduct tbProductSave=new TbProduct();
                            tbProductSave.setPkid(productId);
                            if(jsonObject.getInteger("type")!=null)tbProductSave.setCode(jsonObject.getInteger("type"));
                            if(jsonObject.getString("productNo")!=null&&jsonObject.getString("productNo")!="") tbProductSave.setIdentifier(jsonObject.getString("productNo"));
                            if(jsonObject.getDate("createDate")!=null) {
                                tbProductSave.setCreated(jsonObject.getDate("createDate"));
                            }else{
                                tbProductSave.setCreated(new Date());
                            }
                            if(jsonObject.getInteger("versionCode")!=null){
                                tbProductSave.setVersion(jsonObject.getInteger("versionCode"));
                            } else{
                                tbProductSave.setVersion(1);
                            }
                            if(jsonObject.getInteger("latest")!=null){
                                tbProductSave.setIsLatest(jsonObject.getInteger("latest"));
                            }else{
                                tbProductSave.setIsLatest(0);
                            }
                            if(jsonObject.getString("imisid")!=null) tbProductSave.setDepId(jsonObject.getString("imisid"));
                            if(jsonObject.getString("productName")!=null) tbProductSave.setName(jsonObject.getString("productName"));
                            tbProductMapper.saveProduct(tbProductSave);
                        }else{
                            if(tbProduct.getIsLatest()!=jsonObject.getInteger("latest")&&jsonObject.getInteger("latest")==0){
                                tbProduct.setIsLatest(0);
                                tbProductMapper.updateProduct(tbProduct);
                            }
                        }
                    }
                }
            }
        }
       return 0;
    }
}
