package com.cmit.mvne.billing.infomanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.infomanage.entity.CmProdInsInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
public interface CmProdInsInfoService extends IService<CmProdInsInfo> {
    /**
     * 将对象插入cm_prod_ins_info表
     * @param cmProdInsInfo
     */
    void insert(CmProdInsInfo cmProdInsInfo);
}
