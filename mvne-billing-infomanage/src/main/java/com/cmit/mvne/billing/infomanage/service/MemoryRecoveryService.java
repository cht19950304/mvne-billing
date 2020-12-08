package com.cmit.mvne.billing.infomanage.service;

import java.text.ParseException;

public interface MemoryRecoveryService {
    void memoryRecoveryInterface(String tableName,String startTime,String endTime) throws ParseException;
}
