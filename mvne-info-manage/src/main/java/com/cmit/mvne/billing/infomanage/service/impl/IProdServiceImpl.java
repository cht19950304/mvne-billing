package com.cmit.mvne.billing.infomanage.service.impl;

import com.cmit.mvne.billing.infomanage.entity.IProd;
import com.cmit.mvne.billing.infomanage.mapper.IProdMapper;
import com.cmit.mvne.billing.infomanage.service.IProdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-10
 */
@Service
public class IProdServiceImpl extends ServiceImpl<IProdMapper, IProd> implements IProdService {
    @Autowired
    private IProdMapper iProdMapper;

    @Override
    public List<IProd> selectByOrderId(Long orderId) {
        return iProdMapper.selectByOrderId(orderId);
    }
    @Override
    public List<IProd> selectByUserId(Long userId){ return  iProdMapper.selectByUserId(userId);};
}
