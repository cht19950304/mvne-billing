package com.cmit.mvne.billing.preparation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.preparation.entity.BdOperatorCode;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public interface BdOperatorCodeService extends IService<BdOperatorCode> {
    List<BdOperatorCode> findByOperatorCode(String operatorCode);

//    List<BdOperatorCode> updateByOperatorCode(String operatorCode, String operatorName);
//
//    BdOperatorCode updateByOperatorCodeArray(String operatorCode, String operatorName);
}
