package com.cmit.mvne.billing.preparation.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.preparation.common.MvneCrmResponse;
import com.cmit.mvne.billing.preparation.common.ServerResponse;
import com.cmit.mvne.billing.preparation.entity.dto.RejectedDTO;
import com.cmit.mvne.billing.preparation.service.ReProcessRejectedService;
import com.cmit.mvne.billing.preparation.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 预处理重处理controller，包括查询、重处理
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/29
 */
@Slf4j
@RestController
@RequestMapping("/preparation")
public class ReprocessRejectedController {

    @Autowired
    private ReProcessRejectedService reProcessRejectedService;

    @GetMapping("/rejected")
    public MvneCrmResponse rejectedCdrs(@RequestParam(required = false) String filename, @RequestParam String errorType,
                                                            @RequestParam(required = false) String errorCode, @RequestParam(required = false) String status,
                                                            @RequestParam(required = false) Long startTime, @RequestParam(required = false) Long endTime,
                                                            @PageableDefault Pageable pageable) throws IOException {
        log.info("Invoke interface /preparation/rejected, params: filename:{}, errorType:{}, errorCode:{}, status:{}, startTime:{}, endTime:{}",
                filename, errorType, errorCode, status, startTime, endTime);
        if (status.equals("All") || status.equals("all")) {
            status = "";
        }
        if ("Severe".equals(errorType)) {
            IPage<RejectedDTO> rejectedCdrs = reProcessRejectedService.getRejectedCdrsPage(filename, errorCode, status, startTime, endTime, pageable);
            return new MvneCrmResponse().success().data(rejectedCdrs);
        }else if ("Fatal".equals(errorType)) {
            IPage<RejectedDTO> rejectedFiles = reProcessRejectedService.getRejectedFilesPage(filename, errorCode, status, startTime, endTime, pageable);
            return new MvneCrmResponse().success().data(rejectedFiles);
        } else {
            return new MvneCrmResponse().fail().message("Error type is illegal!");
        }

    }

    @GetMapping("/rejected/excel")
    public void rejectedCdrs(@RequestParam(required = false) String filename, @RequestParam String errorType,
                                                           @RequestParam(required = false) String errorCode, @RequestParam(required = false) String status,
                                                           @RequestParam(required = false) Long startTime, @RequestParam(required = false) Long endTime, @PageableDefault Pageable pageable, HttpServletResponse response) throws IOException {
        log.info("Invoke interface /preparation/rejected/excel, params: filename:{}, errorType:{}, errorCode:{}, status:{}, startTime:{}, endTime:{}",
                filename, errorType, errorCode, status, startTime, endTime);
        List<RejectedDTO> rejectedDTOList = null;

        String excelFilename = null;
        if ("Severe".equals(errorType)) {
            rejectedDTOList = reProcessRejectedService.getRejectedCdrsPage(filename, errorCode, status, startTime, endTime, pageable).getRecords();

            if(startTime == null || endTime == null) {
                excelFilename = "Rejected_Cdrs";
            } else {
                excelFilename = URLEncoder.encode(startTime.equals(endTime)?("Rejected_Cdrs_"+ DateTimeUtil.getDateTimeofTimestamp(startTime).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        :("Rejected_Cdrs_"+ DateTimeUtil.getDateTimeofTimestamp(startTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                        + "_" + DateTimeUtil.getDateTimeofTimestamp(endTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"))), "UTF-8");
            }
        }else if ("Fatal".equals(errorType)) {
            rejectedDTOList = reProcessRejectedService.getRejectedFilesPage(filename, errorCode, status, startTime, endTime, pageable).getRecords();
            if(startTime == null || endTime == null) {
                excelFilename = "Rejected_Files";
            } else {
                excelFilename = URLEncoder.encode(startTime.equals(endTime)?("Rejected_Files_"+ DateTimeUtil.getDateTimeofTimestamp(startTime).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        :("Rejected_Files_"+ DateTimeUtil.getDateTimeofTimestamp(startTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                        + "_" + DateTimeUtil.getDateTimeofTimestamp(endTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"))), "UTF-8");
            }
        } else {
            throw new RuntimeException("Error type is illegal!");
        }

        response.setHeader("Content-disposition", "attachment;filename=" + excelFilename + ".xlsx");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        EasyExcel.write(response.getOutputStream(), RejectedDTO.class).sheet("sheet1").doWrite(rejectedDTOList);

    }

    /**
     * 重处理接口
     * @param idList
     * @return
     */
    @PostMapping("/rejected/reprocess")
    public MvneCrmResponse rejectedCdrsReprocess(@Valid @RequestBody List<Long> idList) {

        log.info("Invoke interface /preparation/rejected, params: {}", idList);
        reProcessRejectedService.reprocessRejectedCdrs(idList);
        return new MvneCrmResponse().success();
    }
}
