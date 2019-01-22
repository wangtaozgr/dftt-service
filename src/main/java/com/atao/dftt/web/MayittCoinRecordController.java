package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.MayittCoinRecord;
import com.atao.dftt.service.MayittCoinRecordWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + MayittCoinRecordController.BASE_URL)
public class MayittCoinRecordController extends BaseController<MayittCoinRecord> {
    public static final String BASE_URL = "/MayittCoinRecord/";

    @Autowired
    private MayittCoinRecordWyService mayittCoinRecordWyService;

    @Override
    protected BaseService<MayittCoinRecord> getService() {
        return mayittCoinRecordWyService;
    }
}
