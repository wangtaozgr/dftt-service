package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class HsttUserVo extends BaseVo {

    /**
    * 
    */
    private Integer id;

    /**
     * 
     */
    private String username;
    /**
     * 
     */
    private String password;
    /**
     * 注册时间
     */
    private String registrationTime;
    /**
     * 
     */
    private String requestId;
    /**
     * 
     */
    private String imei;
    /**
     * 
     */
    private String imsi;
    /**
     * 
     */
    private String mac;
    /**
     * 主板
     */
    private String board;
    /**
     * 
     */
    private String sdk;
    /**
     * 
     */
    private String androidId;
    /**
     * 
     */
    private String model;
    /**
     * 
     */
    private String vendor;
    /**
     * 
     */
    private String brand;
    /**
     * 
     */
    private String osVersion;
    /**
     * 
     */
    private String userAgent;
    /**
     * mob device
     */
    private String device;
    /**
     * mob duid
     */
    private String duid;
    /**
     * mob log4最后一位
     */
    private String mobKey;
    /**
     * 
     */
    private String serialno;
    /**
     * 
     */
    private String mobUseragent;
    /**
     * 
     */
    private Boolean firstDay;
    /**
     * 
     */
    private Long readNum;
    /**
     * 
     */
    private Date readTime;
    /**
     * 
     */
    private Long limitReadNum;
    /**
     * 
     */
    private Long vReadNum;
    /**
     * 
     */
    private Date vReadTime;
    /**
     * 
     */
    private Long vLimitReadNum;
    /**
     * 最后签到时间
     */
    private Date qdTime;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Boolean used;
    /**
     * 
     */
    private Boolean autoTx;
    /**
     * 
     */
    private String txType;
    /**
     * 
     */
    private String txUser;
    /**
     * 
     */
    private String txName;
    /**
     * 
     */
    private String txIdcard;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }
    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }
    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    public String getDuid() {
        return duid;
    }

    public void setDuid(String duid) {
        this.duid = duid;
    }
    public String getMobKey() {
        return mobKey;
    }

    public void setMobKey(String mobKey) {
        this.mobKey = mobKey;
    }
    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }
    public String getMobUseragent() {
        return mobUseragent;
    }

    public void setMobUseragent(String mobUseragent) {
        this.mobUseragent = mobUseragent;
    }
    public Boolean getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(Boolean firstDay) {
        this.firstDay = firstDay;
    }
    public Long getReadNum() {
        return readNum;
    }

    public void setReadNum(Long readNum) {
        this.readNum = readNum;
    }
    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
    public Long getLimitReadNum() {
        return limitReadNum;
    }

    public void setLimitReadNum(Long limitReadNum) {
        this.limitReadNum = limitReadNum;
    }
    public Long getVReadNum() {
        return vReadNum;
    }

    public void setVReadNum(Long vReadNum) {
        this.vReadNum = vReadNum;
    }
    public Date getVReadTime() {
        return vReadTime;
    }

    public void setVReadTime(Date vReadTime) {
        this.vReadTime = vReadTime;
    }
    public Long getVLimitReadNum() {
        return vLimitReadNum;
    }

    public void setVLimitReadNum(Long vLimitReadNum) {
        this.vLimitReadNum = vLimitReadNum;
    }
    public Date getQdTime() {
        return qdTime;
    }

    public void setQdTime(Date qdTime) {
        this.qdTime = qdTime;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
    public Boolean getAutoTx() {
        return autoTx;
    }

    public void setAutoTx(Boolean autoTx) {
        this.autoTx = autoTx;
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
    public String getTxName() {
        return txName;
    }

    public void setTxName(String txName) {
        this.txName = txName;
    }
    public String getTxIdcard() {
        return txIdcard;
    }

    public void setTxIdcard(String txIdcard) {
        this.txIdcard = txIdcard;
    }

    public static class TF {
        public static String id = "id";  // 
        public static String username = "username";  // 
        public static String password = "password";  // 
        public static String registrationTime = "registrationTime";  // 注册时间
        public static String requestId = "requestId";  // 
        public static String imei = "imei";  // 
        public static String imsi = "imsi";  // 
        public static String mac = "mac";  // 
        public static String board = "board";  // 主板
        public static String sdk = "sdk";  // 
        public static String androidId = "androidId";  // 
        public static String model = "model";  // 
        public static String vendor = "vendor";  // 
        public static String brand = "brand";  // 
        public static String osVersion = "osVersion";  // 
        public static String userAgent = "userAgent";  // 
        public static String device = "device";  // mob device
        public static String duid = "duid";  // mob duid
        public static String mobKey = "mobKey";  // mob log4最后一位
        public static String serialno = "serialno";  // 
        public static String mobUseragent = "mobUseragent";  // 
        public static String firstDay = "firstDay";  // 
        public static String readNum = "readNum";  // 
        public static String readTime = "readTime";  // 
        public static String limitReadNum = "limitReadNum";  // 
        public static String vReadNum = "vReadNum";  // 
        public static String vReadTime = "vReadTime";  // 
        public static String vLimitReadNum = "vLimitReadNum";  // 
        public static String qdTime = "qdTime";  // 最后签到时间
        public static String createTime = "createTime";  // 
        public static String used = "used";  // 
        public static String autoTx = "autoTx";  // 
        public static String txType = "txType";  // 
        public static String txUser = "txUser";  // 
        public static String txName = "txName";  // 
        public static String txIdcard = "txIdcard";  // 

    }

}
