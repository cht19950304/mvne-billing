package com.cmit.mvne.billing.user.analysis.mapper;

import com.cmit.mvne.billing.user.analysis.entity.PmProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Mapper
public interface PmProductMapper extends BaseMapper<PmProduct> {
    Long create(PmProduct pmProduct);
    String selectByProductId(Long productId);

}
