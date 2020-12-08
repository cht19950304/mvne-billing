package com.cmit.mvne.billing.user.analysis.service.impl;

import com.cmit.mvne.billing.user.analysis.entity.FreeRes;
import com.cmit.mvne.billing.user.analysis.mapper.FreeResMapper;
import com.cmit.mvne.billing.user.analysis.service.FreeResService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
@Service
public class FreeResServiceImpl extends ServiceImpl<FreeResMapper, FreeRes> implements FreeResService {
    @Autowired
    FreeResMapper freeResMapper;

    Long itemId = 66020001L;

    @Override
    public FreeRes selectByProductId(Long productId) {
        List<FreeRes> freeResList = freeResMapper.selectByProductId(productId);
        for (FreeRes freeRes : freeResList) {
            if (itemId.equals(freeRes.getFreeresItem())) {
                return freeRes;
            }
        }
        return new FreeRes();
    }

    @Override
    public List<FreeRes> selectProduct(Long productId) {
        return freeResMapper.selectByProductId(productId);
    }
}
