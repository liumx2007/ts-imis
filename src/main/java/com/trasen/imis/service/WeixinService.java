package com.trasen.imis.service;

import com.alibaba.fastjson.JSONObject;
import com.trasen.imis.model.JsapiSignature;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.model.UserToken;
import com.trasen.imis.model.WinXinPerson;
import com.trasen.imis.model.resp.*;
import com.trasen.imis.utils.HttpUtil;
import com.trasen.imis.utils.MessageUtil;
import com.trasen.imis.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/6/13.
 */
@Component
public class WeixinService {

    Logger logger = Logger.getLogger(WeixinService.class);

    @Autowired
    private WinXinPersonService winXinPersonService;

    /**
     * 解析微信请求并读取XML
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String,String> readWeixinXml(HttpServletRequest request) throws IOException, DocumentException {
        Map<String,String> map = new HashMap<String,String>();
        //获取输入流
        InputStream input = request.getInputStream();
        //使用dom4j的SAXReader读取（org.dom4j.io.SAXReader;）
        SAXReader sax = new SAXReader();
        Document doc = sax.read(input);
        //获取XML数据包根元素
        Element root = doc.getRootElement();
        //得到根元素的所有子节点
        @SuppressWarnings("unchecked")
        List<Element> elementList = root.elements();
        //遍历所有节点并将其放进map
        for(Element e : elementList){
            map.put(e.getName(), e.getText());
        }
        //释放资源
        input.close();
        input = null;
        return map;

    }

    public String processRequest(HttpServletRequest req) {
        // 解析微信传递的参数
        String str = "success";
        try {
            Map<String,String> xmlMap = readWeixinXml(req);
            str = "请求处理异常，请稍后再试！";

            String ToUserName = xmlMap.get("ToUserName");
            String FromUserName = xmlMap.get("FromUserName");
            String MsgType = xmlMap.get("MsgType");
            logger.info("发送方账号:"+FromUserName+",接收方账号(开发者微信号):"+ToUserName+",消息类型:"+MsgType);


            if (MsgType.equals(MessageUtil.MESSAGG_TYPE_TEXT)) {
                // 用户发送的文本消息
                String content = xmlMap.get("Content");
                logger.info("用户：[" + FromUserName + "]发送的文本消息：" + content);

                // 链接
                if (content.contains("csdn")) {
                    TextMessage tm = new TextMessage();
                    tm.setToUserName(FromUserName);
                    tm.setFromUserName(ToUserName);
                    tm.setMsgType(MessageUtil.MESSAGG_TYPE_TEXT);
                    tm.setCreateTime(System.currentTimeMillis());
                    tm.setContent("CSDN博客");
                    return MessageUtil.textMessageToXml(tm);
                }

                if (content.contains("图文")) {
                    NewsMessage nm = new NewsMessage();
                    nm.setFromUserName(ToUserName);
                    nm.setToUserName(FromUserName);
                    nm.setCreateTime(System.currentTimeMillis());
                    nm.setMsgType(MessageUtil.MESSAGG_TYPE_NEWS);
                    List<Articles> articles = new ArrayList<Articles>();
                    Articles e1 = new Articles();
                    e1.setTitle("马云接受外媒专访：中国的五大银行想杀了“我”");
                    e1.setDescription("阿里巴巴集团上市大获成功，《华尔街日报》日前就阿里巴巴集团、支付宝等话题采访了马云，马云也谈到了与苹果Apple Pay建立电子支付联盟的可能性。本文摘编自《华尔街日报》，原文标题：马云谈阿里巴巴将如何帮助美国出口商，虎嗅略有删节。");
                    e1.setPicUrl("http://img1.gtimg.com/finance/pics/hv1/29/53/1739/113092019.jpg");
                    e1.setUrl("http://finance.qq.com/a/20141105/010616.htm?pgv_ref=aio2012&ptlang=2052");

                    Articles e2 = new Articles();
                    e2.setTitle("史上最牛登机牌：姓名竟是微博名 涉事航空公司公开致歉");
                    e2.setDescription("世上最遥远的距离是飞机在等你登机，你却过不了安检。");
                    e2.setPicUrl("http://p9.qhimg.com/dmfd/328_164_100/t011946ff676981792d.png");
                    e2.setUrl("http://www.techweb.com.cn/column/2014-11-05/2093128.shtml");
                    articles.add(e1);
                    articles.add(e2);

                    nm.setArticles(articles);
                    nm.setArticleCount(articles.size());

                    String newsXml = MessageUtil.NewsMessageToXml(nm);
                    logger.info("\n"+newsXml);
                    return newsXml;
                }
                if (content.contains("音乐")) {
                    MusicMessage mm =  new MusicMessage();
                    mm.setFromUserName(ToUserName);
                    mm.setToUserName(FromUserName);
                    mm.setMsgType(MessageUtil.MESSAGG_TYPE_MUSIC);
                    mm.setCreateTime(System.currentTimeMillis());
                    Music music = new Music();
                    music.setTitle("Maid with the Flaxen Hair");
                    music.setDescription("测试音乐");
                    music.setMusicUrl("http://yinyueshiting.baidu.com/data2/music/123297915/1201250291415073661128.mp3?xcode=e2edf18bbe9e452655284217cdb920a7a6a03c85c06f4409");
                    music.setHQMusicUrl("http://yinyueshiting.baidu.com/data2/music/123297915/1201250291415073661128.mp3?xcode=e2edf18bbe9e452655284217cdb920a7a6a03c85c06f4409");
                    mm.setMusic(music);

                    String musicXml = MessageUtil.MusicMessageToXml(mm);
                    logger.info("musicXml:\n" + musicXml);
                    return musicXml;
                }

                if(content.contains("加入创星#")){
                    String[] ArrayString=content.split("#");
                    TextMessage tmt = new TextMessage();
                    tmt.setToUserName(FromUserName);
                    tmt.setFromUserName(ToUserName);
                    tmt.setMsgType(MessageUtil.MESSAGG_TYPE_TEXT);
                    tmt.setCreateTime(System.currentTimeMillis());
                    String xml_tmt="";
                    if(ArrayString.length!=3){
                        tmt.setContent("您输入的格式有误,请输入：加入创星#工号#身份证后六位");
                        xml_tmt = MessageUtil.textMessageToXml(tmt);
                        logger.info("xml:" + xml_tmt);
                    }else{
                        TbPersonnel tbPersonnel=winXinPersonService.findPersonByWorkNum(ArrayString[1]);
                        if(tbPersonnel==null){
                            tmt.setContent("你的信息暂未在内控系统中录入，请联系人事");
                            xml_tmt = MessageUtil.textMessageToXml(tmt);
                            logger.info("xml:" + xml_tmt);
                        }else{
                            if(tbPersonnel.getOpenId()!=null){
                                tmt.setContent("你已经加入创星");
                                xml_tmt = MessageUtil.textMessageToXml(tmt);
                                logger.info("xml:" + xml_tmt);
                            }else {
                                if (tbPersonnel.getIdCard() != null) {
                                    if (ArrayString[2].equals(tbPersonnel.getIdCard().substring(tbPersonnel.getIdCard().length() - 6, tbPersonnel.getIdCard().length()))) {
                                        tbPersonnel.setOpenId(FromUserName);
                                        Integer updateCount=winXinPersonService.updateT_Weixin_user(tbPersonnel);
                                        if(updateCount>0){
                                            tmt.setContent("恭喜您,你已经加入创星！");
                                            xml_tmt = MessageUtil.textMessageToXml(tmt);
                                            logger.info("xml:" + xml_tmt);
                                        }else{
                                            tmt.setContent("加入创星失败,请联系管理员");
                                            xml_tmt = MessageUtil.textMessageToXml(tmt);
                                            logger.info("xml:" + xml_tmt);
                                        }
                                    } else {
                                        tmt.setContent("您的身份证号后六位和系统比对有误，请核对!");
                                        xml_tmt = MessageUtil.textMessageToXml(tmt);
                                        logger.info("xml:" + xml_tmt);
                                    }
                                } else {
                                    tmt.setContent("您在内控系统里面身份证号为空，请联系人事");
                                    xml_tmt = MessageUtil.textMessageToXml(tmt);
                                    logger.info("xml:" + xml_tmt);
                                }
                            }
                        }

                    }
                    return xml_tmt;
                }


                // 响应
                TextMessage tm = new TextMessage();
                tm.setToUserName(FromUserName);
                tm.setFromUserName(ToUserName);
                tm.setMsgType(MessageUtil.MESSAGG_TYPE_TEXT);
                tm.setCreateTime(System.currentTimeMillis());
                List<WinXinPerson> winXinPersonList=winXinPersonService.getPersonByName(content);
                if(winXinPersonList.size()==0){
                    content = "您好,请检查您输入的姓名是否正确！";
                }else{
                    int wxblacklist= winXinPersonService.getBlackList(content);
                    if(wxblacklist>0){
                        content = "您好，"+content+"的个人资料属于不公开信息。";
                    }else{
                        content="";
                        for(WinXinPerson winXinPerson:winXinPersonList){
                            content = content+winXinPerson.getName() + " 个人信息,请注意保密\n"
                                    +"手机:"+winXinPerson.getPhone()+"\n"
                                    +"部门:"+winXinPerson.getDepName()+"\n";
                                    if(winXinPerson.getPosition()!=null) content=content+"职位:"+winXinPerson.getPosition()+"\n";
                                    if(winXinPerson.getTagName()!=null) content=content+"所在项目:"+winXinPerson.getTagName()+"\n";
                        }
                    }
                }

                tm.setContent(content);

                String xml = MessageUtil.textMessageToXml(tm);
                logger.info("xml:" + xml);
                return xml;
            }
            else if (MsgType.equals(MessageUtil.MESSAGG_TYPE_EVENT)) {
                String event = xmlMap.get("Event");
                if (event.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    // 订阅
                    TextMessage tm = new TextMessage();
                    tm.setToUserName(FromUserName);
                    tm.setFromUserName(ToUserName);
                    tm.setMsgType(MessageUtil.MESSAGG_TYPE_TEXT);
                    tm.setCreateTime(System.currentTimeMillis());
                    tm.setContent("你好，欢迎关注[长沙创星]公众号！[愉快]/呲牙/玫瑰\n加入创星输入格式：加入创星#工号#身份证后六位");
                    return MessageUtil.textMessageToXml(tm);
                }
                else if (event.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // 取消订阅
                    logger.info("用户【" + FromUserName + "]取消关注了。");
                }
                else if (event.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    String eventKey = xmlMap.get("EventKey");
                    if (eventKey.equals("reply_add")) {
                        TextMessage tm = new TextMessage();
                        tm.setToUserName(FromUserName);
                        tm.setFromUserName(ToUserName);
                        tm.setMsgType(MessageUtil.MESSAGG_TYPE_TEXT);
                        tm.setCreateTime(System.currentTimeMillis());
                        tm.setContent("你好，你已经加入创星之家\n" );

                        String xml = MessageUtil.textMessageToXml(tm);
                        logger.info("xml:" + xml);
                        return xml;
                    }
                    else if (eventKey.equals("reply_show")) {
                        TextMessage tm = new TextMessage();
                        tm.setToUserName(FromUserName);
                        tm.setFromUserName(ToUserName);
                        tm.setMsgType(MessageUtil.MESSAGG_TYPE_TEXT);
                        tm.setCreateTime(System.currentTimeMillis());
                        String content = FromUserName+"个人信息\n"
                                +"手机:13671645338\n"
                                +"部门:行政企管部\n"
                                +"家庭住址:长沙市高新区麓云路100号兴工国际产业园5栋6楼\n"
                                +"所在项目:内控系统";
                        tm.setContent(content);

                        String xml = MessageUtil.textMessageToXml(tm);
                        logger.info("xml:" + xml);
                        return xml;
                    }
                    else if (eventKey.equals("reply_jifen")) {
                        TextMessage tm = new TextMessage();
                        tm.setToUserName(FromUserName);
                        tm.setFromUserName(ToUserName);
                        tm.setMsgType(MessageUtil.MESSAGG_TYPE_TEXT);
                        tm.setCreateTime(System.currentTimeMillis());
                        tm.setContent("你的积分暂为开通查询");
                        String xml = MessageUtil.textMessageToXml(tm);
                        logger.info("xml:" + xml);
                        return xml;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理微信请求时发生异常：");
        }

        return str;
    }

    public String fetchAccessToken(){
        String token = null;
        String appid = PropertiesUtils.getProperty("appid");
        String requestUrl = PropertiesUtils.getProperty("access_token_url");
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("appid",appid);
        String parameterJson = JSONObject.toJSONString(parameter);
        logger.info("==调用fetchAccessToken入参["+parameterJson+"]");
        String result = HttpUtil.connectURL(requestUrl,parameterJson,"POST");
        logger.info("==调用fetchAccessToken返回["+result+"]");
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        Integer status = jsonObject.getInteger("status");
        if(status!=null&&status.intValue()==1){
            JSONObject accessToken = (JSONObject) jsonObject.get("accessToken");
            if(accessToken!=null&&accessToken.getString("accessToken")!=null){
                token = accessToken.getString("accessToken");

            }
        }
        return token;
    }

    public JsapiSignature fetchJsapiSignature(String url){
        JsapiSignature signature = null;
        String appid = PropertiesUtils.getProperty("appid");
        String requestUrl = PropertiesUtils.getProperty("signature_url");
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("appid",appid);
        parameter.put("url",url);
        String parameterJson = JSONObject.toJSONString(parameter);
        logger.info("==调用JsapiSignaturer入参["+parameterJson+"]");
        String result = HttpUtil.connectURL(requestUrl,parameterJson,"POST");
        logger.info("==调用JsapiSignature返回["+result+"]");
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        Integer status = jsonObject.getInteger("status");
        if(status!=null&&status.intValue()==1){
            JSONObject signatureJSON = (JSONObject) jsonObject.get("jsapiSignature");
            signature = new JsapiSignature();
            signature.setSignature(signatureJSON.getString("signature"));
            signature.setTimestamp(signatureJSON.getString("timestamp"));
            signature.setUrl(signatureJSON.getString("url"));
            signature.setJsapiTicket(signatureJSON.getString("jsapiTicket"));
            signature.setNoncestr(signatureJSON.getString("noncestr"));
        }
        return signature;
    }

    public UserToken getUserToken(String code){
        UserToken userToken = null;
        String appid = PropertiesUtils.getProperty("appid");
        String requestUrl = PropertiesUtils.getProperty("user_token");
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("appid",appid);
        parameter.put("code",code);
        String parameterJson = JSONObject.toJSONString(parameter);
        logger.info("==获取用户信息授权入参["+parameterJson+"]");
        String result = HttpUtil.connectURL(requestUrl,parameterJson,"POST");
        logger.info("==获取用户信息授权返回["+result+"]");
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        Integer status = jsonObject.getInteger("status");
        if(status!=null&&status.intValue()==1){
            JSONObject object = (JSONObject) jsonObject.get("userToken");
            userToken = new UserToken();
            userToken.setAccessToken(object.getString("access_token"));
            userToken.setExpiresIn(object.getInteger("expires_in"));
            userToken.setOpenid(object.getString("openid"));
            userToken.setScope(object.getString("scope"));
            userToken.setRefreshToken(object.getString("refresh_token"));

        }
        return userToken;
    }
}
