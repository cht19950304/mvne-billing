package com.cmit.mvne.billing.rating.gprs.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/13 17:48
 */

public class CaffeineObjectMapper extends ObjectMapper {
    public CaffeineObjectMapper() {
        super();
        this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

}
