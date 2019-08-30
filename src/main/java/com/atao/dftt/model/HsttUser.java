package com.atao.dftt.model;

import com.atao.base.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class HsttUser extends BaseEntity {

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 
     */
    @Column(name = "username")
    private String username;
    /**
     * 
     */
    @Column(name = "password")
    private String password;
    /**
     * 注册时间
     */
    @Column(name = "registration_time")
    private String registrationTime;
    /**
     * 
     */
    @Column(name = "request_id")
    private String requestId;
    /**
     * 
     */
    @Column(name = "imei")
    private String imei;
    /**
     * 
     */
    @Column(name = "imsi")
    private String imsi;
    /**
     * 
     */
    @Column(name = "mac")
    private String mac;
    /**
     * 主板
     */
    @Column(name = "board")
    private String board;
    /**
     * 
     */
    @Column(name = "sdk")
    private String sdk;
    /**
     * 
     */
    @Column(name = "android_id")
    private String androidId;
    /**
     * 
     */
    @Column(name = "model")
    private String model;
    /**
     * 
     */
    @Column(name = "vendor")
    private String vendor;
    /**
     * 
     */
    @Column(name = "brand")
    private String brand;
    /**
     * 
     */
    @Column(name = "os_version")
    private String osVersion;
    /**
     * 
     */
    @Column(name = "user_agent")
    private String userAgent;
    /**
     * mob device
     */
    @Column(name = "device")
    private String device;
    /**
     * mob duid
     */
    @Column(name = "duid")
    private String duid;
    /**
     * mob log4最后一位
     */
    @Column(name = "mob_key")
    private String mobKey;
    /**
     * 
     */
    @Column(name = "serialno")
    private String serialno;
    /**
     * 
     */
    @Column(name = "mob_useragent")
    private String mobUseragent;
    /**
     * 
     */
    @Column(name = "first_day")
    private Boolean firstDay;
    /**
     * 
     */
    @Column(name = "read_num")
    private Long readNum;
    /**
     * 
     */
    @Column(name = "read_time")
    private Date readTime;
    /**
     * 
     */
    @Column(name = "limit_read_num")
    private Long limitReadNum;
    /**
     * 
     */
    @Column(name = "v_read_num")
    private Long vReadNum;
    /**
     * 
     */
    @Column(name = "v_read_time")
    private Date vReadTime;
    /**
     * 
     */
    @Column(name = "v_limit_read_num")
    private Long vLimitReadNum;
    /**
     * 最后签到时间
     */
    @Column(name = "qd_time")
    private Date qdTime;
    /**
     * 
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 
     */
    @Column(name = "used")
    private Boolean used;
    /**
     * 
     */
    @Column(name = "auto_tx")
    private Boolean autoTx;
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
    @Column(name = "tx_name")
    private String txName;
    /**
     * 
     */
    @Column(name = "tx_idcard")
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

        public static String TABLE_NAME = "HSTT_USER";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String id = "id";  // 
        public static String username = "username";  // 
        public static String password = "password";  // 
        public static String registrationTime = "registration_time";  // 注册时间
        public static String requestId = "request_id";  // 
        public static String imei = "imei";  // 
        public static String imsi = "imsi";  // 
        public static String mac = "mac";  // 
        public static String board = "board";  // 主板
        public static String sdk = "sdk";  // 
        public static String androidId = "android_id";  // 
        public static String model = "model";  // 
        public static String vendor = "vendor";  // 
        public static String brand = "brand";  // 
        public static String osVersion = "os_version";  // 
        public static String userAgent = "user_agent";  // 
        public static String device = "device";  // mob device
        public static String duid = "duid";  // mob duid
        public static String mobKey = "mob_key";  // mob log4最后一位
        public static String serialno = "serialno";  // 
        public static String mobUseragent = "mob_useragent";  // 
        public static String firstDay = "first_day";  // 
        public static String readNum = "read_num";  // 
        public static String readTime = "read_time";  // 
        public static String limitReadNum = "limit_read_num";  // 
        public static String vReadNum = "v_read_num";  // 
        public static String vReadTime = "v_read_time";  // 
        public static String vLimitReadNum = "v_limit_read_num";  // 
        public static String qdTime = "qd_time";  // 最后签到时间
        public static String createTime = "create_time";  // 
        public static String used = "used";  // 
        public static String autoTx = "auto_tx";  // 
        public static String txType = "tx_type";  // 
        public static String txUser = "tx_user";  // 
        public static String txName = "tx_name";  // 
        public static String txIdcard = "tx_idcard";  // 

    }
}
