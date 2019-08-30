package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.PddUser;
import com.atao.dftt.service.PddUserWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + PddUserController.BASE_URL)
public class PddUserController extends BaseController<PddUser> {
    public static final String BASE_URL = "/PddUser/";

    @Autowired
    private PddUserWyService pddUserWyService;

    @Override
    protected BaseService<PddUser> getService() {
        return pddUserWyService;
    }
}
