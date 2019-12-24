package com.atao.dftt.service;

import com.atao.dftt.model.DdhpTask;
import com.atao.dftt.model.DdhpUser;
import com.atao.dftt.model.DdhpTask;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.api.vo.DdhpTaskVo;
import com.atao.dftt.http.DdhpHttp;
import com.atao.dftt.mapper.DdhpTaskMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 *
 * @author twang
 */
@Service
public class DdhpTaskWyService extends BaseService<DdhpTask> {

	@Resource
	private DdhpTaskMapper ddhpTaskMapper;
	@Resource
	private DdhpUserWyService ddhpUserWyService;
	@Resource
	private PddUserWyService pddUserWyService;

	@Override
	public BaseMapper<DdhpTask> getMapper() {
		return ddhpTaskMapper;
	}
	
	public DataVo startTask(String username) {
		DataVo vo = new DataVo();
		logger.info("ddhp-{}:开始接取任务.", username);
		DdhpUser user = ddhpUserWyService.queryByUsername(username);
		if(!user.getUsed()) {
			logger.info("ddhp-{}:接取任务失败.error={}", username,"用户已禁用，请联系管理员!");
			vo = new DataVo(0, "用户已禁用，请联系管理员!", null);
			return vo;
		}
		DdhpHttp http = DdhpHttp.getInstance(user);
		JSONObject json = reciveTask(http, 1);
		if(json==null) {
			logger.info("ddhp-{}:接取任务失败.error={}", username,"没有接取到合适的任务!");
			vo = new DataVo(0, "没有接取到合适的任务!", null);
			return vo;
		}
		DdhpTask task = new DdhpTask();
		task.setTaskId(json.getString("pddTaskId"));
		task.setTaskBuyerDesc(json.getString("memo"));
		task.setTaskBuyerImg(json.getString("pddGoodsPicture"));
		task.setTaskEvaluateType(json.getString("pddTaskEvaluateType"));
		task.setTaskGold(json.getString("pddTaskReward"));
		task.setTaskOrderType(json.getString("pddTaskOrderType"));
		task.setTaskPrice(json.getString("pddGoodsPrice"));
		task.setTaskStatus("0");
		task.setUserId(user.getId());
		task.setUsername(user.getUsername());
		task.setCreateTime(new Date());
		task.setPddUsername(user.getPddUsername());
		//task.setTaskBuyerUrl(json.getString("memo"));
		//task.setTaskEvaluateContent(json.getString("memo"));
		//task.setTaskEvaluateImg(json.getString("memo"));
		//task.setTaskRecType(json.getString("memo"));
		super.save(task);
		vo = new DataVo(1, "接取任务成功", task);
		return vo;
	}
	
	//循环接取任务
	public JSONObject reciveTask(DdhpHttp http, int page) {
		JSONArray taskList = http.tasklist(page);
		if(taskList==null||taskList.size()<1) return null;
		for(int i=0;i<taskList.size();i++) {
			JSONObject task = taskList.getJSONObject(i);
			if(isReciveTask(task)) {
				JSONObject result = http.reciveTask(task.getString("pddTaskId"));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (result.getIntValue("code")==0) {
					return task;
				}
			}
		}
		page++;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return reciveTask(http, page);
	}
	
	public void createPddOrder() {
		DdhpTask t = new DdhpTask();
		t.setPddOrderStatus(DdhpTaskVo.STATUS_WAITPAY);
		List<DdhpTask> tasks = super.queryList(t, null);
		for(DdhpTask task : tasks) {
			DataVo order = pddUserWyService.createOrder(task.getUsername(), "", Float.valueOf(task.getTaskPrice()), true, false, 1, 0);
			if(order.getStatus()==1) {
				JSONObject json = (JSONObject) order.getData();
				String pddOrderNo = json.getString("orderno");
				String pddOrderId = json.getString("orderno");
				task.setTaskStatus(DdhpTaskVo.STATUS_WAITRECIVE);
				task.setPddOrderNo(pddOrderNo);
				task.setPddOrderId(pddOrderId);
				task.setPddOrderStatus(DdhpTaskVo.PDD_STATUS_WAITPAY);
				super.save(task);
			}
		}
	}
	
	public void updateTaskStatus() {
		DdhpTask t = new DdhpTask();
		t.setPddOrderStatus(DdhpTaskVo.STATUS_WAITPAY);
		t.setPddOrderStatus(DdhpTaskVo.PDD_STATUS_WAITPAY);
		List<DdhpTask> tasks = super.queryList(t, null);//查询所有已生成订单的，未付款的任务，更新是否付款状态
		for(DdhpTask task : tasks) {
			JSONObject order = pddUserWyService.queryOrderDetail(task.getUsername(), task.getPddOrderNo());
			if(order.getIntValue("payStatus")==2) {
				task.setTaskStatus(DdhpTaskVo.STATUS_WAITRECIVE);
				task.setPddOrderStatus(DdhpTaskVo.PDD_STATUS_WAITRECIVE);
				super.save(task);
			}
		}
	}
	
	//是否接受任务
	public boolean isReciveTask(JSONObject task) {
		if(task.getIntValue("pddTaskType")!=1) return false;//只接受扫码的任务
		if(task.getIntValue("pddTaskEvaluateType")==1) return false;//只接受字图评价和指定评价
		if(task.getFloatValue("pddGoodsPrice")>30) return false;//只接受30元以下的任务
		if(task.getBooleanValue("pddTaskNeedToChat")) return false;//不接受聊天的任务
		if(task.getBooleanValue("pddTaskIsRealReceived")) return false;//不接受真实收货的任务
		if(task.getBooleanValue("pddTaskIsVipBuyer")) return false;//暂时不接受vip任务
		return true;
	}
	
	@Override
	public Weekend<DdhpTask> genSqlExample(DdhpTask t) {
		Weekend<DdhpTask> w = super.genSqlExample(t);
		WeekendCriteria<DdhpTask, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(DdhpTask::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getPddOrderNo())) {
			c.andEqualTo(DdhpTask::getPddOrderNo, t.getPddOrderNo());
		}
		if (StringUtils.isNotBlank(t.getTaskId())) {
			c.andEqualTo(DdhpTask::getTaskId, t.getTaskId());
		}
		if (StringUtils.isNotBlank(t.getTaskStatus())) {
			c.andEqualTo(DdhpTask::getTaskStatus, t.getTaskStatus());
		}
		if (StringUtils.isNotBlank(t.getPddOrderStatus())) {
			c.andEqualTo(DdhpTask::getPddOrderStatus, t.getPddOrderStatus());
		}
		
		return w;
	}

}
