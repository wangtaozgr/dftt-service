package com.atao.dftt.web;

import com.alibaba.fastjson.JSONObject;
import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.model.DdhpTask;
import com.atao.dftt.service.DdhpTaskWyService;
import com.atao.dftt.service.PddUserWyService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/ddhp")
public class DdhpTaskController extends BaseController<DdhpTask> {
    public static final String BASE_URL = "/DdhpTask/";

    @Autowired
    private DdhpTaskWyService ddhpTaskWyService;
    @Autowired
    private PddUserWyService pddUserWyService;


    @Override
    protected BaseService<DdhpTask> getService() {
        return ddhpTaskWyService;
    }
    
    @RequestMapping("/orderPageData")
	public Object orderPageData(@RequestParam(required=false) String username) throws Exception {
    	DdhpTask t = new DdhpTask();
		t.setPage(1);
		t.setRows(50);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "createTime=1");
		return ddhpTaskWyService.queryPage(t, params).getList();
	}
    
    @RequestMapping("/startTask")
	public Object startTask(String username) throws Exception {
    	DataVo vo = new DataVo();
    	///DataVo vo = ddhpTaskWyService.startTask(username);
    	//pddUserWyService.createOrder("17755117870", "19546213290", 1.96f);
    	JSONObject json = pddUserWyService.queryOrderDetail("17755117870", "190810-632542986914080");
		return json;
	}
}
