package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.BdOperatorCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-06-15
 */
public interface BdOperatorCodeService extends IService<BdOperatorCode> {
    BdOperatorCode cache(BdOperatorCode bdOperatorCode);

    BdOperatorCode selectByOperatorCode(String operatorCode);

    String selectByOperCodeInfo(String operatorCode);
}
