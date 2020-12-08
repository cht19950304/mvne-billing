package com.cmit.mvne.billing.settle.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SettleSiminnSumScheduleTaskTest {

    @Autowired
    private SettleSiminnSumScheduleTask settleSiminnSumScheduleTask;

    @Test
    public void sumGprsByMonth() {
        settleSiminnSumScheduleTask.sumByMonth();
    }
}