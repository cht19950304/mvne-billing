package com.cmit.mvne.billing.settle.service.impl;

import com.cmit.mvne.billing.settle.service.SettleSiminnSumService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SettleSiminnSumServiceImplTest {

    @Autowired
    SettleSiminnSumService settleSiminnSumService;

    @Test
    public void sumByMonth() {

        settleSiminnSumService.sumByMonth("202005");
    }
}