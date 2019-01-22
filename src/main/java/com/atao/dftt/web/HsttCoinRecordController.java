package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.HsttCoinRecord;
import com.atao.dftt.service.HsttCoinRecordWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + HsttCoinRecordController.BASE_URL)
public class HsttCoinRecordController extends BaseController<HsttCoinRecord> {
    public static final String BASE_URL = "/HsttCoinRecord/";

    @Autowired
    private HsttCoinRecordWyService hsttCoinRecordWyService;

    @Override
    protected BaseService<HsttCoinRecord> getService() {
        return hsttCoinRecordWyService;
    }
}
