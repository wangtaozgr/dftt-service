package com.atao.dftt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import com.atao.base.model.BaseEntity;

/**
 * 
 *
 * @author twang
 */
public class Wltt extends BaseEntity {

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
    @Column(name = "pwd")
    private String pwd;
    /**
     * 
     */
    @Column(name = "app_key")
    private String appKey;
    /**
     * 
     */
    @Column(name = "user_agent")
    private String userAgent;
    /**
     * 
     */
    @Column(name = "uid")
    private String uid;
    /**
     * 
     */
    @Column(name = "open_id")
    private String openId;
    /**
     * 
     */
    @Column(name = "x")
    private String x;
    /**
     * 
     */
    @Column(name = "auth_token")
    private String authToken;
    /**
     * 
     */
    @Column(name = "devid")
    private String devid;
    /**
     * 
     */
    @Column(name = "device_id")
    private String deviceId;
    /**
     * 
     */
    @Column(name = "androidId")
    private String androidid;
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
    @Column(name = "channel")
    private String channel;
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
    @Column(name = "city_key")
    private String cityKey;
    /**
     * 
     */
    @Column(name = "lat")
    private String lat;
    /**
     * 
     */
    @Column(name = "lon")
    private String lon;
    /**
     * 
     */
    @Column(name = "device")
    private String device;
    /**
     * 
     */
    @Column(name = "osv")
    private String osv;
    /**
     * 
     */
    @Column(name = "up")
    private String up;
    /**
     * 
     */
    @Column(name = "os_version")
    private String osVersion;
    /**
     * 
     */
    @Column(name = "os")
    private String os;
    /**
     * 
     */
    @Column(name = "pid")
    private String pid;
    /**
     * 
     */
    @Column(name = "mac")
    private String mac;
    /**
     * 
     */
    @Column(name = "visit_code")
    private String visitCode;
    /**
     * 
     */
    @Column(name = "read_num")
    private Long readNum;
    /**
     * 
     */
    @Column(name = "limit_read_num")
    private Long limitReadNum;
    /**
     * 
     */
    @Column(name = "read_time")
    private Date readTime;
    /**
     * 
     */
    @Column(name = "v_read_num")
    private Long vReadNum;
    /**
     * 
     */
    @Column(name = "v_limit_read_num")
    private Long vLimitReadNum;
    /**
     * 
     */
    @Column(name = "v_read_time")
    private Date vReadTime;
    /**
     * 
     */
    @Column(name = "used")
    private Boolean used;
    /**
     * 
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 
     */
    @Column(name = "qd_time")
    private Date qdTime;

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
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getAndroidid() {
        return androidid;
    }

    public void setAndroidid(String androidid) {
        this.androidid = androidid;
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
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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
    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    public String getOsv() {
        return osv;
    }

    public void setOsv(String osv) {
        this.osv = osv;
    }
    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getVisitCode() {
        return visitCode;
    }

    public void setVisitCode(String visitCode) {
        this.visitCode = visitCode;
    }
    public Long getReadNum() {
        return readNum;
    }

    public void setReadNum(Long readNum) {
        this.readNum = readNum;
    }
    public Long getLimitReadNum() {
        return limitReadNum;
    }

    public void setLimitReadNum(Long limitReadNum) {
        this.limitReadNum = limitReadNum;
    }
    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
    public Long getVReadNum() {
        return vReadNum;
    }

    public void setVReadNum(Long vReadNum) {
        this.vReadNum = vReadNum;
    }
    public Long getVLimitReadNum() {
        return vLimitReadNum;
    }

    public void setVLimitReadNum(Long vLimitReadNum) {
        this.vLimitReadNum = vLimitReadNum;
    }
    public Date getVReadTime() {
        return vReadTime;
    }

    public void setVReadTime(Date vReadTime) {
        this.vReadTime = vReadTime;
    }
    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getQdTime() {
        return qdTime;
    }

    public void setQdTime(Date qdTime) {
        this.qdTime = qdTime;
    }

    public static class TF {

        public static String TABLE_NAME = "WLTT";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String id = "id";  // 
        public static String username = "username";  // 
        public static String pwd = "pwd";  // 
        public static String appKey = "app_key";  // 
        public static String userAgent = "user_agent";  // 
        public static String uid = "uid";  // 
        public static String openId = "open_id";  // 
        public static String x = "x";  // 
        public static String authToken = "auth_token";  // 
        public static String devid = "devid";  // 
        public static String deviceId = "device_id";  // 
        public static String androidid = "androidId";  // 
        public static String model = "model";  // 
        public static String vendor = "vendor";  // 
        public static String channel = "channel";  // 
        public static String imei = "imei";  // 
        public static String imsi = "imsi";  // 
        public static String cityKey = "city_key";  // 
        public static String lat = "lat";  // 
        public static String lon = "lon";  // 
        public static String device = "device";  // 
        public static String osv = "osv";  // 
        public static String up = "up";  // 
        public static String osVersion = "os_version";  // 
        public static String os = "os";  // 
        public static String pid = "pid";  // 
        public static String mac = "mac";  // 
        public static String visitCode = "visit_code";  // 
        public static String readNum = "read_num";  // 
        public static String limitReadNum = "limit_read_num";  // 
        public static String readTime = "read_time";  // 
        public static String vReadNum = "v_read_num";  // 
        public static String vLimitReadNum = "v_limit_read_num";  // 
        public static String vReadTime = "v_read_time";  // 
        public static String used = "used";  // 
        public static String createTime = "create_time";  // 
        public static String qdTime = "qd_time";  // 

    }

}
