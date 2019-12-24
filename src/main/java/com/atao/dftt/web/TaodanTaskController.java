package com.atao.dftt.web;

import com.alibaba.fastjson.JSONObject;
import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.http.TaodanHttp;
import com.atao.dftt.model.DdhpTask;
import com.atao.dftt.model.TaodanTask;
import com.atao.dftt.service.TaodanTaskWyService;

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
@RequestMapping("/taodan")
public class TaodanTaskController extends BaseController<TaodanTask> {
	public static final String BASE_URL = "/TaodanTask/";

	@Autowired
	private TaodanTaskWyService taodanTaskWyService;

	@Override
	protected BaseService<TaodanTask> getService() {
		return taodanTaskWyService;
	}

	@RequestMapping("/orderPageData")
	public Object orderPageData(@RequestParam(required = false) String username, String pddOrderStatus,
			String pddOrderNo, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows)
			throws Exception {
		TaodanTask t = new TaodanTask();
		t.setPage(page);
		t.setRows(rows);
		if (StringUtils.isNotBlank(username)) {
			t.setUsername(username);
		}
		if (StringUtils.isNotBlank(pddOrderStatus)) {
			t.setPddOrderStatus(pddOrderStatus);
		}
		if (StringUtils.isNotBlank(pddOrderNo)) {
			t.setPddOrderNo(pddOrderNo);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "createTime=1");
		return taodanTaskWyService.queryPage(t, params);
	}

	@RequestMapping("/updateTask")
	public Object updateTask(String username, String taskId, String taskSn, String taskPrice, String taskRecType,
			String taskSearchType, String taskOrderType, String taskEvaluateType, String taskGold, String taskStatus,
			String taskDetail) throws Exception {
		taskPrice = taskPrice.replace("￥", "").replace("元", "").trim();
		taskGold = taskGold.replace("佣金：", "").replace("元", "").trim();
		DataVo vo = taodanTaskWyService.createTask(username, taskId, taskSn, taskPrice, taskRecType, taskSearchType,
				taskOrderType, taskEvaluateType, taskGold, taskStatus, taskDetail);
		return vo;
	}

	@RequestMapping("/confirmTask")
	public Object confirmTask(String taskSn, String pjcontent, String cookiestr) throws Exception {
		DataVo vo = taodanTaskWyService.confirmReciveGoods(taskSn, pjcontent, cookiestr);
		return vo;
	}

	@RequestMapping("/createPddOrder")
	public Object createPddOrder(String username, String taskId, String cookiestr) throws Exception {
		TaodanHttp tdHttp = new TaodanHttp(username, cookiestr);
		DataVo vo = taodanTaskWyService.createPddOrder(username, taskId, cookiestr);
		return vo;
	}

	@RequestMapping("/orderstatus")
	public Object orderstatus(String username, String taskSn, String cookiestr) throws Exception {
		if (StringUtils.isNotBlank(cookiestr)) {
			TaodanHttp tdHttp = new TaodanHttp(username, cookiestr);
		}
		DataVo vo = taodanTaskWyService.orderstatus(username, taskSn);
		return vo;
	}

	@RequestMapping("/updatePayedOrderStatus")
	public Object updatePayedOrderStatus(String username, String cookiestr) throws Exception {
		// DataVo vo = taodanTaskWyService.updatePayedOrderStatus(username, cookiestr);
		TaodanHttp tdHttp = new TaodanHttp(username, cookiestr);
		DataVo vo = taodanTaskWyService.update10000OrderStatus(username);
		int num = taodanTaskWyService.updateConfirmOrderStatus(username);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		taodanTaskWyService.downloadAllCommentImage(username);
		taodanTaskWyService.uploadTaodanImg(username);
		vo.setData(num);
		return vo;
	}

	/**
	 * 没有找到店铺时，放弃任务，重新查找一遍，插入店铺信息
	 * 
	 * @param username
	 * @param cookiestr
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateMall")
	public Object updateMall(String username, String cookiestr, String taskId) throws Exception {
		DataVo vo = taodanTaskWyService.updateMall(username, cookiestr, taskId);
		return vo;
	}
	
	@RequestMapping("/pj")
	public Object getContent(String username, String taskId) throws Exception {
		TaodanHttp tdHttp = TaodanHttp.getInstance(username);
		String pjcontentDiv = tdHttp.getPjContent(taskId);
		logger.info("pjcontentDiv="+pjcontentDiv);
		return pjcontentDiv;
	}
}
