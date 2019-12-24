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
public class ZhuankeTask extends BaseEntity {

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 
     */
    @Column(name = "username")
    private String username;
    /**
     * 
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * 任务id
     */
    @Column(name = "task_id")
    private String taskId;
    /**
     * 任务号
     */
    @Column(name = "task_sn")
    private String taskSn;
    /**
     * 任务金额
     */
    @Column(name = "task_price")
    private String taskPrice;
    /**
     * 二维码,搜索
     */
    @Column(name = "task_search_type")
    private String taskSearchType;
    /**
     * 二维码地址或搜索内容
     */
    @Column(name = "task_search_content")
    private String taskSearchContent;
    /**
     * 立即;三天;按物流信息
     */
    @Column(name = "task_rec_type")
    private String taskRecType;
    /**
     * 1:参团购买 2:开新团购买 3:有团参团，无团开团
     */
    @Column(name = "task_order_type")
    private String taskOrderType;
    /**
     * 3:字图评价 2：指定评价 1：自由评价
     */
    @Column(name = "task_evaluate_type")
    private String taskEvaluateType;
    /**
     * 评价内容
     */
    @Column(name = "task_evaluate_content")
    private String taskEvaluateContent;
    /**
     * 评价图片
     */
    @Column(name = "task_evaluate_img")
    private String taskEvaluateImg;
    /**
     * 任务佣金
     */
    @Column(name = "task_gold")
    private String taskGold;
    /**
     * 任务状态
     */
    @Column(name = "task_status")
    private String taskStatus;
    /**
     * 购买商品图片
     */
    @Column(name = "task_buyer_img")
    private String taskBuyerImg;
    /**
     * 任务地址
     */
    @Column(name = "task_buyer_url")
    private String taskBuyerUrl;
    /**
     * 商品数量
     */
    @Column(name = "sku_number")
    private String skuNumber;
    /**
     * 优惠卷价格
     */
    @Column(name = "coupon_price")
    private String couponPrice;
    /**
     * 任务留言
     */
    @Column(name = "task_buyer_desc")
    private String taskBuyerDesc;
    /**
     * 
     */
    @Column(name = "pdd_user_id")
    private Long pddUserId;
    /**
     * 
     */
    @Column(name = "pdd_username")
    private String pddUsername;
    /**
     * 
     */
    @Column(name = "pdd_order_id")
    private String pddOrderId;
    /**
     * 
     */
    @Column(name = "pdd_order_no")
    private String pddOrderNo;
    /**
     * 
     */
    @Column(name = "pdd_order_status")
    private String pddOrderStatus;
    /**
     * 
     */
    @Column(name = "create_time")
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
    public String getTaskSn() {
        return taskSn;
    }

    public void setTaskSn(String taskSn) {
        this.taskSn = taskSn;
    }
    public String getTaskPrice() {
        return taskPrice;
    }

    public void setTaskPrice(String taskPrice) {
        this.taskPrice = taskPrice;
    }
    public String getTaskSearchType() {
        return taskSearchType;
    }

    public void setTaskSearchType(String taskSearchType) {
        this.taskSearchType = taskSearchType;
    }
    public String getTaskSearchContent() {
        return taskSearchContent;
    }

    public void setTaskSearchContent(String taskSearchContent) {
        this.taskSearchContent = taskSearchContent;
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
    public String getTaskEvaluateType() {
        return taskEvaluateType;
    }

    public void setTaskEvaluateType(String taskEvaluateType) {
        this.taskEvaluateType = taskEvaluateType;
    }
    public String getTaskEvaluateContent() {
        return taskEvaluateContent;
    }

    public void setTaskEvaluateContent(String taskEvaluateContent) {
        this.taskEvaluateContent = taskEvaluateContent;
    }
    public String getTaskEvaluateImg() {
        return taskEvaluateImg;
    }

    public void setTaskEvaluateImg(String taskEvaluateImg) {
        this.taskEvaluateImg = taskEvaluateImg;
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
    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }
    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
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

        public static String TABLE_NAME = "ZHUANKE_TASK";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String id = "id";  // 
        public static String username = "username";  // 
        public static String userId = "user_id";  // 
        public static String taskId = "task_id";  // 任务id
        public static String taskSn = "task_sn";  // 任务号
        public static String taskPrice = "task_price";  // 任务金额
        public static String taskSearchType = "task_search_type";  // 二维码,搜索
        public static String taskSearchContent = "task_search_content";  // 二维码地址或搜索内容
        public static String taskRecType = "task_rec_type";  // 立即;三天;按物流信息
        public static String taskOrderType = "task_order_type";  // 1:参团购买 2:开新团购买 3:有团参团，无团开团
        public static String taskEvaluateType = "task_evaluate_type";  // 3:字图评价 2：指定评价 1：自由评价
        public static String taskEvaluateContent = "task_evaluate_content";  // 评价内容
        public static String taskEvaluateImg = "task_evaluate_img";  // 评价图片
        public static String taskGold = "task_gold";  // 任务佣金
        public static String taskStatus = "task_status";  // 任务状态
        public static String taskBuyerImg = "task_buyer_img";  // 购买商品图片
        public static String taskBuyerUrl = "task_buyer_url";  // 任务地址
        public static String skuNumber = "sku_number";  // 商品数量
        public static String couponPrice = "coupon_price";  // 优惠卷价格
        public static String taskBuyerDesc = "task_buyer_desc";  // 任务留言
        public static String pddUserId = "pdd_user_id";  // 
        public static String pddUsername = "pdd_username";  // 
        public static String pddOrderId = "pdd_order_id";  // 
        public static String pddOrderNo = "pdd_order_no";  // 
        public static String pddOrderStatus = "pdd_order_status";  // 
        public static String createTime = "create_time";  // 

    }
}
