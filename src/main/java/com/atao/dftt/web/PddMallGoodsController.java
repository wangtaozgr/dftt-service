package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.PddMallGoods;
import com.atao.dftt.service.PddMallGoodsWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + PddMallGoodsController.BASE_URL)
public class PddMallGoodsController extends BaseController<PddMallGoods> {
    public static final String BASE_URL = "/PddMallGoods/";

    @Autowired
    private PddMallGoodsWyService pddMallGoodsWyService;

    @Override
    protected BaseService<PddMallGoods> getService() {
        return pddMallGoodsWyService;
    }
}
