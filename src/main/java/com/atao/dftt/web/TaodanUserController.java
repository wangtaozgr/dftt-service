package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.TaodanUser;
import com.atao.dftt.service.TaodanUserWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + TaodanUserController.BASE_URL)
public class TaodanUserController extends BaseController<TaodanUser> {
    public static final String BASE_URL = "/TaodanUser/";

    @Autowired
    private TaodanUserWyService taodanUserWyService;

    @Override
    protected BaseService<TaodanUser> getService() {
        return taodanUserWyService;
    }
}
