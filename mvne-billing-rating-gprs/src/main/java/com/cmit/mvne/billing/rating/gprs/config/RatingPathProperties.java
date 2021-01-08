package com.cmit.mvne.billing.rating.gprs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: caikunchi
 * @Description: 扫描目录配置
 * @Date: Create in 2019/12/31 17:03
 */
@ConfigurationProperties(prefix = "rating.gprs")
@Data
public class RatingPathProperties {

	private String inPath;
	private String bakPath;

}
