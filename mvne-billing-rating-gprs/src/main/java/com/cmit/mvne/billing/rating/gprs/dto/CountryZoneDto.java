package com.cmit.mvne.billing.rating.gprs.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2021/1/8 15:51
 */

@Data
public class CountryZoneDto {
    private String countryName;

    private String countryInitials;

    /**
     * 归属的漫游区域
     */
    private String countryGroup;

    private String status;

    /**
     * 生效时间
     */
    private Long validDate;

    /**
     * 失效时间
     */
    private Long expireDate;


}
