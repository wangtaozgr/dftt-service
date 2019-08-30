package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class CoinTxRecordVo extends BaseVo {

    /**
    * 
    */
    private Integer id;

    /**
     * 头条种类
     */
    private String type;
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
    private String username;
    /**
     * 
     */
    private Double price;
    /**
     * 
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static class TF {
        public static String id = "id";  // 
        public static String type = "type";  // 头条种类
        public static String txType = "txType";  // 
        public static String txUser = "txUser";  // 
        public static String username = "username";  // 
        public static String price = "price";  // 
        public static String createTime = "createTime";  // 

    }

}
