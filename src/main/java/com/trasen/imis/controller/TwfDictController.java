package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trasen.imis.model.TwfDict;
import com.trasen.imis.service.TwfDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/6/23
 */
@Controller
@RequestMapping(value="/twfDict")
public class TwfDictController {
    @Autowired
    private TwfDictService twfDictService;

    @RequestMapping(value = "/getTwfDictForType", method = RequestMethod.GET)
    @ResponseBody
    public Result getTwfDictForType(@QueryParam("type") String type) throws JsonProcessingException {

        Result result=new Result();
        if(type==null){
            result.setSuccess(false);
            result.setMessage("查询数据字典失败");
            return result;
        }
        List<TwfDict> twfDictList=twfDictService.getTwfDictForType(Integer.valueOf(type));
        /*ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(twfDictList);*/
        /*String json = "{\"list\": " + data + "}";*/
        result.setSuccess(true);
        result.setObject(twfDictList);
        return  result;

    }
}
