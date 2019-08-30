package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.TaottCoinRecord;
import com.atao.dftt.service.TaottCoinRecordWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("taottRecord")
public class TaottCoinRecordController extends BaseController<TaottCoinRecord> {
    public static final String BASE_URL = "/TaottCoinRecord/";

    @Autowired
    private TaottCoinRecordWyService taottCoinRecordWyService;

    @Override
    protected BaseService<TaottCoinRecord> getService() {
        return taottCoinRecordWyService;
    }
}
