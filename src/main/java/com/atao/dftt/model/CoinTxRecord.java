package com.atao.dftt.model;

import com.atao.base.model.BaseEntity;
import com.atao.base.util.DateUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class CoinTxRecord extends BaseEntity {

	public CoinTxRecord() {
		super();
	}

	public CoinTxRecord(String type, String username, Double price, String txType, String txUser, Date createTime) {
		super();
		this.type = type;
		this.username = username;
		this.price = price;
		this.txType = txType;
		this.txUser = txUser;
		this.createTime = createTime;
	}

	/**
	* 
	*/
	@Id
	@Column(name = "id")
	private Integer id;

	/**
	 * 头条种类
	 */
	@Column(name = "type")
	private String type;
	/**
	 * 
	 */
	@Column(name = "tx_type")
	private String txType;
	/**
	 * 
	 */
	@Column(name = "tx_user")
	private String txUser;
	/**
	 * 
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 
	 */
	@Column(name = "price")
	private Double price;
	/**
	 * 
	 */
	@Column(name = "create_time")
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public String getTxUser() {
		return txUser;
	}

	public void setTxUser(String txUser) {
		this.txUser = txUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Transient
	public String getTxTypeStr() {
		if ("wx".equals(txType)) {
			return "微信";
		} else if ("ali".equals(txType)) {
			return "支付宝";
		}
		return "";
	}

	@Transient
	public String getCreateTimeStr() {
		return DateUtils.formatDate(createTime, DateUtils.DATE_TIME_PATTERN);
	}

	public static class TF {

		public static String TABLE_NAME = "COIN_TX_RECORD"; // 表名

		// public static String TABLE_SCHEMA = ConfigUtils.getValue(""); // 库名

		public static String id = "id"; //
		public static String type = "type"; // 头条种类
		public static String txType = "tx_type"; //
		public static String txUser = "tx_user"; //
		public static String username = "username"; //
		public static String price = "price"; //
		public static String createTime = "create_time"; //

	}
}
