package com.atao.dftt.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.base.util.BeanUtils;
import com.atao.dftt.api.vo.CoinRecordVo;
import com.atao.dftt.api.vo.MyCoinVo;
import com.atao.dftt.model.CoinTxRecord;
import com.atao.dftt.model.HsttCoinRecord;
import com.atao.dftt.model.HsttUser;
import com.atao.dftt.model.JkdttCoinRecord;
import com.atao.dftt.model.JkdttUser;
import com.atao.dftt.model.MayittCoinRecord;
import com.atao.dftt.model.MayittUser;
import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.model.TaottCoinRecord;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.model.WlttCoinRecord;
import com.atao.dftt.model.ZqttCoinRecord;
import com.atao.dftt.model.ZqttUser;
import com.atao.dftt.service.CoinTxRecordWyService;
import com.atao.dftt.service.HsttCoinRecordWyService;
import com.atao.dftt.service.HsttUserWyService;
import com.atao.dftt.service.JkdttCoinRecordWyService;
import com.atao.dftt.service.JkdttUserWyService;
import com.atao.dftt.service.MayittCoinRecordWyService;
import com.atao.dftt.service.MayittUserWyService;
import com.atao.dftt.service.TaoToutiaoUserWyService;
import com.atao.dftt.service.TaottCoinRecordWyService;
import com.atao.dftt.service.WlttCoinRecordWyService;
import com.atao.dftt.service.WlttWyService;
import com.atao.dftt.service.ZqttCoinRecordWyService;
import com.atao.dftt.service.ZqttUserWyService;

/**
 *
 * @author twang
 */
@RestController
@RequestMapping("/coin")
public class CoinController extends BaseController<Wltt> {

	@Autowired
	private WlttWyService wlttWyService;
	@Resource
	private WlttCoinRecordWyService wlttCoinRecordWyService;
	@Autowired
	private TaoToutiaoUserWyService taoToutiaoUserWyService;
	@Autowired
	private TaottCoinRecordWyService taottCoinRecordWyService;
	@Autowired
	private JkdttCoinRecordWyService jkdttCoinRecordWyService;
	@Autowired
	private JkdttUserWyService jkdttUserWyService;
	@Autowired
	private MayittCoinRecordWyService mayittCoinRecordWyService;
	@Autowired
	private MayittUserWyService mayittUserWyService;
	@Autowired
	private HsttCoinRecordWyService hsttCoinRecordWyService;
	@Autowired
	private HsttUserWyService hsttUserWyService;
	@Autowired
	private ZqttCoinRecordWyService zqttCoinRecordWyService;
	@Autowired
	private ZqttUserWyService zqttUserWyService;
	@Autowired
	private CoinTxRecordWyService coinTxRecordWyService;

	@Override
	protected BaseService<Wltt> getService() {
		return wlttWyService;
	}

