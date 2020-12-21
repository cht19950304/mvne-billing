package com.cmit.mvne.billing.settle.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.settle.common.ServerResponse;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumDay;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumMonth;
import com.cmit.mvne.billing.settle.entity.dto.SiminnSumAmountDTO;
import com.cmit.mvne.billing.settle.entity.dto.SiminnSumMonthFeeDTO;
import com.cmit.mvne.billing.settle.service.SettleSiminnSumService;
import com.cmit.mvne.billing.settle.util.DateTimeUtil;
import com.cmit.mvne.billing.settle.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * 结算汇总结果查询接口
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/24
 */
@RestController
@RequestMapping("settle")
@Slf4j
public class SettleSiminnSumController {

    @Autowired
    private SettleSiminnSumService settleSiminnSumService;

    @GetMapping("siminnSumFee")
    public ServerResponse<IPage<SiminnSumMonthFeeDTO>> getSettleSiminnSumFee(@RequestParam String startMonth, @RequestParam String endMonth,@RequestParam Integer page, @RequestParam Integer size) {
        log.info("Invoking /settle/siminnSumFee interface ,params include startMonth:{}, endMonth{}, page:{}, size:{}", startMonth, endMonth,page,size);
        List<SiminnSumMonthFeeDTO> siminnSumMonthFeeDTOList = settleSiminnSumService.getSettleSiminnSumFee(startMonth, endMonth);
        //分页输出
        PageUtil<SiminnSumMonthFeeDTO> pageUtil = new PageUtil<>();
        List<SiminnSumMonthFeeDTO> siminnSumMonthFeeDTOPageList = pageUtil.startPage(siminnSumMonthFeeDTOList,page,size);
        IPage<SiminnSumMonthFeeDTO> siminnSumMonthFeeDTOPage = new Page<>(page,size,siminnSumMonthFeeDTOList.size());
        siminnSumMonthFeeDTOPage.setRecords(siminnSumMonthFeeDTOPageList);
        return ServerResponse.createSuccess(siminnSumMonthFeeDTOPage);
        //return ServerResponse.createSuccess(siminnSumMonthFeeDTOList);
    }

