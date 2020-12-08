package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.IProd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2019-12-10
 */
public interface IProdMapper extends BaseMapper<IProd> {
    /**
     * 根据order_id查询
     * @param orderId
     * @return
     */
    List<IProd> selectByOrderId(Long orderId);
    List<IProd> selectByUserId(Long userId);

}
