package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.PddMall;
import com.atao.dftt.service.PddMallWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + PddMallController.BASE_URL)
public class PddMallController extends BaseController<PddMall> {
    public static final String BASE_URL = "/PddMall/";

    @Autowired
    private PddMallWyService pddMallWyService;

    @Override
    protected BaseService<PddMall> getService() {
        return pddMallWyService;
    }
}
