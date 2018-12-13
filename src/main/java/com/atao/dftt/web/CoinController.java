package com.atao.dftt.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atao.base.controller.BaseController;
import com.atao.base.service.BaseService;
import com.atao.base.util.BeanUtils;
import com.atao.dftt.api.vo.CoinRecordVo;
import com.atao.dftt.api.vo.MyCoinVo;
import com.atao.dftt.model.TaoToutiaoUser;
import com.atao.dftt.model.TaottCoinRecord;
import com.atao.dftt.model.Wltt;
import com.atao.dftt.model.WlttCoinRecord;
import com.atao.dftt.service.TaoToutiaoUserWyService;
import com.atao.dftt.service.TaottCoinRecordWyService;
import com.atao.dftt.service.WlttCoinRecordWyService;
import com.atao.dftt.service.WlttWyService;

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
		default:
			break;
		}
		return myCoin();
	}
}
