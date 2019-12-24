package com.atao.dftt.mapper;

import com.atao.dftt.model.PddMallGoods;

import java.util.List;

import com.atao.base.mapper.BaseMapper;
import com.atao.base.support.MyBatisDao;

/**
 * @Description:
 * @author twang
 */
@MyBatisDao
public interface PddMallGoodsMapper extends BaseMapper<PddMallGoods> {
	//查询所有没有商品id的列表
	List<PddMallGoods> queryNoGoodsIdList();

}
