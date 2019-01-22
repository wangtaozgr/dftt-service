package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.ZqttCoinRecord;
import com.atao.dftt.service.ZqttCoinRecordWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + ZqttCoinRecordController.BASE_URL)
public class ZqttCoinRecordController extends BaseController<ZqttCoinRecord> {
    public static final String BASE_URL = "/ZqttCoinRecord/";

    @Autowired
    private ZqttCoinRecordWyService zqttCoinRecordWyService;

    @Override
    protected BaseService<ZqttCoinRecord> getService() {
        return zqttCoinRecordWyService;
    }
}
