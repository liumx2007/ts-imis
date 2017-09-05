package com.trasen.imis.service;

import com.trasen.imis.dao.TbPersonnelMapper;
import com.trasen.imis.dao.TuserMaperr;
import com.trasen.imis.model.TbPersonnel;
import com.trasen.imis.model.Tuser;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/28
 */
@Component
public class TuserService {

    @Autowired
    private TuserMaperr tuserMaperr;

    @Autowired
    private TbPersonnelMapper tbPersonnelMapper;

    public int insertGetId(Tuser tuser){
        int pkid=tuserMaperr.insertGetId(tuser);
        return pkid;
    }
    public int updateTuser(Tuser tuser){
        int updateCount=tuserMaperr.updateTuser(tuser);
        return updateCount;
    }
    public Tuser selectByperId(String perId,String workNum){
        Tuser tuser=tuserMaperr.selectByperId(perId);
        return tuser;
    }
}
