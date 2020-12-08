package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-01-21
 */
@Data
@TableName("sys_special_number")
public class SysSpecialNumber  {
    /**
     * 特服号码
     */
    @TableField("SPECIAL_NUMBER")
    private String specialNumber;

    /**
     * 对应的批价规则id
     */
    @TableField("RULE_ID")
    private Long ruleId;


}
