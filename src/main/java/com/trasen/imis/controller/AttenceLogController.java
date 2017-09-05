package com.trasen.imis.controller;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trasen.imis.model.AttenceLogVo;
import com.trasen.imis.service.AttenceLogService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/6/27
 */
@Controller
@RequestMapping(value="/attencelog")
public class AttenceLogController {

    private static final Logger logger = Logger.getLogger(AttenceLogController.class);

    @Autowired
    private AttenceLogService attenceLogService;

    @RequestMapping(value="/getAttenceLogList" ,method = RequestMethod.POST)
    @ResponseBody
    public String getAttenceLogList(@RequestBody HashMap<String,Object> attenceMap) throws JsonProcessingException {
        if(attenceMap.get("pageNo").toString()==null||attenceMap.get("pageSize")==null){
            String json = "{\"mesg\": " + "查询失败" + ",\"code\": "+"-1"+"}";

            return json;
        }else{
            Integer pageNo = MapUtils.getInteger(attenceMap, "pageNo");
            Integer pageSize = MapUtils.getInteger(attenceMap, "pageSize");
            Page page = new Page(pageNo,pageSize);
            page.setSidx("attence_date");
            page.setSord("desc");
            List<AttenceLogVo> attenceLogVoList= attenceLogService.getAttenceLogList(attenceMap,page);
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(attenceLogVoList);
            String json = "{\"totalPages\": " + String.valueOf(page.getTotalPages()) + ",\"pageNo\": "
                    + String.valueOf(page.getPageNo()) + ",\"totalCount\":"
                    + String.valueOf(page.getTotalCount()) + ",\"list\":" + data + ",\"pageSize\": "+page.getPageSize()+",\"code\": "+"1"+"}";

            return json;
        }
    }

}
