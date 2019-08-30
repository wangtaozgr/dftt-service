package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;
import java.util.Date;

/**
 * 
 *
 * @author twang
 */
public class DdhpTaskVo extends BaseVo {
	public static String STATUS_WAITPAY = "0";//待付款
	public static String STATUS_WAITSEND = "1";//待发货
	public static String STATUS_WAITRECIVE = "2";//待收货
	public static String STATUS_WAITFINISH = "3";//待确认
	public static String STATUS_FINISHED = "4";//已完成
	
	public static String PDD_STATUS_WAITPAY = "0";//待支付--待支付 待收货 待评价
	public static String PDD_STATUS_WAITSEND = "1";//待发货
	public static String PDD_STATUS_WAITRECIVE = "2";//待收货
	public static String PDD_STATUS_WAITFINISH = "3";//待评价
	public static String PDD_STATUS_FINISHED = "4";//已完成
	
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
     * 任务金额
     */
    private String taskPrice;
    /**
     * 立即;三天;按物流信息
     */
    private String taskRecType;
    /**
     * 1:参团购买 2:开新团购买 3:有团参团，无团开团
     */
    private String taskOrderType;
    /**
     * 3:字图评价 2：指定评价 1：自由评价
     */
    private String taskEvaluateType;
    /**
     * 评价内容
     */
    private String taskEvaluateContent;
    /**
     * 评价图片
     */
    private String taskEvaluateImg;
    /**
     * 任务佣金
     */
    private String taskGold;
    /**
     * 任务状态
     */
    private String taskStatus;
    /**
     * 购买商品图片
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
    public String getTaskPrice() {
        return taskPrice;
    }

    public void setTaskPrice(String taskPrice) {
        this.taskPrice = taskPrice;
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
        public static String taskPrice = "taskPrice";  // 任务金额
        public static String taskRecType = "taskRecType";  // 立即;三天;按物流信息
        public static String taskOrderType = "taskOrderType";  // 1:参团购买 2:开新团购买 3:有团参团，无团开团
        public static String taskEvaluateType = "taskEvaluateType";  // 3:字图评价 2：指定评价 1：自由评价
        public static String taskEvaluateContent = "taskEvaluateContent";  // 评价内容
        public static String taskEvaluateImg = "taskEvaluateImg";  // 评价图片
        public static String taskGold = "taskGold";  // 任务佣金
        public static String taskStatus = "taskStatus";  // 任务状态
        public static String taskBuyerImg = "taskBuyerImg";  // 购买商品图片
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
