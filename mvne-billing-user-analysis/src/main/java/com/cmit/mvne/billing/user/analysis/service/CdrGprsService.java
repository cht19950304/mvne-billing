package com.cmit.mvne.billing.user.analysis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsDto;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsErrDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.QueryUserCdrInfoDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author caikc
 * @author zengxf
 * @since 2020/3/2
 */
@Service
public interface CdrGprsService extends IService<CdrGprs> {

    List<CdrGprs> select(LambdaQueryWrapper<CdrGprs> queryWrapper);

    void insertCdr(List<CdrGprs> cdrGprsList);

    List<QueryUserCdrInfoDto> mulSelectCdrInfo(IPage<QueryUserCdrInfoDto> page,List<String> tableNameList, String msisdn, Date startDate, Date endDate, String xdrType);

    //Integer countMulSelectCdr(List<String> tableNameList, String msisdn, Date startDate, Date endDate, String xdrType) ;

    Map<String, String> checkTableExistsWithShow(String tableName);

    int createTableByDate(String cdrDate, String format);

    List<CdrGprs> cdrReratSelect(String sqlSegment);

    IPage<QueryCdrGprsDto> reratQueryCdr(IPage<QueryCdrGprsDto> page, QueryWrapper<QueryCdrGprsDto> queryWrapper);
}
