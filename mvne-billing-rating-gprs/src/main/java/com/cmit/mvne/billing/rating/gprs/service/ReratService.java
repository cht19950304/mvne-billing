package com.cmit.mvne.billing.rating.gprs.service;

import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.RatingCdrGprsRerat;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/18 11:02
 */
public interface ReratService {
    public HashMap<String, Integer> reratGprs(String date, List<RatingCdrGprsRerat> cdrGprsListAfterRollback) throws MvneException, InterruptedException;

    //public Boolean ratGprs(List<CdrGprs> cdrGprsList) throws MvneException, InterruptedException;

}
