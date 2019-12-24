package com.atao.dftt.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.atao.dftt.api.vo.ZhuankeTaskVo;
import com.atao.dftt.api.vo.ZhuankeTaskVo;
import com.atao.dftt.http.PddHttp;
import com.atao.dftt.http.TaodanHttp;
import com.atao.dftt.http.ZhuankeHttp;
import com.atao.dftt.mapper.ZhuankeTaskMapper;
import com.atao.dftt.model.PddMall;
import com.atao.dftt.model.PddMallGoods;
import com.atao.dftt.model.PddUser;
import com.atao.dftt.model.ZhuankeTask;
import com.atao.dftt.model.ZhuankeUser;
import com.atao.dftt.util.CommonUtils;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 *
 * @author twang
 */
@Service
public class ZhuankeTaskWyService extends BaseService<ZhuankeTask> {

	@Resource
	private ZhuankeTaskMapper zhuankeTaskMapper;
	@Resource
	private ZhuankeTaskWyService zhuankeTaskWyService;
	@Resource
	private ZhuankeUserWyService zhuankeUserWyService;
	@Resource
	private PddUserWyService pddUserWyService;
	@Resource
	private PddMallWyService pddMallWyService;
	@Resource
	private PddMallGoodsWyService pddMallGoodsWyService;
	@Value("${pdd.pj.img.path}")
	public String pddImgPath;// 评多多图片路径

	@Override
	public BaseMapper<ZhuankeTask> getMapper() {
		return zhuankeTaskMapper;
	}

