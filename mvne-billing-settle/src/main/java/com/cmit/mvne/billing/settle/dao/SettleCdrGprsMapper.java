package com.cmit.mvne.billing.settle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.settle.entity.SettleCdrGprs;
import com.cmit.mvne.billing.settle.entity.dto.SettleItemGprsVolDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SettleCdrGprsMapper extends BaseMapper<SettleCdrGprs> {
    int deleteByPrimaryKey(Long id);

//    int insert(SettleCdrGprs record);

    int insertSelective(SettleCdrGprs record);

    SettleCdrGprs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SettleCdrGprs record);

    int updateByPrimaryKey(SettleCdrGprs record);

    /**
     *   <select id="selectSettleItmeGprsVolByInvoicingPeriod" parameterType="java.lang.String" resultType="com.cmit.mvne.billing.settle.entity.dto.SettleItemGprsVolDTO">
     *     select gprs.settleItem,sum(gprs.downloadVol),sum(gprs.uploadVol)
     *     from settle_cdr_gprs scg
     *     where scg.INVOICING_PERIOD = #{invoicingPeriod}
     *     group by gprs.settleItem
     *   </select>
     * @return
     */
    List<SettleItemGprsVolDTO> selectSettleItmeGprsVolByDay();
}