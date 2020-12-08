package com.cmit.mvne.billing.infomanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.infomanage.entity.CmProdInsInfo;
import com.cmit.mvne.billing.infomanage.mapper.CmProdInsInfoMapper;
import com.cmit.mvne.billing.infomanage.service.CmProdInsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
@Service
public class CmProdInsInfoServiceImpl extends ServiceImpl<CmProdInsInfoMapper, CmProdInsInfo> implements CmProdInsInfoService {
    @Autowired
    CmProdInsInfoMapper cmProdInsInfoMapper;

    @Override
    public void insert(CmProdInsInfo cmProdInsInfo) {
        cmProdInsInfoMapper.insert(cmProdInsInfo);
    }
}
