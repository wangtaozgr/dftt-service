package com.atao.dftt.web;

import com.alibaba.fastjson.JSONObject;
import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.http.PddHttp;
import com.atao.dftt.model.PddUser;
import com.atao.dftt.service.PddUserWyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/pdd")
public class PddUserController extends BaseController<PddUser> {
    public static final String BASE_URL = "/PddUser/";

    @Autowired
    private PddUserWyService pddUserWyService;

    @Override
    protected BaseService<PddUser> getService() {
        return pddUserWyService;
    }
    
    @RequestMapping("/productDetail")
	public Object updateTask() throws Exception {
    	PddUser user = pddUserWyService.queryUserByUsername("17755117870");
		PddHttp http = PddHttp.getInstance(user);
		JSONObject obj = http.productChromeDetail("5239354092");
		return obj;
	}
}
