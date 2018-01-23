package com.trasen.imis.service;

import cn.trasen.commons.util.DateUtil;
import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.cache.GlobalCache;
import com.trasen.imis.dao.TbAttenceMapper;
import com.trasen.imis.model.AttenceVo;
import com.trasen.imis.model.TbAttenceLocation;
import com.trasen.imis.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangxiahui on 17/6/23.
 */
@Component
public class AttenceService {

    Logger logger = Logger.getLogger(AttenceService.class);

    private GlobalCache globalCache = GlobalCache.getGlobalCache();

    @Autowired
    TbAttenceMapper tbAttenceMapper;

    @Autowired
    MobileAttenceService mobileAttenceService;

    public List<AttenceVo> searchAttList(AttenceVo attenceVo,Page page){
        List<AttenceVo> list = new ArrayList<>();
        if(attenceVo!=null){
            list = tbAttenceMapper.searchAttList(attenceVo,page);
            for(AttenceVo vo : list){
                vo.setAttType(1);
            }
        }
        return list;
    }

    public List<AttenceVo> searchLackAttList(AttenceVo attenceVo,Page page){
        List<AttenceVo> list = new ArrayList<>();
        if(attenceVo!=null){
            list = tbAttenceMapper.searchLackAttList(attenceVo,page);
            for(AttenceVo vo : list){
                vo.setAttType(2);
            }
        }
        return list;
    }

    public List<AttenceVo> searchActualLackAttList(AttenceVo attenceVo,Page page){
        List<AttenceVo> list = new ArrayList<>();
        if(attenceVo!=null){
            //排序和当日未签到修改
            attenceVo.setAttenceDate(DateUtils.getDate("yyyy-MM-dd"));
            page.setSidx("work_num");
            list = tbAttenceMapper.searchActualLackAttList(attenceVo,page);
            for(AttenceVo vo : list){
                vo.setAttType(2);
            }
        }
        return list;
    }

    public List<AttenceVo> searchLeaveAttList(AttenceVo attenceVo,Page page){
        List<AttenceVo> list = new ArrayList<>();
        if(attenceVo!=null){
            list = tbAttenceMapper.searchLeaveAttList(attenceVo,page);
        }
        return list;
    }

    public Map<String,Integer> countAttence(AttenceVo attenceVo){
        Map<String,Integer> map = new HashMap<>();
        map.put("signInNum",0);
        map.put("lateNum",0);
        map.put("backNum",0);
        map.put("lackNum",0);
        map.put("addWorkNum",0);
        map.put("leaveNum",0);
        if(attenceVo!=null){
            map.put("signInNum",tbAttenceMapper.signInNum(attenceVo));
            map.put("lateNum",tbAttenceMapper.lateNum(attenceVo));
            map.put("backNum",tbAttenceMapper.backNum(attenceVo));
            //map.put("lackNum",tbAttenceMapper.lackNum(attenceVo));

            map.put("addWorkNum",tbAttenceMapper.addWorkNum(attenceVo));
            map.put("leaveNum",tbAttenceMapper.leaveNum(attenceVo));
            //通过当天日期获取缺勤人数
            attenceVo.setAttenceDate(DateUtils.getDate("yyyy-MM-dd"));
            map.put("lackNum",tbAttenceMapper.lateActualNum(attenceVo));
        }
        return map;
    }

