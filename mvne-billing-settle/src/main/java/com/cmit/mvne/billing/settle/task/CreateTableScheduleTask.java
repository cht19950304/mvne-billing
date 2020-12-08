package com.cmit.mvne.billing.settle.task;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 结算详单表创建定时任务
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/18
 */
@Slf4j
@Component
public class CreateTableScheduleTask {

    @Autowired
    JdbcOperations jdbcOperations;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;

    @PostConstruct
    public void init() {
        Configuration cfg = new Configuration(Configuration.getVersion());
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "template/");
        freeMarkerConfigurer.setConfiguration(cfg);
    }

    /**
     * 每年12月31日建下一年的结算详单表
     */
    @Scheduled(cron = "${settle.createTableCron}")
    public void createTable() throws IOException, TemplateException {

        log.info("Starting crontab to create tables!");

        String templateName = "settletable.ftl";
        Template template = null;
        template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);

        int nextYear = LocalDate.now().plusYears(1).getYear();
        List<String> dayListOfYear = getDayListOfYear(nextYear);
        Map<String, String> dayMap = new HashMap<>();
        List<String> ddlList = new ArrayList<>();
        for(String day : dayListOfYear) {
            dayMap.put("yyyyMMdd", day);
            String ddls = FreeMarkerTemplateUtils.processTemplateIntoString(template, dayMap);
            String[] ddlArray = ddls.split(";");
            Collections.addAll(ddlList, ddlArray);
        }

        log.info("Executing batch sql : {}", ddlList);

        jdbcOperations.batchUpdate(ddlList.toArray(new String[0]));
    }

    public void createTable(int year) throws IOException, TemplateException {

        String templateName = "settletable.ftl";
        Template template = null;
        template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);

        List<String> dayListOfYear = getDayListOfYear(year);
        Map<String, String> dayMap = new HashMap<>();
        List<String> ddlList = new ArrayList<>();
        for(String day : dayListOfYear) {
            dayMap.put("yyyyMMdd", day);
            String ddls = FreeMarkerTemplateUtils.processTemplateIntoString(template, dayMap);
            String[] ddlArray = ddls.split(";");
            Collections.addAll(ddlList, ddlArray);
        }
        jdbcOperations.batchUpdate(ddlList.toArray(new String[0]));
    }

    private List<String> getDayListOfYear(int year) {
        List<String> dayList = new ArrayList<>();
        LocalDate localDateStart = LocalDate.of(year, 1, 1);
        LocalDate localDateEnd = LocalDate.of(year, 12, 31);
        do {
            dayList.add(localDateStart.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            localDateStart = localDateStart.plusDays(1);
        } while (!localDateStart.isAfter(localDateEnd));

        return dayList;
    }


    public static void main(String[] args) {
////        System.out.println(LocalDate.now().plusYears(1).getYear());
//        List<String> dayListOfYear = getDayListOfYear(2020);
//
//        for(String day : dayListOfYear) {
//            System.out.println(day);
//        }

    }

}
