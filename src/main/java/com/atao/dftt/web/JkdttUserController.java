package com.atao.dftt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.http.JkdttHttp;
import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.service.JkdttUserWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("jkdtt")
public class JkdttUserController extends BaseController<JkdttUser> {
	public static final String BASE_URL = "/JkdttUser/";

	@Autowired
	private JkdttUserWyService jkdttUserWyService;

	@Override
	protected BaseService<JkdttUser> getService() {
		return jkdttUserWyService;
	}

	@RequestMapping("/test")
	public String test(String result, String device, String imei) throws Exception {
		JkdttUser user = jkdttUserWyService.queryById(11);
		JkdttHttp http = JkdttHttp.getInstance(user);
		jkdttUserWyService.readNewsCoin(user);
		return null;
	}
}
