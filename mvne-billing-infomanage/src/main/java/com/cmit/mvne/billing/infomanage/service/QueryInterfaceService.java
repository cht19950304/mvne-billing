package com.cmit.mvne.billing.infomanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.ExportUserCdrInfo;
import com.cmit.mvne.billing.infomanage.entity.QueryUserCdrInfo;
import com.cmit.mvne.billing.infomanage.entity.QueryUserInfo;
import com.cmit.mvne.billing.infomanage.entity.QueryUserOfferInfo;
import com.cmit.mvne.billing.user.analysis.entity.QueryUserCdrInfoDto;

import java.text.ParseException;
import java.util.List;

public interface QueryInterfaceService {
    QueryUserInfo queryUserInfo(String msisdn,String userId) throws MvneException;
    QueryUserOfferInfo queryUserOfferInfo(String userId) throws MvneException;
    IPage<QueryUserCdrInfo> queryUserCdrInfo(String msisdn, Long startDate, Long endDate, String xdrType, Integer page, Integer size) throws MvneException, ParseException;
    List<ExportUserCdrInfo> exportDetailCdr(String msisdn, Long startDate, Long endDate, String xdrType, Integer page, Integer size) throws MvneException, ParseException;

}
