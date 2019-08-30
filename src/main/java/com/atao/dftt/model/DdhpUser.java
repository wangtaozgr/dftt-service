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
public class DdhpUser extends BaseEntity {

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;
    /**
     * 密码
     */
    @Column(name = "pwd")
    private String pwd;
    /**
     * 
     */
    @Column(name = "pdd_username")
    private String pddUsername;
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

        public static String TABLE_NAME = "DDHP_USER";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String id = "id";  // 
        public static String username = "username";  // 用户名
        public static String pwd = "pwd";  // 密码
        public static String pddUsername = "pdd_username";  // 
        public static String createTime = "create_time";  // 
        public static String used = "used";  // 

    }
}
