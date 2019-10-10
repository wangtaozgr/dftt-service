package com.atao.dftt.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.model.PinkeduoTask;
import com.atao.dftt.service.PinkeduoTaskWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/pinkeduo")
public class PinkeduoTaskController extends BaseController<PinkeduoTask> {
	public static final String BASE_URL = "/PinkeduoTask/";

	@Autowired
	private PinkeduoTaskWyService pinkeduoTaskWyService;

	@Override
	protected BaseService<PinkeduoTask> getService() {
		return pinkeduoTaskWyService;
	}

	@RequestMapping("/orderPageData")
	public Object orderPageData(@RequestParam(required = false) String username) throws Exception {
		PinkeduoTask t = new PinkeduoTask();
		t.setPage(1);
		t.setRows(50);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "createTime=1");
		return pinkeduoTaskWyService.queryPage(t, params).getList();
	}

	@RequestMapping("/updateTask")
	public Object updateTask(String username, String taskId, String taskSn, String taskPrice, String taskRecType,
			String taskSearchType, String taskOrderType, String taskEvaluateType, String taskGold, String taskStatus,
			String ewmUrl, String productImg, String pjContent, String taskBuyerDesc, String taskDetail,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Method", "POST,GET");
		DataVo vo = pinkeduoTaskWyService.createTask(username, taskId, taskSn, taskPrice, taskRecType, taskSearchType,
				taskOrderType, taskEvaluateType, taskGold, taskStatus, ewmUrl, productImg, pjContent, taskBuyerDesc,
				taskDetail);
		return vo;
	}

	@RequestMapping("/confirmTask")
	public Object confirmTask(String taskSn, String cookiestr, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Method", "POST,GET");
		DataVo vo = pinkeduoTaskWyService.confirmReciveGoods(taskSn, cookiestr);
		return vo;
	}

	@RequestMapping("/orderstatus")
	public Object orderstatus(String username, String taskSn, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Method", "POST,GET");
		DataVo vo = pinkeduoTaskWyService.orderstatus(username, taskSn);
		return vo;
	}

	@RequestMapping("/wap/updateTask")
	public Object updateTaskWap(String username, String taskId, String taskSn, String taskPrice, String taskRecType,
			String taskSearchType, String taskOrderType, String taskGold, String taskStatus, String productImg,
			String taskBuyerDesc, String goodsId, String pjContent, String pjImg, HttpServletResponse response)
			throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Method", "POST,GET");
		DataVo vo = pinkeduoTaskWyService.updateTaskWap(username, taskId, taskSn, taskPrice, taskRecType,
				taskSearchType, taskOrderType, taskGold, taskStatus, productImg, taskBuyerDesc, goodsId, pjContent,
				pjImg);
		return vo;
	}
}
