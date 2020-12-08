package com.cmit.mvne.billing.settle.handler;

import com.cmit.mvne.billing.settle.entity.CdrGprsSettle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将ResultSet转换为业务对象
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/12
 */
public class CdrGprsSettleRowMapper implements RowMapper<CdrGprsSettle> {
    @Override
    public CdrGprsSettle mapRow(ResultSet rs, int rowNum) throws SQLException {
        CdrGprsSettle cdrGprsSettle = new CdrGprsSettle();
        cdrGprsSettle.setId(rs.getLong("ID"));
        cdrGprsSettle.setRecordType(rs.getString("RECORD_TYPE"));
        cdrGprsSettle.setNumberA(rs.getString("NUMBER_A"));
        cdrGprsSettle.setNumberB(rs.getString("NUMBER_B"));
        cdrGprsSettle.setNumberDialed(rs.getString("NUMBER_DIALED"));
        cdrGprsSettle.setMsisdn(rs.getString("MSISDN"));
        cdrGprsSettle.setImsi(rs.getString("IMSI"));
        cdrGprsSettle.setEventTimeStamp(rs.getString("EVENT_TIME_STAMP"));
        cdrGprsSettle.setLocalEventTimeStamp(rs.getTimestamp("LOCAL_EVENT_TIME_STAMP"));
        cdrGprsSettle.setEventDuration(rs.getInt("EVENT_DURATION"));
        cdrGprsSettle.setDownloadVol(rs.getBigDecimal("DOWNLOAD_VOL"));
        cdrGprsSettle.setUploadVol(rs.getBigDecimal("UPLOAD_VOL"));
        cdrGprsSettle.setOperatorCode(rs.getString("OPERATOR_CODE"));
        cdrGprsSettle.setPreratedAmount(rs.getString("PRERATED_AMOUNT"));
        cdrGprsSettle.setApn(rs.getString("APN"));
        cdrGprsSettle.setNulli(rs.getString("NULLI"));
        cdrGprsSettle.setBroadWorks(rs.getString("BROAD_WORKS"));
        cdrGprsSettle.setTeleServiceCode(rs.getString("TELE_SERVICE_CODE"));
        cdrGprsSettle.setBearerServiceCode(rs.getString("BEARER_SERVICE_CODE"));
        cdrGprsSettle.setOverseasCode(rs.getString("OVERSEAS_CODE"));
        cdrGprsSettle.setVideoIndicator(rs.getString("VIDEO_INDICATOR"));
        cdrGprsSettle.setSource(rs.getString("SOURCE"));
        cdrGprsSettle.setServiceId(rs.getString("SERVICE_ID"));
        cdrGprsSettle.setQuantity(rs.getString("QUANTITY"));
        cdrGprsSettle.setCustNumber(rs.getString("CUST_NUMBER"));
        cdrGprsSettle.setDescription(rs.getString("description"));
        cdrGprsSettle.setCallIdentification(rs.getString("CALL_IDENTIFICATION"));
        cdrGprsSettle.setTailNumber(rs.getString("TAIL_NUMBER"));
        cdrGprsSettle.setOriginalFile(rs.getString("ORIGINAL_FILE"));
        cdrGprsSettle.setLineNumber(rs.getLong("LINE_NUMBER"));
        cdrGprsSettle.setReceiveTime(rs.getTimestamp("RECEIVE_TIME"));
        cdrGprsSettle.setCreateTime(rs.getTimestamp("CREATE_TIME"));
        cdrGprsSettle.setFinishTime(rs.getTimestamp("FINISH_TIME"));
        return cdrGprsSettle;
    }
}
