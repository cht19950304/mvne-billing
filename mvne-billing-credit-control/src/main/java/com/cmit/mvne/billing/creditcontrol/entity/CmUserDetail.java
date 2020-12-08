package com.cmit.mvne.billing.creditcontrol.entity;

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
 * @author jiangxm
 * @since 2020-02-17
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
}
