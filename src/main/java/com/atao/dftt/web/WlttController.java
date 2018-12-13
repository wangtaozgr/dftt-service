package com.atao.dftt.web;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.http.WlttHttp;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.service.WlttCoinRecordWyService;
import com.atao.dftt.service.WlttWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/wltt")
public class WlttController extends BaseController<Wltt> {

	@Autowired
	private WlttWyService wlttWyService;
	@Resource
	private WlttCoinRecordWyService wlttCoinRecordWyService;

	@Override
	protected BaseService<Wltt> getService() {
		return wlttWyService;
	}

	@RequestMapping("/test")
	public String test(String result, String device, String imei) throws Exception {
		Wltt user = wlttWyService.queryById(1);
		WlttHttp dftt = WlttHttp.getInstance(user);
		dftt.ad();
		// dftt.daka();
		// dftt.login();
		// dftt.qiandao();
		// System.out.println(dftt.firstGetTask("15489358"));
		//wlttCoinRecordWyService.updateCoin(user);
		return null;
	}

}
