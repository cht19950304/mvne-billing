package com.cmit.mvne.billing.preparation.controller;

import com.cmit.mvne.billing.preparation.entity.BdOperatorCode;
import com.cmit.mvne.billing.preparation.service.BdOperatorCodeService;
import com.cmit.mvne.billing.preparation.service.CdrDupCheckService;
import com.cmit.mvne.billing.preparation.service.impl.CdrDupCheckServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/8/24 16:24
 */

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private CdrDupCheckService cdrDupCheckService;
    @Autowired
    private BdOperatorCodeService bdOperatorCodeService;

    @GetMapping("/rejected")
    String test() {
        return cdrDupCheckService.dupOrNot(false, false, false, 1).toString().concat("\n")
                .concat(cdrDupCheckService.dupOrNot(true, false, false, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(false, true, false, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(true, false, false, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(false, true, false, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(false, false, false, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(true, false, false, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(true, true, true, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(false, true, true, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(false, true, true, 0).toString()).concat("\n")
                .concat(cdrDupCheckService.dupOrNot(false, true, true, 0).toString());
    }

    @GetMapping("/cache")
    List<BdOperatorCode> cache() {
        List<BdOperatorCode> bdOperatorCodes = bdOperatorCodeService.findByOperatorCode("testcode");
        return bdOperatorCodes;
    }

    @GetMapping("/size")
    void size() {
        
    }
}
