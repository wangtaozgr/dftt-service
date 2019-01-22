package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;

import com.atao.dftt.model.JkdttCoinRecord;
import com.atao.dftt.service.JkdttCoinRecordWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("${adminPath}" + JkdttCoinRecordController.BASE_URL)
public class JkdttCoinRecordController extends BaseController<JkdttCoinRecord> {
    public static final String BASE_URL = "/JkdttCoinRecord/";

    @Autowired
    private JkdttCoinRecordWyService jkdttCoinRecordWyService;

    @Override
    protected BaseService<JkdttCoinRecord> getService() {
        return jkdttCoinRecordWyService;
    }
}
