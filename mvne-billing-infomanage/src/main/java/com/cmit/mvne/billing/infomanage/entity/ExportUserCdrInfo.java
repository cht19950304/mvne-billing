package com.cmit.mvne.billing.infomanage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExportUserCdrInfo {
        private String msisdn;
        private Date localEventTimestamp;
        private Date eventTimestamp;
        private String location;
        private String apn;
        private Integer eventDuration;
        private BigDecimal totalVol;
        private String resource;
        private BigDecimal fee1;

        public ExportUserCdrInfo(String msisdn,Date localEventTimestamp,Date eventTimestamp,String location,String apn,Integer eventDuration,BigDecimal totalVol,String resource,BigDecimal fee1)
        {
            this.msisdn = msisdn;
            this.localEventTimestamp = localEventTimestamp;
            this.eventTimestamp = eventTimestamp;
            this.location = location;
            this.apn = apn;
            this.eventDuration = eventDuration;
            this.totalVol = totalVol;
            this.resource = resource;
            this.fee1 = fee1;
        }

}
