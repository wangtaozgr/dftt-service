package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.DdhpUser;
import com.atao.dftt.service.DdhpUserWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + DdhpUserController.BASE_URL)
public class DdhpUserController extends BaseController<DdhpUser> {
    public static final String BASE_URL = "/DdhpUser/";

    @Autowired
    private DdhpUserWyService ddhpUserWyService;

    @Override
    protected BaseService<DdhpUser> getService() {
        return ddhpUserWyService;
    }
}
