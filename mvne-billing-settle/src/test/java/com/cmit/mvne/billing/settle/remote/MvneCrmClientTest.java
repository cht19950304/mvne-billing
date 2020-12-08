package com.cmit.mvne.billing.settle.remote;

import com.cmit.mvne.billing.settle.common.MvneCrmResponse;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MvneCrmClientTest {

    @Autowired
    private MvneCrmClient mvneCrmClient;

    @Test
    public void queryCreateUserOrders() {

        MvneCrmResponse mvneCrmResponse = mvneCrmClient.queryCreateUserOrdersByMonth("202005");
        TestCase.assertEquals(0, mvneCrmResponse.get("data"));
    }
}