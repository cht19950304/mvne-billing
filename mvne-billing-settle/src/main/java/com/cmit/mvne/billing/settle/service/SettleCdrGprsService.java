package com.cmit.mvne.billing.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprs;
import com.cmit.mvne.billing.settle.entity.dto.SettleItemGprsVolDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/12
 */
public interface SettleCdrGprsService extends IService<SettleCdrGprs> {

    List<SettleItemGprsVolDTO> findSettleItmeGprsVolByDay(String yyyyMMdd);

    void saveBatchWithTableName(List<SettleCdrGprs> settleCdrGprsList);
}
