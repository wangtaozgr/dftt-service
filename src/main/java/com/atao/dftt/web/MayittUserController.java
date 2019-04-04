package com.atao.dftt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.http.MayittHttp;
import com.atao.dftt.model.MayittUser;
import com.atao.dftt.service.MayittUserWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("mayitt")
public class MayittUserController extends BaseController<MayittUser> {
	public static final String BASE_URL = "/MayittUser/";

	@Autowired
	private MayittUserWyService mayittUserWyService;

	@Override
	protected BaseService<MayittUser> getService() {
		return mayittUserWyService;
	}

	@RequestMapping("/test")
	public String test() throws Exception {
		MayittUser user = mayittUserWyService.queryById(1);
		MayittHttp http = MayittHttp.getInstance(user);
		/*Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
		mayittUserWyService.readVideoCoin(user, endTime);*/
		//mayittUserWyService.readNewsCoin(user);
		mayittUserWyService.daka(user);
		return null;
	}
}
