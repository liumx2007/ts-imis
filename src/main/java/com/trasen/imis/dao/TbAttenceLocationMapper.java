package com.trasen.imis.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.trasen.imis.model.TbAttenceLocation;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: TbAttenceLocationMapper
 * @Description: 操作类型
 * @date 2017/6/16
 */
public interface TbAttenceLocationMapper {
    public List<TbAttenceLocation> getLocationList(Page page);
    public void locationSave(TbAttenceLocation tbAttenceLocation);
    public TbAttenceLocation locationViewForid(int pkid);
    public int locationUpdateForid(TbAttenceLocation location);
    public int locationDeleteForid(int pkid);
}
