package com.cmit.mvne.billing.rating.gprs.service;

import com.cmit.mvne.billing.rating.gprs.common.CreditCallInfo;
import com.cmit.mvne.billing.rating.gprs.common.RatingError;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprsError;

import java.util.HashMap;
import java.util.List;

/**
 * @author caikunchi
 * @since 2020/1/19 15:08
 */
public interface GprsRatingService {


    /**
     * 对单条话单批价
     * @param cdrGprs 话单
     * @param successList 成功话单列表
     * @param errorList 失败话单列表
     * @throws MvneException
     * @throws InterruptedException
     */
    RatingError ratingGprs(CdrGprs cdrGprs, HashMap<String, List<CdrGprs>> successList, List<CdrGprsError> errorList, List<CreditCallInfo> creditCallInfoList, String readFlag) throws MvneException, InterruptedException;
}
