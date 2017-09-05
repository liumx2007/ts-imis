package com.trasen.imis.controller.vo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/13.
 */
public class ViewExcel extends AbstractExcelView {
    @Override
    public void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String filename = "抽奖活动人员名单.xls";//设置下载时客户端Excel的名称
        filename = "filename";//处理中文文件名
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + filename);
        OutputStream ouputStream = httpServletResponse.getOutputStream();
        hssfWorkbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

}
