package com.cmit.mvne.billing.preparation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.preparation.dao.BdOperatorCodeMapper;
import com.cmit.mvne.billing.preparation.entity.BdOperatorCode;
import com.cmit.mvne.billing.preparation.service.BdOperatorCodeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 运营商编码服务类
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/4/16
 */
@Service
public class BdOperatorCodeServiceImpl extends ServiceImpl<BdOperatorCodeMapper, BdOperatorCode> implements BdOperatorCodeService {

//    @Autowired
//    private BdOperatorCodeRepository operatorCodeRepository;

    @Override
    @Cacheable(value = "bd_operator_code", key = "#root.args[0]", unless="#result == null", cacheManager = "localRedisCacheManager")
    public List<BdOperatorCode> findByOperatorCode(String operatorCode) {
        LambdaQueryWrapper<BdOperatorCode> queryWrapper = new LambdaQueryWrapper<>();

        if (!StringUtils.isEmpty(operatorCode)) {
            queryWrapper.eq(BdOperatorCode::getOperatorCode, operatorCode);
            List<BdOperatorCode> bdOperatorCodeList = this.baseMapper.selectList(queryWrapper);
            if (bdOperatorCodeList.size() != 0) {
                return bdOperatorCodeList;
            } else {
                return null;
            }
        }

        return null;
    }

//    @Override
//    @Transactional
//    @CachePut(value = "bd_operator_code", key = "#root.args[0]", unless = "#result == null", cacheManager = "localRedisCacheManager")
//    public List<BdOperatorCode> updateByOperatorCode(String operatorCode, String operatorName) {
//        LambdaQueryWrapper<BdOperatorCode> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(BdOperatorCode::getOperatorCode, operatorCode);
//        List<BdOperatorCode> bdOperatorCodeList = this.baseMapper.selectList(queryWrapper);
//
//        if (bdOperatorCodeList == null) {
//            // 返回空，则说明数据库没有任何更新
//            return null;
//        }
//        BdOperatorCode boc = bdOperatorCodeList.get(0);
//        boc.setOperatorName(operatorName);
//        operatorCodeRepository.save(operatorCode1);
//
//        return operatorCodeRepository.findAllByOperatorCode(operatorCode);
//    }
//
//    @Override
//    @CachePut(value = "bd_operator_code", key = "#root.args[0]", unless = "#result == null", cacheManager = "localRedisCacheManager")
//    public BdOperatorCode updateByOperatorCodeArray(String operatorCode, String operatorName) {
//
//        BdOperatorCode bdOperatorCode = operatorCodeRepository.findByOperatorCode(operatorCode);
//            if (bdOperatorCode == null) {
//                return null;
//        }
//        bdOperatorCode.setOperatorName(operatorName);
//        return operatorCodeRepository.save(bdOperatorCode);
//
//    }
}
