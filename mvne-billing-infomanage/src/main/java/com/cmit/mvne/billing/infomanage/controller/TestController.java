package com.cmit.mvne.billing.infomanage.controller;

import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.CmProdInsInfo;
import com.cmit.mvne.billing.user.analysis.entity.CmUserDetail;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.service.IOrdOrderService;
//import com.cmit.mvne.billing.infomanage.service.InfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2019/11/27 9:16
 */

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private DistributeLock distributeLock;

/*    @Autowired
    private InfoManageService infoManageService;*/

    @Autowired
    private IOrdOrderService iOrdOrderService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "/serialize", method = RequestMethod.POST)
    CmUserDetail testSerialize() {
        Object userObject = redisTemplate.opsForList().index("UserDetail:1705", 0);
        Object prodObject = redisTemplate.opsForList().index("ProdInsInfo:400", 0);
            CmUserDetail cmUserDetail = new CmUserDetail(userObject.toString());
            CmProdInsInfo cmProdInsInfo = new CmProdInsInfo(prodObject.toString());

            return cmUserDetail;

    }

/*    @RequestMapping(value = "/scan", method = RequestMethod.POST)
    boolean testScan() {
        try {
            infoManageService.scanTable();
        } catch (MvneException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }*/

/*    @RequestMapping(value = "/thread", method = RequestMethod.POST)
    boolean testThread() {
        infoManageService.syncInfo();

        return true;
    }*/

    @RequestMapping(value = "mapper", method = RequestMethod.POST)
    List<IOrdOrder> testMapper() {

        List<IOrdOrder> lastMinuteOrder = iOrdOrderService.scanLastMinuteOrder();

        return lastMinuteOrder;
    }

    @RequestMapping(value = "lock", method = RequestMethod.POST)
    void lock() {
        try {
            distributeLock.fairLock("InfoManageKey:200", TimeUnit.SECONDS, 1, 60);
        } catch (MvneException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "tryLock", method = RequestMethod.POST)
    boolean tryLock() {
        try {
            return distributeLock.fairLock("InfoManageKey:200", TimeUnit.SECONDS, 1, 30);
        } catch (MvneException e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping(value = "hello",method = RequestMethod.GET)
    String test(){
            return "Hello";
    }

}
