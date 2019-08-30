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
public class WlttCoinRecord extends BaseEntity {

    /**
    * 
    */
    @Id
    @Column(name = "coin_day")
    private String coinDay;
    /**
    * 
    */
    @Id
    @Column(name = "username")
    private String username;

    /**
     * 今日金币
     */
    @Column(name = "today_coin")
    private Integer todayCoin;
    /**
     * 剩余金币
     */
    @Column(name = "balance")
    private Double balance;
    /**
     * 总收入
     */
    @Column(name = "total_income")
    private Double totalIncome;
    /**
     * 
     */
    @Column(name = "update_time")
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

        public static String TABLE_NAME = "WLTT_COIN_RECORD";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String coinDay = "coin_day";  // 
        public static String username = "username";  // 
        public static String todayCoin = "today_coin";  // 今日金币
        public static String balance = "balance";  // 剩余金币
        public static String totalIncome = "total_income";  // 总收入
        public static String updateTime = "update_time";  // 

    }
}
