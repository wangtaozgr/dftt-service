package com.atao.dftt.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.atao.base.mapper.BaseMapper;
import com.atao.base.service.BaseService;
import com.atao.base.util.StringUtils;
import com.atao.dftt.api.vo.DataVo;
import com.atao.dftt.api.vo.TaodanTaskVo;
import com.atao.dftt.http.PddHttp;
import com.atao.dftt.http.TaodanHttp;
import com.atao.dftt.mapper.TaodanTaskMapper;
import com.atao.dftt.model.PddMall;
import com.atao.dftt.model.PddMallGoods;
import com.atao.dftt.model.PddUser;
import com.atao.dftt.model.TaodanTask;
import com.atao.dftt.model.TaodanUser;
import com.atao.dftt.util.CommonUtils;
import com.atao.dftt.util.MobileHttpUrlConnectUtils;
import com.atao.dftt.util.qrcode.QRCodeUtil;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class TaodanTaskWyService extends BaseService<TaodanTask> {

	@Resource
	private TaodanTaskMapper taodanTaskMapper;
	@Resource
	private TaodanUserWyService taodanUserWyService;
	@Resource
	private PddUserWyService pddUserWyService;
	@Resource
	private PddMallWyService pddMallWyService;
	@Resource
	private PddMallGoodsWyService pddMallGoodsWyService;
	
	@Value("${pdd.pj.img.path}")
	public String pddImgPath;// 评多多图片路径

	@Override
	public BaseMapper<TaodanTask> getMapper() {
		return taodanTaskMapper;
	}

	public DataVo createTask(String username, String taskId, String taskSn, String taskPrice, String taskRecType,
			String taskSearchType, String taskOrderType, String taskEvaluateType, String taskGold, String taskStatus,
			String taskDetail) {
		DataVo vo = new DataVo(1, "", null);
		TaodanUser user = taodanUserWyService.queryByUsername(username);
		if (!user.getUsed()) {
			logger.info("taodan-{}:创建淘单任务失败.error={}", username, "用户已禁用，请联系管理员!");
			vo = new DataVo(0, "用户已禁用，请联系管理员!", null);
			return vo;
		}
		TaodanTask t = new TaodanTask();
		t.setTaskId(taskId);
		TaodanTask task = super.queryOne(t, null);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp http = PddHttp.getInstance(pdd);
		if (task == null) {
			logger.info("taodan-{}:{}:淘单任务不存在，数据库新建任务", username, taskSn);
			task = new TaodanTask();
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
			task.setTaskStatus(TaodanTaskVo.STATUS_WAITPAY);
			Document doc = Jsoup.parse(taskDetail);
			Element table = doc.getElementsByTag("table").get(0);
			Elements imgs = table.getElementsByTag("img");
			Elements tds = table.getElementsByClass("cokmjhf");
			String taskBuyerDesc = tds.get(0).text().trim();
			if (taskSearchType.equals("扫")) {
				String taskSearchContent = imgs.get(0).attr("src");
				task.setTaskSearchContent(taskSearchContent);
				String taskBuyerImg = imgs.get(1).attr("src");
				task.setTaskBuyerImg(taskBuyerImg);
				String taskBuyerUrl = QRCodeUtil.decode(taskSearchContent);// 扫二维码
				if (StringUtils.isBlank(taskBuyerUrl)) {
					logger.info("taodan-{}:创建淘单任务失败.error={}", username, "二维码有问题!");
					vo = new DataVo(0, "二维码有问题!", null);
					return vo;
				}
				task.setTaskBuyerUrl(taskBuyerUrl);
				try {
					int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
					if (endFix < 0)
						endFix = taskBuyerUrl.length();
					String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
					JSONObject product = http.productChromeDetail(goodsId);
					JSONObject mall = product.getJSONObject("store").getJSONObject("initDataObj").getJSONObject("mall");
					pddMallWyService.savePddMall(mall.getString("mallID"), mall.getString("mallName"),
							mall.getString("logo"));
				} catch (Exception e) {
					logger.error("pdd-{}: pdd异常，请去拼多多刷新一下", username);
				}

			} else if (taskSearchType.equals("搜")) {
				String taskSearchContent = tds.get(1).text().trim();
				String mallName = tds.get(2).getElementsByClass("cokmjhf").get(0).text().replace("*", "").trim();
				String mallImg = imgs.get(0).attr("src");
				task.setTaskSearchContent(taskSearchContent);
				String taskBuyerImg = imgs.get(1).attr("src");
				task.setTaskBuyerImg(taskBuyerImg);
				logger.info("taodan-{}:开始搜索任务.taskId={}|mallName={}|mallImg={}|taskBuyerImg={}", username, taskId,
						mallName, mallImg, taskBuyerImg);
				if (StringUtils.isBlank(mallName) || StringUtils.isBlank(mallImg)) {
					vo = new DataVo(0, "没有找到合适的商品!", null);
					return vo;
				}
				String logoKey = getImageName(mallImg);
				PddMall pddMall = pddMallWyService.queryPddMallByLogoKey(logoKey);
				String goodsId = "";
				if (pddMall != null && StringUtils.isNotBlank(pddMall.getMallId())) {
					PddMallGoods pddMallGoods = pddMallGoodsWyService.queryPddMallGoodsByLogoKey(pddMall.getMallId(), getImageName(taskBuyerImg));
					if(pddMallGoods!=null && StringUtils.isNotBlank(pddMallGoods.getGoodsId())) {
						goodsId = pddMallGoods.getGoodsId();
					}else {
						goodsId = http.searchGoodsIdByMallId(pddMall.getMallId(), getImageName(taskBuyerImg));
						pddMallGoodsWyService.savePddMallGoods(pddMall.getMallId(), taskBuyerImg, goodsId);
					}
					if (StringUtils.isBlank(goodsId)) {
						logger.info("taodan-{}:创建任务失败01.error={}|mallId={}", username, "没有找到合适的商品",
								pddMall.getMallId());
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					}
				} else {
					String mallId = http.searchMall(mallName, mallImg);
					if (StringUtils.isBlank(mallId)) {
						logger.info("taodan-{}:创建任务失败02.error={}", username, "没有找到合适的商品");
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					} else {
						pddMallWyService.savePddMall(mallId, mallName, mallImg);
					}
					goodsId = http.searchGoodsIdByMallId(mallId, getImageName(taskBuyerImg));
					pddMallGoodsWyService.savePddMallGoods(mallId, taskBuyerImg, goodsId);
					if (StringUtils.isBlank(goodsId)) {
						logger.info("taodan-{}:创建任务失败03.error={}|mallId={}", username, "没有找到合适的商品", mallId);
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					}
				}
				String taskBuyerUrl = "http://mobile.yangkeduo.com/goods.html?goods_id=" + goodsId;// 搜索
				task.setTaskBuyerUrl(taskBuyerUrl);
			} else if (taskSearchType.equals("类")) {
				logger.info("taodan-{}:创建淘单任务失败.error={}", username, "不支持搜索任务");
				String taskSearchContent = tds.get(1).text().trim();
				String mallName = tds.get(2).text().replace("*", "").trim();
				String mallImg = imgs.get(0).attr("src");
				task.setTaskSearchContent(taskSearchContent);
				String taskBuyerImg = imgs.get(1).attr("src");
				task.setTaskBuyerImg(taskBuyerImg);
				logger.info("taodan-{}:开始搜索任务.mallName={}|mallImg={}|taskBuyerImg={}", mallName, mallImg, mallImg,
						taskBuyerImg);
				if (StringUtils.isBlank(mallName)) {
					vo = new DataVo(0, "没有找到合适的商品!", null);
					return vo;
				}
				String logoKey = getImageName(mallImg);
				PddMall pddMall = pddMallWyService.queryPddMallByLogoKey(logoKey);
				String goodsId = "";
				if (pddMall != null) {
					goodsId = http.searchGoodsIdByMallId(pddMall.getMallId(), getImageName(taskBuyerImg));
					if (StringUtils.isBlank(goodsId)) {
						logger.info("taodan-{}:创建任务失败.error={}", username, "没有找到合适的商品");
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					}
				} else {
					String mallId = http.searchMall(mallName, mallImg);
					if (StringUtils.isBlank(mallId)) {
						logger.info("taodan-{}:创建任务失败.error={}", username, "没有找到合适的商品");
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					} else {
						pddMallWyService.savePddMall(mallId, mallName, mallImg);
					}
					goodsId = http.searchGoodsIdByMallId(mallId, getImageName(taskBuyerImg));
					if (StringUtils.isBlank(goodsId)) {
						logger.info("taodan-{}:创建任务失败.error={}", username, "没有找到合适的商品");
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					}
				}
				String taskBuyerUrl = "http://mobile.yangkeduo.com/goods.html?goods_id=" + goodsId;// 搜索
				task.setTaskBuyerUrl(taskBuyerUrl);
			}
			task.setTaskBuyerDesc(taskBuyerDesc);
			String taskEvaluateContent = getCommentContent();
			task.setTaskEvaluateContent(taskEvaluateContent);
			task.setTaskSearchType(taskSearchType);
		}
		if (StringUtils.isBlank(task.getPddOrderNo())) {
			String taskBuyerUrl = task.getTaskBuyerUrl();
			int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
			if (endFix < 0)
				endFix = taskBuyerUrl.length();
			String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
			boolean group = true;
			if (task.getTaskOrderType().equals("开团"))
				group = false;
			boolean ddtz = false;
			if ("17755117870".equals(user.getPddUsername())) {
				ddtz = true;
			}
			DataVo orderinfo = pddUserWyService.createOrder(user.getPddUsername(), goodsId,
					Float.valueOf(task.getTaskPrice()), group, ddtz);
			if (orderinfo.getStatus() == 1) {
				JSONObject order = (JSONObject) orderinfo.getData();
				vo = new DataVo(1, "创建pdd订单成功.", null);
				task.setPddOrderId(order.getString("group_order_id"));
				task.setPddOrderNo(order.getString("order_sn"));
				task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_WAITPAY);
			} else {
				vo = orderinfo;
			}
		}
		JSONObject order = new JSONObject();
		order.put("pddOrderNo", task.getPddOrderNo());
		order.put("pddOrderId", task.getPddOrderId());
		order.put("name", user.getName());
		if (StringUtils.isNotBlank(task.getPddOrderNo())
				&& TaodanTaskVo.PDD_STATUS_WAITPAY.equals(task.getPddOrderStatus())) {
			boolean payed = http.isPayed(task.getPddOrderNo());
			if (payed) {
				task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_WAITSEND);
				vo = new DataVo(1, "更新pdd订单状态成功.", null);
			} else {
				logger.info("taodan-{}:{}:生成支付宝支付地址", username, taskSn);
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

	private static String getCommentContent() {
		String[] content = { "五星好评,东西价格便宜性价比高,商家态度也很热情,东西到的很快!", "不错,很喜欢,价格便宜性价比高,商家态度也很热情!",
				"性价比高,商家态度也很热情,东西到的很快,非常满意!", "比在商场上的强太多了,最主要很实惠.很喜欢 ,值得推荐!",
				"五星好评,东西价格便宜性价比高,商家态度也很热情,超喜欢下次还来!", "和描述一样好,商家态度也很热情,东西到的很快!",
				"宝贝收到了,太好了,五星好评,最主要很实惠.很喜欢,值得推荐!", "还不错,挺好用的,商家态度也很热情,价格也不贵,值得推荐!" };
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

	public DataVo confirmReciveGoods(String taskSn, String pjcontentDiv, String cookiestr) {
		DataVo vo = new DataVo(1, "确认收货完成.", taskSn);
		TaodanTask t = new TaodanTask();
		t.setTaskSn(taskSn);
		TaodanTask task = super.queryOne(t, null);
		String pddUsername = task.getPddUsername();
		String taskBuyerUrl = task.getTaskBuyerUrl();
		int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
		if (endFix < 0)
			endFix = taskBuyerUrl.length();
		String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);

		if (StringUtils.isNotBlank(pjcontentDiv)) {
			Document doc = Jsoup.parse(pjcontentDiv);
			String pjcontent = doc.getElementsByClass("ynkdnd").get(0).text().trim();
			if (StringUtils.isNotBlank(pjcontent) && !"评价内容自由发挥，5分好评".equals(pjcontent) && !"无字好评".equals(pjcontent)) {
				task.setTaskEvaluateType("2");
				task.setTaskEvaluateContent(CommonUtils.filter(pjcontent));
			}
			if (StringUtils.isBlank(task.getTaskEvaluateContent())) {
				String taskEvaluateContent = getCommentContent();
				task.setTaskEvaluateContent(taskEvaluateContent);
			}
			Element tpimages = doc.getElementsByClass("tpimages").get(0);
			Elements images = tpimages.getElementsByTag("img");
			String taskEvaluateImg = "";
			for (int i = 0; i < images.size(); i++) {
				String imgUrl = images.get(i).attr("src");
				taskEvaluateImg += ";" + imgUrl;
			}
			if (StringUtils.isNotBlank(taskEvaluateImg)) {
				taskEvaluateImg = taskEvaluateImg.substring(1);
				task.setTaskEvaluateType("3");
			}
			task.setTaskEvaluateImg(taskEvaluateImg);
			super.save(task);
		}
		String msg = pddUserWyService.confirmReciveGoods(pddUsername, task.getPddOrderNo(), goodsId,
				task.getTaskEvaluateContent(),
				task.getTaskEvaluateImg().isEmpty() ? null : task.getTaskEvaluateImg().split(";"));
		if (StringUtils.isBlank(msg)) {
			logger.info("taodan-{}:{}|拼多多确认收货已完成，开始淘单上传截图.", task.getUsername(), taskSn);
			TaodanHttp tdHttp = new TaodanHttp(task.getUsername(), cookiestr);
			String uploadImgMsg = tdHttp.recProductFrom(task.getUsername(), task.getTaskId(),
					pddImgPath + File.separator + task.getPddOrderNo() + ".png");
			if (StringUtils.isBlank(uploadImgMsg)) {
				task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_FINISHED);
				task.setTaskStatus(TaodanTaskVo.STATUS_WAITFINISH);
				super.save(task);
			} else {
				vo = new DataVo(0, uploadImgMsg, taskSn);
			}
		} else {
			vo = new DataVo(0, msg, taskSn);
		}
		return vo;
	}

	public DataVo orderstatus(String username, String taskSn) {
		DataVo vo = new DataVo(1, "", null);
		TaodanUser user = taodanUserWyService.queryByUsername(username);
		TaodanTask t = new TaodanTask();
		t.setTaskSn(taskSn);
		TaodanTask task = super.queryOne(t, null);

		if (StringUtils.isNotBlank(task.getPddOrderNo())) {
			if (TaodanTaskVo.PDD_STATUS_WAITPAY.equals(task.getPddOrderStatus())) {
				PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
				PddHttp http = PddHttp.getInstance(pdd);
				boolean payed = http.isPayed(task.getPddOrderNo());
				if (payed) {
					task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_WAITSEND);
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

	public List<String> getTaskIds(String username, int state, int page) {
		TaodanHttp tdHttp = TaodanHttp.getInstance(username);
		List<String> taskIds = tdHttp.MyRecTaskList(state, page);
		if (taskIds != null && taskIds.size() > 0) {
			List<String> tIds = getTaskIds(username, state, page + 1);
			taskIds.addAll(tIds);
		}
		return taskIds;
	}

	public DataVo updatePayedOrderStatus(String username, String cookie) {
		DataVo vo = new DataVo(1, "", null);
		TaodanUser user = taodanUserWyService.queryByUsername(username);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp pddHttp = PddHttp.getInstance(pdd);
		TaodanHttp tdHttp = new TaodanHttp(username, cookie);
		List<String> taskIds = getTaskIds(username, 2, 1);
		for (String taskId : taskIds) {
			TaodanTask t = new TaodanTask();
			t.setTaskId(taskId);
			TaodanTask task = super.queryOne(t, null);
			long time = (new Date().getTime() - task.getCreateTime().getTime()) / 3600000;
			if (time > 29) {
				JSONObject order = pddHttp.queryOrderDetail(task.getPddOrderNo());
				if ("待收货".equals(order.getString("chatStatusPrompt"))
						|| "待评价".equals(order.getString("chatStatusPrompt"))
						|| "已评价".equals(order.getString("chatStatusPrompt"))) {
					boolean success = tdHttp.updateTaskFh(taskId);
					if (!success) {
						vo = new DataVo(0, "更新任务成发货状态失败", taskId);
						return vo;
					} else {
						task.setTaskStatus(TaodanTaskVo.STATUS_WAITRECIVE);
						super.save(task);
					}
				}
			}
		}
		// vo = updateConfirmOrderStatus(username, cookie);
		return vo;
	}

	public DataVo updateConfirmOrderStatus(String username, String cookie) {
		logger.info("taodan-{}:|开始淘单确认收货.", username);
		DataVo vo = new DataVo(1, "", null);
		TaodanHttp tdHttp = new TaodanHttp(username, cookie);
		update10000OrderStatus(username);
		List<String> taskIds = getTaskIds(username, 3, 1);
		logger.info("taodan-{}:|确认收货.taskIds={}", username, taskIds);
		for (String taskId : taskIds) {
			TaodanTask t = new TaodanTask();
			t.setTaskId(taskId);
			TaodanTask task = super.queryOne(t, null);
			long time = (new Date().getTime() - task.getCreateTime().getTime()) / 3600000;
			if (time > 24 * 5) {
				String pjcontentDiv = tdHttp.getPjContent(taskId);
				if (StringUtils.isNotBlank(pjcontentDiv)) {
					Document doc = Jsoup.parse(pjcontentDiv);
					String pjcontent = doc.getElementsByClass("ynkdnd").get(0).text().trim();
					if (StringUtils.isNotBlank(pjcontent) && !"评价内容自由发挥，5分好评".equals(pjcontent)
							&& !"无字好评".equals(pjcontent)) {
						task.setTaskEvaluateType("2");
						task.setTaskEvaluateContent(CommonUtils.filter(pjcontent));
					}
					if (StringUtils.isBlank(task.getTaskEvaluateContent())) {
						String taskEvaluateContent = getCommentContent();
						task.setTaskEvaluateContent(taskEvaluateContent);
					}
					Element tpimages = doc.getElementsByClass("tpimages").get(0);
					Elements images = tpimages.getElementsByTag("img");
					String taskEvaluateImg = "";
					for (int i = 0; i < images.size(); i++) {
						String imgUrl = images.get(i).attr("src");
						taskEvaluateImg += ";" + imgUrl;
					}
					if (StringUtils.isNotBlank(taskEvaluateImg)) {
						taskEvaluateImg = taskEvaluateImg.substring(1);
						task.setTaskEvaluateType("3");
					}
					task.setTaskEvaluateImg(taskEvaluateImg);
					super.save(task);

					String taskBuyerUrl = task.getTaskBuyerUrl();
					int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
					if (endFix < 0)
						endFix = taskBuyerUrl.length();
					String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
					String msg = pddUserWyService.confirmReciveGoodsNew(task.getPddUsername(), task.getPddOrderNo(),
							goodsId, task.getTaskEvaluateContent(),
							task.getTaskEvaluateImg().isEmpty() ? null : task.getTaskEvaluateImg().split(";"));
					if (StringUtils.isBlank(msg)) {
						logger.info("taodan-{}:{}|拼多多确认收货已完成，等待下载图片和淘单上传截图.", task.getUsername(), task.getTaskSn());
						/*
						 * String uploadImgMsg = tdHttp.recProductFrom(task.getUsername(),
						 * task.getTaskId(), pddImgPath + File.separator + task.getPddOrderNo() +
						 * ".png"); if (StringUtils.isBlank(uploadImgMsg)) {
						 * task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_FINISHED);
						 * task.setTaskStatus(TaodanTaskVo.STATUS_WAITFINISH); super.save(task); } else
						 * { vo = new DataVo(0, uploadImgMsg, task.getTaskSn()); return vo; }
						 */
						task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_WAITFINISH);
						task.setTaskStatus(TaodanTaskVo.STATUS_WAITRECIVE);
						super.save(task);
					} else {
						vo = new DataVo(0, msg, task.getTaskSn());
						return vo;
					}
				} else {
					vo = new DataVo(0, "获取评价内容失败!", task.getTaskSn());
					return vo;
				}
			}
		}
		logger.info("taodan-{}:{}|结束淘单确认收货.", username);
		return vo;
	}

	public int updateConfirmOrderStatus(String username) {
		int count = 0;
		logger.info("taodan-{}:{}|开始淘单确认收货.", username);
		TaodanHttp tdHttp = TaodanHttp.getInstance(username);
		List<TaodanTask> tasks = queryFiveDayTask(username);
		logger.info("taodan-{}:|确认收货.任务数={}", username, tasks.size());
		logger.info("taodan-{}:|确认收货.cookie={}", username, tdHttp.getCookie());
		for (TaodanTask task : tasks) {
			logger.info("taodan-{}:|确认收货.taskId={}", username, task.getTaskId());
			String pjcontentDiv = tdHttp.getPjContent(task.getTaskId());
			if (StringUtils.isNotBlank(pjcontentDiv)) {
				Document doc = Jsoup.parse(pjcontentDiv);
				String pjcontent = doc.getElementsByClass("ynkdnd").get(0).text().trim();
				logger.info("taodan-{}:|确认收货.taskId={}|评价内容={}", username, task.getTaskId(), pjcontent);
				if (StringUtils.isNotBlank(pjcontent) && !"评价内容自由发挥，5分好评".equals(pjcontent)
						&& !"无字好评".equals(pjcontent)) {
					task.setTaskEvaluateType("2");
					task.setTaskEvaluateContent(CommonUtils.filter(pjcontent));
				}
				if (StringUtils.isBlank(task.getTaskEvaluateContent())) {
					String taskEvaluateContent = getCommentContent();
					task.setTaskEvaluateContent(taskEvaluateContent);
				}
				Element tpimages = doc.getElementsByClass("tpimages").get(0);
				Elements images = tpimages.getElementsByTag("img");
				String taskEvaluateImg = "";
				for (int i = 0; i < images.size(); i++) {
					String imgUrl = images.get(i).attr("src");
					taskEvaluateImg += ";" + imgUrl;
				}
				if (StringUtils.isNotBlank(taskEvaluateImg)) {
					taskEvaluateImg = taskEvaluateImg.substring(1);
					task.setTaskEvaluateType("3");
				}
				task.setTaskEvaluateImg(taskEvaluateImg);
				super.save(task);

				String taskBuyerUrl = task.getTaskBuyerUrl();
				int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
				if (endFix < 0)
					endFix = taskBuyerUrl.length();
				String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
				String msg = pddUserWyService.confirmReciveGoodsNew(task.getPddUsername(), task.getPddOrderNo(),
						goodsId, task.getTaskEvaluateContent(),
						task.getTaskEvaluateImg().isEmpty() ? null : task.getTaskEvaluateImg().split(";"));
				if (StringUtils.isBlank(msg)) {
					logger.info("taodan-{}:{}|拼多多确认收货已完成，等待下载图片和淘单上传截图.", task.getUsername(), task.getTaskSn());
					/*
					 * String uploadImgMsg = tdHttp.recProductFrom(task.getUsername(),
					 * task.getTaskId(), pddImgPath + File.separator + task.getPddOrderNo() +
					 * ".png"); if (StringUtils.isBlank(uploadImgMsg)) {
					 * task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_FINISHED);
					 * task.setTaskStatus(TaodanTaskVo.STATUS_WAITFINISH); super.save(task);
					 * count++; } else { return count; }
					 */
					task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_WAITFINISH);
					task.setTaskStatus(TaodanTaskVo.STATUS_WAITRECIVE);
					super.save(task);
					count++;
				} else if ("拼单成功，待发货".equals(msg)) {
					logger.info("taodan-{}:{}|订单状态:拼单成功，待发货", task.getUsername(), task.getTaskSn());
				} else {
					return count;
				}
			} else {
				return count;
			}
		}
		logger.info("taodan-{}:{}|结束淘单确认收货.", username);
		return count;
	}

	public DataVo update10000OrderStatus(String username) {
		logger.info("taodan-{}:{}|开始淘单催收确认收货.", username);
		DataVo vo = new DataVo(1, "", null);
		TaodanHttp tdHttp = TaodanHttp.getInstance(username);
		List<String> taskIds = getTaskIds(username, 10000, 1);
		logger.info("taodan-{}:|催收确认收货.taskIds={}", username, taskIds);
		for (String taskId : taskIds) {
			TaodanTask t = new TaodanTask();
			t.setTaskId(taskId);
			TaodanTask task = super.queryOne(t, null);
			String pjcontentDiv = tdHttp.getPjContent(taskId);
			if (StringUtils.isNotBlank(pjcontentDiv)) {
				Document doc = Jsoup.parse(pjcontentDiv);
				String pjcontent = doc.getElementsByClass("ynkdnd").get(0).text().trim();
				if (StringUtils.isNotBlank(pjcontent) && !"评价内容自由发挥，5分好评".equals(pjcontent)
						&& !"无字好评".equals(pjcontent)) {
					task.setTaskEvaluateType("2");
					task.setTaskEvaluateContent(CommonUtils.filter(pjcontent));
				}
				if (StringUtils.isBlank(task.getTaskEvaluateContent())) {
					String taskEvaluateContent = getCommentContent();
					task.setTaskEvaluateContent(taskEvaluateContent);
				}
				Element tpimages = doc.getElementsByClass("tpimages").get(0);
				Elements images = tpimages.getElementsByTag("img");
				String taskEvaluateImg = "";
				for (int i = 0; i < images.size(); i++) {
					String imgUrl = images.get(i).attr("src");
					taskEvaluateImg += ";" + imgUrl;
				}
				if (StringUtils.isNotBlank(taskEvaluateImg)) {
					taskEvaluateImg = taskEvaluateImg.substring(1);
					task.setTaskEvaluateType("3");
				}
				task.setTaskEvaluateImg(taskEvaluateImg);
				super.save(task);

				String taskBuyerUrl = task.getTaskBuyerUrl();
				int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
				if (endFix < 0)
					endFix = taskBuyerUrl.length();
				String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
				String msg = pddUserWyService.confirmReciveGoodsNew(task.getPddUsername(), task.getPddOrderNo(),
						goodsId, task.getTaskEvaluateContent(),
						task.getTaskEvaluateImg().isEmpty() ? null : task.getTaskEvaluateImg().split(";"));
				if (StringUtils.isBlank(msg)) {
					logger.info("taodan-{}:{}|拼多多确认收货已完成，等待下载图片和淘单上传截图.", task.getUsername(), task.getTaskSn());
					/*
					 * String uploadImgMsg = tdHttp.recProductFrom(task.getUsername(),
					 * task.getTaskId(), pddImgPath + File.separator + task.getPddOrderNo() +
					 * ".png"); if (StringUtils.isBlank(uploadImgMsg)) {
					 * task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_FINISHED);
					 * task.setTaskStatus(TaodanTaskVo.STATUS_WAITFINISH); super.save(task); } else
					 * { vo = new DataVo(0, uploadImgMsg, task.getTaskSn()); return vo; }
					 */
					task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_WAITFINISH);
					task.setTaskStatus(TaodanTaskVo.STATUS_WAITRECIVE);
					super.save(task);
				} else {
					vo = new DataVo(0, msg, task.getTaskSn());
					return vo;
				}
			} else {
				vo = new DataVo(0, "获取评价内容失败!", task.getTaskSn());
				return vo;
			}
		}
		logger.info("taodan-{}:{}|结束淘单催收确认收货.", username);
		return vo;
	}

	public DataVo updateMall(String username, String cookie, String taskId) {
		DataVo vo = new DataVo(1, "", null);
		TaodanUser user = taodanUserWyService.queryByUsername(username);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp pddHttp = PddHttp.getInstance(pdd);
		TaodanHttp tdHttp = new TaodanHttp(username, cookie);
		JSONObject mall = tdHttp.taskDetail(taskId);
		if (mall == null) {
			vo = new DataVo(0, "晚了一步，任务已被接走了", taskId);
			return vo;
		}
		String mallName = mall.getString("mallName");
		String mallLogo = mall.getString("mallLogo");
		String logoKey = getImageName(mallLogo);
		PddMall pddMall = pddMallWyService.queryPddMallByLogoKey(logoKey);
		if (pddMall == null) {
			String mallId = pddHttp.searchMall(mallName, logoKey);
			pddMall = new PddMall();
			pddMall.setMallId(mallId);
			pddMall.setMallLogo(mallLogo);
			pddMall.setMallName(mallName);
			pddMall.setLogoKey(logoKey);
			pddMallWyService.save(pddMall);
			vo = new DataVo(1, "成功解析商家id,已保存到数据库了", mallId);
		} else {
			vo = new DataVo(0, "任务已经解析过了，还是没有找到商家id", taskId);
			return vo;
		}
		return vo;
	}

	/**
	 * 下载拼多多拼论图片
	 * 
	 * @param username
	 * @return
	 */
	public int downloadAllCommentImage(String username) {
		TaodanUser user = taodanUserWyService.queryByUsername(username);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp pddHttp = PddHttp.getInstance(pdd);
		int count = pddHttp.downloadAllCommentImage(pddImgPath);
		return count;
	}

	/**
	 * 上传淘单图片
	 * 
	 * @param username
	 * @return
	 */
	public int uploadTaodanImg(String username) {
		int count = 0;
		logger.info("taodan-{}:{}|开始上传淘单图片.", username);
		TaodanHttp tdHttp = TaodanHttp.getInstance(username);
		List<TaodanTask> tasks = queryUnfinishTask(username);
		logger.info("taodan-{}:|上传淘单图片.任务数={}", username, tasks.size());
		logger.info("taodan-{}:|上传淘单图片.cookie={}", username, tdHttp.getCookie());
		for (TaodanTask task : tasks) {
			logger.info("taodan-{}:|上传淘单图片.taskId={}", username, task.getTaskId());
			String orginImg = pddImgPath + File.separator + task.getPddOrderNo() + ".png";
			if (new File(orginImg).exists()) {
				String uploadImgMsg = tdHttp.recProductFrom(task.getUsername(), task.getTaskId(), orginImg);
				if (StringUtils.isBlank(uploadImgMsg)) {
					task.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_FINISHED);
					task.setTaskStatus(TaodanTaskVo.STATUS_WAITFINISH);
					super.save(task);
					count++;
				} else {
					return count;
				}
			} else {
				logger.info("taodan-{}:{}|上传淘单图片失败，没有找到图片.", username, task.getTaskId());
				continue;
			}
		}
		logger.info("taodan-{}:{}|结束上传淘单图片.", username);
		return count;

	}

	/**
	 * 自动提现
	 * 
	 * @param username
	 * @return
	 */
	public String withdraw(String username) {
		TaodanHttp tdHttp = TaodanHttp.getInstance(username);
		String msg = tdHttp.withdraw();
		return msg;
	}

	/**
	 * 查询5天前的任务 未确认收货的任务
	 * 
	 * @param username
	 * @return
	 */
	public List<TaodanTask> queryFiveDayTask(String username) {
		TaodanTask t = new TaodanTask();
		t.setUsername(username);
		t.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_WAITSEND);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_YEAR, -5);
		t.setCreateTime(c.getTime());
		return super.queryList(t, null);
	}

	/**
	 * 查询5天前的任务 未上传淘单图片
	 * 
	 * @param username
	 * @return
	 */
	public List<TaodanTask> queryUnfinishTask(String username) {
		TaodanTask t = new TaodanTask();
		t.setUsername(username);
		t.setTaskStatus(TaodanTaskVo.STATUS_WAITRECIVE);
		t.setPddOrderStatus(TaodanTaskVo.PDD_STATUS_WAITFINISH);
		/*
		 * Calendar c = Calendar.getInstance(); c.setTime(new Date());
		 * c.add(Calendar.DAY_OF_YEAR, -5); t.setCreateTime(c.getTime());
		 */
		return super.queryList(t, null);
	}

	@Override
	public Weekend<TaodanTask> genSqlExample(TaodanTask t) {
		Weekend<TaodanTask> w = super.genSqlExample(t);
		WeekendCriteria<TaodanTask, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(TaodanTask::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getPddOrderNo())) {
			c.andLike(TaodanTask::getPddOrderNo, "%" + t.getPddOrderNo().trim() + "%");
		}
		if (StringUtils.isNotBlank(t.getTaskId())) {
			c.andEqualTo(TaodanTask::getTaskId, t.getTaskId());
		}
		if (StringUtils.isNotBlank(t.getTaskSn())) {
			c.andEqualTo(TaodanTask::getTaskSn, t.getTaskSn());
		}
		if (StringUtils.isNotBlank(t.getTaskStatus())) {
			c.andEqualTo(TaodanTask::getTaskStatus, t.getTaskStatus());
		}
		if (StringUtils.isNotBlank(t.getPddOrderStatus())) {
			c.andEqualTo(TaodanTask::getPddOrderStatus, t.getPddOrderStatus());
		}
		if (t.getCreateTime() != null) {
			c.andLessThan(TaodanTask::getCreateTime, t.getCreateTime());
		}
		w.and(c);
		return w;
	}
}
