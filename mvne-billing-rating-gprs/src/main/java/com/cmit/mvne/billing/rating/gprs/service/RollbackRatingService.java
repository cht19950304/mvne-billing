package com.cmit.mvne.billing.rating.gprs.service;

import com.cmit.mvne.billing.user.analysis.entity.RatingCdrGprsRerat;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/14 17:32
 */
public interface RollbackRatingService {
    List<RatingCdrGprsRerat> rollbackGprs(List<RatingCdrGprsRerat> ratingCdrGprsReratList, HashMap<String, Integer> resultMap) throws InterruptedException;
}
