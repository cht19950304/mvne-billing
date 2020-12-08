package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2019-12-10
 */
@Data
@TableName("i_user")
public class IUser{


    /**
     * 订单Id（唯一）
     */
    @TableId(value = "ORDER_ID", type = IdType.INPUT)
    private Long orderId;

    /**
     * 用户Id
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 账户Id
     */
    @TableField("ACCT_ID")
    private Long acctId;

    /**
     * 客户Id
     */
    @TableField("CUST_ID")
    private Long custId;

    /**
     * 用户号码
     */
    @TableField("MSISDN")
    private String msisdn;

    /**
     * imsi
     */
    @TableField("IMSI")
    private String imsi;

    /**
     * 渠道商Id
     */
    @TableField("CHANNEL_CODE")
    private String channelCode;

    /**
     * 生效时间（精确到秒）
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//  @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    private Date validDate;

    /**
     * 失效时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//  @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 用户当前状态（1：正常；11：停机；21：销户）
     */
    @TableField("USER_STATUS")
    private String userStatus;

    public IUser(IUser iUser) {
        this.orderId = iUser.getOrderId();
        this.userId = iUser.getUserId();
        this.acctId = iUser.getAcctId();
        this.custId = iUser.getCustId();
        this.msisdn = iUser.getMsisdn();
        this.imsi = iUser.getImsi();
        this.channelCode = iUser.getChannelCode();
        this.validDate = iUser.getValidDate();
        this.expireDate = iUser.getExpireDate();
        this.userStatus = iUser.getUserStatus();
    }


/*    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }*/

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public IUser(Long orderId, Long userId, Long acctId, Long custId, String msisdn, String imsi, String channelCode,
			Date validDate, Date expireDate, String userStatus) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.acctId = acctId;
		this.custId = custId;
		this.msisdn = msisdn;
		this.imsi = imsi;
		this.channelCode = channelCode;
		this.validDate = validDate;
		this.expireDate = expireDate;
		this.userStatus = userStatus;
	}

	@Override
	public String toString() {
		return "IUser [orderId=" + orderId + ", userId=" + userId + ", acctId=" + acctId + ", custId=" + custId
				+ ", msisdn=" + msisdn + ", imsi=" + imsi + ", channelCode=" + channelCode + ", validDate=" + validDate
				+ ", expireDate=" + expireDate + ", userStatu=" + userStatus + "]";
	}
}
