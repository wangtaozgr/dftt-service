package com.atao.dftt.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import com.atao.dftt.http.DtffHttp;
import com.atao.util.StringUtils;

import sun.misc.BASE64Encoder;

public class SignHelper {
	public static String[] psw = new String[] { "d67e7c46c37fe53c1da0807d2344d6961170b7fd",
			"648a4312a6c8734b293d9c295c70e1c3dc482814", "cdc0595bb81e36b5b424a648aee984723355b487",
			"b070866f550d21d0b752a5d501a7da1af424851b", "3bd7e11a3bc071085f5c9afc635a3d947c7d466e",
			"c56ab0f2c35684f7880358a71bb91345fdf73529", "22d547ce3468c7f2b01cd399b517cd3769c6e61c",
			"4772fd82a0d7df82079c4786193366a8284083b9", "8315042f69a6b571fef01e28fc13b9b345f18b5b",
			"33f9dbcb7a5f011af9779318941dce1ebc81fdf9", "6bb551da4dea94d55bcc5e8c8b71ea6491f69f43",
			"666b96f50e066e5d130549df529412adaf315bde", "b4d0eea3cfd0b5be7ac6c0aed268eaf163ac5cea",
			"bae11adb11e6da10a4e67741d21a54f712f1cbea", "d9174bc91fbd06291bdd6bdfab8dff9325dc9c18",
			"7ff2466dbf197ed47200e80cf9d7e2c999c27cff", "4be55ca4d48bb52b9b03f74eb9fd0c5d217c103a",
			"078450514829bccc36f9566a2ceb457cd980908e", "7658777b4b7cfaef9760dc3d42df83ec121e0564",
			"79b342a3295462ea00d76a9995600c25570e8e6e", "2da6f8e408da828db217651a60612c21e9a0ddaf",
			"433e577900305c774bb4e4678cb4f5eb6fa8edb7", "50d7ed786ef8df2127926fa808532fdb3b6568ac",
			"5b590087a0eed7d9dbddfbcd277b3bb5d1d48df3", "8859948ea949c13245c4d6b35258c5d7b1094087",
			"a792d027c4bdbe3fcbee4b8fd09d303dd5d4120d", "c1b996ded4c03563edf689479261e2504dd5e5e0",
			"f84cabede98be1ffd997b17873a1f1ace5b68c0a", "e4a58446d3aa98964b4f291bf47f128d5ad5fc57",
			"0d2abed0b9f2128799ca00b4f07067390f4f8ac7", "9ad8f971ee0a23885d6131fe6e6a948d4b70dd74",
			"fde1a3f4083a1410c4c0f3e28dd850c80bee7f44", "846f8feee4c156681d71dfc7ce3d6a6db21cc83c",
			"b2e0f43b9e74aff83998b46fc4c4ed961b296416", "a3457e5115bc1bdd8612a353cab4b906816adc75",
			"dd6b0c2efce396ed6393cbac1d1f130ad188ee52", "7cb9f7c3b26fca20bb6263583b8fc619c5ad61ba",
			"686b502312b5b04cc655172f50f6a554ebbc986a", "13036d980823ef9d79b859029483cc3bbbc90bc4",
			"39de6201c2bc856b14be2bc4a8d5155562987257", "958075cafbde78f2813596b380ecdceaef02f711",
			"8f1c5da3ee0eecb7b704e8487e75cc7a45218339", "9e15ba2b324c1d52395f8f8d100b1c093d73a99e",
			"54c197b06c8f16b7c53e4b122c032c9020cbe573", "6121d327cad65977160c43c27857fbed19061217",
			"a959ec7fb39780a18b2541cf2643481a000662ab", "bd8cbd5af11dceb1a5255ae5fe2a4f0ef8874ed5",
			"46d13407f589be96daf9b2939e5f300a2fff51f3", "b9a4362e7ade153131c4e4f6651882170ae3a153",
			"346920187a1ede2675d867ac4ccf5fa7bcbe93a8", "fd7e6b6b37d84220e09399597481d4d388cbe7f1",
			"71ac4eaa24582ca130a104502990d9df68155f2d", "07cc2c5dbe2ad6e65c23be3c56d88650169783b5",
			"73daeef61b26a855cf0a1320b0fb32afc87a32c5", "1255715e03a7be2f7d1f3b0b26900d1d9219a515",
			"a422f8f1073a7b8788059e172044e148886e76e6", "57676cf1c0fab6e7d0194e4c4645dafac4ecc663",
			"22f9f84afb0340934a10cc932e92430c79652a99", "d49f1c9751d34a3a0b3992d31e16b2b2b17820b2",
			"28d2cca8300898e6b52538dbfe467a73acbc7b2d", "385d64892f1ec7ff80aca083526195aed7834045",
			"49013b2b381e2a02f4833613366fc72d44ed25d0", "7179e7d351edb08f258735fdf07ff0cecb705be2",
			"26d07e4955fa8cc1456d4c166465ea29418e6a17", "0d4392781898027d11ef21304b45bce6e6423845",
			"6aab88ceaea7651d494cd86d89113e7e5bb10dc0", "16a542482e26463316001316d7e6f5e5b7072755",
			"cb877d9d4f8b78d97c91b5a41c73273269e95f39", "eb9ef98dc25aeb3eee9844004efdafcd31232fe2",
			"484f85dcc1c9c5250b1d99df06715fb785546152", "665fd28475b48cb75900826b72297a4b41b414bc",
			"3eabc5eb726783d475952cfb9305eb48b707e335", "4800be4fa268f6c145dae57ca539a656c3bf332c",
			"dbfc6e99822de4d04e3f80b060f2546637c6da22", "96ef86402b19acb786528edb83a9c261e74b81f1",
			"9ea4e4acb57a73ab4b8dfe1e7786279e6b20205a", "c6c6c987d8cf20ea33ac4c3a406fe9a8820bda02",
			"1b0446890d5d1180637d1f27d07783ff087d3971", "ddccacd1caa5dcd82299d383d2197d298f8b0614",
			"e88f9ce7a50b6b358b3b8c23df8a980379648fb7", "3c69fcc57ee6853c6334d90e99ef5a20172bcd55",
			"7dec8c15d4fee1dca64993873655ed1031f337e3", "bce60be24d796fc01ae5dc1d1c9fc837a0be596c",
			"23f3c7fa0a556aa9cabe99975aedf6b3d6cd3122", "4545e6392be94f12052a3b6157fa5460a98fa5f7",
			"b7bb13d431d690be3792585f1c4198a963d925e7", "b66aec2bf4827533afec8596ac9682f87300a961",
			"8a8d867d76871c4d9f2c09cb26a53f4d19399930", "40cb68eb5e889adc1d9f65c657596eca09ad966a",
			"1ecc430ed70867b20eacffecea1e0a36012cff68", "18602f9ce2a81629aebc1d9800c92de35a6392c9",
			"cecb8a0fd4eb634dd113bcb68fabb674f077fb26", "b5b3c98e2db5429b699607c8b0c55b077848a21a",
			"2d4bf2dae7cb8f24f7147165e3097e577a1380ed", "4bb183e11067f92543af539328d1d2ee25205cbb",
			"852ceac3f6694f030571f88eb5972051b605bac8", "83c163fb066f9b301ae9ce99caee718f05aef0ef",
			"810671f0f0f271cd4dc6ba636481da830a4ff24c", "e43795fb31077e1c861c423aeb79c4c2d1219f97",
			"639cfdff1134203267ced5d43cd6621a9345fb16", "d570915a21e66c83a181405a22c833f326d21994",
			"655e21eda518538391314c66b3f6496254e38156", "a5a8f9dbf8c1d0b34e2724599952483d01f86e60",
			"2452a0c9b3e5ce7de074af544f6926eef4f1defc", "da7a72ae14af30f30bd5c618a20bb4ed11d0d8c5",
			"aebe67244d2c5e2c60befbde63b7a169e86f2c19", "e06be6c30e6ac05176283511f9cbdd0d9f3d5662",
			"4054c5ada9d4d9782dadae1e06420beb127e92b0", "51f3b4eeb02dd4bba4221103c693ccd200ef79a5",
			"962c481a9348ddaac0729537fa829041d0305d1c", "6507e50bd4526c5fa037f094d4d1d3213f54dd8b",
			"97dfd64bf9326325ca7a206edccc3617965c4068", "31bc06d2ade6c46aef635fcd40055696524528bc",
			"84f4754228aec7445b9f80996488af8326cbbe94", "37ddfd9ebbb707f34d94bc888f15d3c7d4a4a10a",
			"dd52ef0a81906a600d92b9d8ede4a1035581922b", "8f72d592ff5ec819e1b96ab4886b68d8e648c059",
			"73af4f0b707fb89428871e1d92ecead4d2171a84", "b5a9ebcbe8b0da80cec0f0e8bd29fdb32d62ab2a",
			"ffcc1efba943e0cb0e70c8a00dc71cb95e621fac", "4170c1d7aff2109baf2fa55509a3a0164acbfd05",
			"1f5c26a2d23f8b4e7e6f7b35339009f9782e27b9", "44fb05dda199b056cb53817b5a924033351f2084",
			"9a5bc3438174dca564e1834498ed5c8323df2e34", "8620d97cc7b0a92d4e297916a8b2e0a01eb3cc83",
			"b532f7c95b833182e0e2f54bfa73a7f4f33b11a8", "f2a247334e5ed510d576ade3594f691acb13c96e",
			"86102763cb177421e12e66f947ac1cc66cd0bb43", "7a605c4651d78a0ec4f05017a3cb609dc556facb",
			"079d21165cc6dfa17edad06be49be2a9aeae11a4", "19fd4466f68a171af33040f9ed882ee033402d01",
			"3debc47306fa32b0b0e59440665c3e8502035efe", "3d82bdf715dd172098042ad973a6759ab7b87eaf",
			"0bdb32f555b15be1c4180dbd016b0f06f6e9bcb0", "6473e72ef8a633c7ace4b2e42e70c8162c2e9ee7",
			"d3c428c312e9159459a6e790d75178131330021f", "4de3deefc1ee924a0adfc48d3a0ac0a5173f6a15",
			"c411cb016abbd94deae316c2b738706d7054d62a", "e157f31faf51022f672e37c1d3ea846a69f88caf",
			"d5eca295ca92b0c794816dbec167f88da0354d14", "17846fe95e7a0752e44d5238e8e70d56d536102b",
			"d828d5c675d915975c68bfc8dc1ae3f449aee755", "f3bd684d9dec8ddef5e7392c502646911f180a7a",
			"f938d9f7d70a9e69daa0f95d4ffee49648239c02", "f82a1ab101df06851fbd37850bbf06eede893927",
			"12c0a2b2e7e549e187a683a253992ea19773f34c", "7bf9e06f09beb57dd15f8f6ddd3f6ca23875b087",
			"01f7f5e945d31609cfbb53eeaccdfcfff4e65f6b", "f229badc4b865e9ff01e1373251429bb8603cc80",
			"0c5a8a7800a89209f7a939e54d8d4bcf6324978b", "0ed5d0deafb7cee7d5d65ad3cc549873da85120a",
			"a9067490bac211c4ab64bd555e6d954fbaecf2da", "0765756acbfe54d9b56c7d657c73102951523eae",
			"a86d055628a06a501e33e128f9e2e2970f744799", "c15787936de73fca6cdfb12ac6c0c5016104e9d2",
			"2c11f210d4f337e0cec2b32e34858b5e59ec73bd", "3f8a4bf3d3653d0be63fbad90afc3263fd3d3463",
			"6c7ae02d58a0f9939399e142917b2a5d3c86f45b", "685f8c7ec24ae9bd4452c476de6d9690682e459a",
			"fd8237a3fb49a03a0ce4a50bcce9438bc00834b6", "6687fa565c73e7efe2c559b853c5a19a0df25726",
			"c1b91dee2c0fe4fd301c3cc2aba6602aa8fd9c8c", "746cf4cf29b925633d40d7136ddaccb9dadd395d",
			"ee7d8b9ba31754824565efdf35dc2ddd0bfec472", "49840cb29808979093f5972a6fae1fe739acaf82",
			"fbef8753362f4c06e6ff94118269d5ad706dfcc1", "ebf40e7b8be353e4a3bbd1316acbbf0b384cd29e",
			"da33844b2c41bfbd24363674c11f7195a6f0680b", "de1d754de28517ba868e47cbf9f69a344e999da4",
			"06fac858bd0a443cc6469f0d2d1892f03a70c14c", "0a064793062382266dd1506a2bfbf54ec1174211",
			"619791d8126bfa7f7eb7b0eb0be7317dd164a913", "7fba26b32f9053b7d6bd7b15aeda1261fca7a5fe",
			"0cf5a7b96421f8e13fd053850c320645b2a4cb13", "cc7c8fa2fe6b500bfe17d201fcf6c435c9aa3bb5",
			"3166a6f112e07438102e9ac6ae9647696c2bfafe", "375f4bde13b0a063eb832cebd627de2f61737357",
			"a694836a860e0e2d73d7f61978127e239fc513be", "72df4004765971c567dd1033927a99e8805f1eff",
			"688bbfa715ea632ab838a51480fb42943853f071", "1d2638a2a01b74c285bf60b73bc8a73c866c8c04",
			"ad5e9f65a9b0997c0cbecae22375f60bfaf50573", "be873b9770d17780cfb994e18dea5c2d11989c11",
			"d2f8c4d5dd6ade65d3e418e640928fe6abff794b", "984e464a6a717daaffa926733d80ff9f88bc111c",
			"472780c68d9107ad98a9295422b277f04b3491f9", "52218a77c9f92b04a734bcb5673f3486272df2c7",
			"5d377e40abef3150accc16233830d5102b14d197", "3e52f32af2cc816669662ed24a4f535591014ee6",
			"e56574951a0e8a1f6c98f345950770f39f582db5", "a2c61a89869538b80a1ea4e69eeae57edb724a6a",
			"3c40d7437f31f89cbf41b9e73613c7a1868a3f14", "26961550e15b51488f96711991b6ab742d0b1019",
			"baf4cfcca2732437d658ac4a85c35c355f760abc", "178fb87725bc790eaf22eaf6357b90542ef02f74",
			"a3509971e2742ffe7e6f656786aa1674ee846b8f", "2ffc62c66f13b0d60382c4682701e5cefef3d998",
			"d515a1cfad8dccbf3c6e8cfc762b5eed800a2cb6", "00740fd35eabba4b43a06cfac88c307ccc142d9b",
			"a8c4b2bf94cfe7b1e135597895a5d365469544b4", "b98a0fb5c6098c9e59a496dc873c0b1cd7fee640",
			"d993f2d9bb8815a6c19587332529ca2cf03dfe74", "1d41d510c10559c63b7c57120e47b4947326fdac",
			"91c653684ca8ff75b3f6de3959f270a36786999d", "4b4960081f38b1ff1df6ffce77c947c06113d237",
			"35411023a8ccafc794116fbc087b4c6611f15a53", "db4b1961be2eb15258956344edda8cdb09b7cb61",
			"94b1e0d83b2ab2fb0c6e6789e978073477dcf600", "0c0fa27f217e02787bb78b29bc7f3f47f4e1cdd2",
			"9ea0e7b8066b8284edcf88778fb06bf98ffffda8", "f41990c5f50eb26f0f0ed2968204d6937b450259",
			"eeaf1f84cdac3b7dee20b11075ed57f2d988e582", "24f48f450d91fd77d577f5b98360cf7892194576",
			"8c3db780fe55d491660e20591659c1eb6c444f38", "cc684a618afce91d7e36a8f1b16490a3f662bde4",
			"bb8b4ecf8aff75996daa298cbe4f9564347dc041", "b5ab54709661eb32ac1e798057c2ceb6c245cf3f",
			"b544ba93f37ba1e3c5e67fc24e8c9bc7a55e743c", "c507c6076a19a45fed1fcb77b970e6ed2c8202c6",
			"c86538820a7cc001e27429a12b0eaba98bc9eec2", "d0d9126ba2d8650e3c3718c619ea051aa089591c",
			"470cf9211fbd90e187f70e0c92756d683beba0d7", "d7af723e487c6f0aae2c1ea3021a71fed9425b3e",
			"90d544611ccd66ce08f62dda7527a6b478dfbc71", "077b2107e1c39ab2abf874390d2b602ad7379a7a",
			"39bc965889c6440d69572227dbaed16d8ae58689", "6448033bd6c032ef4c55725a4f2775a63a040c48",
			"5c6e8552c719287f898eee1b85649962b523e791", "7d7f3cdde0384b30dd43fe95580b94ff4cfba001",
			"5d21fd24c75f9309c977e66c71a377cfa24b95cf", "cd5263f8cf882086e7277bf98f7898a218815db3",
			"2323debcedf3f359f23a9f69b9a76b3067dfa23f", "27c169b5f6f504841fdedcf59e94dc04f706d82f",
			"3a8ae074547becfa56acd850ea4fec8c7c471c9c", "586a1ed48fc18a394b156488fb91672bded747ef",
			"4a17bf58539be4c9093a309b9cf978cf3e02b511", "5a3c4d818c9bc19d8a1bcd45b2217ffb39395127",
			"2b14ab52a19462a371bce4392c2977fc6c660706", "b8e72f4713dc4b460cdb98ccb1259419e2c1bc33",
			"627e1ac4599ab004d4f0292b06ad61981f2026c3", "427c6dcbc7e028841ad65ea610afefd6a64846ae",
			"3f44bcff9e6706ccd691e3120a2addfb8da9395e", "1af8cbf4223b5358b2ade3db470e0d6c0f51cc10",
			"36f3b56b776540f39de9ab05938eac9b1286be5d", "80f0e99c66ffef9908d1a4424cf22bbd74ad7fd1",
			"a2ebce145da1a0c0cb5f604e930dd50dc68a230b", "c003de589d6d45ac08f7f8090b311c3a5cc4265b",
			"e43794e92b2cc191328319162fd6dc7ec2e1010b", "ce698ab8ee1df63577b3fe34427364a4884f3750",
			"16678b3e8b6eab55906fbfa811ad5f9f70c7fac0", "18a85a0f16e4f2de6625533e98bfe373697d0b26",
			"23eff4c024ac429719ef3f1748bd9675f26fa313", "0c5123ce8a7b8cddf97d5ba5b64550109cbcfe23",
			"aa0d14e97495018e03ab7cce0fb819d848301f2c", "9e6503cec5d1a2d0956d08e6a66c6bfa21e57269",
			"6a5cb8485918c631ec7673ab12d52a2765ab7064" };

