package com.trasen.imis.dao;

import com.trasen.imis.model.TwfDict;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/6/23
 */
public interface TwfDictMapper {
    public List<TwfDict>  getTwfDictForType(int type);
}
