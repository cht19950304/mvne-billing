package com.cmit.mvne.billing.infomanage.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2019-12-06
 */
@Data
@TableName("i_ord_order")
public class IOrdOrder {


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBusiOperCode() {
        return busiOperCode;
    }

    public void setBusiOperCode(String busiOperCode) {
        this.busiOperCode = busiOperCode;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getFactMoney() {
        return factMoney;
    }

    public void setFactMoney(BigDecimal factMoney) {
        this.factMoney = factMoney;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public Date getXferDate() {
        return xferDate;
    }

    public void setXferDate(Date xferDate) {
        this.xferDate = xferDate;
    }

/*    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }*/

    /**
     * ??????Id????????????
     */
//    @TableId(value = "ORDER_ID", type = IdType.INPUT)
//    @NotNull
    private Long orderId;

    /**
     * ????????????
     */
   @TableField("ORDER_TYPE")
    private String orderType;

    /**
     * ??????????????????
     */
     @TableField("BUSI_OPER_CODE")
//    @NotBlank
    private String busiOperCode;

    /**
     * ??????id
     */
     @TableField("CUST_ID")
//    @NotNull
    private Long custId;

    /**
     * ??????Id
     */
     @TableField("USER_ID")
//    @NotNull
    private Long userId;

    /**
     * ??????????????????
     */
     @TableField("FACT_MONEY")
    private BigDecimal factMoney;

    /**
     * ??????????????????
     */
    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_DATE")
    @NotNull
    private Date createDate;

    /**
     * ????????????
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("DONE_DATE")
    @NotNull
    private Date doneDate;

    /**
     * ?????????????????????
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("XFER_DATE")
    private Date xferDate;

/*    @TableField("UPDATE_FLAG")
    @NotBlank
    private String updateFlag;*/

    public IOrdOrder() {
    }

    public IOrdOrder(IOrdOrder iOrdOrder) {
        this.orderId = iOrdOrder.getOrderId();
        this.orderType = iOrdOrder.getOrderType();
        this.busiOperCode = iOrdOrder.getBusiOperCode();
        this.custId = iOrdOrder.getCustId();
        this.userId=iOrdOrder.getUserId();
        this.factMoney = iOrdOrder.getFactMoney();
        this.createDate = iOrdOrder.getCreateDate();
        this.doneDate = iOrdOrder.getDoneDate();
        this.xferDate = iOrdOrder.getXferDate();
        //this.updateFlag = iOrdOrder.getUpdateFlag();
    }



    public IOrdOrder(@NotNull Long orderId, String orderType, @NotBlank String busiOperCode, @NotNull Long custId,
			@NotNull Long userId, BigDecimal factMoney, @NotNull Date createDate, @NotNull Date doneDate, Date xferDate) {
		super();
		this.orderId = orderId;
		this.orderType = orderType;
		this.busiOperCode = busiOperCode;
		this.custId = custId;
		this.userId=userId;
		this.factMoney = factMoney;
		this.createDate = createDate;
		this.doneDate = doneDate;
		this.xferDate = xferDate;
		//this.updateFlag = updateFlag;
	}

	@Override
	public String toString() {
		return "IOrdOrder [orderId=" + orderId + ", orderType=" + orderType + ", busiOperCode=" + busiOperCode
				+ ", custId=" + custId + ", userId"+userId + ", factMoney=" + factMoney + ", createDate=" + createDate + ", doneDate="
				+ doneDate + ", xferDate=" + xferDate + "]";
	}
}
