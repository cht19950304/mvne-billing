package com.cmit.mvne.billing.settle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.settle.config.MybatisPlusConfig;
import com.cmit.mvne.billing.settle.dao.SettleCdrGprsMapper;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprs;
import com.cmit.mvne.billing.settle.entity.dto.SettleItemGprsVolDTO;
import com.cmit.mvne.billing.settle.service.SettleCdrGprsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
@Service
public class SettleCdrGprsServiceImpl extends ServiceImpl<SettleCdrGprsMapper, SettleCdrGprs> implements SettleCdrGprsService {

    private String srcTablePrefix = "settle_cdr_gprs_";

    @Override
    public List<SettleItemGprsVolDTO> findSettleItmeGprsVolByDay(String yyyyMMdd) {
        String tableName = srcTablePrefix + yyyyMMdd;
        MybatisPlusConfig.tableNameHolder.set(tableName);
        List<SettleItemGprsVolDTO> settleItemGprsVolDTOList = getBaseMapper().selectSettleItmeGprsVolByDay();
        MybatisPlusConfig.tableNameHolder.remove();
        return settleItemGprsVolDTOList;

    }

    @Override
    public void saveBatchWithTableName(List<SettleCdrGprs> settleCdrGprsList) {

        // 可能存在一个批次中多个recieveTime的情况
        Map<String, List<SettleCdrGprs>> settleCdrMap = new HashMap<>();
        for (SettleCdrGprs settleCdrGprs : settleCdrGprsList) {
            String tableName = srcTablePrefix + getTableSuffixByReceiveTime(settleCdrGprs.getReceiveTime().toInstant());

            if (settleCdrMap.get(tableName) == null) {
                List<SettleCdrGprs> gprsList = new ArrayList<>();
                gprsList.add(settleCdrGprs);
                settleCdrMap.put(tableName, gprsList);
            } else {
                settleCdrMap.get(tableName).add(settleCdrGprs);
            }
        }

        for(Map.Entry<String, List<SettleCdrGprs>> entry : settleCdrMap.entrySet()) {
            MybatisPlusConfig.tableNameHolder.set(entry.getKey());
            saveBatch(entry.getValue());
        }
        // 防止内存泄漏
        MybatisPlusConfig.tableNameHolder.remove();
    }

    /**
     * 根据系统时区获取表尾
     * @param instant
     * @return
     */
    private String getTableSuffixByReceiveTime(Instant instant) {
//        Instant instant = Instant.ofEpochMilli((Long) receiveTime);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }


}
