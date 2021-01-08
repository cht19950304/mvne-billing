package com.cmit.mvne.billing.rating.gprs.service;


import com.cmit.mvne.billing.user.analysis.common.MvneException;

import java.util.Queue;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/1/2 16:34
 */
public interface ProcessCdrService {
    void dealCdr(String cdr, Queue<String> waitQueue) throws MvneException, InterruptedException;
}
