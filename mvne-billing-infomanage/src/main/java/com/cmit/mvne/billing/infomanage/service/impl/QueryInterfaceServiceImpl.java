package com.cmit.mvne.billing.infomanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.*;
import com.cmit.mvne.billing.infomanage.mapper.QueryUserMonthCdrInfoMapper;
import com.cmit.mvne.billing.infomanage.service.QueryInterfaceService;
import com.cmit.mvne.billing.infomanage.util.DateTimeUtil;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.mapper.CdrGprsMapper;
import com.cmit.mvne.billing.user.analysis.mapper.CmUserDetailMapper;
import com.cmit.mvne.billing.user.analysis.mapper.IUserMapper;
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
    @Autowired
    private QueryUserMonthCdrInfoMapper queryUserMonthCdrInfoMapper;
    @Autowired
    private CmUserDetailMapper cmUserDetailMapper;
    @Autowired
    private IUserMapper iUserMapper;

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
    public QueryUserProductInfo queryUserProductInfo(String msisdn) throws MvneException {
        ProductMsg productMsg = null;
        try {
            //获取userId
            String userId = null;
            try {
                userId = String.valueOf(iUserMapper.selectOne(new QueryWrapper<IUser>().eq("msisdn",msisdn)).getUserId());
            } catch (NullPointerException e) {
                throw new MvneException("500","msisdn does not exist !");
            }
            //获取最新的余额
            BigDecimal balanceFee = new BigDecimal(0);
            ApsBalanceFee apsBalanceFee= apsBalanceFeeService.selectByUserId(Long.valueOf(userId));
            log.info("QueryInterfaceServiceImpl-queryUserProductInfo apsBalanceFee is {}",apsBalanceFee);
            if ( apsBalanceFee != null ){
                balanceFee = apsBalanceFee.getRemainFee();
                log.info("QueryInterfaceServiceImpl-queryUserProductInfo select userId is {} , balanceFee is {}",userId,balanceFee);
            }else {
                log.error("QueryInterfaceServiceImpl-queryUserProductInfo apsBalanceFee is null , userId is {}",userId);
                throw new MvneException("500","select apsBalanceFee is null !");
            }
            //获取产品信息，通过产品表查询免费资源
            List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoService.findAllByUserId(Long.valueOf(userId));
            log.info("QueryInterfaceServiceImpl-queryUserProductInfo select userId prodInfo , cmProdInsInfoList is {}",cmProdInsInfoList);
            if ( cmProdInsInfoList.size() > 0 ){
                for (CmProdInsInfo cmProdInsInfo : cmProdInsInfoList) {
                    //获取产品名
                    String productName = pmProductService.selectByProductId(cmProdInsInfo.getProductId());
                    log.info("QueryInterfaceServiceImpl-queryUserProductInfo select userId prodInfo , cmProdInsInfo is {} , productName is {}",cmProdInsInfo,productName);
                    //获取流量的免费资源
                    List<ApsFreeRes> apsFreeResList = apsFreeResService.findByUserIdProdIns(Long.valueOf(userId),cmProdInsInfo.getProductInsId());
                    log.info("QueryInterfaceServiceImpl-queryUserProductInfo apsFreeResList is {} ",apsFreeResList);

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
                            log.info("QueryInterfaceServiceImpl-queryUserProductInfo select apsFreeRes amountGB is {} , remainMB is {} , productOfferDate is {} , productExpireDate is {}",amountGB,remainMB,productOfferDate,productExpireDate);

                            //ProductMsg productMsgObject = new ProductMsg(productName,amountGB,remainMB,sdt.parse(sdt.format(productOfferDate)).getTime(),sdt.parse(sdt.format(productExpireDate)).getTime());
                            productMsg = new ProductMsg(productName,amountGB,remainMB,productOfferDate.getTime(),productExpireDate.getTime());
                        }else {
                            log.error("QueryInterfaceServiceImpl-queryUserProductInfo select apsFreeResList size is {}",apsFreeResList.size());
                            throw new MvneException("500","select apsFreeResList error , select apsFreeResList size more than 1");
                        }
                    }else {
                        log.info("QueryInterfaceServiceImpl-queryUserProductInfo apsFreeResList is null , userId is {}",userId );
                    }

                }
            }else {
                log.info("QueryInterfaceServiceImpl-queryUserProductInfo select userId prodInfo , cmProdInsInfoList size is {} ,cmProdInsInfoList is {}",cmProdInsInfoList.size(),cmProdInsInfoList);
            }

            log.info("QueryInterfaceServiceImpl-queryUserProductInfo select productMsgList is {}",productMsg);
            QueryUserProductInfo queryUserProductInfo = new QueryUserProductInfo(msisdn, balanceFee.divide(new BigDecimal(100)).longValue(), productMsg);
            log.info("QueryInterfaceServiceImpl-queryUserProductInfo response queryUserOfferInfo is {}",queryUserProductInfo );
            return queryUserProductInfo;
        } catch (Exception e) {
            log.error("QueryInterfaceServiceImpl-queryUserProductInfo fail! error message is {}",StringUtils.getExceptionText(e));
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
                    queryUserCdrInfoListPage = findUserCdrInfo(cdrGprsPage,dayList, msisdn, startDateYMD, endDateYMD, xdrType,page,size);
//                    queryUserCdrInfoListPage = findUserCdrInfo(cdrGprsPage,dayList, msisdn, sdf2.parse(startDateYMD), sdf2.parse(endDateYMD), xdrType,page,size);
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
    public List<QueryUserMonthCdrInfo> queryUserMonthCdrInfo(String msisdn, Long date, String xdrType) throws MvneException, ParseException {
        log.info("QueryInterfaceServiceImpl-queryUserMonthCdrInfo select msisdn is {} ",msisdn);
        List<QueryUserMonthCdrInfo> queryUserMonthCdrInfoList = new ArrayList<>();
        //获取年月列表
        List<String> monthList = getMonthList(date);
        //根据msisdn和前五个月的月份查询月流量表,要注意monthList是按照时间倒叙排序的，所以monthList的第一个是本月,第二个是上月
        for (int i=5;i>0;i--){
            queryUserMonthCdrInfoList.add(queryUserMonthCdrInfoMapper.selectOne(new QueryWrapper<QueryUserMonthCdrInfo>().eq("msisdn", msisdn).eq("year_months", monthList.get(i))));
        }
        //查询本月总流量
        queryUserMonthCdrInfoList.add(getMonthCdr(msisdn, monthList.get(0), xdrType));
        return queryUserMonthCdrInfoList;
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
                    exportUserCdrInfoList = exportUserCdrInfo(cdrGprsPage,dayList, msisdn, startDateYMD, endDateYMD, xdrType,page,size);
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


    //每月凌晨1点定时更新上个月的总流量
    @Override
    public void monthCdrSchedule(Date date)throws MvneException{
        List<String> msisdnList = getMsisdnList();
        String xdrType = "RGP";
        //获取上月的月份
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateString = sdf.format(date);
        Integer year = Integer.valueOf(dateString.split("-")[0]);
        Integer month = Integer.valueOf(dateString.split("-")[1]);
        String yearMonth;
        if (month==1){
            yearMonth = (year-1)+"-12";
        }else if (month<10){
            yearMonth = year+"-0"+(month-1);
        }else {
            yearMonth = year+"-"+(month-1);
        }
        for (String msisdn :msisdnList) {
            queryUserMonthCdrInfoMapper.insert(getMonthCdr(msisdn,yearMonth,xdrType));
        }
    }

    //根据msisdn和年月查询月总流量
    private QueryUserMonthCdrInfo getMonthCdr(String msisdn,String yearMonth,String xdrType)throws MvneException{
        QueryUserMonthCdrInfo queryUserMonthCdrInfo = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<String> dayList ;
            queryUserMonthCdrInfo = new QueryUserMonthCdrInfo();
            queryUserMonthCdrInfo.setMsisdn(msisdn);
            queryUserMonthCdrInfo.setYearMonths(yearMonth);
            switch (xdrType) {
                case "GP":
                case "RGP":
                    String startDateYMD = yearMonth + "-01 00:00:00";
                    String endDateYMD = yearMonth+"-"+getMonthDays(yearMonth)+ " 23:59:59";
                    dayList = getDayList(sdf.parse(startDateYMD).getTime(),sdf.parse(endDateYMD).getTime());
                    BigDecimal totalVolKB ;
                    BigDecimal dayTotalVolKB = new BigDecimal("0");
                    BigDecimal monthTotalVolKB = new BigDecimal("0");
                    //查询详单,获取指定时间段内的每条详单的上行加下行的总流量
//                    queryUserCdrInfoListPage = findUserCdrInfo(cdrGprsPage,dayList, msisdn, sdf.parse(startDateYMD), sdf.parse(endDateYMD), xdrType,page,size);
                    for (String day :dayList) {
                        List<QueryUserCdrInfoDto> userCdrInfoDtoList = cdrGprsService.selectCdrInfo(day, msisdn, yearMonth + "-" + day.substring(6) + " 00:00:00", yearMonth + "-" + day.substring(6) + " 23:59:59", xdrType);
                        for (QueryUserCdrInfoDto queryUserCdrInfoDto :userCdrInfoDtoList) {
                            BigDecimal downVol = queryUserCdrInfoDto.getDownloadVol();
                            BigDecimal upVol = queryUserCdrInfoDto.getUploadVol();
                            BigDecimal totalVol = downVol.add(upVol);
                            if ( totalVol.compareTo(BigDecimal.ZERO) == 0 ){
                                totalVolKB = new BigDecimal("0");
                            }else {
                                totalVolKB = totalVol.divide(new BigDecimal(1024),0,BigDecimal.ROUND_UP);
                            }
                            dayTotalVolKB = dayTotalVolKB.add(totalVolKB);
                        }
                        monthTotalVolKB = monthTotalVolKB.add(dayTotalVolKB);
                        dayTotalVolKB = new BigDecimal("0");
                    }
                    queryUserMonthCdrInfo.setTotalVol(monthTotalVolKB);
                    //计算月流量总和并添加到list中
//                    queryUserMonthCdrInfo=getQueryUserMonthCdrInfo(queryUserCdrInfoListPage, yearMonth);
                    break;
                default:
                    break;
            }
            return queryUserMonthCdrInfo;
        } catch (Exception e) {
            log.error("QueryInterfaceServiceImpl-queryUserMonthCdrInfo fail! error message is {}",StringUtils.getExceptionText(e));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            } else {
                throw new MvneException("500",StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }
    }

    //获取需要定时更新的用户的msisdn列表
    private List<String> getMsisdnList(){
        List<String> msisdnList = new ArrayList<>();
        //获取所有状态正常的用户信息
        List<CmUserDetail> userDetailList = cmUserDetailMapper.selectList(new QueryWrapper<CmUserDetail>().eq("USER_STATUS", "03"));
        //获取msisdn列表
        for (CmUserDetail user:userDetailList) {
            msisdnList.add(user.getMsisdn());
        }
        return msisdnList;
    }

    //计算话单流量总和
    /*private QueryUserMonthCdrInfo getQueryUserMonthCdrInfo(IPage<QueryUserCdrInfo> queryUserCdrInfoIPage,String yearMonth){
        QueryUserMonthCdrInfo queryUserMonthCdrInfo = new QueryUserMonthCdrInfo();
        List<QueryUserCdrInfo> userCdrInfoList = queryUserCdrInfoIPage.getRecords();
        queryUserMonthCdrInfo.setMsisdn(userCdrInfoList.get(0).getMsisdn());
        BigDecimal totalMonthVol = new BigDecimal("0");
        for (QueryUserCdrInfo cdr :userCdrInfoList) {
            totalMonthVol.add(cdr.getTotalVol());
        }
        queryUserMonthCdrInfo.setTotalVol(totalMonthVol);
        queryUserMonthCdrInfo.setYearMonths(yearMonth);
        return queryUserMonthCdrInfo;
    }*/

    //获取年月份列表,并按时间倒序排序
    private List<String> getMonthList(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(new Date(date*1000L));
        Integer yearInteger = Integer.valueOf(dateString.split("-")[0]);
        Integer monthInteger = Integer.valueOf(dateString.split("-")[1]);
        Integer dayInteger = Integer.valueOf(dateString.split("-")[2]);

        List<String> monthList = new ArrayList<>();
        String currentMonth ;

        //如果查询日期是六月及以后，则前六个月没有跨年,否则就要跨年
        for (int i=0;i<6;i++){
            if (monthInteger-i>0){
                currentMonth = yearInteger+"-"+(monthInteger-i);
            }else {
                currentMonth = (yearInteger-1)+"-"+(monthInteger-i+12);
            }
            if (currentMonth.length()==6){
                currentMonth = currentMonth.split("-")[0]+"-0"+currentMonth.split("-")[1];
            }
            monthList.add(currentMonth);
        }
        return monthList;
    }

    //获取指定年月的天数
    public static int getMonthDays(String date) {
        Integer year = Integer.valueOf(date.split("-")[0]);
        Integer month = Integer.valueOf(date.split("-")[1]);
        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
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

    private IPage<QueryUserCdrInfo> findUserCdrInfo(IPage<QueryUserCdrInfoDto> cdrGprsPage,List<String> dayList, String msisdn, String startDateYMD, String endDateYMD, String xdrType,Integer page, Integer size){
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
            //Date eventTimeStampDate = DateTimeUtil.strToDate(queryUserCdrInfoDto.getEventTimeStamp());
            QueryUserCdrInfo queryUserCdrInfo = new QueryUserCdrInfo(msisdn, queryUserCdrInfoDto.getLocalEventTimeStamp().getTime(),queryUserCdrInfoDto.getEventTimeStamp(),
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

    private List<ExportUserCdrInfo> exportUserCdrInfo(IPage<QueryUserCdrInfoDto> cdrGprsPage,List<String> dayList, String msisdn, String startDateYMD, String endDateYMD, String xdrType,Integer page, Integer size){
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
            //Date eventTimeStampDate = DateTimeUtil.strToDate(queryUserCdrInfoDto.getEventTimeStamp());
            ExportUserCdrInfo exportUserCdrInfo = new ExportUserCdrInfo(msisdn, queryUserCdrInfoDto.getLocalEventTimeStamp(),queryUserCdrInfoDto.getEventTimeStamp(),
                    countryName,queryUserCdrInfoDto.getApn(), queryUserCdrInfoDto.getEventDuration(), totalVolKB, resources, queryUserCdrInfoDto.getFee1().divide(new BigDecimal(100)));
            exportUserCdrInfoList.add(exportUserCdrInfo);

        }
        return exportUserCdrInfoList;

    }

}


