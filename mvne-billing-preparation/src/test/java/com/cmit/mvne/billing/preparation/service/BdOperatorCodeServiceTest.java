package com.cmit.mvne.billing.preparation.service;

import com.cmit.mvne.billing.preparation.entity.BdOperatorCode;
//import jdk.jfr.events.FileReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/15
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BdOperatorCodeServiceTest {

    @Autowired
    private BdOperatorCodeService operatorCodeService;

    @Test
    public void batchSaveBdOperatorCode() throws IOException {

        File input0 = new File("D:\\country-0416.csv");
        String line0 = null;
        BufferedReader br0 = new BufferedReader(new InputStreamReader(new FileInputStream(input0), "GB2312"));

        Map<String, String[]> map = new HashMap<>();
        while((line0 = br0.readLine()) != null) {

            String[] split = line0.split(",", -1);
            String[] array = new String[2];
            array[0] = split[1];
            array[1] = split[2];
            map.put(split[0], array);
        }

        File input = new File("D:\\OperatorCode.csv");
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input)));

        List<BdOperatorCode> bdOperatorCodeList = new ArrayList<>();
        while((line = reader.readLine()) != null) {
            BdOperatorCode bdOperatorCode = new BdOperatorCode();
            String[] split = line.split(",", -1);
            bdOperatorCode.setCountryInitials(split[0]);
            String[] result = map.get(split[0]);
            if (null != result) {
                bdOperatorCode.setCountryName(result[0]);
                bdOperatorCode.setCountryNameCn(result[1]);
            }

            bdOperatorCode.setOperatorCode(split[2]);
            bdOperatorCode.setOperatorName(split[1]);


            bdOperatorCodeList.add(bdOperatorCode);
        }
        operatorCodeService.saveBatch(bdOperatorCodeList);
    }

    @Test
    public void findByOperatorCode() {

        List<BdOperatorCode> chntd = operatorCodeService.findByOperatorCode("CHNTD");
//        log.info(chntd.get(0).getCountryName());

    }
}