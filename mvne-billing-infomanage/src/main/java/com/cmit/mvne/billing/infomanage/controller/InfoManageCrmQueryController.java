package com.cmit.mvne.billing.infomanage.controller;

import com.alibaba.excel.EasyExcel;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @RequestMapping(value = "queryUserCdrInfo/{msisdn}/{startDate}/{endDate}/{xdrType}/{page}/{size}",method = RequestMethod.GET)
    public MvneInfoManageResponse queryUserCdrInfo(@PathVariable("msisdn") @NotNull String msisdn,@PathVariable("startDate") @NotNull Long startDate,@PathVariable("endDate") @NotNull Long endDate,@PathVariable("xdrType") @NotNull String xdrType,@PathVariable("page") @NotNull Integer page,@PathVariable("size") @NotNull Integer size)
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

        EasyExcel.write(response.getOutputStream(), ExportUserCdrInfo.class).sheet("sheet1").doWrite(exportDetailCdrList);
    }

}