	@RequestMapping("/myCoinList")
	public Object myCoin() throws Exception {
		List<MyCoinVo> myCoins = new ArrayList<MyCoinVo>();
		// 微鲤看看
		MyCoinVo coin = new MyCoinVo(MyCoinVo.wltt, MyCoinVo.wlttName);
		List<Wltt> wlttUsers = wlttWyService.getUsedUser();
		List<CoinRecordVo> coinRecordVos = new ArrayList<CoinRecordVo>();
		for (Wltt user : wlttUsers) {
			List<WlttCoinRecord> records = wlttCoinRecordWyService.queryMyCoinRecord(user.getUsername()).getList();
			CoinRecordVo coinRecordVo = null;
			for (WlttCoinRecord record : records) {
				coinRecordVo = new CoinRecordVo();
				BeanUtils.copy(record, coinRecordVo);
				coinRecordVos.add(coinRecordVo);
			}
		}
		coin.setRecords(coinRecordVos);
		myCoins.add(coin);

		// 淘头条
		coin = new MyCoinVo(MyCoinVo.taott, MyCoinVo.taottName);
		List<TaoToutiaoUser> taottUsers = taoToutiaoUserWyService.getUsedUser();
		coinRecordVos = new ArrayList<CoinRecordVo>();
		for (TaoToutiaoUser user : taottUsers) {
			List<TaottCoinRecord> records = taottCoinRecordWyService.queryMyCoinRecord(user.getUsername()).getList();
			CoinRecordVo coinRecordVo = null;
			for (TaottCoinRecord record : records) {
				coinRecordVo = new CoinRecordVo();
				BeanUtils.copy(record, coinRecordVo);
				coinRecordVos.add(coinRecordVo);
			}
		}
		coin.setRecords(coinRecordVos);
		myCoins.add(coin);

		// 聚看点
		coin = new MyCoinVo(MyCoinVo.jkdtt, MyCoinVo.jkdttName);
		List<JkdttUser> jkdttUsers = jkdttUserWyService.getUsedUser();
		coinRecordVos = new ArrayList<CoinRecordVo>();
		for (JkdttUser user : jkdttUsers) {
			List<JkdttCoinRecord> records = jkdttCoinRecordWyService.queryMyCoinRecord(user.getUsername()).getList();
			CoinRecordVo coinRecordVo = null;
			for (JkdttCoinRecord record : records) {
				coinRecordVo = new CoinRecordVo();
				BeanUtils.copy(record, coinRecordVo);
				coinRecordVos.add(coinRecordVo);
			}
		}
		coin.setRecords(coinRecordVos);
		myCoins.add(coin);

		// 蚂蚁头条
		coin = new MyCoinVo(MyCoinVo.mayitt, MyCoinVo.mayittName);
		List<MayittUser> mayittUsers = mayittUserWyService.getUsedUser();
		coinRecordVos = new ArrayList<CoinRecordVo>();
		for (MayittUser user : mayittUsers) {
			List<MayittCoinRecord> records = mayittCoinRecordWyService.queryMyCoinRecord(user.getUsername()).getList();
			CoinRecordVo coinRecordVo = null;
			for (MayittCoinRecord record : records) {
				coinRecordVo = new CoinRecordVo();
				BeanUtils.copy(record, coinRecordVo);
				coinRecordVos.add(coinRecordVo);
			}
		}
		coin.setRecords(coinRecordVos);
		myCoins.add(coin);

		// 花生头条
		coin = new MyCoinVo(MyCoinVo.hstt, MyCoinVo.hsttName);
		List<HsttUser> hsttUsers = hsttUserWyService.getUsedUser();
		coinRecordVos = new ArrayList<CoinRecordVo>();
		for (HsttUser user : hsttUsers) {
			List<HsttCoinRecord> records = hsttCoinRecordWyService.queryMyCoinRecord(user.getUsername()).getList();
			CoinRecordVo coinRecordVo = null;
			for (HsttCoinRecord record : records) {
				coinRecordVo = new CoinRecordVo();
				BeanUtils.copy(record, coinRecordVo);
				coinRecordVos.add(coinRecordVo);
			}
		}
		coin.setRecords(coinRecordVos);
		myCoins.add(coin);

		// 中青看点
		coin = new MyCoinVo(MyCoinVo.zqtt, MyCoinVo.zqttName);
		List<ZqttUser> zqttUsers = zqttUserWyService.getUsedUser();
		coinRecordVos = new ArrayList<CoinRecordVo>();
		for (ZqttUser user : zqttUsers) {
			List<ZqttCoinRecord> records = zqttCoinRecordWyService.queryMyCoinRecord(user.getUsername()).getList();
			CoinRecordVo coinRecordVo = null;
			for (ZqttCoinRecord record : records) {
				coinRecordVo = new CoinRecordVo();
				BeanUtils.copy(record, coinRecordVo);
				coinRecordVos.add(coinRecordVo);
			}
		}
		coin.setRecords(coinRecordVos);
		myCoins.add(coin);
		return myCoins;
	}

	@RequestMapping("/refreshData")
	public Object refreshData(int type) throws Exception {
		switch (type) {
		case 1:
			wlttCoinRecordWyService.updateAllCoin();
			break;
		case 2:
			taottCoinRecordWyService.updateAllCoin();
			break;
		case 3:
			jkdttCoinRecordWyService.updateAllCoin();
			break;
		case 4:
			mayittCoinRecordWyService.updateAllCoin();
			break;
		case 5:
			hsttCoinRecordWyService.updateAllCoin();
			break;
		case 6:
			zqttCoinRecordWyService.updateAllCoin();
			break;
		default:
			break;
		}
		return myCoin();
	}

	@RequestMapping("/cointx")
	public Object cointx(int type, String username) throws Exception {
		JSONObject json = new JSONObject();
		switch (type) {
		case 1:
			Wltt user = wlttWyService.queryUserByUsername(username);
			json = wlttWyService.cointx(user);
			return json;
		case 2:
			TaoToutiaoUser taottUser = taoToutiaoUserWyService.queryUserByUsername(username);
			json = taoToutiaoUserWyService.cointx(taottUser);
			return json;
		case 3:
			JkdttUser jkdttUser = jkdttUserWyService.queryUserByUsername(username);
			json = jkdttUserWyService.cointx(jkdttUser);
			return json;
		case 4:
			MayittUser mayittUser = mayittUserWyService.queryUserByUsername(username);
			json = mayittUserWyService.cointx(mayittUser);
			return json;
		case 5:
			HsttUser hsttUser = hsttUserWyService.queryUserByUsername(username);
			json = hsttUserWyService.cointx(hsttUser);
			return json;
		case 6:
			ZqttUser zqttUser = zqttUserWyService.queryUserByUsername(username);
			json = zqttUserWyService.cointx(zqttUser);
			return json;
		default:
			break;
		}
		return null;
	}

	@RequestMapping("/myTxRecord")
	public Object myTxRecord() throws Exception {
		CoinTxRecord t = new CoinTxRecord();
		t.setPage(1);
		t.setRows(50);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sorts", "createTime=1");
		return coinTxRecordWyService.queryPage(t, params).getList();
	}
}
