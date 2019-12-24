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
	public Object productDetail(String goodsId, String username) throws Exception {
    	PddUser user = pddUserWyService.queryUserByUsername(username);
		PddHttp http = PddHttp.getInstance(user);
		JSONObject obj = http.productChromeDetail(goodsId);
		return obj;
	}
    
    @RequestMapping("/downloadImg")
	public Object download(String goodsId, String keyword) throws Exception {
    	PddUser user = pddUserWyService.queryUserByUsername("m_17755117870");
		PddHttp http = PddHttp.getInstance(user);
		http.downloadProductSearchImage(goodsId, goodsId, keyword, "/share/code/test");
		return null;
	}
    
    @RequestMapping("/addorder")
   	public Object addorder() throws Exception {
       	PddUser user = pddUserWyService.queryUserByUsername("17755117870");
   		PddHttp http = PddHttp.getInstance(user);
   		pddUserWyService.createOrder("17755117870", "57504422295", 9.77f, false, false, 1, 0);
   		return null;
   	}
}
