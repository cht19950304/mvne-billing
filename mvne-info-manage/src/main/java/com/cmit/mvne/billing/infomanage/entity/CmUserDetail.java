package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
@Data
@TableName("cm_user_detail")
@JSONType(orders={"userId","acctId","custId","msisdn","imsi","channelCode","validDate","expireDate","userStatus"})
public class CmUserDetail{


    /**
     * 用户Id（唯一）
     */
    @TableId(value = "USER_ID", type = IdType.INPUT)
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
     * IMSI
     */
    @TableField("IMSI")
    private String imsi;

    /**
     * 渠道商ID
     */
    @TableField("CHANNEL_CODE")
    private String channelCode;

    /**
     * 生效时间（精确到秒）
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    private Date validDate;

    /**
     * 失效时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 用户当前状态（1：正常；11：停机；21：销户）
     */
    @TableField("USER_STATUS")
    private String userStatus;

    public CmUserDetail(IUser iUser) {
        this.acctId = iUser.getAcctId();
        this.custId = iUser.getCustId();
        this.msisdn = iUser.getMsisdn();
        this.userId = iUser.getUserId();
        this.imsi=iUser.getImsi();
        this.channelCode=iUser.getChannelCode();
        this.validDate = iUser.getValidDate();
        this.expireDate = iUser.getExpireDate();
        this.userStatus = iUser.getUserStatus();
    }

    public CmUserDetail(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        this.acctId = jsonObject.getLong("acctId");
        this.custId = jsonObject.getLong("custId");
        this.msisdn = jsonObject.getString("msisdn");
        this.userId = jsonObject.getLong("userId");
        this.imsi = jsonObject.getString("imsi");
        this.channelCode = jsonObject.getString("channelCode");
        this.validDate = jsonObject.getDate("validDate");
        this.expireDate = jsonObject.getDate("expireDate");
        this.userStatus = jsonObject.getString("userStatus");
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


}
