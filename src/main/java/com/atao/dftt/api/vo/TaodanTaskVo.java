package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class TaodanTaskVo extends BaseVo {

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
    private Long userId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务号
     */
    private String taskNo;
    /**
     * 任务金额
     */
    private String taskPrice;
    /**
     * 搜索;二维码;类目
     */
    private String taskBuyType;
    /**
     * 立即;三天;按物流信息
     */
    private String taskRecType;
    /**
     * 单买;参团;开团;有团开团,无团再开
     */
    private String taskOrderType;
    /**
     * 任务佣金
     */
    private String taskGold;
    /**
     * 任务状态
     */
    private String taskStatus;
    /**
     * 
     */
    private String taskBuyerImg;
    /**
     * 任务地址
     */
    private String taskBuyerUrl;
    /**
     * 任务留言
     */
    private String taskBuyerDesc;
    /**
     * 
     */
    private Long pddUserId;
    /**
     * 
     */
    private String pddUsername;
    /**
     * 
     */
    private String pddOrderId;
    /**
     * 
     */
    private String pddOrderNo;
    /**
     * 
     */
    private String pddOrderStatus;
    /**
     * 
     */
    private Date createTime;

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
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }
    public String getTaskPrice() {
        return taskPrice;
    }

    public void setTaskPrice(String taskPrice) {
        this.taskPrice = taskPrice;
    }
    public String getTaskBuyType() {
        return taskBuyType;
    }

    public void setTaskBuyType(String taskBuyType) {
        this.taskBuyType = taskBuyType;
    }
    public String getTaskRecType() {
        return taskRecType;
    }

    public void setTaskRecType(String taskRecType) {
        this.taskRecType = taskRecType;
    }
    public String getTaskOrderType() {
        return taskOrderType;
    }

    public void setTaskOrderType(String taskOrderType) {
        this.taskOrderType = taskOrderType;
    }
    public String getTaskGold() {
        return taskGold;
    }

    public void setTaskGold(String taskGold) {
        this.taskGold = taskGold;
    }
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    public String getTaskBuyerImg() {
        return taskBuyerImg;
    }

    public void setTaskBuyerImg(String taskBuyerImg) {
        this.taskBuyerImg = taskBuyerImg;
    }
    public String getTaskBuyerUrl() {
        return taskBuyerUrl;
    }

    public void setTaskBuyerUrl(String taskBuyerUrl) {
        this.taskBuyerUrl = taskBuyerUrl;
    }
    public String getTaskBuyerDesc() {
        return taskBuyerDesc;
    }

    public void setTaskBuyerDesc(String taskBuyerDesc) {
        this.taskBuyerDesc = taskBuyerDesc;
    }
    public Long getPddUserId() {
        return pddUserId;
    }

    public void setPddUserId(Long pddUserId) {
        this.pddUserId = pddUserId;
    }
    public String getPddUsername() {
        return pddUsername;
    }

    public void setPddUsername(String pddUsername) {
        this.pddUsername = pddUsername;
    }
    public String getPddOrderId() {
        return pddOrderId;
    }

    public void setPddOrderId(String pddOrderId) {
        this.pddOrderId = pddOrderId;
    }
    public String getPddOrderNo() {
        return pddOrderNo;
    }

    public void setPddOrderNo(String pddOrderNo) {
        this.pddOrderNo = pddOrderNo;
    }
    public String getPddOrderStatus() {
        return pddOrderStatus;
    }

    public void setPddOrderStatus(String pddOrderStatus) {
        this.pddOrderStatus = pddOrderStatus;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static class TF {
        public static String id = "id";  // 
        public static String username = "username";  // 
        public static String userId = "userId";  // 
        public static String taskId = "taskId";  // 任务id
        public static String taskNo = "taskNo";  // 任务号
        public static String taskPrice = "taskPrice";  // 任务金额
        public static String taskBuyType = "taskBuyType";  // 搜索;二维码;类目
        public static String taskRecType = "taskRecType";  // 立即;三天;按物流信息
        public static String taskOrderType = "taskOrderType";  // 单买;参团;开团;有团开团,无团再开
        public static String taskGold = "taskGold";  // 任务佣金
        public static String taskStatus = "taskStatus";  // 任务状态
        public static String taskBuyerImg = "taskBuyerImg";  // 
        public static String taskBuyerUrl = "taskBuyerUrl";  // 任务地址
        public static String taskBuyerDesc = "taskBuyerDesc";  // 任务留言
        public static String pddUserId = "pddUserId";  // 
        public static String pddUsername = "pddUsername";  // 
        public static String pddOrderId = "pddOrderId";  // 
        public static String pddOrderNo = "pddOrderNo";  // 
        public static String pddOrderStatus = "pddOrderStatus";  // 
        public static String createTime = "createTime";  // 

    }

}
