package com.cmit.mvne.billing.infomanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
public interface IOrdOrderService extends IService<IOrdOrder> {
    /**
     * 将对象插入i_ord_order表
     * @param iOrdOrder
     */
    void createOrder(IOrdOrder iOrdOrder);

    /**
     * 根据主键order_id删除
     * @param orderId
     */
    void deleteOrderById(Long orderId);

    /**
     * 查询返回上一分钟（0秒到0秒）插入i_ord_order表的记录
     * @return
     */
    List<IOrdOrder> scanLastMinuteOrder();

    List<IOrdOrder> scanOrdOrder();

    int updateFlag(List<IOrdOrder> orderList);
}
