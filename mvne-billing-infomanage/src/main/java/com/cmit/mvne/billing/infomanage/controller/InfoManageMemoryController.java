package com.cmit.mvne.billing.infomanage.controller;

import com.cmit.mvne.billing.infomanage.service.MemoryRecoveryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/infoManage")
public class InfoManageMemoryController {

    @Autowired
    private MemoryRecoveryService memoryRecoveryService;

    @RequestMapping(value = "memoryRecovery/{tableName}/{startTime}/{endTime}",method = RequestMethod.GET)
    public void memoryRecovery(@PathVariable("tableName") String tableName,@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime)
    {
        try {
            memoryRecoveryService.memoryRecoveryInterface(tableName,startTime,endTime);
        }catch (Exception e)
        {
            log.error("InfoManageMemoryController-memoryRecovery fail! error message:{}",e.getMessage());
        }
    }
}
