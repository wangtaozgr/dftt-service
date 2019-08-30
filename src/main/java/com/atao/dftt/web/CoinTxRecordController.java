package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.service.CoinTxRecordWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + CoinTxRecordController.BASE_URL)
public class CoinTxRecordController extends BaseController<CoinTxRecord> {
    public static final String BASE_URL = "/CoinTxRecord/";

    @Autowired
    private CoinTxRecordWyService coinTxRecordWyService;

    @Override
    protected BaseService<CoinTxRecord> getService() {
        return coinTxRecordWyService;
    }
}
