package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.WlttCoinRecord;
import com.atao.dftt.service.WlttCoinRecordWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + WlttCoinRecordController.BASE_URL)
public class WlttCoinRecordController extends BaseController<WlttCoinRecord> {
    public static final String BASE_URL = "/WlttCoinRecord/";

    @Autowired
    private WlttCoinRecordWyService wlttCoinRecordWyService;

    @Override
    protected BaseService<WlttCoinRecord> getService() {
        return wlttCoinRecordWyService;
    }
}
