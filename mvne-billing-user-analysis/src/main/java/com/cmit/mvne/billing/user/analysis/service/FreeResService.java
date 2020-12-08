package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.FreeRes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
public interface FreeResService extends IService<FreeRes> {
    FreeRes selectByProductId(Long productId);
    List<FreeRes> selectProduct(Long productId);
}
