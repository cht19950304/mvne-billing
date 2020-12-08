package com.cmit.mvne.billing.infomanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.mapper.IOrdOrderMapper;
import com.cmit.mvne.billing.infomanage.service.IOrdOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
@Service
@Slf4j
public class IOrdOrderServiceImpl  extends ServiceImpl<IOrdOrderMapper, IOrdOrder> implements IOrdOrderService {
    @Autowired
    private IOrdOrderMapper iOrdOrderMapper;

    @Override
    public void createOrder(IOrdOrder iOrdOrder) {
        iOrdOrderMapper.insert(iOrdOrder);
    }

    @Override
    public void deleteOrderById(Long orderId) {
        iOrdOrderMapper.deleteById(orderId);
    }

    @Override
    public List<IOrdOrder> scanLastMinuteOrder() {
        List<IOrdOrder> lastMinuteOrder = iOrdOrderMapper.selectLastMinuteOrder();

        return lastMinuteOrder;
    }

    @Override
    public List<IOrdOrder> scanOrdOrder() {
        List<IOrdOrder> orderList = iOrdOrderMapper.scanOrdOrder();

        return orderList;
    }

    @Override
    public int updateFlag(List<IOrdOrder> orderList) {
        int updateCount = iOrdOrderMapper.updateFlag(orderList);

        return updateCount;
    }

    @Override
    public void insert(IOrdOrder iOrdOrder) {
        iOrdOrderMapper.insert(iOrdOrder);
    }

}
