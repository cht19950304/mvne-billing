package com.cmit.mvne.billing.settle.entity.dto;

import lombok.Data;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/8
 */
@Data
public class SettleCdrGprsErrorDTO {
    private Long id;
    private String filename;
    private String recordNumber;
    private String eventTimeStamp;
    private String errorCode;
    private String status;
    private Long createTime;
    private Long receiveTime;
    private Long redoTime;
}
