package com.trasen.imis.dao;

import com.trasen.imis.model.TbProduct;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/22
 */
public interface TbProductMapper {
    TbProduct selectProductCount(String pkid);
    void saveProduct(TbProduct tbProduct);
    void updateProduct(TbProduct tbProduct);
}
