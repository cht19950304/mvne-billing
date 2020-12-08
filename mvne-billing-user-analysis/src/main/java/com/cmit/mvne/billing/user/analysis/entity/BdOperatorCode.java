package com.cmit.mvne.billing.user.analysis.entity;

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
 * @since 2020-06-15
 */
@Data
@TableName("bd_operator_code")
public class BdOperatorCode  {

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("COUNTRY_INITIALS")
    private String countryInitials;

    @TableField("COUNTRY_NAME")
    private String countryName;

    /**
     * the chinese name of country
     */
    @TableField("COUNTRY_NAME_CN")
    private String countryNameCn;

    @TableField("OPERATOR_CODE")
    private String operatorCode;

    @TableField("OPERATOR_NAME")
    private String operatorName;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;


}
