package com.atao.dftt.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.http.ZqttHttp;
import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.service.ZqttUserWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/zqtt")
public class ZqttUserController extends BaseController<ZqttUser> {
	public static final String BASE_URL = "/ZqttUser/";

	@Autowired
	private ZqttUserWyService zqttUserWyService;

	@Override
	protected BaseService<ZqttUser> getService() {
		return zqttUserWyService;
	}

	@RequestMapping("/test")
	public String test() throws Exception {
		ZqttUser user = zqttUserWyService.queryById(2);
		ZqttHttp http = ZqttHttp.getInstance(user);
		zqttUserWyService.searchTask(user);
		//zqttUserWyService.readNewsCoin(user);
		//http.newsList();
		//http.article_collect("13984387");
		//zqttUserWyService.readNewsCoin(user);
		//zqttUserWyService.readNewsCoin(user);
		//zqttUserWyService.readNewsCoin(user);
		//ZqttHttp http = ZqttHttp.getInstance(user);
		/*
		 * Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
		 * mayittUserWyService.readVideoCoin(user, endTime);
		 */
		/*
		 * JSONObject login = http.login();
		 * user.setZqkey(login.getJSONObject("items").getString("zqkey"));
		 * user.setZqkeyId(login.getJSONObject("items").getString("zqkey_id"));
		 * zqttUserWyService.save(user); http = http.refreshUser(user);
		 */
		// http.cointxWx(1);

		
		return null;
	}
}
