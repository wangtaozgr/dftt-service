package com.atao.dftt.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.http.ZhuankeHttp;
import com.atao.dftt.model.ZhuankeTask;
import com.atao.dftt.service.ZhuankeTaskWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/zhuanke")
public class ZhuankeTaskController extends BaseController<ZhuankeTask> {
	public static final String BASE_URL = "/ZhuankeTask/";

	@Autowired
	private ZhuankeTaskWyService zhuankeTaskWyService;

	@Override
	protected BaseService<ZhuankeTask> getService() {
		return zhuankeTaskWyService;
	}

	@RequestMapping("/orderPageData")
	public Object orderPageData(@RequestParam(required = false) String username, String pddOrderStatus,
			String pddOrderNo, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows)
			throws Exception {
		ZhuankeTask t = new ZhuankeTask();
		t.setPage(page);
		t.setRows(rows);
		if(StringUtils.isNotBlank(username)) {
			t.setUsername(username);
		}
		if(StringUtils.isNotBlank(pddOrderStatus)) {
			t.setPddOrderStatus(pddOrderStatus);
		}
		if(StringUtils.isNotBlank(pddOrderNo)) {
			t.setPddOrderNo(pddOrderNo);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "createTime=1");
		return zhuankeTaskWyService.queryPage(t, params);
	}

	@RequestMapping("/updateTask")
	public Object updateTask(String username, String taskId, String taskSn, String taskPrice, String taskRecType,
			String taskSearchType, String taskOrderType, String taskEvaluateType, String taskGold, String taskStatus,
			String taskDetail, String cookiestr) throws Exception {
		taskPrice = taskPrice.replace("￥", "").replace("元", "").trim();
		taskGold = taskGold.replace("佣金：", "").replace("元", "").trim();
		DataVo vo = zhuankeTaskWyService.createTask(username, taskId, taskSn, taskPrice, taskRecType, taskSearchType,
				taskOrderType, taskEvaluateType, taskGold, taskStatus, taskDetail, cookiestr);
		return vo;
	}

	@RequestMapping("/createPddOrder")
	public Object createPddOrder(String username, String taskId, String cookiestr) throws Exception {
		ZhuankeHttp http = new ZhuankeHttp(username, cookiestr);
		DataVo vo = zhuankeTaskWyService.createPddOrder(username, taskId, cookiestr);
		return vo;
	}

	@RequestMapping("/orderstatus")
	public Object orderstatus(String username, String taskSn, String cookiestr) throws Exception {
		if (StringUtils.isNotBlank(cookiestr)) {
			ZhuankeHttp http = new ZhuankeHttp(username, cookiestr);
		}
		DataVo vo = zhuankeTaskWyService.orderstatus(username, taskSn);
		return vo;
	}

	@RequestMapping("/updatePayedOrderStatus")
	public Object updatePayedOrderStatus(String username, String cookiestr) throws Exception {
		ZhuankeHttp http = new ZhuankeHttp(username, cookiestr);
		zhuankeTaskWyService.updateTxConfirmOrderStatus(username);
		int num = zhuankeTaskWyService.updateConfirmOrderStatus(username);
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		zhuankeTaskWyService.downloadAllCommentImage(username);
		zhuankeTaskWyService.uploadZhuankeImg(username);
		return num;
	}
}
