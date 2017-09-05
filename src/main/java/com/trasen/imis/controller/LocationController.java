package com.trasen.imis.controller;

import cn.trasen.core.entity.Result;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trasen.imis.model.TbAttenceLocation;
import com.trasen.imis.service.LocationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/6/16
 */
@Controller
@RequestMapping(value="/location")
public class LocationController {

    private static final Logger logger = Logger.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    /*
    *
    * 考勤list查询
    * */
    @ResponseBody
    @RequestMapping(value = "/getLocationList", method = RequestMethod.GET)
    public String getLocationList(@QueryParam("pageNo") int pageNo, @QueryParam("pageSize") int pageSize) throws JsonProcessingException {
        Page page = new Page(pageNo,pageSize);
        page.setSidx("created");
        page.setSord("desc");
        List<TbAttenceLocation> tbAttenceLocationList = locationService.getLocationList(page);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(tbAttenceLocationList);

        String json = "{\"totalPages\": " + String.valueOf(page.getTotalPages()) + ",\"pageNo\": "
                + String.valueOf(page.getPageNo()) + ",\"totalCount\":"
                + String.valueOf(page.getTotalCount()) + ",\"list\":" + data + ",\"pageSize\": "+page.getPageSize()+"}";

        return json;

    }
    /*
    * 考勤地点保存
    * */
    @ResponseBody
    @RequestMapping(value="/locationSave", method = RequestMethod.POST)
    public Result locationSave(@RequestBody  TbAttenceLocation tbAttenceLocation)  {
        Result result=new Result();
        //数据更新
        if(tbAttenceLocation.getPkid()!=null){
            tbAttenceLocation.setOperator("系统");
            int locationInt=locationService.locationUpdateForid(tbAttenceLocation);
            if(locationInt==0){
                result.setSuccess(true);
                result.setMessage("无新数据更新");
            }else{
                result.setSuccess(true);
                result.setMessage("数据更新成功");
            }
            result.setObject(tbAttenceLocation);
            return  result;
        }
        String coordinate= null;
        String conrdinateForbd09ll=null;
        try {
            /*coordinate = new BaiDuUtil().getCoordinateforAddress(tbAttenceLocation.getAddress());
            conrdinateForbd09ll= new BaiDuUtil().getCoordinateForbd09ll(coordinate);
            String[] coordinateArray=conrdinateForbd09ll.split(",");
            tbAttenceLocation.setLatitude(Float.parseFloat(coordinateArray[1]));
            tbAttenceLocation.setLongitude(Float.parseFloat(coordinateArray[0]));*/
            locationService.locationSave(tbAttenceLocation);
            result.setSuccess(true);
            result.setObject(tbAttenceLocation);
            result.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("保存失败");
            return  result;
        }


        return  result;

    }
    /**
     * 查看考勤详细
     */

    @ResponseBody
    @RequestMapping(value="/locationViewForid", method = RequestMethod.POST)
    public Result locationViewForid(@QueryParam("pkid") int pkid) throws JsonProcessingException {
        TbAttenceLocation tbAttenceLocation=locationService.locationViewForid(pkid);

        Result result=new Result();
        if (tbAttenceLocation==null){
            result.setSuccess(false);
            result.setMessage("数据查询失败");
        }
        ObjectMapper mapper = new ObjectMapper();
        String data=mapper.writeValueAsString(tbAttenceLocation);
        result.setSuccess(true);
        result.setObject(data);
        return result;
    }
    /*
    * 更新考勤地点
    * */
    @ResponseBody
    @RequestMapping(value="/locationUpdateForid", method = RequestMethod.POST)
    public Result locationUpdateForid(@RequestBody  TbAttenceLocation location)  {
        Result result=new Result();
        if(String.valueOf(location.getPkid())==null){
            result.setSuccess(false);
            result.setMessage("数据更新失败");
            return result;
        }
        location.setOperator("系统");
        int locationInt=locationService.locationUpdateForid(location);
        if(locationInt==0){
            result.setSuccess(true);
            result.setMessage("数据无更新");
        }else{
            result.setSuccess(true);
            result.setMessage("数据更新成功");
        }
        return result;
    }

    /*
    * 删除考勤地址
    * */
    @ResponseBody
    @RequestMapping(value="/locationDeleteForid", method = RequestMethod.POST)
    public Result locationDeleteForid(@QueryParam("pkid") int pkid){
        Result result=new Result();
        int code=locationService.locationDeleteForid(pkid);
        if(code==0){
            result.setSuccess(false);
            result.setMessage("删除失败");
        }else{
            result.setSuccess(true);
            result.setMessage("删除成功");
        }
        return result;
    }
}
