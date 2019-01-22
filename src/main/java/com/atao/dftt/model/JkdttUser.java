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
public class JkdttUser extends BaseEntity {

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
    @Column(name = "open_id")
    private String openId;
    /**
     * 
     */
    @Column(name = "smid")
    private String smid;
    /**
     * 
     */
    @Column(name = "uniqueid")
    private String uniqueid;
    /**
     * 
     */
    @Column(name = "umengid")
    private String umengid;
    /**
     * 
     */
    @Column(name = "x")
    private String x;
    /**
     * 
     */
    @Column(name = "channel")
    private String channel;
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
    @Column(name = "os")
    private String os;
    /**
     * 
     */
    @Column(name = "user_agent")
    private String userAgent;
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

        public static String TABLE_NAME = "JKDTT_USER";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String id = "id";  // 
        public static String username = "username";  // 
        public static String password = "password";  // 
        public static String openId = "open_id";  // 
        public static String smid = "smid";  // 
        public static String uniqueid = "uniqueid";  // 
        public static String umengid = "umengid";  // 
        public static String x = "x";  // 
        public static String channel = "channel";  // 
        public static String model = "model";  // 
        public static String vendor = "vendor";  // 
        public static String os = "os";  // 
        public static String userAgent = "user_agent";  // 
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
