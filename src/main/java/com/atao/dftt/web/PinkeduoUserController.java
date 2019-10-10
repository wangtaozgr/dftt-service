package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.PinkeduoUser;
import com.atao.dftt.service.PinkeduoUserWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + PinkeduoUserController.BASE_URL)
public class PinkeduoUserController extends BaseController<PinkeduoUser> {
    public static final String BASE_URL = "/PinkeduoUser/";

    @Autowired
    private PinkeduoUserWyService pinkeduoUserWyService;

    @Override
    protected BaseService<PinkeduoUser> getService() {
        return pinkeduoUserWyService;
    }
}
