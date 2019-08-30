package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class PddUserVo extends BaseVo {

    /**
    * 
    */
    private Long id;

    /**
     * 
     */
    private String username;
    /**
     * 
     */
    private String uid;
    /**
     * 
     */
    private String addressId;
    /**
     * 
     */
    private String token;
    /**
     * 
     */
    private String apiUid;
    /**
     * 
     */
    private String userAgent;
    /**
     * 
     */
    private String shareUser;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Boolean used;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getApiUid() {
        return apiUid;
    }

    public void setApiUid(String apiUid) {
        this.apiUid = apiUid;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public String getShareUser() {
        return shareUser;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
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

    public static class TF {
        public static String id = "id";  // 
        public static String username = "username";  // 
        public static String uid = "uid";  // 
        public static String addressId = "addressId";  // 
        public static String token = "token";  // 
        public static String apiUid = "apiUid";  // 
        public static String userAgent = "userAgent";  // 
        public static String shareUser = "shareUser";  // 
        public static String createTime = "createTime";  // 
        public static String used = "used";  // 

    }

}
