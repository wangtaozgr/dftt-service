package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class TaodanUserVo extends BaseVo {

    /**
    * 
    */
    private Long id;

    /**
     * 用户名
     */
    private String username;
    /**
     * 
     */
    private String name;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 
     */
    private String pddUsername;
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getPddUsername() {
        return pddUsername;
    }

    public void setPddUsername(String pddUsername) {
        this.pddUsername = pddUsername;
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
        public static String username = "username";  // 用户名
        public static String name = "name";  // 
        public static String pwd = "pwd";  // 密码
        public static String pddUsername = "pddUsername";  // 
        public static String createTime = "createTime";  // 
        public static String used = "used";  // 

    }

}
