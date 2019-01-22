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
public class MayittUser extends BaseEntity {

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
     * 
     */
    @Column(name = "user_id")
    private String userId;
    /**
     * 
     */
    @Column(name = "device_id")
    private String deviceId;
    /**
     * 
     */
    @Column(name = "sm_device_id")
    private String smDeviceId;
    /**
     * 
     */
    @Column(name = "uuid")
    private String uuid;
    /**
     * 
     */
    @Column(name = "channel")
    private String channel;
    /**
     * 
     */
    @Column(name = "os_version")
    private String osVersion;
    /**
     * 
     */
    @Column(name = "os_api")
    private String osApi;
    /**
     * 
     */
    @Column(name = "model")
    private String model;
    /**
     * 
     */
    @Column(name = "imei")
    private String imei;
    /**
     * 
     */
    @Column(name = "openudid")
    private String openudid;
    /**
     * 
     */
    @Column(name = "carrier")
    private String carrier;
    /**
     * 
     */
    @Column(name = "user_agent")
    private String userAgent;
    /**
     * 
     */
    @Column(name = "log_os_version")
    private String logOsVersion;
    /**
     * 
     */
    @Column(name = "vendor")
    private String vendor;
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

        public static String TABLE_NAME = "MAYITT_USER";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String id = "id";  // 
        public static String username = "username";  // 
        public static String password = "password";  // 
        public static String userId = "user_id";  // 
        public static String deviceId = "device_id";  // 
        public static String smDeviceId = "sm_device_id";  // 
        public static String uuid = "uuid";  // 
        public static String channel = "channel";  // 
        public static String osVersion = "os_version";  // 
        public static String osApi = "os_api";  // 
        public static String model = "model";  // 
        public static String imei = "imei";  // 
        public static String openudid = "openudid";  // 
        public static String carrier = "carrier";  // 
        public static String userAgent = "user_agent";  // 
        public static String logOsVersion = "log_os_version";  // 
        public static String vendor = "vendor";  // 
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
