package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class TaottCoinRecordVo extends BaseVo {

    /**
    * 
    */
    private String coinDay;
    /**
    * 
    */
    private String username;

    /**
     * 今日金币
     */
    private Integer todayCoin;
    /**
     * 剩余金币
     */
    private Double balance;
    /**
     * 总收入
     */
    private Double totalIncome;
    /**
     * 
     */
    private Date updateTime;

    public String getCoinDay() {
        return coinDay;
    }

    public void setCoinDay(String coinDay) {
        this.coinDay = coinDay;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getTodayCoin() {
        return todayCoin;
    }

    public void setTodayCoin(Integer todayCoin) {
        this.todayCoin = todayCoin;
    }
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static class TF {
        public static String coinDay = "coinDay";  // 
        public static String username = "username";  // 
        public static String todayCoin = "todayCoin";  // 今日金币
        public static String balance = "balance";  // 剩余金币
        public static String totalIncome = "totalIncome";  // 总收入
        public static String updateTime = "updateTime";  // 

    }

}
