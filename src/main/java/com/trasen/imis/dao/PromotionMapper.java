package com.trasen.imis.dao;

import com.trasen.imis.model.*;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/8/29
 */
public interface PromotionMapper {

    public TbJfPerson getJfPersonByopenId(String openId);

    public List<TbTree> selectCompany();

    public TbJfRank selectNextRank(int px);

    public TbJfRank selectNextCjRank(int px);

    public TbJfRank selectWzRank(int px);

    public void savaCheck(TbRankCheck tbRankCheck);

    public Integer getPersonJfCount(String workNum);

    public void savaPersonJf(TbJfPerson tbJfPerson);

    public Integer selectTbCheckCount(String workNum);

    public List<TbJfRecord> getJfRecordByOpendId(String openId);

    public TbJfPerson getPersonByopenId(String openId);

}
