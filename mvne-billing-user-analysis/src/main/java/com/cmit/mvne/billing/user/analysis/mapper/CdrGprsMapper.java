package com.cmit.mvne.billing.user.analysis.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.QueryUserCdrInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/2
 */
public interface CdrGprsMapper extends BaseMapper<CdrGprs> {

    List<QueryUserCdrInfoDto> mulSelectCdrInfo(IPage<QueryUserCdrInfoDto> page,List<String> tableNameList, String msisdn, Date startDate, Date endDate, String xdrType) ;

    //Integer countMulSelectCdr(List<String> tableNameList, String msisdn, Date startDate, Date endDate, String xdrType) ;

    void insert(String yearMonth, List<CdrGprs> cdrGprsList);

    Map<String, String> checkTableExistsWithShow(@Param("tableName") String tableName);

    int createTableByDate(@Param("cdrDate") String cdrDate, @Param("format") String format);

    List<CdrGprs> cdrSelect(@Param("sqlSegment") String sqlSegment);

    IPage<QueryCdrGprsDto> reratQueryCdr(IPage<QueryCdrGprsDto> page, @Param(Constants.WRAPPER) QueryWrapper<QueryCdrGprsDto> queryWrapper);
}
