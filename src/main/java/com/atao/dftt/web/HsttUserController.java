package com.atao.dftt.web;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.http.HsttHttp;
import com.atao.dftt.http.JkdttHttp;
import com.atao.dftt.model.HsttUser;
import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.service.HsttUserWyService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/hstt")
public class HsttUserController extends BaseController<HsttUser> {
	public static final String BASE_URL = "/HsttUser/";

	@Autowired
	private HsttUserWyService hsttUserWyService;

	@Override
	protected BaseService<HsttUser> getService() {
		return hsttUserWyService;
	}

	@RequestMapping("/test")
	public String test() throws Exception {
		HsttUser user = hsttUserWyService.queryById(7);
		HsttHttp http = HsttHttp.getInstance(user);
		Date endTime = new Date(new Date().getTime() + 5 * 60 * 1000l);
		http.getAppUserByUidV2();
		//hsttUserWyService.readNewsCoin(user);
		return null;
	}
}
