package com.atao.dftt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.api.vo.PinkeduoTaskVo;
import com.atao.dftt.http.PddHttp;
import com.atao.dftt.http.PinkeduoHttp;
import com.atao.dftt.mapper.PinkeduoTaskMapper;
import com.atao.dftt.model.PddUser;
import com.atao.dftt.model.PinkeduoTask;
import com.atao.dftt.model.PinkeduoUser;
import com.atao.dftt.util.qrcode.QRCodeUtil;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class PinkeduoTaskWyService extends BaseService<PinkeduoTask> {

	@Resource
	private PinkeduoTaskMapper pinkeduoTaskMapper;
	@Resource
	private PinkeduoUserWyService pinkeduoUserWyService;
	@Resource
	private PddUserWyService pddUserWyService;
	@Resource
	private PddMallWyService pddMallWyService;

	@Value("${pdd.pj.img.path}")
	public String pddImgPath;// 评多多图片路径

	@Override
	public BaseMapper<PinkeduoTask> getMapper() {
		return pinkeduoTaskMapper;
	}

	public DataVo createTask(String username, String taskId, String taskSn, String taskPrice, String taskRecType,
			String taskSearchType, String taskOrderType, String taskEvaluateType, String taskGold, String taskStatus,
			String ewmUrl, String productImg, String pjContent, String taskBuyerDesc, String taskDetail) {
		PddUser mUser = pddUserWyService.queryUserByUsername(PddHttp.searchUsername);
		PddHttp mHttp = new PddHttp(mUser);

		DataVo vo = new DataVo(1, "", null);
		PinkeduoUser user = pinkeduoUserWyService.queryByUsername(username);
		if (!user.getUsed()) {
			logger.info("pinkeduo-{}:创建拼客多任务失败.error={}", username, "用户已禁用，请联系管理员!");
			vo = new DataVo(0, "用户已禁用，请联系管理员!", null);
			return vo;
		}
		PinkeduoTask t = new PinkeduoTask();
		t.setTaskId(taskId);
		t.setUsername(username);
		PinkeduoTask task = super.queryOne(t, null);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp http = PddHttp.getInstance(pdd);
		if (task == null) {
			logger.info("pinkeduo-{}:{}:拼客多任务不存在，数据库新建任务", username, taskSn);
			task = new PinkeduoTask();
			task.setTaskId(taskId);
			task.setTaskSn(taskSn);
			task.setUserId(user.getId());
			task.setUsername(user.getUsername());
			task.setPddUsername(user.getPddUsername());
			task.setCreateTime(new Date());
			task.setTaskEvaluateType(taskEvaluateType);
			task.setTaskGold(taskGold);
			task.setTaskOrderType(taskOrderType);
			task.setTaskPrice(taskPrice);
			task.setTaskRecType(taskRecType);
			task.setTaskStatus(PinkeduoTaskVo.STATUS_WAITPAY);
			if (taskSearchType.equals("扫码")) {
				task.setTaskSearchContent(ewmUrl);
				task.setTaskBuyerImg(productImg);
				task.setTaskEvaluateImg(productImg);
				String taskBuyerUrl = QRCodeUtil.decode(ewmUrl);// 扫二维码
				if (StringUtils.isBlank(taskBuyerUrl)) {
					logger.info("pinkeduo-{}:创建拼客多任务失败.error={}", username, "二维码有问题!");
					vo = new DataVo(0, "二维码有问题!", null);
					return vo;
				}
				task.setTaskBuyerUrl(taskBuyerUrl);
			} else {
				Document doc = Jsoup.parse(taskDetail);
				String sellerinfo = doc.getElementsByTag("dd").get(0).text().trim();
				String sellerName = sellerinfo.substring(0, sellerinfo.indexOf("|")).replace("店铺名：", "").trim();
				String goodsName = doc.getElementsByTag("dt").get(0).text().replace("品名：", "");
				if ("商品标题6分钟后显示，如关键词很难搜到，可等6分钟后搜索标题".equals(goodsName)) {
					String goodsId = mHttp.searchGoodsIdByMallNameAndGoodImg(sellerName, getImageName(productImg));
					if (StringUtils.isNotBlank(goodsId)) {
						task.setTaskSearchContent(goodsName);
						task.setTaskBuyerImg(productImg);
						task.setTaskEvaluateImg(productImg);
						String taskBuyerUrl = "http://mobile.yangkeduo.com/goods.html?goods_id=" + goodsId;// 搜索
						task.setTaskBuyerUrl(taskBuyerUrl);
					} else {
						vo = new DataVo(0, "商品名称还没出来!", null);
						return vo;
					}
				} else {
					String goodsId = mHttp.searchGoodsId(goodsName, sellerName);
					if (StringUtils.isBlank(goodsId)) {
						logger.info("pinkeduo-{}:创建拼客多任务失败.error={}", username, "没有找到合适的商品");
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					}
					task.setTaskSearchContent(goodsName);
					task.setTaskBuyerImg(productImg);
					task.setTaskEvaluateImg(productImg);
					String taskBuyerUrl = "http://mobile.yangkeduo.com/goods.html?goods_id=" + goodsId;// 搜索
					task.setTaskBuyerUrl(taskBuyerUrl);
				}
			}

			/*
			 * try { String taskBuyerUrl = task.getTaskBuyerUrl(); int endFix =
			 * taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9); if (endFix
			 * < 0) endFix = taskBuyerUrl.length(); String goodsId =
			 * taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
			 * JSONObject product = http.productChromeDetail(goodsId); JSONObject mall =
			 * product.getJSONObject("store").getJSONObject("initDataObj").getJSONObject(
			 * "mall"); pddMallWyService.savePddMall(mall.getString("mallID"),
			 * mall.getString("mallName"), mall.getString("logo")); } catch (Exception e) {
			 * }
			 */

			if (StringUtils.isBlank(pjContent)) {
				String taskEvaluateContent = getCommentContent();
				task.setTaskEvaluateContent(taskEvaluateContent);
				task.setTaskEvaluateType("1");
			} else {
				task.setTaskEvaluateContent(pjContent);
				task.setTaskEvaluateType("2");
			}
			task.setTaskSearchType(taskSearchType);
			task.setTaskBuyerDesc(taskBuyerDesc);
		}
		if (StringUtils.isBlank(task.getPddOrderNo())) {
			String taskBuyerUrl = task.getTaskBuyerUrl();
			int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
			if (endFix < 0)
				endFix = taskBuyerUrl.length();
			String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix).trim();
			boolean group = true;
			if (task.getTaskOrderType().equals("开团"))
				group = false;
			DataVo orderinfo = pddUserWyService.createOrder(user.getPddUsername(), goodsId,
					Float.valueOf(task.getTaskPrice()), group, false, 1, 0);
			if (orderinfo.getStatus() == 1) {
				JSONObject order = (JSONObject) orderinfo.getData();
				vo = new DataVo(1, "创建pdd订单成功.", null);
				task.setPddOrderId(order.getString("group_order_id"));
				task.setPddOrderNo(order.getString("order_sn"));
				task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_WAITPAY);
				if (StringUtils.isNotBlank(task.getTaskBuyerDesc()) && task.getTaskBuyerDesc().contains("收藏")) {
					JSONObject product = mHttp.productChromeDetail(goodsId);
					JSONObject mall = product.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall");
					http.scGoods(goodsId);
					http.scMaller(mall.getString("mallID"));
				}
			} else {
				vo = orderinfo;
			}
		}
		JSONObject order = new JSONObject();
		order.put("pddOrderNo", task.getPddOrderNo());
		order.put("pddOrderId", task.getPddOrderId());
		order.put("name", user.getName());
		if (StringUtils.isNotBlank(task.getPddOrderNo())
				&& PinkeduoTaskVo.PDD_STATUS_WAITPAY.equals(task.getPddOrderStatus())) {
			boolean payed = http.isPayed(task.getPddOrderNo());
			if (payed) {
				task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_WAITSEND);
				vo = new DataVo(1, "更新pdd订单状态成功.", null);
			} else {
				String aliPayUrl = http.genAliPayUrl(task.getPddOrderNo());
				logger.info("pinkeduo-{}:{}:生成支付宝支付地址,aliPayUrl={}", username, taskSn, aliPayUrl);
				order.put("aliPayUrl", aliPayUrl);
				vo = new DataVo(1, "生成支付宝支付地址成功.", null);
			}
		}
		order.put("pddOrderStatus", task.getPddOrderStatus());
		vo.setData(order);
		super.save(task);
		return vo;
	}

	private static String getCommentContent() {
		String[] content = { "五星好评,东西价格便宜性价比高,商家态度也很热情,东西到的很快!", "不错,很喜欢,价格便宜性价比高,商家态度也很热情!",
				"性价比高,商家态度也很热情,东西到的很快,非常满意!", "比在商场上的强太多了,最主要很实惠.很喜欢 ,值得推荐!", "五星好评,东西价格便宜性价比高,商家态度也很热情,超喜欢下次还来!",
				"和描述一样好,商家态度也很热情,东西到的很快!", "宝贝收到了,太好了,五星好评,最主要很实惠.很喜欢,值得推荐!", "还不错,挺好用的,商家态度也很热情,价格也不贵,值得推荐!" };
		int size = content.length;
		Random random = new Random();
		return content[random.nextInt(size)];
	}

	private String getImageName(String img) {
		if (img.indexOf(".jp") > -1) {
			img = img.substring(0, img.indexOf(".jp"));
		}
		if (img.indexOf(".pn") > -1) {
			img = img.substring(0, img.indexOf(".pn"));
		}
		img = img.replace("\\", "/");
		img = img.substring(img.lastIndexOf("/") + 1, img.length());
		return img;
	}

	public DataVo confirmTxReciveGoods(String taskSn, String cookiestr, String Authorization) {
		DataVo vo = new DataVo(1, "确认收货完成.", taskSn);
		PinkeduoTask t = new PinkeduoTask();
		t.setTaskSn(taskSn);
		PinkeduoTask task = super.queryOne(t, null);
		String pddUsername = task.getPddUsername();
		String taskBuyerUrl = task.getTaskBuyerUrl();
		int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
		if (endFix < 0)
			endFix = taskBuyerUrl.length();
		String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
		String msg = pddUserWyService.confirmReciveGoodsNew(pddUsername, task.getPddOrderNo(), goodsId,
				task.getTaskEvaluateContent(), null);
		if (StringUtils.isBlank(msg)) {
			logger.info("pinkeduo-{}:{}|拼多多确认收货已完成，开始拼客多确认.", task.getUsername(), taskSn);
			task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_FINISHED);
			task.setTaskStatus(PinkeduoTaskVo.STATUS_WAITFINISH);
			super.save(task);
		} else {
			vo = new DataVo(0, msg, taskSn);
		}
		return vo;
	}

	public DataVo confirmReciveGoods(String taskSn, String cookiestr, String Authorization) {
		DataVo vo = new DataVo(1, "确认收货完成.", taskSn);
		PinkeduoTask t = new PinkeduoTask();
		t.setTaskSn(taskSn);
		PinkeduoTask task = super.queryOne(t, null);
		String pddUsername = task.getPddUsername();
		String taskBuyerUrl = task.getTaskBuyerUrl();
		int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
		if (endFix < 0)
			endFix = taskBuyerUrl.length();
		String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
		String msg = "";
		long time = (new Date().getTime() - task.getCreateTime().getTime()) / 3600000;
		if (time >= 168) {
			msg = pddUserWyService.confirmReciveGoodsNew(pddUsername, task.getPddOrderNo(), goodsId,
					task.getTaskEvaluateContent(), null);
		} else {
			msg = pddUserWyService.confirmReciveGoodsPkd(pddUsername, task.getPddOrderNo(), goodsId,
					task.getTaskEvaluateContent(), null);
		}
		if (StringUtils.isBlank(msg)) {
			logger.info("pinkeduo-{}:{}|拼多多确认收货已完成，开始拼客多确认.", task.getUsername(), taskSn);
			/*
			 * PinkeduoHttp pinkeduoHttp = new PinkeduoHttp(task.getUsername(), cookiestr,
			 * Authorization); boolean success =
			 * pinkeduoHttp.confirmRecGoods(task.getTaskId()); if (success) {
			 * task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_FINISHED);
			 * task.setTaskStatus(PinkeduoTaskVo.STATUS_WAITFINISH); super.save(task); }
			 * else { vo = new DataVo(0, "拼客多确认收货失败!", taskSn); }
			 */
			task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_FINISHED);
			task.setTaskStatus(PinkeduoTaskVo.STATUS_WAITFINISH);
			super.save(task);
		} else {
			vo = new DataVo(0, msg, taskSn);
		}
		return vo;
	}

	public DataVo orderstatus(String username, String taskSn) {
		DataVo vo = new DataVo(1, "", null);
		PinkeduoUser user = pinkeduoUserWyService.queryByUsername(username);
		PinkeduoTask t = new PinkeduoTask();
		t.setUsername(username);
		t.setTaskSn(taskSn);
		PinkeduoTask task = super.queryOne(t, null);

		if (StringUtils.isNotBlank(task.getPddOrderNo())) {
			if (PinkeduoTaskVo.PDD_STATUS_WAITPAY.equals(task.getPddOrderStatus())) {
				PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
				PddHttp http = PddHttp.getInstance(pdd);
				boolean payed = http.isPayed(task.getPddOrderNo());
				if (payed) {
					task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_WAITSEND);
					vo = new DataVo(1, "更新pdd订单状态成功.", null);
					super.save(task);
				}
			}
		} else {
			vo = new DataVo(0, "pdd订单还未生成.", null);
		}
		JSONObject order = new JSONObject();
		order.put("pddOrderNo", task.getPddOrderNo());
		order.put("pddOrderId", task.getPddOrderId());
		order.put("name", user.getName());
		order.put("pddOrderStatus", task.getPddOrderStatus());
		vo.setData(order);
		return vo;
	}

	public DataVo updateTaskWap(String username, String taskId, String taskSn, String taskPrice, String taskRecType,
			String taskSearchType, String taskOrderType, String taskGold, String taskStatus, String productImg,
			String taskBuyerDesc, String goodsId, String taskEvaluateContent, String taskEvaluateImg) {
		DataVo vo = new DataVo(1, "", null);
		PinkeduoUser user = pinkeduoUserWyService.queryByUsername(username);
		if (!user.getUsed()) {
			logger.info("pinkeduo-{}:创建拼客多任务失败.error={}", username, "用户已禁用，请联系管理员!");
			vo = new DataVo(0, "用户已禁用，请联系管理员!", null);
			return vo;
		}
		PinkeduoTask t = new PinkeduoTask();
		t.setTaskId(taskId);
		PinkeduoTask task = super.queryOne(t, null);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp http = PddHttp.getInstance(pdd);
		if (task == null) {
			logger.info("pinkeduo-{}:{}:拼客多任务不存在，数据库新建任务", username, taskSn);
			task = new PinkeduoTask();
			task.setTaskId(taskId);
			task.setTaskSn(taskSn);
			task.setUserId(user.getId());
			task.setUsername(user.getUsername());
			task.setPddUsername(user.getPddUsername());
			task.setCreateTime(new Date());
			task.setTaskGold(taskGold);
			task.setTaskOrderType(taskOrderType);
			task.setTaskPrice(taskPrice);
			task.setTaskRecType(taskRecType);
			task.setTaskStatus(PinkeduoTaskVo.STATUS_WAITPAY);
			String taskBuyerUrl = "http://mobile.yangkeduo.com/goods.html?goods_id=" + goodsId;// 搜索
			task.setTaskBuyerImg(productImg);
			task.setTaskEvaluateImg(productImg);
			task.setTaskBuyerUrl(taskBuyerUrl);
			task.setTaskSearchType(taskSearchType);
			task.setTaskEvaluateContent(taskEvaluateContent);
			task.setTaskEvaluateType("1");
			task.setTaskEvaluateImg(taskEvaluateImg);
			task.setTaskBuyerDesc(taskBuyerDesc);
			if (StringUtils.isBlank(taskEvaluateContent))
				taskEvaluateContent = getCommentContent();
			task.setTaskEvaluateContent(taskEvaluateContent);
		}
		if (StringUtils.isBlank(task.getPddOrderNo())) {
			boolean group = true;
			if (task.getTaskOrderType().equals("开团"))
				group = false;
			DataVo orderinfo = pddUserWyService.createOrder(user.getPddUsername(), goodsId,
					Float.valueOf(task.getTaskPrice()), group, false, 1, 0);
			if (orderinfo.getStatus() == 1) {
				JSONObject order = (JSONObject) orderinfo.getData();
				vo = new DataVo(1, "创建pdd订单成功.", null);
				task.setPddOrderId(order.getString("group_order_id"));
				task.setPddOrderNo(order.getString("order_sn"));
				task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_WAITPAY);

				if (StringUtils.isNotBlank(task.getTaskBuyerDesc()) && task.getTaskBuyerDesc().contains("收藏")) {
					PddUser mUser = pddUserWyService.queryUserByUsername(PddHttp.searchUsername);
					PddHttp mHttp = new PddHttp(mUser);
					JSONObject product = mHttp.productChromeDetail(goodsId);
					JSONObject mall = product.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall");
					http.scGoods(goodsId);
					http.scMaller(mall.getString("mallID"));
				}
			} else {
				vo = orderinfo;
			}
		}
		JSONObject order = new JSONObject();
		order.put("pddOrderNo", task.getPddOrderNo());
		order.put("pddOrderId", task.getPddOrderId());
		order.put("name", user.getName());
		if (StringUtils.isNotBlank(task.getPddOrderNo())
				&& PinkeduoTaskVo.PDD_STATUS_WAITPAY.equals(task.getPddOrderStatus())) {
			boolean payed = http.isPayed(task.getPddOrderNo());
			if (payed) {
				task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_WAITSEND);
				vo = new DataVo(1, "更新pdd订单状态成功.", null);
			} else {
				logger.info("pinkeduo-{}:{}:生成支付宝支付地址", username, taskSn);
				String aliPayUrl = http.genAliPayUrl(task.getPddOrderNo());
				order.put("aliPayUrl", aliPayUrl);
				vo = new DataVo(1, "生成支付宝支付地址成功.", null);
			}
		}
		order.put("pddOrderStatus", task.getPddOrderStatus());
		vo.setData(order);
		super.save(task);
		return vo;
	}

	/**
	 * 查询5天前的任务 未确认收货的任务
	 * 
	 * @param username
	 * @return
	 */
	public List<PinkeduoTask> queryConfirmGoodsTask(String username) {
		PinkeduoTask t = new PinkeduoTask();
		t.setUsername(username);
		t.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_WAITSEND);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_YEAR, -3);
		t.setCreateTime(c.getTime());
		return super.queryList(t, null);
	}

	public int updateConfirmOrderStatus(String username) {
		int count = 0;
		logger.info("pingkeduo-{}|开始拼客多确认收货.", username);
		PinkeduoHttp pinkeduoHttp = PinkeduoHttp.getInstance(username);
		logger.info("pingkeduo-{}:|cookie={}|auth={}", username, pinkeduoHttp.cookie, pinkeduoHttp.Authorization);
		List<PinkeduoTask> tasks = queryConfirmGoodsTask(username);
		logger.info("pingkeduo-{}:|确认收货.任务数={}", username, tasks.size());
		logger.info("pingkeduo-{}:|确认收货.cookie={}", username, pinkeduoHttp.getCookie());
		for (PinkeduoTask task : tasks) {
			logger.info("pingkeduo-{}:|确认收货.taskId={}", username, task.getTaskId());
			String pddUsername = task.getPddUsername();
			String taskBuyerUrl = task.getTaskBuyerUrl();
			int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
			if (endFix < 0)
				endFix = taskBuyerUrl.length();
			String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
			String msg = "";
			long time = (new Date().getTime() - task.getCreateTime().getTime()) / 3600000;
			if (time > 120) {
				msg = pddUserWyService.confirmReciveGoodsNew(pddUsername, task.getPddOrderNo(), goodsId,
						task.getTaskEvaluateContent(), null);
			} else {
				msg = pddUserWyService.confirmReciveGoodsPkd(pddUsername, task.getPddOrderNo(), goodsId,
						task.getTaskEvaluateContent(), null);
			}
			if (StringUtils.isBlank(msg)) {
				logger.info("pinkeduo-{}:{}|拼多多确认收货已完成，开始拼客多确认.", task.getUsername(), task.getTaskSn());
				boolean success = pinkeduoHttp.confirmRecGoods(task.getTaskId());
				if (success) {
					task.setPddOrderStatus(PinkeduoTaskVo.PDD_STATUS_FINISHED);
					task.setTaskStatus(PinkeduoTaskVo.STATUS_WAITFINISH);
					super.save(task);
					count++;
				}
			} else {
				logger.info("pingkeduo-{}:{}|订单确认收货失败={}", task.getUsername(), task.getTaskSn(), msg);
			}
		}
		logger.info("pingkeduo-{}|结束拼客多确认收货.", username);
		return count;
	}

	@Override
	public Weekend<PinkeduoTask> genSqlExample(PinkeduoTask t) {
		Weekend<PinkeduoTask> w = super.genSqlExample(t);
		WeekendCriteria<PinkeduoTask, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(PinkeduoTask::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getPddOrderNo())) {
			c.andEqualTo(PinkeduoTask::getPddOrderNo, t.getPddOrderNo());
		}
		if (StringUtils.isNotBlank(t.getTaskId())) {
			c.andEqualTo(PinkeduoTask::getTaskId, t.getTaskId());
		}
		if (StringUtils.isNotBlank(t.getTaskSn())) {
			c.andEqualTo(PinkeduoTask::getTaskSn, t.getTaskSn());
		}
		if (StringUtils.isNotBlank(t.getTaskStatus())) {
			c.andEqualTo(PinkeduoTask::getTaskStatus, t.getTaskStatus());
		}
		if (StringUtils.isNotBlank(t.getPddOrderStatus())) {
			c.andEqualTo(PinkeduoTask::getPddOrderStatus, t.getPddOrderStatus());
		}
		if (t.getCreateTime() != null) {
			c.andLessThan(PinkeduoTask::getCreateTime, t.getCreateTime());
		}

		return w;
	}

}
