package com.cmit.mvne.billing.settle.task;

import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateTableScheduleTaskTest {

    @Autowired
    private CreateTableScheduleTask createTableScheduleTask;

    @Test
    public void createTable() throws IOException, TemplateException {
        createTableScheduleTask.createTable(2020);
    }
}