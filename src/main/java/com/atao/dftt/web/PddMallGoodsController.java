package com.atao.dftt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.model.PddMallGoods;
import com.atao.dftt.service.PddMallGoodsWyService;
import com.atao.dftt.service.PddMallWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/pddmallgoods")
public class PddMallGoodsController extends BaseController<PddMallGoods> {
	public static final String BASE_URL = "/PddMallGoods/";

	@Autowired
	private PddMallGoodsWyService pddMallGoodsWyService;
	@Autowired
	private PddMallWyService pddMallWyService;


	@Override
	protected BaseService<PddMallGoods> getService() {
		return pddMallGoodsWyService;
	}

	@RequestMapping("/updateGoodsName")
	public Object updateGoodsName() throws Exception {
		pddMallGoodsWyService.updateGoodsName();
		return null;
	}

	@RequestMapping("/orderPageData")
	public Object orderPageData() throws Exception {
		return pddMallGoodsWyService.queryNoGoodsIdList();
	}
	
	@RequestMapping("/saveGoodsId")
	public Object saveGoodsId(String mallId, String goodsImg, String goodsId) throws Exception {
		pddMallGoodsWyService.savePddMallGoods(mallId, goodsImg, goodsId);
		return null;
	}
	
	@RequestMapping("/saveMall")
	public Object saveMall(String mallId, String mallName, String mallLogo) throws Exception {
		pddMallWyService.savePddMall(mallId, mallName, mallLogo);
		return "店铺保存成功";
	}
	
	@RequestMapping("/saveMallName")
	public Object saveMallName(String mallName) throws Exception {
		boolean success = pddMallWyService.saveMallName(mallName);
		if(success) return "店铺保存成功";
		return "没有找到店铺";
	}
}
