package com.cmit.mvne.billing.infomanage.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.baomidou.mybatisplus.annotation.TableField;
import com.cmit.mvne.billing.infomanage.crm.entity.IOrdOrderDto;
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

    public String getMsisdn() { return msisdn; }

    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

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
     * 订单Id（唯一）
     */
//    @TableId(value = "ORDER_ID", type = IdType.INPUT)
//    @NotNull
    private Long orderId;

    /**
     * 订单类型
     */
   @TableField("ORDER_TYPE")
    private String orderType;

    /**
     * 业务操作类型
     */
     @TableField("BUSI_OPER_CODE")
//    @NotBlank
    private String busiOperCode;

    /**
     * 客户id
     */
     @TableField("CUST_ID")
//    @NotNull
    private Long custId;

    /**
     * 用户Id
     */
     @TableField("USER_ID")
//    @NotNull
    private Long userId;

    /**
     * 用户号码
     */
    @TableField("MSISDN")
    private String msisdn;
    /**
     * 订单支付金额
     */
     @TableField("FACT_MONEY")
    private BigDecimal factMoney;

    /**
     * 订单创建时间
     */
    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_DATE")
    @NotNull
    private Date createDate;

    /**
     * 竣工时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("DONE_DATE")
    @NotNull
    private Date doneDate;

    /**
     * 写到信管的时间
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
        this.msisdn = iOrdOrder.getMsisdn();
        this.factMoney = iOrdOrder.getFactMoney();
        this.createDate = iOrdOrder.getCreateDate();
        this.doneDate = iOrdOrder.getDoneDate();
        this.xferDate = iOrdOrder.getXferDate();
        //this.updateFlag = iOrdOrder.getUpdateFlag();
    }



    public IOrdOrder(@NotNull Long orderId, String orderType, @NotBlank String busiOperCode, @NotNull Long custId,
			@NotNull Long userId,@NotNull String msisdn, BigDecimal factMoney, @NotNull Date createDate, @NotNull Date doneDate, Date xferDate) {
		super();
		this.orderId = orderId;
		this.orderType = orderType;
		this.busiOperCode = busiOperCode;
		this.custId = custId;
		this.userId=userId;
		this.msisdn=msisdn;
		this.factMoney = factMoney;
		this.createDate = createDate;
		this.doneDate = doneDate;
		this.xferDate = xferDate;
		//this.updateFlag = updateFlag;
	}

	public IOrdOrder(IOrdOrderDto iOrdOrderDto){
        this.orderId = iOrdOrderDto.getOrderId();
        this.orderType = iOrdOrderDto.getOrderType();
        this.busiOperCode = iOrdOrderDto.getBusiOperCode();
        this.custId = iOrdOrderDto.getCustId();
        this.userId=iOrdOrderDto.getUserId();
        this.msisdn = iOrdOrderDto.getMsisdn();
        this.factMoney = iOrdOrderDto.getFactMoney();
        Date ord_createDate = new Date();
        ord_createDate.setTime(iOrdOrderDto.getCreateDate());
        Date ord_doneDate = new Date();
        ord_doneDate.setTime(iOrdOrderDto.getDoneDate());
        Date ord_xferDate = new Date();
        ord_xferDate.setTime(iOrdOrderDto.getXferDate());
        this.createDate = ord_createDate;
        this.doneDate = ord_doneDate;
        this.xferDate = ord_xferDate;
    }

	@Override
	public String toString() {
		return "IOrdOrder [orderId=" + orderId + ", orderType=" + orderType + ", busiOperCode=" + busiOperCode
				+ ", custId=" + custId + ", userId"+userId + ", msisdn"+msisdn+", factMoney=" + factMoney + ", createDate=" + createDate + ", doneDate="
				+ doneDate + ", xferDate=" + xferDate + "]";
	}
}
