package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.ZhuankeUser;
import com.atao.dftt.service.ZhuankeUserWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + ZhuankeUserController.BASE_URL)
public class ZhuankeUserController extends BaseController<ZhuankeUser> {
    public static final String BASE_URL = "/ZhuankeUser/";

    @Autowired
    private ZhuankeUserWyService zhuankeUserWyService;

    @Override
    protected BaseService<ZhuankeUser> getService() {
        return zhuankeUserWyService;
    }
}