	private static String a(TreeMap<String, String> paramTreeMap) {
		List<String> localArrayList = new ArrayList<String>(paramTreeMap.keySet());
		StringBuilder localStringBuilder = new StringBuilder();
		int i = 0;
		while (i < localArrayList.size()) {
			localStringBuilder.append((String) localArrayList.get(i));
			localStringBuilder.append((String) paramTreeMap.get(localArrayList.get(localArrayList.size() - i - 1)));
			i += 1;
		}
		return localStringBuilder.toString();
	}

	public static String sign(TreeMap<String, String> paramTreeMap, String paramString) {
		if ((paramTreeMap == null) || (paramTreeMap.isEmpty()) || (StringUtils.isBlank(paramString)))
			return null;
		paramTreeMap.put("ts", paramString);
		paramString = Md5Util.md5(paramString);
		paramString = paramString.substring(paramString.length() - 2, paramString.length());
		int i = 0;
		try {
			int j = Integer.parseInt(paramString, 16);
			i = j;
			paramString = psw[i];
			String key = "3iN0xu07ceILF565";
			return Md5Util.md5(key + a(paramTreeMap) + paramString);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * 阅读新闻签名
	 * 
	 * @param accid
	 * @param imei
	 * @param oem
	 * @param qid
	 * @param version
	 * @param machine
	 * @param plantform
	 * @param newsid
	 * @param ts
	 * @return
	 */
	public static String signNews(String accid, String imei, String oem, String qid, String cqid, String version,
			String machine, String plantform, String os, String deviceid, String nodes, String timer_type, String ts) {
		TreeMap paramTreeMap = new TreeMap();
		paramTreeMap.put("accid", accid);
		paramTreeMap.put("imei", imei);
		paramTreeMap.put("oem", oem);
		paramTreeMap.put("qid", qid);
		paramTreeMap.put("cqid", cqid);
		paramTreeMap.put("version", version);
		paramTreeMap.put("machine", machine);
		paramTreeMap.put("plantform", plantform);
		paramTreeMap.put("os", os);
		paramTreeMap.put("deviceid", deviceid);
		paramTreeMap.put("nodes", nodes);
		paramTreeMap.put("timer_type", timer_type);
		paramTreeMap.put("ts", ts);
		String sign = sign(paramTreeMap, ts);
		return sign;
	}

	/**
	 * 签名邀请码
	 * 
	 * @param accid
	 * @param imei
	 * @param oem
	 * @param qid
	 * @param version
	 * @param machine
	 * @param plantform
	 * @param ts
	 * @return
	 */
	public static String signVisiteCode(String accid, String imei, String oem, String qid, String version,
			String machine, String plantform, String code, String from, String ts) {
		TreeMap paramTreeMap = new TreeMap();
		/*
		 * paramTreeMap.put("accid", accid); paramTreeMap.put("imei", imei);
		 * paramTreeMap.put("oem", oem); paramTreeMap.put("qid", qid);
		 * paramTreeMap.put("version", version); paramTreeMap.put("machine", machine);
		 * paramTreeMap.put("plantform", plantform); paramTreeMap.put("code", code);
		 * paramTreeMap.put("from", from); paramTreeMap.put("ts", ts);
		 */
		paramTreeMap.put("accid", accid);
		paramTreeMap.put("code", code);
		paramTreeMap.put("qid", qid);
		paramTreeMap.put("version", version);
		paramTreeMap.put("from", from);
		paramTreeMap.put("imei", imei);
		paramTreeMap.put("machine", machine);
		paramTreeMap.put("oem", oem);
		paramTreeMap.put("plantform", plantform);
		paramTreeMap.put("ts", ts);
		String sign = sign(paramTreeMap, ts);
		return sign;
	}

	public static String getLastParam() {
		Random r = new Random();
		String a = (r.nextInt(2) / 1 == 0 ? "" : "-") + r.nextInt(2) + "." + new Date().getTime() % 1000 * 832604l;
		String b = (r.nextInt(2) / 1 == 0 ? "" : "-") + r.nextInt(2) + "." + new Date().getTime() % 1000 * 732694l;
		String c = (r.nextInt(2) / 1 == 0 ? "" : "-") + r.nextInt(2) + "." + new Date().getTime() % 1000 * 93969l;
		return a + "|" + b + "|" + c;
	}

	public static String custom(String accid, String qid, String device, String imei, String osType, String area,
			String mac, String ssid, String bssid, String lat, String lng, String param1, String param2) {
		String param = imei + "\t" + qid + "\t" + device + "\t" + accid + "\t" + "DFTT" + "\t"
				+ DtffHttp.version + "\t" + "DFTTAndroid" + "\t" + "TouTiao" + "\t" + osType
				+ "\t" + area + "\t" + "{\"mac\":\"" + mac + "\",\"ssid\":\"" + ssid + "\",\"bssid\":\"" + bssid
				+ "\",\"lat\":\"" + lat + "\",\"lng\":\"" + lng
				+ "\",\"ele\":\"18\",\"state\":\"2\",\"temperature\":\"34\"}" + "\t" + param1 + "\t" + param2;
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			String result = encoder.encode(param.getBytes("UTF-8"));
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		;
		return null;
	}
	
	public static void main(String[] args) {
		TreeMap<String, String> paramTreeMap = new TreeMap<String, String>();
		paramTreeMap.put("token",
				"MlZpdmpudnNvU0gydmZjYzBRMGlGUEhud1Y0dVBxNi80QUtieEhFRitpTitYQnNaY29nRTFIVkZKaFBCZm5STkwrWHN2SlBNbnBaNGpDQ3I2QWVHcUhLNmZCRHFEQStQZ3h3THBBM0ZDRkNSb05tWlpET0Mrd1RnUndBeHFpOXZXcDdmd1VialN6eEdmK0t0YWloZUE5VnRqaFNZeUVZSXdCZEVlcjY0bXdFPQ==");
		paramTreeMap.put("mobile", "17356575091");
		paramTreeMap.put("agree", "1");
		paramTreeMap.put("review", "");
		paramTreeMap.put("cashin", "");

		String sign = sign(paramTreeMap, "1542848410");
		System.out.println(sign);
		/*
		 * SignHelper h = new SignHelper(); BASE64Decoder decoder = new BASE64Decoder();
		 * String abc =
		 * "ODYzMjU0MDEwMjI0MjE5CWRmdHQxODA5MjAJZTBkYjU1OWRmZDMwOTExNwkwCURGVFQJMi4yLjIJREZUVEFuZHJvaWQJVG91VGlhbwlBbmRyb2lkNC40LjIJ5a6J5b69CXdpZmkJeyJic3NpZCI6IkUwOkRCOjU1OjlEOkZEOjMwIiwic3RhdGUiOiIzIiwic3NpZCI6InNhZ2l0YWUwZGI1NTlkZmQzMDkxMTciLCJsbmciOiJudWxsIiwibWFjIjoiMDg6MDA6Mjc6RTc6NTM6NjciLCJsYXQiOiJudWxsIiwidGVtcGVyYXR1cmUiOiI0MSIsImVsZSI6IjkwIn0JMC4wfDAuMHwwLjAJMC4wfDAuMHwwLjA=";
		 * try { byte[] b = decoder.decodeBuffer(abc); System.out.println(new String(b,
		 * "UTF-8")); } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * String sign = signNews("808128561", "863254010224219", "DFTT", "dftt180920",
		 * "dftt", "2.2.2", "MI 6 ", "android", "Android4.4.2", "e0db559dfd309117",
		 * "[{\"type\":\"news\",\"urlto\":\"https:\\/\\/mini.eastday.com\\/mobile\\/180919160458449.html\",\"urlfrom\":\"yule\",\"node\":8},{\"type\":\"news\",\"urlto\":\"https:\\/\\/mini.eastday.com\\/mobile\\/180919160458449.html\",\"urlfrom\":\"yule\",\"node\":18},{\"type\":\"news\",\"urlto\":\"https:\\/\\/mini.eastday.com\\/mobile\\/180919160458449.html\",\"urlfrom\":\"yule\",\"node\":28},{\"type\":\"news\",\"urlto\":\"https:\\/\\/mini.eastday.com\\/mobile\\/180919160458449.html\",\"urlfrom\":\"yule\",\"node\":30}]",
		 * "news_timer", "1537434735"); System.out.println(sign);
		 */

		// 2e15590a237755fa0edc5afe148c0e03
		// h.sign("808826224", "860854015238445", "DFTT", "dftt180512", "2.0.8",
		// "NX40X", "android", "180514115441901");
		// for(int i=0;i<20;i++)System.out.println(getLastParam());
		/*
		 * paramTreeMap.put("accid", localObject1); paramTreeMap.put("imei", str1);
		 * paramTreeMap.put("oem", str2); paramTreeMap.put("qid", str3);
		 * paramTreeMap.put("version", str6); paramTreeMap.put("machine", str5);
		 * paramTreeMap.put("plantform", str4); if (!TextUtils.isEmpty(paramString2))
		 * paramTreeMap.put("newsid", paramString2); if ((paramMap != null) &&
		 * (paramMap.size() > 0)) { localObject2 = paramMap.keySet().iterator(); while
		 * (((Iterator)localObject2).hasNext()) { str7 =
		 * (String)((Iterator)localObject2).next(); if
		 * (!TextUtils.isEmpty((CharSequence)paramMap.get(str7))) paramTreeMap.put(str7,
		 * paramMap.get(str7)); } } String str7 =
		 * String.valueOf(System.currentTimeMillis() / 1000L); String str8 = new
		 * z().a(paramTreeMap, str7); paramTreeMap.put("ts", str7);
		 * paramTreeMap.put("sign", str8); Object localObject2 = new ArrayList();
		 * ((List)localObject2).add(new BasicNameValuePair("accid",
		 * (String)localObject1)); ((List)localObject2).add(new
		 * BasicNameValuePair("imei", str1)); ((List)localObject2).add(new
		 * BasicNameValuePair("oem", str2)); ((List)localObject2).add(new
		 * BasicNameValuePair("qid", str3)); ((List)localObject2).add(new
		 * BasicNameValuePair("version", str6)); ((List)localObject2).add(new
		 * BasicNameValuePair("machine", str5)); ((List)localObject2).add(new
		 * BasicNameValuePair("plantform", str4)); if (!TextUtils.isEmpty(paramString2))
		 * ((List)localObject2).add(new BasicNameValuePair("newsid", paramString2));
		 * ((List)localObject2).add(new BasicNameValuePair("ts", str7));
		 * ((List)localObject2).add(new BasicNameValuePair("sign", str8)); if ((paramMap
		 * != null) && (paramMap.size() > 0)) { paramString2 =
		 * paramMap.keySet().iterator(); while (paramString2.hasNext()) { localObject1 =
		 * (String)paramString2.next(); ((List)localObject2).add(new
		 * BasicNameValuePair((String)localObject1,
		 * (String)paramMap.get(localObject1))); } } au.a(paramString1,
		 * (List)localObject2); ba.a(paramString1, (List)localObject2); if (parami !=
		 * null) { a(paramString1, paramTreeMap, parami); return; } a(paramString1,
		 * paramTreeMap);
		 */
	}
}
