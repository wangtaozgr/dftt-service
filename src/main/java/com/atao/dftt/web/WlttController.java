package com.atao.dftt.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

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
	public String test() throws Exception {

		Wltt user = wlttWyService.queryById(2);
		WlttHttp http = WlttHttp.getInstance(user);
		
		wlttWyService.searchTask(user);
		//http.ad();
		//http.finishSearchTask(10);
		//wlttWyService.readNewsCoin(user);
		//wlttWyService.searchTask(user);
		System.out.println("finished");
		return null;
	}

}
