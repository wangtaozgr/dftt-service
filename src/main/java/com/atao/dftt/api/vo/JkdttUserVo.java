package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class JkdttUserVo extends BaseVo {

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
    private String openId;
    /**
     * 
     */
    private String smid;
    /**
     * 
     */
    private String uniqueid;
    /**
     * 
     */
    private String umengid;
    /**
     * 
     */
    private String x;
    /**
     * 
     */
    private String channel;
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
    private String os;
    /**
     * 
     */
    private String userAgent;
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
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getSmid() {
        return smid;
    }

    public void setSmid(String smid) {
        this.smid = smid;
    }
    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }
    public String getUmengid() {
        return umengid;
    }

    public void setUmengid(String umengid) {
        this.umengid = umengid;
    }
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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
        public static String openId = "openId";  // 
        public static String smid = "smid";  // 
        public static String uniqueid = "uniqueid";  // 
        public static String umengid = "umengid";  // 
        public static String x = "x";  // 
        public static String channel = "channel";  // 
        public static String model = "model";  // 
        public static String vendor = "vendor";  // 
        public static String os = "os";  // 
        public static String userAgent = "userAgent";  // 
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
