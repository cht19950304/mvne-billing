package com.cmit.mvne.billing.infomanage.service;

import com.cmit.mvne.billing.infomanage.entity.IProd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-10
 */
public interface IProdService extends IService<IProd> {
    /**
     * 根据order_id查询
     * @param orderId
     * @return
     */
    List<IProd> selectByOrderId(Long orderId);
    List<IProd> selectByUserId(Long userId);
}
