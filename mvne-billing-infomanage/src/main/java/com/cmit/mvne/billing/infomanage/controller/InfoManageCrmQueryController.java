package com.cmit.mvne.billing.infomanage.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.common.MvneInfoManageResponse;
import com.cmit.mvne.billing.infomanage.entity.*;
import com.cmit.mvne.billing.infomanage.service.QueryInterfaceService;
import com.cmit.mvne.billing.infomanage.util.DateTimeUtil;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.entity.QueryUserCdrInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/infoManage")
public class InfoManageCrmQueryController {

    @Autowired
    private QueryInterfaceService queryInterfaceService;

    @RequestMapping(value = "queryUserInfo/{msisdn}/{userId}" , method = RequestMethod.GET)
    public MvneInfoManageResponse queryUserInfo(@PathVariable("msisdn") @NotNull String msisdn, @PathVariable("userId") @NotNull String userId )
    {
        QueryUserInfo queryUserInfo ;
        try{
            queryUserInfo = queryInterfaceService.queryUserInfo(msisdn,userId);
        }catch (MvneException e)
        {
            log.error("InfoManageCrmQueryController-queryUserInfo is fail! : {}",StringUtils.getExceptionText(e));
            //return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
            return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
        }
        return  new MvneInfoManageResponse().success().data(queryUserInfo.toJsonString());
    }

    @RequestMapping(value = "queryUserOfferInfo/{userId}",method = RequestMethod.GET)
    public MvneInfoManageResponse queryUserOfferInfo(@PathVariable("userId") @NotNull String userId)
    {
        QueryUserOfferInfo queryUserOfferInfo ;
        try {
            queryUserOfferInfo =  queryInterfaceService.queryUserOfferInfo(userId);
        }catch (MvneException e)
        {
            log.error("InfoManageCrmQueryController-queryUserOfferInfo is fail! : {}",StringUtils.getExceptionText(e));
            //return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
            return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
        }

        return  new MvneInfoManageResponse().success().data(queryUserOfferInfo.toJsonString());
    }

    @RequestMapping(value = "queryUserProductInfo/{msisdn}",method = RequestMethod.GET)
    public MvneInfoManageResponse queryUserProductInfo(@PathVariable("msisdn") @NotNull String msisdn)
    {
        QueryUserProductInfo queryUserProductInfo ;
        try {
            queryUserProductInfo =  queryInterfaceService.queryUserProductInfo(msisdn);
        }catch (MvneException e)
        {
            log.error("InfoManageCrmQueryController-queryUserProductInfo is fail! : {}",StringUtils.getExceptionText(e));
            //return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
            return new MvneInfoManageResponse().servfail(e.getErrCode()).message(e.getErrDesc());
        }

        return  new MvneInfoManageResponse().success().data(queryUserProductInfo);
    }