    /**
     * 考勤规则设置说明:优先读取考勤设置的试用范围(标签),如果考勤范围有值,则设置的部门无效
     * 如果考勤规则为空,则部门规则有效。
     * 部门规则可以为多个部门以"|"分隔,多个部门会拆解为多个规则
     * */
    public Map<String,TbAttenceLocation> getAttenceRuleData(){
        List<TbAttenceLocation> list = tbAttenceMapper.getAttenceLoactionList();
        Map<String,TbAttenceLocation> map = new HashMap<>();
        if(list!=null){
            for(TbAttenceLocation location : list){
                if(location.getTagId()!=null){
                    String key = location.getTagId().replace("|","");
                    map.put(key,location);
                    logger.info("规则key:["+key+"]===value:["+location.getPkid()+"]===加入缓存==");
                }else if(location.getDepId()!=null){
                    String [] keys = location.getDepId().split("\\|");
                    for(int i = 0;i<keys.length;i++){
                        if(!StringUtil.isEmpty(keys[i])){
                            map.put(keys[i],location);
                            logger.info("规则key:["+keys[i]+"]===value:["+location.getPkid()+"]===加入缓存==");
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 考勤规则匹配:每个人都有个tag,tag为该员工所在部门的部门路径,在试用部门路径,
     * 匹配部门时,位置最大(最下级部门),为有效规则
     *
     * */
    public TbAttenceLocation getAttenceRule(String tagId,Map<String,TbAttenceLocation> attRuleMap){
        int point = -1;
        TbAttenceLocation attenceLocation = null;
        if(attRuleMap!=null&&attRuleMap.size()>0){
            Set<String> keys = attRuleMap.keySet();
            for(String key : keys){
                int p = tagId.indexOf("|"+key+"|");
                logger.info("员工标签["+tagId+"],匹配规则["+key+"]==["+p+"]");
                if(p>-1&&p>=point){
                    attenceLocation = attRuleMap.get(key);
                    point = p;
                }
            }
        }
        return attenceLocation;

    }

    /**
     * 处理某一天的缺勤数据
     * */
    public boolean makeAttenceLack(String date){
        if(!StringUtil.isEmpty(date)){
            Integer count = tbAttenceMapper.selectLackAttence(date);
            if(count==0){
                //String isHolidayDate = date.replace("-","");
                Date dateT = DateUtil.stringToDate(date);
                String week = DateUtils.getWeek(dateT);
                //获取已考勤的工号
                List<String> attList = tbAttenceMapper.queryAttenceToDate(date);
                //获取所有人
                List<AttenceVo> weixinList = tbAttenceMapper.queryWeixinUser();

                Map<String,AttenceVo> userMap = new HashMap<>();
                for(AttenceVo attenceVo : weixinList){
                    userMap.put(attenceVo.getWorkNum(),attenceVo);
                }
                List<String> workNumList = new ArrayList<>();
                Set<String> set = userMap.keySet();
                workNumList.addAll(set);
                List<AttenceVo> lackList = new ArrayList<>();
                for(String workNum : workNumList){
                    if(!attList.contains(workNum)){
                        AttenceVo lack = userMap.get(workNum);
                        //判断该员工是否在工作日
                        String key = lack.getTagId();
                        Integer type = 0;
                        if(key!=null&&!StringUtil.isEmpty(key)) {
                            TbAttenceLocation rule = getAttenceRule(key, globalCache.getAttRuleMap());
                            if (rule == null) {
                                logger.info("===姓名[" + lack.getName() + "]没有匹配到考勤规则,计算缺勤失败");
                                continue;
                            }
                            String outDate = rule.getOutDate();
                            if(outDate!=null&&outDate.indexOf(date)>0){
                                type = 1;//在排除日期内为加班
                            }else if(mobileAttenceService.isHoliday(date)
                                    ||(rule.getWorkingDay()!=null&&rule.getWorkingDay().indexOf(week)==-1)){
                                type = 1;//在法定节假日,或者非工作日时间算加班
                            }
                        }
                        if(type==0){
                            lack.setAttenceDate(date);
                            lack.setWeek(week);
                            lackList.add(lack);
                        }
                    }
                }
                if(lackList.size()>0){
                    Map<String,Object> para = new HashMap<>();
                    para.put("list",lackList);
                    tbAttenceMapper.insertLackAttence(para);
                }
                return true;
            }
        }
        return false;




    }
    /*
    * 导出excel方法
    *
    * */
    public List<AttenceVo> excelExprotAttList(AttenceVo attenceVo){
        List<AttenceVo> list = new ArrayList<>();
        if(attenceVo!=null){
            list = tbAttenceMapper.searchAttList(attenceVo);
        }
        return list;
    }

    public List<AttenceVo> excelExprotLackAttList(AttenceVo attenceVo){
        List<AttenceVo> list = new ArrayList<>();
        if(attenceVo!=null){
            //排序和当日未签到修改
            attenceVo.setAttenceDate(DateUtils.getDate("yyyy-MM-dd"));
            list = tbAttenceMapper.searchActualLackAttList(attenceVo);
        }
        return list;
    }
    public List<AttenceVo> excelExprotLeaveAttList(AttenceVo attenceVo){
        List<AttenceVo> list = new ArrayList<>();
        if(attenceVo!=null){
            list = tbAttenceMapper.searchLeaveAttList(attenceVo);
        }
        return list;
    }

    /**
     * 删除考勤数据
     * */
    public void deleAttence(Long pkid){
        tbAttenceMapper.deleAttence(pkid);
    }

    /**
     * 删除缺勤数据
     * */
    public void deleLackAttence(Long pkid){
        tbAttenceMapper.deleLackAttence(pkid);
    }

    public String getDeptCode(String depName){
        if(depName!=null){
            String tagId =  tbAttenceMapper.getDeptCode(depName);
            if(tagId!=null){
                return "|"+tagId+"|";
            }
        }
        return null;
    }
}