	public DataVo createTask(String username, String taskId, String taskSn, String taskPrice, String taskRecType,
			String taskSearchType, String taskOrderType, String taskEvaluateType, String taskGold, String taskStatus,
			String taskDetail, String cookie) {
		PddUser mUser = pddUserWyService.queryUserByUsername(PddHttp.searchUsername);
		PddHttp mHttp = new PddHttp(mUser);

		DataVo vo = new DataVo(1, "图片已生成成功,请稍后下单支付.", null);
		ZhuankeUser user = zhuankeUserWyService.queryByUsername(username);
		if (!user.getUsed()) {
			logger.info("zhuanke-{}:创建赚客任务失败.error={}", username, "用户已禁用，请联系管理员!");
			vo = new DataVo(0, "用户已禁用，请联系管理员!", null);
			return vo;
		}
		ZhuankeTask t = new ZhuankeTask();
		t.setUsername(username);
		t.setTaskId(taskId);
		ZhuankeTask task = super.queryOne(t, null);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp http = PddHttp.getInstance(pdd);
		ZhuankeHttp zhuankeHttp = new ZhuankeHttp(username, cookie);
		if (task == null) {
			logger.info("zhuanke-{}:{}|开始创建任务", username, taskId);
			task = new ZhuankeTask();
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
			task.setTaskStatus(ZhuankeTaskVo.STATUS_WAITPAY);
			Document doc = Jsoup.parse(taskDetail);
			Element table = doc.getElementsByTag("table").get(0);
			Elements trs = table.getElementsByTag("tr");
			String skuNumber = trs.get(10).getElementsByClass("red").get(1).text();// 商品数量
			task.setSkuNumber(skuNumber);
			boolean isYhj = false;
			String couponPrice = "";// 优惠卷价格
			String yhj = trs.get(11).getElementsByTag("td").get(0).text();
			if (!"不使用".equals(yhj)) {
				isYhj = true;
				couponPrice = trs.get(11).getElementsByTag("td").get(0).getElementsByClass("red").get(0).text();
			}
			task.setCouponPrice(couponPrice);
			if (taskSearchType.equals("扫")) {
				logger.info("zhuanke-{}:{}|开始扫码任务", username, taskId);
				String taskBuyerDesc = trs.get(0).text().trim();
				task.setTaskBuyerDesc(taskBuyerDesc);
				String taskSearchContent = "http://zk.gxrdwl.com"
						+ trs.get(1).getElementsByTag("img").get(0).attr("src");
				task.setTaskSearchContent(taskSearchContent);
				String taskBuyerImg = trs.get(3).getElementsByTag("img").get(0).attr("src");
				task.setTaskBuyerImg(taskBuyerImg);
				String taskBuyerUrl = zhuankeHttp.decode(taskSearchContent);// 扫二维码
				String goodsId = "";
				if (StringUtils.isBlank(taskBuyerUrl)) {
					logger.error("zhuanke-{}:{}|创建任务失败.error={}", username, taskId, "二维码有问题!");
					vo = new DataVo(0, "二维码有问题!", null);
					return vo;
				} else if (!taskBuyerUrl.contains("goods_id")) {
					taskBuyerUrl = http.decodeCouponUrl(taskBuyerUrl);
					if (StringUtils.isBlank(taskBuyerUrl)) {
						logger.error("zhuanke-{}:{}|创建任务失败.error={}", username, taskId, "二维码有问题!");
						vo = new DataVo(0, "二维码有问题!", null);
						return vo;
					}
					taskBuyerUrl = CommonUtils.decode(taskBuyerUrl);
					logger.info("zhuanke-{}:{}|优惠卷领取地址.taskBuyerUrl={}", username, taskId, taskBuyerUrl);
					if (StringUtils.isBlank(taskBuyerUrl) || taskBuyerUrl.indexOf("goods_id=") < 0) {
						logger.error("zhuanke-{}:{}|优惠卷领取地址有问题", username, taskId);
						vo = new DataVo(0, "二维码有问题!", null);
						return vo;
					}
					int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
					if (endFix < 0)
						endFix = taskBuyerUrl.length();
					goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
					goodsId = goodsId.trim();
					if (isYhj) {// 领取优惠卷
						JSONObject couponDetail = mHttp.couponUrlDetail(taskBuyerUrl);
						if (couponDetail == null) {
							logger.error("zhuanke-{}:{}|领取优惠卷详细有问题", username, taskId);
							vo = new DataVo(0, "二维码有问题!", null);
							return vo;
						}
						JSONObject couponData = couponDetail.getJSONObject("store").getJSONObject("couponData");
						JSONObject mallCollectCoupon = couponData.getJSONObject("mallCollectCoupon");
						String couponPriceAct = couponData.getString("discount");
						String couponSn = couponData.getString("couponSn");
						if (StringUtils.isBlank(couponSn) && mallCollectCoupon != null) {// 收藏优惠卷
							/*
							 * http.scMaller(couponData.getString("mallId")); couponPrice =
							 * mallCollectCoupon.getIntValue("coupon_price")/100+""; couponSn =
							 * mallCollectCoupon.getString("coupon_sn");
							 */
							logger.error("zhuanke-{}:{}|收藏优惠卷暂不支持", username, taskId);
							vo = new DataVo(0, "二维码有问题!", null);
							return vo;
						}
						endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("pid=") + 4);
						if (endFix < 0)
							endFix = taskBuyerUrl.length();
						String pid = taskBuyerUrl.substring(taskBuyerUrl.indexOf("pid=") + 4, endFix);
						boolean success = http.getGoodsCoupon(couponData.getString("mallId"), goodsId, couponSn, pid);
						if (!success) {
							logger.error("zhuanke-{}:{}|获取优惠卷失败", username, taskId);
							vo = new DataVo(0, "二维码有问题!", null);
							return vo;
						}
					}
				} else {
					int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
					if (endFix < 0)
						endFix = taskBuyerUrl.length();
					goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
					goodsId = goodsId.trim();
					if (isYhj) {// 领取优惠卷
						String couponSn = mHttp.getCouponSn(goodsId, Float.valueOf(couponPrice));
						boolean success = http.getProductDetailCoupon(goodsId, couponSn);
						if (!success) {
							logger.error("zhuanke-{}:{}|获取商品详情优惠卷失败", username, taskId);
							vo = new DataVo(0, "二维码有问题!", null);
							return vo;
						}
					}
				}
				taskBuyerUrl = taskBuyerUrl.trim();
				task.setTaskBuyerUrl("https://mobile.yangkeduo.com/goods.html?goods_id="+goodsId);
				if (StringUtils.isBlank(goodsId)) {
					logger.info("zhuanke-{}:{}|taskBuyerUrl={}",username, taskId, taskBuyerUrl);
					logger.error("zhuanke-{}:{}|没有找到商品id", username, taskId);
					vo = new DataVo(0, "二维码有问题!", null);
					return vo;
				}
			} else if (taskSearchType.equals("搜")) {
				String orginImg = pddImgPath + File.separator + taskSn + ".png";
				String taskBuyerDesc = trs.get(0).text().trim();
				String taskSearchContent = table.getElementById("copyLink").text().trim();
				String mallName = trs.get(2).getElementsByClass("orange").get(1).text().trim();
				task.setTaskSearchContent(taskSearchContent);
				String taskBuyerImg = trs.get(3).getElementsByTag("img").get(0).attr("src");
				task.setTaskBuyerImg(taskBuyerImg);
				logger.info("zhuanke-{}:{}|开始搜索任务.mallName={}|taskBuyerImg={}", username, taskId, mallName,
						taskBuyerImg);
				if (StringUtils.isBlank(mallName)) {
					vo = new DataVo(0, "没有找到合适的商品!", null);
					return vo;
				}
				PddMall pddMall = pddMallWyService.queryPddMallByName(mallName);
				String goodsId = "";
				if (pddMall != null && StringUtils.isNotBlank(pddMall.getMallId())) {
					PddMallGoods pddMallGoods = pddMallGoodsWyService.queryPddMallGoodsByLogoKey(pddMall.getMallId(),
							getImageName(taskBuyerImg));
					if (pddMallGoods != null && StringUtils.isNotBlank(pddMallGoods.getGoodsId())) {
						goodsId = pddMallGoods.getGoodsId();
					} else {
						JSONObject goods = mHttp.searchGoodsIdByMallId(pddMall.getMallId(), getImageName(taskBuyerImg));
						if (goods != null) {
							goodsId = goods.getString("goodsId");
							pddMallGoodsWyService.savePddMallGoods(pddMall.getMallId(), taskBuyerImg, goodsId);
						}
					}
					if (StringUtils.isBlank(goodsId)) {
						logger.error(
								"zhuanke-{}:{}|创建任务失败01.error={}|mallId={}|taskSearchContent={}|taskBuyerImg={}|taskPrice={}",
								username, taskId, "没有找到合适的商品", pddMall.getMallId(), taskSearchContent, taskBuyerImg,
								taskPrice);
						pddMallGoodsWyService.savePddMallGoods(pddMall.getMallId(), taskBuyerImg, "");
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					}
				} else {
					JSONObject mallObject = mHttp.searchMallName(mallName);
					if (mallObject == null) {
						logger.error(
								"zhuanke-{}:{}|person manager|创建任务失败02.error={},mallName={},taskSearchContent={}|taskBuyerImg={}|taskPrice={}",
								username, taskId, "没有找到店铺", mallName, taskSearchContent, taskBuyerImg, taskPrice);
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					} else {
						pddMallWyService.savePddMall(mallObject.getString("mallId"), mallName,
								mallObject.getString("mallImg"));
					}
					JSONObject goods = mHttp.searchGoodsIdByMallId(mallObject.getString("mallId"),
							getImageName(taskBuyerImg));
					if (goods != null) {
						goodsId = goods.getString("goodsId");
						pddMallGoodsWyService.savePddMallGoods(mallObject.getString("mallId"), taskBuyerImg, goodsId);
					}
					if (StringUtils.isBlank(goodsId)) {
						pddMallGoodsWyService.savePddMallGoods(mallObject.getString("mallId"), taskBuyerImg, "");
						logger.error(
								"zhuanke-{}:{}|创建任务失败03.error={}|mallId={}|taskSearchContent={}|taskBuyerImg={}|taskPrice={}",
								username, taskId, "没有找到合适的商品", mallObject.getString("mallId"), taskSearchContent,
								taskBuyerImg, taskPrice);
						vo = new DataVo(0, "没有找到合适的商品!", null);
						return vo;
					}
				}
				String taskBuyerUrl = "http://mobile.yangkeduo.com/goods.html?goods_id=" + goodsId
						+ "&refer_page_name=search_result&refer_page_sn=10015";// 搜索
				boolean success = zhuankeHttp.checkGoods(taskId, taskBuyerUrl);
				if (!success) {
					logger.error(
							"zhuanke-{}:{}|error={}|taskBuyerUrl={}|taskSearchContent={}|taskBuyerImg={}|taskPrice={}",
							username, taskId, "商品检测不正确", taskBuyerUrl, taskSearchContent, taskBuyerImg, taskPrice);
					vo = new DataVo(0, "商品检测不正确!", null);
					return vo;
				}
				task.setTaskBuyerUrl(taskBuyerUrl);
				task.setTaskBuyerDesc(taskBuyerDesc);

				if (isYhj) {// 领取优惠卷
					String couponSn = mHttp.getCouponSn(goodsId, Float.valueOf(couponPrice));
					success = http.getProductDetailCoupon(goodsId, couponSn);
					if (!success) {
						logger.error("zhuanke-{}:{}|获取商品详情优惠卷失败", username, taskId);
						/*
						 * vo = new DataVo(0, "二维码有问题!", null); return vo;
						 */
					}
				}

				// 开始下载商品列表图片
				String msg = mHttp.downloadProductSearchImage(taskSn, goodsId, taskSearchContent, pddImgPath);
				if (StringUtils.isNotBlank(msg)) {
					logger.error("zhuanke-{}:{}|下载搜索商品图片失败,keyword={}|goodsId={}|msg={}", username, taskId,
							taskSearchContent, goodsId, msg);
					vo = new DataVo(0, "下载搜索商品图片失败!", null);
					return vo;
				}
				String url = zhuankeHttp.uploadProductImg(username, taskId, orginImg);
				if (StringUtils.isBlank(url)) {
					logger.error("zhuanke-{}:{}|上传搜索商品图片失败", username, taskId);
					vo = new DataVo(0, "上传搜索商品图片失败!", null);
					return vo;
				}
				String productSearchUrl = "//zk.gxrdwl.com" + url;
				vo.setData(productSearchUrl);
			}
			// String taskEvaluateContent = getCommentContent();
			// task.setTaskEvaluateContent(taskEvaluateContent);
			task.setTaskSearchType(taskSearchType);
		} else {
			if (taskSearchType.equals("搜")) {
				String orginImg = pddImgPath + File.separator + taskSn + ".png";
				String url = zhuankeHttp.uploadProductImg(username, taskId, orginImg);
				if (StringUtils.isBlank(url)) {
					logger.error("zhuanke-{}:{}|上传搜索商品图片失败", username, taskId);
					vo = new DataVo(0, "上传搜索商品图片失败!", null);
					return vo;
				}
				String productSearchUrl = "//zk.gxrdwl.com" + url;
				vo = new DataVo(1, "图片已生成成功,请稍后下单支付.", null);
				vo.setData(productSearchUrl);
			}
		}
		super.save(task);
		logger.info("zhuanke-{}:{}|完成接取任务", username, taskId);
		long sec = new Date().getTime() - task.getCreateTime().getTime();
		vo.setMsg(sec + "");
		return vo;
	}

	public DataVo createPddOrder(String username, String taskId, String cookie) {
		DataVo vo = new DataVo(1, "", null);
		ZhuankeUser user = zhuankeUserWyService.queryByUsername(username);
		ZhuankeTask t = new ZhuankeTask();
		t.setTaskId(taskId);
		t.setUsername(username);
		ZhuankeTask task = super.queryOne(t, null);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp http = PddHttp.getInstance(pdd);
		if (StringUtils.isBlank(task.getPddOrderNo())) {
			String taskBuyerUrl = task.getTaskBuyerUrl();
			int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
			if (endFix < 0)
				endFix = taskBuyerUrl.length();
			String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
			goodsId = goodsId.trim();
			boolean group = true;
			if (task.getTaskOrderType().equals("开团"))
				group = false;
			boolean ddtz = false;
			float coutonPrice = 0;
			if (StringUtils.isNotBlank(task.getCouponPrice())) {
				coutonPrice = Float.valueOf(task.getCouponPrice());
			}
			int skuNumber = 1;
			if (StringUtils.isNotBlank(task.getSkuNumber())) {
				skuNumber = Integer.valueOf(task.getSkuNumber());
			}
			DataVo orderinfo = pddUserWyService.createOrder(user.getPddUsername(), goodsId,
					Float.valueOf(task.getTaskPrice()), group, ddtz, skuNumber, coutonPrice);
			if (orderinfo.getStatus() == 1) {
				JSONObject order = (JSONObject) orderinfo.getData();
				vo = new DataVo(1, "创建pdd订单成功.", null);
				task.setPddOrderId(order.getString("group_order_id"));
				task.setPddOrderNo(order.getString("order_sn"));
				task.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_WAITPAY);
			} else {
				vo = orderinfo;
			}
		}
		JSONObject order = new JSONObject();
		order.put("pddOrderNo", task.getPddOrderNo());
		order.put("pddOrderId", task.getPddOrderId());
		order.put("pddurl", task.getTaskBuyerUrl());
		order.put("name", user.getName());
		if (StringUtils.isNotBlank(task.getPddOrderNo())
				&& ZhuankeTaskVo.PDD_STATUS_WAITPAY.equals(task.getPddOrderStatus())) {
			boolean payed = http.isPayed(task.getPddOrderNo());
			if (payed) {
				task.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_WAITSEND);
				vo = new DataVo(1, "更新pdd订单状态成功.", null);
			} else {
				logger.info("zhuanke-{}:{}:生成支付宝支付地址", username, task.getTaskSn());
				String aliPayUrl = http.genAliPayUrl(task.getPddOrderNo());
				order.put("aliPayUrl", aliPayUrl);
				if (StringUtils.isBlank(aliPayUrl)) {
					vo = new DataVo(0, "生成支付宝支付地址失败.", null);
				} else {
					vo = new DataVo(1, "生成支付宝支付地址成功.", null);
				}
			}
		}
		order.put("pddOrderStatus", task.getPddOrderStatus());
		vo.setData(order);
		super.save(task);
		return vo;

	}

	private static String getCommentContent() {
		String[] content = { "五星好评,东西价格便宜性价比高,商家态度也很热情!", "不错,很喜欢,价格便宜性价比高,商家态度也很热情!", "性价比高,商家态度也很热情,非常满意!",
				"实惠.很喜欢 ,值得推荐,专门来给个好评!", "五星好评,东西便宜性价比高,商家态度也很热情,超喜欢下次还来!", "和描述一样好,商家态度也很热情,非常满意!",
				"太好了,五星好评,实惠.很喜欢,值得推荐!", "还不错,商家态度也很热情,便宜实惠,值得推荐!" };
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

	/**
	 * 查询5天前的任务 未确认收货的任务
	 * 
	 * @param username
	 * @return
	 */
	public List<ZhuankeTask> queryFiveDayTask(String username) {
		ZhuankeTask t = new ZhuankeTask();
		t.setUsername(username);
		t.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_WAITSEND);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_YEAR, -5);
		t.setCreateTime(c.getTime());
		return super.queryList(t, null);
	}

	public ZhuankeTask queryByTaskId(String username, String taskId) {
		ZhuankeTask t = new ZhuankeTask();
		t.setUsername(username);
		t.setTaskId(taskId);
		return super.queryOne(t, null);
	}

	public int updateTxConfirmOrderStatus(String username) {
		int count = 0;
		logger.info("zhuanke-{}:{}|开始赚客提醒确认收货.", username);
		ZhuankeHttp zhuankeHttp = ZhuankeHttp.getInstance(username);
		List<String> taskIds = zhuankeHttp.txReceiveGoods();
		logger.info("zhuanke-{}:|提醒确认收货.任务数={}", username, taskIds.size());
		logger.info("zhuanke-{}:|提醒确认收货.cookie={}", username, zhuankeHttp.getCookie());
		ZhuankeTask task = null;
		if (StringUtils.isBlank(zhuankeHttp.getCookie())) {
			return 0;
		}
		for (String taskId : taskIds) {
			task = queryByTaskId(username, taskId);
			logger.info("zhuanke-{}:|确认收货.taskId={}", username, task.getTaskId());
			if (StringUtils.isBlank(task.getTaskEvaluateContent())) {
				String pjcontentDiv = zhuankeHttp.getPjContent(task.getTaskId());
				if (StringUtils.isNotBlank(pjcontentDiv)) {
					Document doc = Jsoup.parse(pjcontentDiv);
					Element editform = doc.getElementById("editform");
					if (editform == null)
						continue;
					Element table = editform.getElementsByClass("tanctable").get(0);
					Elements trs = table.getElementsByTag("tr");
					String pjcontent = trs.get(0).getElementsByTag("td").get(0).text().trim();
					logger.info("zhuanke-{}:|确认收货.taskId={}|评价内容={}", username, task.getTaskId(), pjcontent);
					if (StringUtils.isNotBlank(pjcontent)) {
						if (pjcontent.startsWith("自由发挥好评内容")) {
							task.setTaskEvaluateType("1");
							String taskEvaluateContent = getCommentContent();
							task.setTaskEvaluateContent(taskEvaluateContent);
						} else if (pjcontent.startsWith("直接全五星好评")) {
							task.setTaskEvaluateType("1");
							task.setTaskEvaluateContent("blank");
						} else {
							task.setTaskEvaluateType("2");
							task.setTaskEvaluateContent(CommonUtils.filter(pjcontent));
						}
					}
					if (StringUtils.isBlank(task.getTaskEvaluateContent())) {
						String taskEvaluateContent = getCommentContent();
						task.setTaskEvaluateContent(taskEvaluateContent);
					}

					String taskEvaluateImg = "";
					String imgStr = trs.get(1).getElementsByTag("th").get(0).text();
					if ("晒图要求".equals(imgStr)) {
						Element tpimages = trs.get(1).getElementsByTag("ul").get(0);
						Elements images = tpimages.getElementsByTag("img");
						for (int i = 0; i < images.size(); i++) {
							String imgUrl = images.get(i).attr("src");
							if (imgUrl.startsWith("//")) {// 赚客的图片默认不带http
								imgUrl = "http:" + imgUrl;
							}
							taskEvaluateImg += ";" + imgUrl;
						}
					}
					if (StringUtils.isNotBlank(taskEvaluateImg)) {
						taskEvaluateImg = taskEvaluateImg.substring(1);
						task.setTaskEvaluateType("3");
						logger.info("zhuanke-{}:|确认收货.taskId={}|评价图片={}", username, task.getTaskId(), taskEvaluateImg);
					}
					task.setTaskEvaluateImg(taskEvaluateImg);
					super.save(task);
				} else {
					return count;
				}
			}
			String taskBuyerUrl = task.getTaskBuyerUrl();
			int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
			if (endFix < 0)
				endFix = taskBuyerUrl.length();
			String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
			String msg = "";
			String pjcontent = task.getTaskEvaluateContent();
			if ("blank".equals(pjcontent)) {
				pjcontent = "";
			}
			msg = pddUserWyService.confirmReciveGoodsNew(task.getPddUsername(), task.getPddOrderNo(), goodsId,
					pjcontent,
					StringUtils.isBlank(task.getTaskEvaluateImg()) ? null : task.getTaskEvaluateImg().split(";"));
			if (StringUtils.isBlank(msg)) {
				logger.info("zhuanke-{}:{}|拼多多确认收货已完成，等待下载图片和赚客上传截图.", task.getUsername(), task.getTaskSn());
				task.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_WAITFINISH);
				task.setTaskStatus(ZhuankeTaskVo.STATUS_WAITRECIVE);
				super.save(task);
				count++;
			} else if (StringUtils.isNotBlank(msg) && msg.equals("拼单成功，待发货")) {// 预售商品未发货，直接传个假的商品图片确认收货
				if (!ZhuankeTaskVo.STATUS_FINISHED.equals(task.getTaskStatus())) {
					logger.info("zhuanke-{}:|上传赚客图片yssp.taskId={}", username, task.getTaskId());
					String orginImg = pddImgPath + File.separator + "sysimg" + File.separator + "yssp.png";
					if (new File(orginImg).exists()) {
						String uploadImgMsg = zhuankeHttp.recProductFrom(task.getUsername(), task.getTaskId(),
								orginImg);
						if (StringUtils.isBlank(uploadImgMsg)) {
							task.setTaskStatus(ZhuankeTaskVo.STATUS_FINISHED);
							super.save(task);
						}
					}
				}
			} else if (StringUtils.isNotBlank(msg) && msg.equals("拼单未成功，已退款")) {
				if (!ZhuankeTaskVo.STATUS_FINISHED.equals(task.getTaskStatus())) {
					logger.info("zhuanke-{}:|上传赚客图片tdpdfail.taskId={}", username, task.getTaskId());
					String orginImg = pddImgPath + File.separator + "sysimg" + File.separator + "pdfail.png";
					if (new File(orginImg).exists()) {
						String uploadImgMsg = zhuankeHttp.recProductFrom(task.getUsername(), task.getTaskId(),
								orginImg);
						if (StringUtils.isBlank(uploadImgMsg)) {
							task.setTaskStatus(ZhuankeTaskVo.STATUS_FINISHED);
							super.save(task);
						}
					}
				}
			} else {
				logger.error("zhuanke-{}:{}|订单确认收货失败={}", task.getUsername(), task.getTaskSn(), msg);
			}
		}
		logger.info("zhuanke-{}:{}|结束赚客确认收货.", username);
		return count;
	}

	public int updateConfirmOrderStatus(String username) {
		int count = 0;
		logger.info("zhuanke-{}:{}|开始赚客确认收货.", username);
		ZhuankeHttp zhuankeHttp = ZhuankeHttp.getInstance(username);
		List<ZhuankeTask> tasks = queryFiveDayTask(username);
		logger.info("zhuanke-{}:|确认收货.任务数={}", username, tasks.size());
		logger.info("zhuanke-{}:|确认收货.cookie={}", username, zhuankeHttp.getCookie());
		if (StringUtils.isBlank(zhuankeHttp.getCookie())) {
			return 0;
		}
		for (ZhuankeTask task : tasks) {
			logger.info("zhuanke-{}:|确认收货.taskId={}", username, task.getTaskId());
			if (StringUtils.isBlank(task.getTaskEvaluateContent())) {
				String pjcontentDiv = zhuankeHttp.getPjContent(task.getTaskId());
				if (StringUtils.isNotBlank(pjcontentDiv)) {
					Document doc = Jsoup.parse(pjcontentDiv);
					Element editform = doc.getElementById("editform");
					if (editform == null)
						continue;
					Element table = editform.getElementsByClass("tanctable").get(0);
					Elements trs = table.getElementsByTag("tr");
					String pjcontent = trs.get(0).getElementsByTag("td").get(0).text().trim();
					logger.info("zhuanke-{}:|确认收货.taskId={}|评价内容={}", username, task.getTaskId(), pjcontent);
					if (StringUtils.isNotBlank(pjcontent)) {
						if (pjcontent.startsWith("自由发挥好评内容")) {
							task.setTaskEvaluateType("1");
							String taskEvaluateContent = getCommentContent();
							task.setTaskEvaluateContent(taskEvaluateContent);
						} else if (pjcontent.startsWith("直接全五星好评")) {
							task.setTaskEvaluateType("1");
							task.setTaskEvaluateContent("blank");
						} else {
							task.setTaskEvaluateType("2");
							task.setTaskEvaluateContent(CommonUtils.filter(pjcontent));
						}
					}
					if (StringUtils.isBlank(task.getTaskEvaluateContent())) {
						String taskEvaluateContent = getCommentContent();
						task.setTaskEvaluateContent(taskEvaluateContent);
					}

					String taskEvaluateImg = "";
					String imgStr = trs.get(1).getElementsByTag("th").get(0).text();
					if ("晒图要求".equals(imgStr)) {
						Element tpimages = trs.get(1).getElementsByTag("ul").get(0);
						Elements images = tpimages.getElementsByTag("img");
						for (int i = 0; i < images.size(); i++) {
							String imgUrl = images.get(i).attr("src");
							if (imgUrl.startsWith("//")) {// 赚客的图片默认不带http
								imgUrl = "http:" + imgUrl;
							}
							taskEvaluateImg += ";" + imgUrl;
						}
					}
					if (StringUtils.isNotBlank(taskEvaluateImg)) {
						taskEvaluateImg = taskEvaluateImg.substring(1);
						task.setTaskEvaluateType("3");
						logger.info("zhuanke-{}:|确认收货.taskId={}|评价图片={}", username, task.getTaskId(), taskEvaluateImg);
					}
					task.setTaskEvaluateImg(taskEvaluateImg);
					super.save(task);
				}
			}
			String taskBuyerUrl = task.getTaskBuyerUrl();
			int endFix = taskBuyerUrl.indexOf("&", taskBuyerUrl.indexOf("goods_id=") + 9);
			if (endFix < 0)
				endFix = taskBuyerUrl.length();
			String goodsId = taskBuyerUrl.substring(taskBuyerUrl.indexOf("goods_id=") + 9, endFix);
			String msg = "";
			long time = (new Date().getTime() - task.getCreateTime().getTime()) / 3600000;
			if (time > 120) {
				String pjcontent = task.getTaskEvaluateContent();
				if ("blank".equals(pjcontent)) {
					pjcontent = "";
				}
				msg = pddUserWyService.confirmReciveGoodsNew(task.getPddUsername(), task.getPddOrderNo(), goodsId,
						pjcontent,
						StringUtils.isBlank(task.getTaskEvaluateImg()) ? null : task.getTaskEvaluateImg().split(";"));
				if (StringUtils.isNotBlank(msg) && msg.equals("拼单成功，待发货")) {// 预售商品未发货，直接传个假的商品图片确认收货
					if (!ZhuankeTaskVo.STATUS_FINISHED.equals(task.getTaskStatus())) {
						logger.info("zhuanke-{}:|上传赚客图片yssp.taskId={}", username, task.getTaskId());
						String orginImg = pddImgPath + File.separator + "sysimg" + File.separator + "yssp.png";
						if (new File(orginImg).exists()) {
							String uploadImgMsg = zhuankeHttp.recProductFrom(task.getUsername(), task.getTaskId(),
									orginImg);
							if (StringUtils.isBlank(uploadImgMsg)) {
								task.setTaskStatus(ZhuankeTaskVo.STATUS_FINISHED);
								super.save(task);
							}
						}
					}
				} else if (StringUtils.isNotBlank(msg) && msg.equals("拼单未成功，已退款")) {
					if (!ZhuankeTaskVo.STATUS_FINISHED.equals(task.getTaskStatus())) {
						logger.info("zhuanke-{}:|上传赚客图片tdpdfail.taskId={}", username, task.getTaskId());
						String orginImg = pddImgPath + File.separator + "sysimg" + File.separator + "pdfail.png";
						if (new File(orginImg).exists()) {
							String uploadImgMsg = zhuankeHttp.recProductFrom(task.getUsername(), task.getTaskId(),
									orginImg);
							if (StringUtils.isBlank(uploadImgMsg)) {
								task.setTaskStatus(ZhuankeTaskVo.STATUS_FINISHED);
								super.save(task);
							}
						}
					}
				}
			} else {
				String pjcontent = task.getTaskEvaluateContent();
				if ("blank".equals(pjcontent)) {
					pjcontent = "";
				}
				msg = pddUserWyService.confirmReciveGoodsPkd(task.getPddUsername(), task.getPddOrderNo(), goodsId,
						pjcontent,
						StringUtils.isBlank(task.getTaskEvaluateImg()) ? null : task.getTaskEvaluateImg().split(";"));
			}
			if (StringUtils.isBlank(msg)) {
				logger.info("zhuanke-{}:{}|拼多多确认收货已完成，等待下载图片和赚客上传截图.", task.getUsername(), task.getTaskSn());
				task.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_WAITFINISH);
				task.setTaskStatus(ZhuankeTaskVo.STATUS_WAITRECIVE);
				super.save(task);
				count++;
			} else {
				logger.error("zhuanke-{}:{}|订单确认收货失败={}", task.getUsername(), task.getTaskSn(), msg);
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("zhuanke-{}:{}|结束赚客确认收货.", username);
		return count;
	}

	/**
	 * 下载拼多多拼论图片
	 * 
	 * @param username
	 * @return
	 */
	public int downloadAllCommentImage(String username) {
		ZhuankeUser user = zhuankeUserWyService.queryByUsername(username);
		PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
		PddHttp pddHttp = PddHttp.getInstance(pdd);
		int count = pddHttp.downloadAllCommentImage(pddImgPath);
		return count;
	}

	/**
	 * 查询5天前的任务 未上传淘单图片
	 * 
	 * @param username
	 * @return
	 */
	public List<ZhuankeTask> queryUnfinishTask(String username) {
		ZhuankeTask t = new ZhuankeTask();
		t.setUsername(username);
		t.setTaskStatus(ZhuankeTaskVo.STATUS_WAITRECIVE);
		t.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_WAITFINISH);
		return super.queryList(t, null);
	}

	/**
	 * 上传赚客图片
	 * 
	 * @param username
	 * @return
	 */
	public int uploadZhuankeImg(String username) {
		int count = 0;
		logger.info("zhuanke-{}:{}|开始上传赚客图片.", username);
		ZhuankeHttp zhuankeHttp = ZhuankeHttp.getInstance(username);
		List<ZhuankeTask> tasks = queryUnfinishTask(username);
		logger.info("zhuanke-{}:|上传赚客图片.任务数={}", username, tasks.size());
		logger.info("zhuanke-{}:|上传赚客图片.cookie={}", username, zhuankeHttp.getCookie());

		for (ZhuankeTask task : tasks) {
			PddUser pdd = pddUserWyService.queryUserByUsername(task.getPddUsername());
			PddHttp pddHttp = PddHttp.getInstance(pdd);
			logger.info("zhuanke-{}:|上传赚客图片.taskId={}", username, task.getTaskId());
			String orginImg = pddImgPath + File.separator + task.getPddOrderNo() + ".png";
			if (new File(orginImg).exists()) {
				String uploadImgMsg = zhuankeHttp.recProductFrom(task.getUsername(), task.getTaskId(), orginImg);
				if (StringUtils.isBlank(uploadImgMsg)) {
					task.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_FINISHED);
					task.setTaskStatus(ZhuankeTaskVo.STATUS_WAITFINISH);
					super.save(task);
					count++;
				} else {
					return count;
				}
			} else {
				String msg = pddHttp.downloadCommentImage(task.getPddOrderNo(), pddImgPath);
				logger.info("zhuanke-{}:{}|没有找到图片.重新下载图片,msg={}", username, task.getTaskId(), msg);
				if (StringUtils.isBlank(msg)) {
					logger.info("zhuanke-{}:{}|没有找到图片.重新下载图片成功", username, task.getTaskId());
					String uploadImgMsg = zhuankeHttp.recProductFrom(task.getUsername(), task.getTaskId(), orginImg);
					if (StringUtils.isBlank(uploadImgMsg)) {
						task.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_FINISHED);
						task.setTaskStatus(ZhuankeTaskVo.STATUS_WAITFINISH);
						super.save(task);
						count++;
					} else {
						return count;
					}
				} else {
					logger.info("zhuanke-{}:{}|没有找到图片.重新下载图片失败,msg={}", username, task.getTaskId(), msg);
				}
			}
		}
		logger.info("zhuanke-{}:{}|结束上传赚客图片.", username);
		return count;

	}

	public DataVo orderstatus(String username, String taskSn) {
		DataVo vo = new DataVo(1, "", null);
		ZhuankeUser user = zhuankeUserWyService.queryByUsername(username);
		ZhuankeTask t = new ZhuankeTask();
		t.setTaskSn(taskSn);
		t.setUsername(username);
		ZhuankeTask task = super.queryOne(t, null);
		if (StringUtils.isNotBlank(task.getPddOrderNo())) {
			if (ZhuankeTaskVo.PDD_STATUS_WAITPAY.equals(task.getPddOrderStatus())) {
				PddUser pdd = pddUserWyService.queryUserByUsername(user.getPddUsername());
				PddHttp http = PddHttp.getInstance(pdd);
				boolean payed = http.isPayed(task.getPddOrderNo());
				if (payed) {
					task.setPddOrderStatus(ZhuankeTaskVo.PDD_STATUS_WAITSEND);
					vo = new DataVo(1, "更新pdd订单状态成功.", null);
					super.save(task);
				} else {
					long time = (new Date().getTime() - task.getCreateTime().getTime()) / 1000;
					if (time > 2400) {// 三十分钟未支付的订单，任务取消
						vo = new DataVo(0, "订单30分钟未支付,可以取消任务了", null);
					}
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

	/**
	 * 自动提现
	 * 
	 * @param username
	 * @return
	 */
	public String withdraw(String username) {
		ZhuankeHttp http = ZhuankeHttp.getInstance(username);
		String msg = http.withdraw();
		return msg;
	}

	@Override
	public Weekend<ZhuankeTask> genSqlExample(ZhuankeTask t) {
		Weekend<ZhuankeTask> w = super.genSqlExample(t);
		WeekendCriteria<ZhuankeTask, Object> c = w.weekendCriteria();
		if (StringUtils.isNotBlank(t.getUsername())) {
			c.andEqualTo(ZhuankeTask::getUsername, t.getUsername());
		}
		if (StringUtils.isNotBlank(t.getPddOrderNo())) {
			c.andLike(ZhuankeTask::getPddOrderNo, "%" + t.getPddOrderNo().trim() + "%");
		}
		if (StringUtils.isNotBlank(t.getTaskId())) {
			c.andEqualTo(ZhuankeTask::getTaskId, t.getTaskId());
		}
		if (StringUtils.isNotBlank(t.getTaskSn())) {
			c.andEqualTo(ZhuankeTask::getTaskSn, t.getTaskSn());
		}
		if (StringUtils.isNotBlank(t.getTaskStatus())) {
			c.andEqualTo(ZhuankeTask::getTaskStatus, t.getTaskStatus());
		}
		if (StringUtils.isNotBlank(t.getPddOrderStatus())) {
			c.andEqualTo(ZhuankeTask::getPddOrderStatus, t.getPddOrderStatus());
		}
		if (t.getCreateTime() != null) {
			c.andLessThan(ZhuankeTask::getCreateTime, t.getCreateTime());
		}
		return w;
	}
}
