package com.trasen.imis.service;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.dao.TbAttenceLocationMapper;
import com.trasen.imis.model.TbAttenceLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: LocationService
 * @Description: 操作类型
 * @date 2017/6/16
 */
@Component
public class LocationService {
    @Autowired
    private TbAttenceLocationMapper tbAttenceLocationMapper;

    public List<TbAttenceLocation> getLocationList(Page page){
        return tbAttenceLocationMapper.getLocationList(page);
    }
    public void locationSave(TbAttenceLocation tbAttenceLocation){
        tbAttenceLocationMapper.locationSave(tbAttenceLocation);
    }
    public TbAttenceLocation locationViewForid(int pkid){
        return tbAttenceLocationMapper.locationViewForid(pkid);
    }
    public int locationUpdateForid(TbAttenceLocation location){
        return tbAttenceLocationMapper.locationUpdateForid(location);
    }
    public int locationDeleteForid(int pkid){
        return tbAttenceLocationMapper.locationDeleteForid(pkid);
    }
}
