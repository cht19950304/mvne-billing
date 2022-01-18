package com.cmit.mvne.billing.infomanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.*;
import com.cmit.mvne.billing.user.analysis.entity.QueryUserCdrInfoDto;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface QueryInterfaceService {
    QueryUserInfo queryUserInfo(String msisdn,String userId) throws MvneException;
    QueryUserOfferInfo queryUserOfferInfo(String userId) throws MvneException;
    QueryUserProductInfo queryUserProductInfo(String msisdn) throws MvneException;
    IPage<QueryUserCdrInfo> queryUserCdrInfo(String msisdn, Long startDate, Long endDate, String xdrType, Integer page, Integer size) throws MvneException, ParseException;
    List<QueryUserMonthCdrInfo> queryUserMonthCdrInfo(String msisdn, Long date, String xdrType) throws MvneException, ParseException;
    List<ExportUserCdrInfo> exportDetailCdr(String msisdn, Long startDate, Long endDate, String xdrType, Integer page, Integer size) throws MvneException, ParseException;
    void monthCdrSchedule(Date date) throws MvneException;
}
