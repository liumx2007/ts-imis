package com.trasen.imis.controller;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.google.common.collect.ImmutableMap;
import com.trasen.imis.controller.vo.TestVo;
import com.trasen.imis.model.UserVo;
import com.trasen.imis.service.TbTestService;
import com.trasen.imis.service.TestService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangxiahui on 17/5/26.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @Autowired
    TbTestService tbTestService;


    private static final Logger logger = Logger.getLogger(TestController.class);




    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map<String, Object> getTestID(@PathVariable String id) {
        String name = testService.getTestName(id);
        String name1 = tbTestService.getTestName(id);
        return new ImmutableMap.Builder<String, Object>().put("status", 1)
                .put("msg", "成功").put("data",name+"+哈哈+"+name1).build();
    }

    @ResponseBody
    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    public Map<String,Object> queryList(@QueryParam("pageNo") int pageNo,@QueryParam("pageSize") int pageSize) {
        Page page = new Page(pageNo,pageSize);
        List<TestVo> list = tbTestService.getList(page);
        return new ImmutableMap.Builder<String, Object>().put("code", 1).put("pageNo",page.getPageNo()).put("pageSize", page.getPageSize())
                .put("totalCount",page.getTotalCount()).put("totalPages",page.getTotalPages()).put("list",list).build();
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String,Object>  loginValid(@RequestBody UserVo userVo) {
        String name = userVo.getName();
        String password = userVo.getPassword();
        Map<String,Object> map = new HashMap();
        map.put("name",name);
        map.put("password",password);
        return map;
    }


}
