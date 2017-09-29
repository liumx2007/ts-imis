package com.trasen.imis.dao;

import com.trasen.imis.model.TbProModule;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/22
 */
public interface TbProductMapper {
    TbProModule selectProductCount(String pkid);
    void saveProduct(TbProModule tbProduct);
    void updateProduct(TbProModule tbProduct);
}
