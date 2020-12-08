package com.cmit.mvne.billing.preparation.service;

import com.cmit.mvne.billing.preparation.service.impl.CdrDupCheckServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/8/24 14:31
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CdrDupCheckServiceImpl.class})
public class CdrDupCheckServiceTest {
    @Autowired
    private CdrDupCheckServiceImpl cdrDupCheckService;

    @Test
    public void checkDup() {
        Assert.assertEquals(cdrDupCheckService.dupOrNot(false, false, false, 1), false);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(true, false, false, 0), true);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(false, true, false, 0), false);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(true, false, false, 0), true);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(false, true, false, 0), false);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(false, false, false, 0), true);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(true, false, false, 0), true);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(true, true, true, 0), true);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(false, true, true, 0), true);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(false, true, true, 0), true);
        Assert.assertEquals(cdrDupCheckService.dupOrNot(false, true, true, 0), true);
    }
}
