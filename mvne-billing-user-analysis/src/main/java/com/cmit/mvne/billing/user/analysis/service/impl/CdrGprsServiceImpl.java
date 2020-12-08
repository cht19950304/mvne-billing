package com.cmit.mvne.billing.user.analysis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.QueryUserCdrInfoDto;
import com.cmit.mvne.billing.user.analysis.mapper.CdrGprsMapper;
import com.cmit.mvne.billing.user.analysis.service.CdrGprsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author caikc
 * @author zengxf
 * @since 2020/3/2
 */
@Slf4j
@Service
public class CdrGprsServiceImpl extends ServiceImpl<CdrGprsMapper, CdrGprs> implements CdrGprsService {
    @Autowired
    CdrGprsMapper cdrGprsMapper;

    @Override
    public List<CdrGprs> select(LambdaQueryWrapper<CdrGprs> queryWrapper) {
        return cdrGprsMapper.selectList(queryWrapper);
    }

    @Override
    public void insertCdr(List<CdrGprs> cdrGprsList) {

    }

    @Override
    public List<QueryUserCdrInfoDto> mulSelectCdrInfo(IPage<QueryUserCdrInfoDto> page,List<String> tableNameList, String msisdn, Date startDate, Date endDate, String xdrType) {
        return cdrGprsMapper.mulSelectCdrInfo(page,tableNameList,msisdn,startDate,endDate,xdrType);
    }

/*    @Override
    public Integer countMulSelectCdr(List<String> tableNameList, String msisdn, Date startDate, Date endDate, String xdrType) {
        return cdrGprsMapper.countMulSelectCdr(tableNameList,msisdn,startDate,endDate,xdrType);
    }*/


    @Override
    public Map<String, String> checkTableExistsWithShow(String cdrDate) {
        Map<String, String> tableMap = cdrGprsMapper.checkTableExistsWithShow(cdrDate);
        return tableMap;
    }

    @Override
    public int createTableByDate(String cdrDate, String format) {
        return cdrGprsMapper.createTableByDate(cdrDate, format);
    }

    @Override
    public List<CdrGprs> cdrReratSelect(String sqlSegment) {
        return cdrGprsMapper.cdrSelect(sqlSegment);
    }

    @Override
    public IPage<QueryCdrGprsDto> reratQueryCdr(IPage<QueryCdrGprsDto> page, QueryWrapper<QueryCdrGprsDto> queryWrapper) {
        return cdrGprsMapper.reratQueryCdr(page, queryWrapper);
    }
}

