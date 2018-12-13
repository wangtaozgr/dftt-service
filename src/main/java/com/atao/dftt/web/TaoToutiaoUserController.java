package com.atao.dftt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.http.TaottHttp;
import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.service.TaoToutiaoUserWyService;
import com.atao.dftt.service.TaottCoinRecordWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/taott")
public class TaoToutiaoUserController extends BaseController<TaoToutiaoUser> {
	public static final String BASE_URL = "/TaoToutiaoUser/";

	@Autowired
	private TaoToutiaoUserWyService taoToutiaoUserWyService;
	@Autowired
	private TaottCoinRecordWyService taottCoinRecordWyService;

	@Override
	protected BaseService<TaoToutiaoUser> getService() {
		return taoToutiaoUserWyService;
	}

	@RequestMapping("/test")
	public String test() throws Exception {
		TaoToutiaoUser user = taoToutiaoUserWyService.queryById(1);
		TaottHttp http = TaottHttp.getInstance(user);
		// http.login();
		//Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
		//taoToutiaoUserWyService.readNewsCoin(user, endTime);
		// http.daka();
		taottCoinRecordWyService.updateCoin(user);
		return null;
	}
}