    @RequestMapping(value = "queryUserCdrInfo/{msisdn}/{startDate}/{endDate}/{xdrType}/{page}/{size}",method = RequestMethod.GET)
    public MvneInfoManageResponse queryUserCdrInfo(@PathVariable("msisdn")  @NotNull String msisdn,@PathVariable("startDate") @NotNull Long startDate,@PathVariable("endDate") @NotNull Long endDate,@PathVariable("xdrType") @NotNull String xdrType,@PathVariable("page") @NotNull Integer page,@PathVariable("size") @NotNull Integer size)
//    public MvneInfoManageResponse queryUserCdrInfo(@RequestParam("msisdn")  @NotNull String msisdn,@RequestParam("startDate") @NotNull Long startDate,@RequestParam("endDate") @NotNull Long endDate,@RequestParam("xdrType") @NotNull String xdrType,@RequestParam("page") @NotNull Integer page,@RequestParam("size") @NotNull Integer size)
    {
        IPage<QueryUserCdrInfo> queryUserCdrInfo ;
        try {
            queryUserCdrInfo = queryInterfaceService.queryUserCdrInfo(msisdn,startDate,endDate,xdrType,page,size);
        }catch (MvneException | ParseException e)
        {
            log.error("InfoManageCrmQueryController-queryUserCdrInfo is fail! : {}",StringUtils.getExceptionText(e));
            //return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
            if ( e instanceof MvneException) {
                return new MvneInfoManageResponse().servfail(((MvneException) e).getErrCode()).message( ((MvneException) e).getErrDesc());
            }else {
                return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }
        return  new MvneInfoManageResponse().success().data(queryUserCdrInfo);
    }

    @RequestMapping(value = "queryUserCdrMonthInfo/{msisdn}/{date}/{xdrType}",method = RequestMethod.GET)
    public MvneInfoManageResponse queryUserMonthCdrInfo(@PathVariable("msisdn") @NotNull String msisdn,@PathVariable("date") @NotNull Long date,@PathVariable("xdrType") @NotNull String xdrType)
    {
        List<QueryUserMonthCdrInfo> queryUserMonthCdrInfoList;
        try {
            queryUserMonthCdrInfoList = queryInterfaceService.queryUserMonthCdrInfo(msisdn,date,xdrType);
        }catch (MvneException | ParseException e)
        {
            log.error("InfoManageCrmQueryController-queryUserMonthCdrInfo is fail! : {}",StringUtils.getExceptionText(e));
            //return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
            if ( e instanceof MvneException) {
                return new MvneInfoManageResponse().servfail(((MvneException) e).getErrCode()).message( ((MvneException) e).getErrDesc());
            }else {
                return new MvneInfoManageResponse().fail().message(StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }
        return  new MvneInfoManageResponse().success().data(queryUserMonthCdrInfoList);
    }



    @RequestMapping(value = "exportDetailCdr/{msisdn}/{startDate}/{endDate}/{xdrType}/{page}/{size}",method = RequestMethod.GET)
    public void exportUserCdrInfoExcel(@PathVariable("msisdn") @NotNull String msisdn, @PathVariable("startDate") @NotNull Long startDate, @PathVariable("endDate") @NotNull Long endDate, @PathVariable("xdrType") @NotNull String xdrType, @PathVariable("page") @NotNull Integer page, @PathVariable("size") @NotNull Integer size, HttpServletResponse response) throws IOException, MvneException, ParseException {
        log.info("Invoke interface exportDetailCdr msisdn is {} , startDate is {} , endDate is {} , xdrType is {} , page is {} , size is {}",msisdn,startDate,endDate,xdrType,page,size);

        List<ExportUserCdrInfo> exportDetailCdrList  = queryInterfaceService.exportDetailCdr(msisdn,startDate,endDate,xdrType,page,size);

        String excelFilename = URLEncoder.encode(startDate.equals(endDate)?("Detail_Cdrs_"+ DateTimeUtil.getDateTimeofTimestamp(startDate).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                :("Detail_Cdrs_"+ DateTimeUtil.getDateTimeofTimestamp(startDate).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "_" + DateTimeUtil.getDateTimeofTimestamp(endDate).format(DateTimeFormatter.ofPattern("yyyyMMdd"))), "UTF-8");;
        log.info("excelFilename is {}",excelFilename);
        response.setHeader("Content-disposition", "attachment;filename=" + excelFilename + ".xlsx");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

//        //单元格宽度默认
//        EasyExcel.write(response.getOutputStream(), ExportUserCdrInfo.class).sheet("sheet1").doWrite(exportDetailCdrList);

        //单元格宽度自动适应数据长度
        EasyExcel.write(response.getOutputStream(), ExportUserCdrInfo.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1").doWrite(exportDetailCdrList);
    }

    @RequestMapping("monthCdrSchedule")
    public String monthCdrSchedule(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            queryInterfaceService.monthCdrSchedule(sdf.parse("2020-06-21"));
            return "success";
        } catch (MvneException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("hello")
    public String test(){
        return "hello";
    }

}
