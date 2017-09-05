package com.trasen.imis.dao;

import com.trasen.imis.model.TbTagPersonnel;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/11
 */
public interface TbTagPersonnelMapper {
    public List<TbTagPersonnel>  getTaTagPersonnelList(String workNum);

    public int deleteTaTagPersonnel(String pkid);

    public int deleteTaTagPersonnelForWorkNum(String workNum);

    public void saveTagPersonnel(List<Map<String,Object>> parms);

    public String getTagNameforWorNum(String workNum);

}
