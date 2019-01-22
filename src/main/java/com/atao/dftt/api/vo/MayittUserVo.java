package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class MayittUserVo extends BaseVo {

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
     * 
     */
    private String userId;
    /**
     * 
     */
    private String deviceId;
    /**
     * 
     */
    private String smDeviceId;
    /**
     * 
     */
    private String uuid;
    /**
     * 
     */
    private String channel;
    /**
     * 
     */
    private String osVersion;
    /**
     * 
     */
    private String osApi;
    /**
     * 
     */
    private String model;
    /**
     * 
     */
    private String imei;
    /**
     * 
     */
    private String openudid;
    /**
     * 
     */
    private String carrier;
    /**
     * 
     */
    private String userAgent;
    /**
     * 
     */
    private String logOsVersion;
    /**
     * 
     */
    private String vendor;
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
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getSmDeviceId() {
        return smDeviceId;
    }

    public void setSmDeviceId(String smDeviceId) {
        this.smDeviceId = smDeviceId;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
    public String getOsApi() {
        return osApi;
    }

    public void setOsApi(String osApi) {
        this.osApi = osApi;
    }
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
    public String getOpenudid() {
        return openudid;
    }

    public void setOpenudid(String openudid) {
        this.openudid = openudid;
    }
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public String getLogOsVersion() {
        return logOsVersion;
    }

    public void setLogOsVersion(String logOsVersion) {
        this.logOsVersion = logOsVersion;
    }
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
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
        public static String userId = "userId";  // 
        public static String deviceId = "deviceId";  // 
        public static String smDeviceId = "smDeviceId";  // 
        public static String uuid = "uuid";  // 
        public static String channel = "channel";  // 
        public static String osVersion = "osVersion";  // 
        public static String osApi = "osApi";  // 
        public static String model = "model";  // 
        public static String imei = "imei";  // 
        public static String openudid = "openudid";  // 
        public static String carrier = "carrier";  // 
        public static String userAgent = "userAgent";  // 
        public static String logOsVersion = "logOsVersion";  // 
        public static String vendor = "vendor";  // 
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
