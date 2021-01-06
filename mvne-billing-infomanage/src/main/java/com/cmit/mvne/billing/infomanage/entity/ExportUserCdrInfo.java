package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExportUserCdrInfo {
        @ExcelProperty(value = "MSISDN")
        private String msisdn;
        @ExcelProperty(value = "Call Event Start Time-icelandic")
        private Date localEventTimestamp;
        @ExcelProperty(value = "Call Event Start Time-Original")
        private String eventTimestamp;
        @ExcelProperty(value = "Place")
        private String location;
        @ExcelProperty(value = "Connection Mode")
        private String apn;
        @ExcelProperty(value = "Duration(s)")
        private Integer eventDuration;
        @ExcelProperty(value = "Volume(kb)")
        private BigDecimal totalVol;
        @ExcelProperty(value = "Resource Origin")
        private String resource;
        @ExcelProperty(value = "Actual Fee")
        private BigDecimal fee1;

        public ExportUserCdrInfo(String msisdn,Date localEventTimestamp,String eventTimestamp,String location,String apn,Integer eventDuration,BigDecimal totalVol,String resource,BigDecimal fee1)
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
