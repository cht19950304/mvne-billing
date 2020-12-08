package com.cmit.mvne.billing.infomanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
@Mapper
@Repository
public interface IOrdOrderMapper extends BaseMapper<IOrdOrder> {

    /**
     * 从i_ord_order中查询上一分钟（00~00）的所有订单
     * @return 查询到的订单
     */
    public List<IOrdOrder> selectLastMinuteOrder();

    public List<IOrdOrder> scanOrdOrder();

    public int updateFlag(List<IOrdOrder> orderList);

    //test
    void  deleteById(long orderId);
}
