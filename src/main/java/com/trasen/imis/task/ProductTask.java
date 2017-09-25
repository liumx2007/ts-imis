package com.trasen.imis.task;

import com.trasen.imis.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/22
 */
@Component
public class ProductTask implements Runnable{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductService productService;

    @Override
    public void run() {
        logger.info("======================同步产品数据开始=====================");
        productService.saveOrUpdateProductList();
        logger.info("======================同步产品数据结束=====================");
    }
}
