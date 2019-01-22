package com.atao.dftt.api.vo;

import java.util.ArrayList;
import java.util.List;

public class MyCoinVo {
	public static Integer wltt = 1;
	public static String wlttName = "微鲤头条";
	public static Integer taott = 2;
	public static String taottName = "淘头条";
	public static Integer jkdtt = 3;
	public static String jkdttName = "聚看点";
	public static Integer mayitt = 4;
	public static String mayittName = "蚂蚁头条";
	public static Integer hstt = 5;
	public static String hsttName = "花生头条";
	public static Integer zqtt = 6;
	public static String zqttName = "中青看点";

	private int type;
	private String typeName;
	private List<CoinRecordVo> records = new ArrayList<CoinRecordVo>();

	public MyCoinVo(int type, String typeName) {
		super();
		this.type = type;
		this.typeName = typeName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<CoinRecordVo> getRecords() {
		return records;
	}

	public void setRecords(List<CoinRecordVo> records) {
		this.records = records;
	}

}
