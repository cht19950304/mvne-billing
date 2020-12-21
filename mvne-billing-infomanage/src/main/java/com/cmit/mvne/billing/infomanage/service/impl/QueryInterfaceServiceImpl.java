package com.cmit.mvne.billing.infomanage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.*;
import com.cmit.mvne.billing.infomanage.service.QueryInterfaceService;
import com.cmit.mvne.billing.infomanage.util.DateTimeUtil;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.mapper.CdrGprsMapper;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class QueryInterfaceServiceImpl implements QueryInterfaceService {

    @Autowired
    private PmProductService pmProductService;
    @Autowired
    private ApsFreeResService apsFreeResService;
    @Autowired
    private CdrGprsService cdrGprsService;
    @Autowired
    private BdOperatorCodeService bdOperatorCodeService;
    @Autowired
    private ApsBalanceFeeService apsBalanceFeeService;
    @Autowired
    private CmProdInsInfoService cmProdInsInfoService;

    @Override
    public QueryUserInfo queryUserInfo(String msisdn, String userId) throws MvneException {
        log.info("QueryInterfaceServiceImpl-queryUserInfo msisdn is {} , userId is {}",msisdn,userId);
        try {
            //获取最新的余额
            BigDecimal balanceFee = new BigDecimal(0);
            ApsBalanceFee apsBalanceFee= apsBalanceFeeService.selectByUserId(Long.valueOf(userId));
            log.info("QueryInterfaceServiceImpl-queryUserInfo apsBalanceFee is {} ",apsBalanceFee);
            if ( apsBalanceFee != null )
            {
                balanceFee = apsBalanceFee.getRemainFee();
                log.info("QueryInterfaceServiceImpl-queryUserInfo select balanceFee is {}",balanceFee);
            }else {
                log.error("QueryInterfaceServiceImpl-queryUserInfo select apsBalanceFee is null , userId is {}",userId);
                throw new MvneException("500","select apsBalanceFee is null !");
            }

           //从用户流量余额,数据库的单位为B，需转换为M内存库查询
           //获取最新的免费资源信息
            BigDecimal remainResourceM = new BigDecimal(0);
            List<ApsFreeRes> apsFreeResList = apsFreeResService.findByUserId(Long.valueOf(userId));
            log.info("QueryInterfaceServiceImpl-queryUserInfo apsFreeResList is {}",apsFreeResList);
            //目前只查找流量
            if ( apsFreeResList.size() != 0 )
            {
                if ( apsFreeResList.size() == 1 ) {
                    BigDecimal amount = apsFreeResList.get(0).getAmount();
                    BigDecimal userValue = apsFreeResList.get(0).getUsedValue();
                    BigDecimal remainB = amount.subtract(userValue);
                    if (remainB.compareTo(BigDecimal.ZERO) == 0) {
                        remainResourceM = new BigDecimal(0);
                    } else {
                        remainResourceM = remainB.divide(new BigDecimal(1048576), 2, BigDecimal.ROUND_UP);
                    }
                }else {
                    log.error("QueryInterfaceServiceImpl-queryUserInfo select apsFreeResList size is {}",apsFreeResList.size());
                    throw new MvneException("500","select apsFreeResList error , select apsFreeResList size more than 1");
                }
                log.info("QueryInterfaceServiceImpl-queryUserInfo select FreeRes remainResourceM is {} ",remainResourceM);

            }else {
                log.info("QueryInterfaceServiceImpl-queryUserInfo apsFreeResList is null , userId is {}",userId );
            }
            QueryUserInfo queryUserInfo = new QueryUserInfo(msisdn, balanceFee.divide(new BigDecimal(100)), remainResourceM);
            log.info("QueryInterfaceServiceImpl-queryUserInfo response queryUserInfo is {}",queryUserInfo);
            return queryUserInfo;
        } catch (Exception e) {
            log.error("QueryInterfaceServiceImpl-queryUserInfo fail! error message is {}",StringUtils.getExceptionText(e));
            //throw new MvneException("500", StringUtils.getExceptionText(e));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            } else {
                throw new MvneException("500",StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }

    }

    @Override
    public QueryUserOfferInfo queryUserOfferInfo(String userId) throws MvneException {
        List<String> productMsgList = new ArrayList<>();
        try {
            //获取最新的余额
            BigDecimal balanceFee = new BigDecimal(0);
            ApsBalanceFee apsBalanceFee= apsBalanceFeeService.selectByUserId(Long.valueOf(userId));
            log.info("QueryInterfaceServiceImpl-queryUserOfferInfo apsBalanceFee is {}",apsBalanceFee);
            if ( apsBalanceFee != null ){
                balanceFee = apsBalanceFee.getRemainFee();
                log.info("QueryInterfaceServiceImpl-queryUserOfferInfo select userId is {} , balanceFee is {}",userId,balanceFee);
            }else {
                log.error("QueryInterfaceServiceImpl-queryUserOfferInfo apsBalanceFee is null , userId is {}",userId);
                throw new MvneException("500","select apsBalanceFee is null !");
            }
            //获取产品信息，通过产品表查询免费资源
            List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoService.findAllByUserId(Long.valueOf(userId));
            log.info("QueryInterfaceServiceImpl-queryUserOfferInfo select userId prodInfo , cmProdInsInfoList is {}",cmProdInsInfoList);
            if ( cmProdInsInfoList.size() > 0 ){
                for (CmProdInsInfo cmProdInsInfo : cmProdInsInfoList) {
                    //获取产品名
                    String productName = pmProductService.selectByProductId(cmProdInsInfo.getProductId());
                    log.info("QueryInterfaceServiceImpl-queryUserOfferInfo select userId prodInfo , cmProdInsInfo is {} , productName is {}",cmProdInsInfo,productName);
                    //获取流量的免费资源
                    List<ApsFreeRes> apsFreeResList = apsFreeResService.findByUserIdProdIns(Long.valueOf(userId),cmProdInsInfo.getProductInsId());
                    log.info("QueryInterfaceServiceImpl-queryUserOfferInfo apsFreeResList is {} ",apsFreeResList);

                    if ( apsFreeResList.size() != 0 ){
                        if ( apsFreeResList.size() == 1 ){
                            //目前只查找流量
                            BigDecimal amountB = apsFreeResList.get(0).getAmount();
                            BigDecimal remainB = amountB.subtract(apsFreeResList.get(0).getUsedValue());
                            BigDecimal amountGB = amountB.divide(new BigDecimal(1073741824),0,BigDecimal.ROUND_HALF_UP);
                            BigDecimal remainMB = new BigDecimal(0);
                            if ( remainB.compareTo(BigDecimal.ZERO) == 0 ){
                                remainMB = new BigDecimal(0);
                            }else {
                                //remainMB = remainB.divide(new BigDecimal(1048576),2,BigDecimal.ROUND_UP);
                                remainMB = remainB.divide(new BigDecimal(1048576),2,BigDecimal.ROUND_DOWN);
                            }
                            Date productOfferDate = apsFreeResList.get(0).getValidDate();
                            Date productExpireDate = apsFreeResList.get(0).getExpireDate();
                            //SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            log.info("QueryInterfaceServiceImpl-queryUserOfferInfo select apsFreeRes amountGB is {} , remainMB is {} , productOfferDate is {} , productExpireDate is {}",amountGB,remainMB,productOfferDate,productExpireDate);

                            //ProductMsg productMsgObject = new ProductMsg(productName,amountGB,remainMB,sdt.parse(sdt.format(productOfferDate)).getTime(),sdt.parse(sdt.format(productExpireDate)).getTime());
                            ProductMsg productMsgObject = new ProductMsg(productName,amountGB,remainMB,productOfferDate.getTime(),productExpireDate.getTime());
                            productMsgList.add(productMsgObject.toJsonString());
                        }else {
                            log.error("QueryInterfaceServiceImpl-queryUserOfferInfo select apsFreeResList size is {}",apsFreeResList.size());
                            throw new MvneException("500","select apsFreeResList error , select apsFreeResList size more than 1");
                        }
                    }else {
                        log.info("QueryInterfaceServiceImpl-queryUserOfferInfo apsFreeResList is null , userId is {}",userId );
                    }

                }
            }else {
                log.info("QueryInterfaceServiceImpl-queryUserOfferInfo select userId prodInfo , cmProdInsInfoList size is {} ,cmProdInsInfoList is {}",cmProdInsInfoList.size(),cmProdInsInfoList);
            }

            log.info("QueryInterfaceServiceImpl-queryUserOfferInfo select productMsgList is {}",productMsgList);
            QueryUserOfferInfo queryUserOfferInfo = new QueryUserOfferInfo(userId, balanceFee.divide(new BigDecimal(100)), productMsgList);
            log.info("QueryInterfaceServiceImpl-queryUserOfferInfo response queryUserOfferInfo is {}",queryUserOfferInfo );
            return queryUserOfferInfo;
        } catch (Exception e) {
            log.error("QueryInterfaceServiceImpl-queryUserOfferInfo fail! error message is {}",StringUtils.getExceptionText(e));
            //throw new MvneException("500", StringUtils.getExceptionText(e));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            } else {
                throw new MvneException("500",StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }

    }

    @Override
    public IPage<QueryUserCdrInfo> queryUserCdrInfo(String msisdn, Long startDate, Long endDate, String xdrType, Integer page, Integer size) throws MvneException, ParseException {
        log.info("QueryInterfaceServiceImpl-queryUserCdrInfo select msisdn is {} , page is {} , page size is {}",msisdn,page,size);

        try {

            List<String> dayList=getDayList(startDate, endDate);
            log.info("QueryInterfaceServiceImpl-queryUserCdrInfo dayList is {}",dayList);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startDateYMD = sdf1.format(startDate) + " 00:00:00";
            String endDateYMD = sdf1.format(endDate) + " 23:59:59";

            //MultipleResult multipleResult = new MultipleResult();
            IPage<QueryUserCdrInfoDto> cdrGprsPage = new Page<>(page,size);
            IPage<QueryUserCdrInfo> queryUserCdrInfoListPage = new Page<>(page,size);
            switch (xdrType) {
                case "GP":
                case "RGP":

                    //查询详单
                    queryUserCdrInfoListPage = findUserCdrInfo(cdrGprsPage,dayList, msisdn, sdf2.parse(startDateYMD), sdf2.parse(endDateYMD), xdrType,page,size);
                    log.info("QueryInterfaceServiceImpl-queryUserCdrInfo findUserCdrInfo multipleResult PageNo is {} , PageSize is {} , Total is {}",cdrGprsPage.getCurrent(),cdrGprsPage.getSize(),cdrGprsPage.getTotal());

                    break;
                default:
                    break;
            }
            return  queryUserCdrInfoListPage;
        } catch (Exception e) {
            log.error("QueryInterfaceServiceImpl-queryUserCdrInfo fail! error message is {}",StringUtils.getExceptionText(e));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            } else {
                throw new MvneException("500",StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }
    }

    @Override
    public List<ExportUserCdrInfo> exportDetailCdr(String msisdn, Long startDate, Long endDate, String xdrType, Integer page, Integer size) throws MvneException, ParseException {
        log.info("QueryInterfaceServiceImpl-exportDetailCdr select msisdn is {} , page is {} , page size is {}",msisdn,page,size);

        try {

            List<String> dayList=getDayList(startDate, endDate);
            log.info("QueryInterfaceServiceImpl-exportDetailCdr dayList is {}",dayList);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startDateYMD = sdf1.format(startDate) + " 00:00:00";
            String endDateYMD = sdf1.format(endDate) + " 23:59:59";

            //MultipleResult multipleResult = new MultipleResult();
            IPage<QueryUserCdrInfoDto> cdrGprsPage = new Page<>(page,size);
            List<ExportUserCdrInfo> exportUserCdrInfoList = new ArrayList<>();
            switch (xdrType) {
                case "GP":
                case "RGP":

                    //查询详单
                    exportUserCdrInfoList = exportUserCdrInfo(cdrGprsPage,dayList, msisdn, sdf2.parse(startDateYMD), sdf2.parse(endDateYMD), xdrType,page,size);
                    log.info("QueryInterfaceServiceImpl-exportDetailCdr exportUserCdrInfo multipleResult PageNo is {} , PageSize is {} , Total is {}",cdrGprsPage.getCurrent(),cdrGprsPage.getSize(),cdrGprsPage.getTotal());

                    break;
                default:
                    break;
            }
            return  exportUserCdrInfoList;
        } catch (Exception e) {
            log.error("QueryInterfaceServiceImpl-exportDetailCdr fail! error message is {}",StringUtils.getExceptionText(e));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            } else {
                throw new MvneException("500",StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }
    }

    //获取日期列表
    private List<String> getDayList(Long startTime, Long endTime) {

        DateTimeFormatter dayFormater = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime localStartTime = DateTimeUtil.getDateTimeofTimestamp(startTime);
        LocalDateTime localEndTime = DateTimeUtil.getDateTimeofTimestamp(endTime);

        List<String> dayList = new ArrayList<>();
        while(localStartTime.isBefore(localEndTime)) {
            dayList.add(localStartTime.format(dayFormater));
            localStartTime = localStartTime.plusDays(1);
        }
        if(localStartTime.equals(localEndTime)) {
            dayList.add(localStartTime.format(dayFormater));
        }
        return dayList;
    }

    private IPage<QueryUserCdrInfo> findUserCdrInfo(IPage<QueryUserCdrInfoDto> cdrGprsPage,List<String> dayList, String msisdn, Date startDateYMD, Date endDateYMD, String xdrType,Integer page, Integer size){
        List<QueryUserCdrInfo> queryUserCdrInfoList = new ArrayList<>();

        BigDecimal totalVolKB = null;
        //MultipleSelectPage multipleSelectPage = new MultipleSelectPage();
        //multipleSelectPage.setPage(page,size);

        //查询详单，limit限制分页范围
        List<QueryUserCdrInfoDto> queryUserCdrInfoDtoList = cdrGprsService.mulSelectCdrInfo(cdrGprsPage,dayList, msisdn, startDateYMD, endDateYMD, xdrType);
        for ( QueryUserCdrInfoDto queryUserCdrInfoDto : queryUserCdrInfoDtoList){

            String operatorCode = queryUserCdrInfoDto.getOperatorCode();
            BigDecimal downVol = queryUserCdrInfoDto.getDownloadVol();
            BigDecimal upVol = queryUserCdrInfoDto.getUploadVol();
            BigDecimal totalVol = downVol.add(upVol);
            if ( totalVol.compareTo(BigDecimal.ZERO) == 0 ){
                totalVolKB = new BigDecimal("0");
            }else {
                totalVolKB = totalVol.divide(new BigDecimal(1024),0,BigDecimal.ROUND_UP);
            }
            String countryName = bdOperatorCodeService.selectByOperCodeInfo(operatorCode);
            BigDecimal fee1 = queryUserCdrInfoDto.getFee1();
            String resources;
            if (fee1.compareTo(BigDecimal.ZERO) == 1 ) {
                resources = "out of the bundle";
            } else {
                resources = "in the bundle";
            }
            Date eventTimeStampDate = DateTimeUtil.strToDate(queryUserCdrInfoDto.getEventTimeStamp());
            QueryUserCdrInfo queryUserCdrInfo = new QueryUserCdrInfo(msisdn, queryUserCdrInfoDto.getLocalEventTimeStamp().getTime(),eventTimeStampDate.getTime(),
                    countryName, queryUserCdrInfoDto.getApn(), queryUserCdrInfoDto.getEventDuration(),
                    totalVolKB, resources, queryUserCdrInfoDto.getFee1().divide(new BigDecimal(100)));
            queryUserCdrInfoList.add(queryUserCdrInfo);

        }
        IPage<QueryUserCdrInfo> queryUserCdrInfoListPage = new Page<>(cdrGprsPage.getCurrent(), cdrGprsPage.getSize(), cdrGprsPage.getTotal());
        queryUserCdrInfoListPage.setRecords(queryUserCdrInfoList);
        return queryUserCdrInfoListPage;

        /*MultipleResult result = new MultipleResult();
        result.setData(queryUserCdrInfoList);
        //查询详单总条数
        if (multipleSelectPage.getStart() != null && multipleSelectPage.getEnd() != null) {
            Integer total = cdrGprsMapper.countMulSelectCdr(dayList, msisdn, startDateYMD, endDateYMD,xdrType);
            result.setPageNo(multipleSelectPage.getStart() / multipleSelectPage.getEnd() + 1);
            result.setPageSize(multipleSelectPage.getEnd());
            result.setTotal(total);
        }*/

    }

    private List<ExportUserCdrInfo> exportUserCdrInfo(IPage<QueryUserCdrInfoDto> cdrGprsPage,List<String> dayList, String msisdn, Date startDateYMD, Date endDateYMD, String xdrType,Integer page, Integer size){
        List<ExportUserCdrInfo> exportUserCdrInfoList = new ArrayList<>();

        BigDecimal totalVolKB = null;

        //查询详单，limit限制分页范围
        List<QueryUserCdrInfoDto> queryUserCdrInfoDtoList = cdrGprsService.mulSelectCdrInfo(cdrGprsPage,dayList, msisdn, startDateYMD, endDateYMD, xdrType);
        for ( QueryUserCdrInfoDto queryUserCdrInfoDto : queryUserCdrInfoDtoList){

            String operatorCode = queryUserCdrInfoDto.getOperatorCode();
            BigDecimal downVol = queryUserCdrInfoDto.getDownloadVol();
            BigDecimal upVol = queryUserCdrInfoDto.getUploadVol();
            BigDecimal totalVol = downVol.add(upVol);
            if ( totalVol.compareTo(BigDecimal.ZERO) == 0 ){
                totalVolKB = new BigDecimal("0");
            }else {
                totalVolKB = totalVol.divide(new BigDecimal(1024),0,BigDecimal.ROUND_UP);
            }
            String countryName = bdOperatorCodeService.selectByOperCodeInfo(operatorCode);
            BigDecimal fee1 = queryUserCdrInfoDto.getFee1();
            String resources;
            if (fee1.compareTo(BigDecimal.ZERO) == 1 ) {
                resources = "out of the bundle";
            } else {
                resources = "in the bundle";
            }
            Date eventTimeStampDate = DateTimeUtil.strToDate(queryUserCdrInfoDto.getEventTimeStamp());
            ExportUserCdrInfo exportUserCdrInfo = new ExportUserCdrInfo(msisdn, queryUserCdrInfoDto.getLocalEventTimeStamp(),eventTimeStampDate,
                    countryName, queryUserCdrInfoDto.getApn(), queryUserCdrInfoDto.getEventDuration(),
                    totalVolKB, resources, queryUserCdrInfoDto.getFee1().divide(new BigDecimal(100)));
            exportUserCdrInfoList.add(exportUserCdrInfo);

        }
        return exportUserCdrInfoList;

    }

}


