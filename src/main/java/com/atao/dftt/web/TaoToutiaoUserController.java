package com.atao.dftt.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
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
		taoToutiaoUserWyService.cointx(user);
		/*JSONObject result = http.readNews("10920889");
		 if (result.getIntValue("code") == 20012) {// 用户登陆过期
				JSONObject loginInfo = http.login();
				if (loginInfo.getIntValue("code") == 0) {
					logger.info("taott-{}:用户登陆已失效，重新登陆成功，更新用户信息", user.getUsername());
					String ticket = loginInfo.getJSONObject("result").getString("ticket");
					user.setTicket(ticket);
					taoToutiaoUserWyService.updateBySelect(user);
					http = http.refreshUser(user);
				} else {
					long readNum = user.getLimitReadNum();
					user.setReadNum(readNum);
					user.setReadTime(new Date());
					taoToutiaoUserWyService.updateBySelect(user);
					logger.error("taott-{}:用户登陆已失效，登陆失败，设置成最大阅读数", user.getUsername());
				}
			} */
		//JSONObject object = http.login();
		/*JSONObject loginInfo = http.login();
		if (loginInfo.getIntValue("code") == 0) {
			logger.info("taott-{}:用户登陆已失效，重新登陆成功，更新用户信息", user.getUsername());
			String ticket = loginInfo.getJSONObject("result").getString("ticket");
			user.setTicket(ticket);
			taoToutiaoUserWyService.updateBySelect(user);
			http.user = user;
		}*/
		//Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
		//taoToutiaoUserWyService.readNewsCoin(user, endTime);
		//Date endTime = new Date(new Date().getTime() + 10 * 60 * 1000l);
		//taoToutiaoUserWyService.readNewsCoin(user, endTime);
		// http.daka();
		//taottCoinRecordWyService.updateCoin(user);
		return null;
	}
}