    @GetMapping("siminnSumFee/excel")
    public void downloadSettleSiminnSumFeeExcel(@RequestParam String startMonth, @RequestParam String endMonth, HttpServletResponse response) throws IOException {
        log.info("Invoking /settle/siminnSumFee/excel interface ,params include startMonth:{}, endMonth{}", startMonth, endMonth);
        List<SiminnSumMonthFeeDTO> siminnSumMonthFeeDTOList = settleSiminnSumService.getSettleSiminnSumFee(startMonth, endMonth);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String filename = URLEncoder.encode(startMonth.equals(endMonth)?("Siminn_Sum_Fee_"+startMonth):("siminn_sum_fee_"+ startMonth + "_" + endMonth), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

        EasyExcel.write(response.getOutputStream(), SiminnSumMonthFeeDTO.class).sheet("sheet1").doWrite(siminnSumMonthFeeDTOList);
    }

    @GetMapping("siminnSumAmount")
    public ServerResponse<IPage<SiminnSumAmountDTO>> getSettleSiminnSumAmount(@RequestParam String startMonth, @RequestParam String endMonth, @RequestParam Integer page, @RequestParam Integer size) {
        log.info("Invoking /settle/siminnSumAmount interface, params include startMonth:{}, endMonth:{}", startMonth, endMonth);
        List<SiminnSumAmountDTO> siminnSumAmountDTOList = settleSiminnSumService.getSettleSiminnSumMonth(startMonth, endMonth);
        //分页输出
        PageUtil<SiminnSumAmountDTO> pageUtil = new PageUtil<>();
        List<SiminnSumAmountDTO>  siminnSumAmountDTOPageList = pageUtil.startPage(siminnSumAmountDTOList,page,size);
        IPage<SiminnSumAmountDTO> siminnSumAmountDTOPage = new Page<>(page,size,siminnSumAmountDTOList.size());
        siminnSumAmountDTOPage.setRecords(siminnSumAmountDTOPageList);
        return ServerResponse.createSuccess(siminnSumAmountDTOPage);
    }

    @GetMapping("siminnSumAmount/excel")
    public void downloadSettleSiminnSumAmountExcel(@RequestParam String startMonth, @RequestParam String endMonth, HttpServletResponse response) throws IOException {

        log.info("Invoking /settle/siminnSumAmount/excel interface, params include startMonth:{}, endMonth:{}", startMonth, endMonth);
        List<SiminnSumAmountDTO> siminnSumAmountDTOList = settleSiminnSumService.getSettleSiminnSumMonth(startMonth, endMonth);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String filename = URLEncoder.encode(startMonth.equals(endMonth)?("Siminn_Sum_Amount _"+startMonth):("siminn_sum_amount_"+ startMonth + "_" + endMonth), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

        EasyExcel.write(response.getOutputStream(), SiminnSumAmountDTO.class).sheet("sheet1").doWrite(siminnSumAmountDTOList);
    }

    @GetMapping("siminnSumDayAmount")
    public ServerResponse<IPage<SiminnSumAmountDTO>> getSettleSiminnSumDayAmount(@RequestParam Long startTime, @RequestParam Long endTime, @RequestParam Integer page, @RequestParam Integer size) {
        log.info("Invoking /settle/siminnSumDayAmount interface, params include startTime:{}, endTime:{}", startTime, endTime);
        List<SiminnSumAmountDTO> siminnSumAmountDTOList = settleSiminnSumService.getSettleSiminnSumDay(startTime, endTime);

        if ( page == 0 && size == 0 ){
            IPage<SiminnSumAmountDTO> siminnSumAmountDTOPage = new Page<>(1,siminnSumAmountDTOList.size(),siminnSumAmountDTOList.size());
            siminnSumAmountDTOPage.setRecords(siminnSumAmountDTOList);
            return ServerResponse.createSuccess(siminnSumAmountDTOPage);
        }else {
            //分页输出
            PageUtil<SiminnSumAmountDTO> pageUtil = new PageUtil<>();
            List<SiminnSumAmountDTO> siminnSumAmountDTOPageList = pageUtil.startPage(siminnSumAmountDTOList,page,size);
            IPage<SiminnSumAmountDTO> siminnSumAmountDTOPage = new Page<>(page,size,siminnSumAmountDTOList.size());
            siminnSumAmountDTOPage.setRecords(siminnSumAmountDTOPageList);
            return ServerResponse.createSuccess(siminnSumAmountDTOPage);
        }

    }

    @GetMapping("siminnSumDayAmount/excel")
    public void downloadSettleSiminnSumDayAmountExcel(@RequestParam Long startTime, @RequestParam Long endTime, HttpServletResponse response) throws IOException {
        log.info("Invoking /settle/siminnSumDayAmount/excel interface, params include startTime:{}, endTime:{}", startTime, endTime);
        List<SiminnSumAmountDTO> siminnSumAmountDTOList = settleSiminnSumService.getSettleSiminnSumDay(startTime, endTime);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String filename = URLEncoder.encode(startTime.equals(endTime)?("Siminn_Sum_Amount _"+ DateTimeUtil.getDateTimeofTimestamp(startTime).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                :("siminn_sum_amount_"+ DateTimeUtil.getDateTimeofTimestamp(startTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "_" + DateTimeUtil.getDateTimeofTimestamp(endTime).format(DateTimeFormatter.ofPattern("yyyyMMdd"))), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

        EasyExcel.write(response.getOutputStream(), SiminnSumAmountDTO.class).sheet("sheet1").doWrite(siminnSumAmountDTOList);
    }

    @GetMapping("sumByDay")
    public ServerResponse<List<SettleSiminnSumDay>> sumByDay(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Date day) {
        log.info("Invoking /settle/sumByDay interface, params:{}", day);
        LocalDateTime localDateTime = DateTimeUtil.dateToLocalDateTime(day);
        String invoicingPeriod = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
        String yyyyMMdd = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<SettleSiminnSumDay> settleSiminnSumDayList = settleSiminnSumService.sumByDay(invoicingPeriod, yyyyMMdd);
        return ServerResponse.createSuccess(settleSiminnSumDayList);
    }

    @GetMapping("sumByMonth")
    public ServerResponse<List<SettleSiminnSumMonth>> sumByMonth(@RequestParam @DateTimeFormat(pattern = "yyyyMM") Date month) {
        log.info("Invoking /settle/sumByMonth interface, params:{}", month);
        LocalDateTime localDateTime = DateTimeUtil.dateToLocalDateTime(month);
        String invoicingPeriod = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<SettleSiminnSumMonth> siminnSumMonthList = settleSiminnSumService.sumByMonth(invoicingPeriod);
        return ServerResponse.createSuccess(siminnSumMonthList);
    }
}
