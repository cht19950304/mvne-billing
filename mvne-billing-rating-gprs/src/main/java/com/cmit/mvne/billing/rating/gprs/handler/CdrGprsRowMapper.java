package com.cmit.mvne.billing.rating.gprs.handler;

import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将ResultSet转换为业务对象
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/1
 */
public class CdrGprsRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        CdrGprs cdrGprs = new CdrGprs();
        cdrGprs.setId(rs.getLong("id"));
        cdrGprs.setRecordType(rs.getString("record_type"));
        cdrGprs.setNumberA(rs.getString("number_a"));
        cdrGprs.setNumberB(rs.getString("number_b"));
        cdrGprs.setNumberDialed(rs.getString("number_dialed"));
        cdrGprs.setMsisdn(rs.getString("msisdn"));
        cdrGprs.setImsi(rs.getString("imsi"));
        cdrGprs.setEventTimeStamp(rs.getString("event_time_stamp"));
        cdrGprs.setLocalEventTimeStamp(rs.getTimestamp("local_event_time_stamp"));
        cdrGprs.setEventDuration(rs.getInt("event_duration"));
        cdrGprs.setDownloadVol(rs.getBigDecimal("download_vol"));
        cdrGprs.setUploadVol(rs.getBigDecimal("upload_vol"));
        cdrGprs.setOperatorCode(rs.getString("operator_code"));
        cdrGprs.setPreratedAmount(rs.getString("prerated_amount"));
        cdrGprs.setApn(rs.getString("apn"));
        cdrGprs.setNulli(rs.getString("nulli"));
        cdrGprs.setBroadWorks(rs.getString("broad_works"));
        cdrGprs.setTeleServiceCode(rs.getString("tele_service_code"));
        cdrGprs.setBearerServiceCode(rs.getString("bearer_service_code"));
        cdrGprs.setOverseasCode(rs.getString("overseas_code"));
        cdrGprs.setVideoIndicator(rs.getString("video_indicator"));
        cdrGprs.setSource(rs.getString("source"));
        cdrGprs.setServiceId(rs.getString("service_id"));
        cdrGprs.setQuantity(rs.getString("quantity"));
        cdrGprs.setCustNumber(rs.getString("cust_number"));
        cdrGprs.setDescription(rs.getString("description"));
        cdrGprs.setCallIdentification(rs.getString("call_identification"));
        cdrGprs.setOriginalFile(rs.getString("original_file"));
        cdrGprs.setReceiveTime(rs.getTimestamp("receive_time"));
        cdrGprs.setCreateTime(rs.getTimestamp("create_time"));
        cdrGprs.setFinishTime(rs.getTimestamp("finish_time"));
        cdrGprs.setLineNumber(rs.getLong("line_number"));
        cdrGprs.setTailNumber(rs.getString("tail_number"));

        return cdrGprs;
    }
}
