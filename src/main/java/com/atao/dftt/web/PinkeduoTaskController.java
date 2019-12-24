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
import com.atao.base.util.StringUtils;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.http.PinkeduoHttp;
import com.atao.dftt.http.TaodanHttp;
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
	public Object orderPageData(@RequestParam(required = false) String username, String pddOrderStatus,String pddOrderNo,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int rows) throws Exception {
		PinkeduoTask t = new PinkeduoTask();
		t.setPage(1);
		t.setRows(50);
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
	public Object confirmTask(String taskSn, String cookiestr, String Authorization, HttpServletResponse response)
			throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Method", "POST,GET");
		DataVo vo = pinkeduoTaskWyService.confirmReciveGoods(taskSn, cookiestr, Authorization);
		return vo;
	}
	
	@RequestMapping("/confirmTxTask")
	public Object confirmTxTask(String taskSn, String cookiestr, String Authorization, HttpServletResponse response)
			throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Method", "POST,GET");
		DataVo vo = pinkeduoTaskWyService.confirmTxReciveGoods(taskSn, cookiestr, Authorization);
		return vo;
	}

	@RequestMapping("/orderstatus")
	public Object orderstatus(String username, String taskSn, String cookiestr, String Authorization,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Method", "POST,GET");
		PinkeduoHttp tdHttp = new PinkeduoHttp(username, cookiestr, Authorization);
		DataVo vo = pinkeduoTaskWyService.orderstatus(username, taskSn);
		return vo;
	}

	@RequestMapping("/wap/updatePayedOrderStatus")
	public Object updatePayedOrderStatus(String username, String cookiestr, String Authorization) throws Exception {
		PinkeduoHttp tdHttp = new PinkeduoHttp(username, cookiestr, Authorization);
		int num = pinkeduoTaskWyService.updateConfirmOrderStatus(username);
		return num;
	}

	/**
	 * wap端创建订单
	 * 
	 * @param username
	 * @param taskId
	 * @param taskSn
	 * @param taskPrice
	 * @param taskRecType
	 * @param taskSearchType
	 * @param taskOrderType
	 * @param taskGold
	 * @param taskStatus
	 * @param productImg
	 * @param taskBuyerDesc
	 * @param goodsId
	 * @param pjContent
	 * @param pjImg
	 * @param response
	 * @return
	 * @throws Exception
	 */
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
