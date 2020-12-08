package com.cmit.mvne.billing.infomanage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmit.mvne.billing.user.analysis.entity.CmProdInsInfo;
import com.cmit.mvne.billing.user.analysis.entity.CmUserDetail;
import com.cmit.mvne.billing.user.analysis.service.CmProdInsInfoService;
import com.cmit.mvne.billing.user.analysis.service.CmUserDetailService;
import com.cmit.mvne.billing.infomanage.service.MemoryRecoveryService;
import com.cmit.mvne.billing.user.analysis.entity.ApsBalanceFee;
import com.cmit.mvne.billing.user.analysis.entity.ApsFreeRes;
import com.cmit.mvne.billing.user.analysis.entity.FreeRes;
import com.cmit.mvne.billing.user.analysis.service.ApsBalanceFeeService;
import com.cmit.mvne.billing.user.analysis.service.ApsFreeResService;
import com.cmit.mvne.billing.user.analysis.service.FreeResService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class MemoryRecoveryServiceImpl implements MemoryRecoveryService {

    @Autowired
    private CmUserDetailService cmUserDetailService;
    @Autowired
    private CmProdInsInfoService cmProdInsInfoService;
    @Autowired
    private ApsFreeResService apsFreeResService;
    @Autowired
    private ApsBalanceFeeService apsBalanceFeeService;
    @Autowired
    private FreeResService freeResService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void memoryRecoveryInterface(String tableName,String startTime,String endTime) throws ParseException {

        log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface tableName is {} , startTime is {} , endTime is {}",tableName,startTime,endTime);
        SimpleDateFormat std = new SimpleDateFormat("yyyyMMddHHmmss");

        switch (tableName){
            case "userDetail":
                List<CmUserDetail> cmUserDetailDBList = cmUserDetailService.selectUserDetail(std.parse(startTime),std.parse(endTime));
                //cmUserDetailDB按validDate排序
                //cmUserDetailDB.sort((CmUserDetail a,CmUserDetail b) -> a.getValidDate() - b.getValidDate());
                //cmUserDetailDBList.sort(Comparator.comparing(CmUserDetail::getValidDate));
                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmUserDetailDB size is {}",cmUserDetailDBList.size());
                for (CmUserDetail cmUserDetailDB:cmUserDetailDBList)
                {
                    //System.out.println("cmUserDetailDB"+cmUserDetailDB);
                    String userTableKey = "UserDetail:" + cmUserDetailDB.getMsisdn();
                    List<CmUserDetail> cmUserDetailRedisList= JSONObject.parseArray(redisTemplate.opsForList().range(userTableKey,0,-1).toString(), CmUserDetail.class);
                    log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmUserDetailRedis size is {}",cmUserDetailRedisList.size());
                    if ( cmUserDetailRedisList.size() == 0 )
                    {
                        String userTableValue = cmUserDetailDB.toJsonString();
                        redisTemplate.opsForList().leftPush(userTableKey, userTableValue);
                        log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmUserDetailRedisList size is 0 , insert Redis data {}!",cmUserDetailDB);
                    }else {
                        int count = 0;

                        for ( CmUserDetail cmUserDetailRedis:cmUserDetailRedisList) {
                            //log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmUserDetailRedis is {}",cmUserDetailRedis);
                            //System.out.println("cmUserDetailRedis"+cmUserDetailRedis);
                            //Boolean status = cmUserDetailDB.equals(cmUserDetailRedis);
                            //比较是否相等
                            Boolean status = compareCmUserDetail(cmUserDetailDB,cmUserDetailRedis);
                            if (  status )
                            {
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmUserDetailRedis exits this data : {}",cmUserDetailDB);
                                break;
                            }
                            /*if( cmUserDetailRedisList.size() == 1 )
                            {
                                String userTableValue = cmUserDetailDB.toJsonString();
                                redisTemplate.opsForList().leftPush(userTableKey, userTableValue);
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmUserDetailRedisList size is 1 , insert Redis data {}!",cmUserDetailDB);
                                break;
                            }*/
                            if( cmUserDetailDB.getValidDate().after(cmUserDetailRedisList.get(0).getValidDate()))
                            {
                                String userTableValue = cmUserDetailDB.toJsonString();
                                redisTemplate.opsForList().leftPush(userTableKey, userTableValue);
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface leftPush insert Redis data {}!",cmUserDetailDB);
                                break;
                            }
                            if( cmUserDetailDB.getValidDate().before(cmUserDetailRedisList.get(cmUserDetailRedisList.size()-1).getValidDate()))
                            {
                                String userTableValue = cmUserDetailDB.toJsonString();
                                redisTemplate.opsForList().rightPush(userTableKey, userTableValue);
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface rightPush insert Redis data {}!",cmUserDetailDB);
                                break;
                            }
                            if ( cmUserDetailDB.getValidDate().before(cmUserDetailRedis.getValidDate()) && cmUserDetailDB.getValidDate().after(cmUserDetailRedisList.get(count+1).getValidDate())) {
                                //log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmUserDetailRedis is not exits this data : {}",cmUserDetailDB);
                                //插入列表
                                //cmUserDetailRedisList.add(count+1,cmUserDetailDB);
                                List<CmUserDetail> cmUserDetailRedisPop= JSONObject.parseArray(redisTemplate.opsForList().range(userTableKey,0,count).toString(), CmUserDetail.class);
                                for (int i=0;i<cmUserDetailRedisPop.size();i++){
                                    redisTemplate.opsForList().leftPop(userTableKey);
                                }
                                redisTemplate.opsForList().leftPush(userTableKey,cmUserDetailDB.toJsonString());
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface leftPush insert Redis data {}!",cmUserDetailDB);
                                for (int i=cmUserDetailRedisPop.size()-1;i>=0;i--){
                                    redisTemplate.opsForList().leftPush(userTableKey,cmUserDetailRedisPop.get(i).toJsonString());
                                }
                                break;
                            }
                            count++;
                        }
                    }

                }
                break;
            case "prodInsInfo":
                List<CmProdInsInfo> cmProdInsInfoDBList = cmProdInsInfoService.selectProdInsInfo(std.parse(startTime),std.parse(endTime));
                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmProdInsInfoDB size is {}",cmProdInsInfoDBList.size());
                for (CmProdInsInfo cmProdInsInfoDB:cmProdInsInfoDBList)
                {
                    String prodTableKey = "ProdInsInfo:" + cmProdInsInfoDB.getUserId();
                    List<CmProdInsInfo> cmProdInsInfoRedisList= JSONObject.parseArray(redisTemplate.opsForList().range(prodTableKey,0,-1).toString(), CmProdInsInfo.class);
                    log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmProdInsInfoRedis size is {}",cmProdInsInfoRedisList.size());
                    if ( cmProdInsInfoRedisList.size() == 0 )
                    {
                        String userTableValue = cmProdInsInfoDB.toJsonString();
                        redisTemplate.opsForList().leftPush(prodTableKey, userTableValue);
                        log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmProdInsInfoRedisList size is 0 , insert Redis data {}!",cmProdInsInfoDB);
                    }else {
                        int count = 0;

                        for ( CmProdInsInfo cmProdInsInfoRedis:cmProdInsInfoRedisList) {
                            //比较是否相等
                            Boolean status = compareProdInsInfo(cmProdInsInfoDB,cmProdInsInfoRedis);
                            if (  status )
                            {
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmProdInsInfoRedis exits this data : {}",cmProdInsInfoDB);
                                break;
                            }
                           /* if( cmProdInsInfoRedisList.size() == 1 && cmProdInsInfoDB.getValidDate().after(cmProdInsInfoRedisList.get(0).getValidDate()))
                            {
                                String userTableValue = cmProdInsInfoDB.toJsonString();
                                redisTemplate.opsForList().leftPush(prodTableKey, userTableValue);
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmProdInsInfoRedisList size is 1 , insert Redis data {}!",cmProdInsInfoDB);
                                break;
                            }else if ( cmProdInsInfoRedisList.size() == 1 && cmProdInsInfoDB.getValidDate().before(cmProdInsInfoRedisList.get(0).getValidDate()) )
                            {
                                String userTableValue = cmProdInsInfoDB.toJsonString();
                                redisTemplate.opsForList().rightPush(prodTableKey, userTableValue);
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface cmProdInsInfoRedisList size is 1 , insert Redis data {}!",cmProdInsInfoDB);
                                break;
                            }*/
                            if( cmProdInsInfoDB.getValidDate().after(cmProdInsInfoRedisList.get(0).getValidDate()))
                            {
                                String userTableValue = cmProdInsInfoDB.toJsonString();
                                redisTemplate.opsForList().leftPush(prodTableKey, userTableValue);
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface leftPush insert Redis data {}!",cmProdInsInfoDB);
                                break;
                            }
                            if( cmProdInsInfoDB.getValidDate().before(cmProdInsInfoRedisList.get(cmProdInsInfoRedisList.size()-1).getValidDate()))
                            {
                                String userTableValue = cmProdInsInfoDB.toJsonString();
                                redisTemplate.opsForList().rightPush(prodTableKey, userTableValue);
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface rightPush insert Redis data {}!",cmProdInsInfoDB);
                                break;
                            }
                            if ( cmProdInsInfoDB.getValidDate().before(cmProdInsInfoRedis.getValidDate()) && cmProdInsInfoDB.getValidDate().after(cmProdInsInfoRedisList.get(count+1).getValidDate())) {

                                List<CmProdInsInfo> cmProdInsInfoRedisPop= JSONObject.parseArray(redisTemplate.opsForList().range(prodTableKey,0,count).toString(), CmProdInsInfo.class);
                                for (int i=0;i<cmProdInsInfoRedisPop.size();i++){
                                    redisTemplate.opsForList().leftPop(prodTableKey);
                                }
                                redisTemplate.opsForList().leftPush(prodTableKey,cmProdInsInfoDB.toJsonString());
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface leftPush insert Redis data {}!",cmProdInsInfoDB);
                                for (int i=cmProdInsInfoRedisPop.size()-1;i>=0;i--){
                                    redisTemplate.opsForList().leftPush(prodTableKey,cmProdInsInfoRedisPop.get(i).toJsonString());
                                }
                                break;
                            }
                            count++;
                        }
                    }

                }
                break;
            case "freeRes":
                List<ApsFreeRes> apsFreeResDBList = apsFreeResService.selectFreeRes(std.parse(startTime),std.parse(endTime));
                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsFreeResDB size is {}",apsFreeResDBList.size());

                List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoService.selectProdInsInfo(std.parse(startTime),std.parse(endTime));
                List<String> freeResKeyList = new ArrayList<>();
                for (CmProdInsInfo cmProdInsInfo:cmProdInsInfoList)
                {
                    List<FreeRes> freeResList = freeResService.selectProduct(cmProdInsInfo.getProductId());
                    for(FreeRes freeRes : freeResList) {
                        String freeResKey = "FreeRes:" +  cmProdInsInfo.getUserId() + ":" + cmProdInsInfo.getProductInsId() + ":" + freeRes.getFreeresItem();
                        freeResKeyList.add(freeResKey);
                    }
                }

                for (ApsFreeRes apsFreeResDB : apsFreeResDBList)
                {
                    for ( String freeResKey : freeResKeyList)
                    {
                        //List<ApsFreeRes> apsFreeResRedisList= JSONObject.parseArray(redisTemplate.opsForList().range(freeResKey,0,-1).toString(), ApsFreeRes.class);
                        Map freeResMap = redisTemplate.opsForHash().entries(freeResKey);
                        log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsFreeResRedis size is {}",freeResMap.size());
                        if ( freeResMap.size() == 0 )
                        {
                            freeResInsertRedis(freeResKey,apsFreeResDB);
                            log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsFreeResRedisList size is 0 , insert Redis data {}!",apsFreeResDB);
                        }else {

                            //比较是否相等
                            Boolean status = compareFreeRes(apsFreeResDB,freeResMap);
                            if (  status )
                            {
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsFreeResRedis exits this data : {}",apsFreeResDB);
                                break;
                            }else if( freeResMap.size() == 1 )
                            {
                                //freeResInsertRedis(freeResKey,apsFreeResDB);
                                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsFreeResRedisList size is 1 , insert Redis data {}!",apsFreeResDB);
                                break;
                            }


                        }
                    }


                }
                break;
            case "balanceFee":
                ApsBalanceFee apsBalanceFeeDB = apsBalanceFeeService.selectBalanceFee();
                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsBalanceFeeDB  is {}",apsBalanceFeeDB);

                String balanceFeeKey = "BalanceFee:" + apsBalanceFeeDB.getUserId();
                Map balanceFeeMap=redisTemplate.opsForHash().entries(balanceFeeKey);
                log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsBalanceFeeRedis is {}",balanceFeeMap);
                if ( balanceFeeMap.size() == 0 )
                {
                    balanceFeeInsertRedis(balanceFeeKey,apsBalanceFeeDB);
                    log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsBalanceFeeRedisList size is 0 , insert Redis data {}!",apsBalanceFeeDB);
                }else {

                    //比较是否相等
                    Boolean status = compareBalanceFee(apsBalanceFeeDB,balanceFeeMap);
                    if (  status )
                    {
                        log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsBalanceFeeRedis exits this data : {}",apsBalanceFeeDB);
                        break;
                    }else
                    {
                        balanceFeeInsertRedis(balanceFeeKey,apsBalanceFeeDB);
                        log.info("MemoryRecoveryServiceImpl-memoryRecoveryInterface apsBalanceFeeList size is 1 , insert Redis data {}!",apsBalanceFeeDB);
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    public boolean compareCmUserDetail(CmUserDetail cmUserDetailDB,CmUserDetail cmUserDetailRedis)
    {
        Calendar validDateDB = Calendar.getInstance();
        Calendar validDateRedis = Calendar.getInstance();
        Calendar expireDateDB = Calendar.getInstance();
        Calendar expireDateRedis = Calendar.getInstance();
        validDateDB.setTime(cmUserDetailDB.getValidDate());
        expireDateDB.setTime(cmUserDetailDB.getExpireDate());
        validDateRedis.setTime(cmUserDetailRedis.getValidDate());
        expireDateRedis.setTime(cmUserDetailRedis.getExpireDate());

        if ( cmUserDetailDB.getUserId().equals(cmUserDetailRedis.getUserId())
            && cmUserDetailDB.getAcctId().equals(cmUserDetailRedis.getAcctId())
            && cmUserDetailDB.getCustId().equals(cmUserDetailRedis.getCustId())
            && cmUserDetailDB.getMsisdn().equals(cmUserDetailRedis.getMsisdn())
            && cmUserDetailDB.getImsi().equals(cmUserDetailRedis.getImsi())
            && cmUserDetailDB.getChannelCode().equals(cmUserDetailRedis.getChannelCode())
            && validDateDB.equals(validDateRedis) && expireDateDB.equals(expireDateRedis))
        {
            return true;
        }
        return false;
    }

    public boolean compareProdInsInfo(CmProdInsInfo cmProdInsInfoDB,CmProdInsInfo cmProdInsInfoRedis)
    {
        Calendar validDateDB = Calendar.getInstance();
        Calendar validDateRedis = Calendar.getInstance();
        Calendar expireDateDB = Calendar.getInstance();
        Calendar expireDateRedis = Calendar.getInstance();
        validDateDB.setTime(cmProdInsInfoDB.getValidDate());
        expireDateDB.setTime(cmProdInsInfoDB.getExpireDate());
        validDateRedis.setTime(cmProdInsInfoRedis.getValidDate());
        expireDateRedis.setTime(cmProdInsInfoRedis.getExpireDate());

        if ( cmProdInsInfoDB.getUserId().equals(cmProdInsInfoRedis.getUserId())
                && cmProdInsInfoDB.getRectype().equals(cmProdInsInfoRedis.getRectype())
                && cmProdInsInfoDB.getProductFee().compareTo(cmProdInsInfoRedis.getProductFee()) == 0
                && cmProdInsInfoDB.getProductId().equals(cmProdInsInfoRedis.getProductId())
                && cmProdInsInfoDB.getProductInsId().equals(cmProdInsInfoRedis.getProductInsId())
                && validDateDB.equals(validDateRedis) && expireDateDB.equals(expireDateRedis))
        {
            return true;
        }
        return false;
    }

    public boolean compareFreeRes(ApsFreeRes apsFreeResDB,Map freeResMapRedis) throws ParseException {
        SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar validDateDB = Calendar.getInstance();
        Calendar validDateRedis = Calendar.getInstance();
        Calendar expireDateDB = Calendar.getInstance();
        Calendar expireDateRedis = Calendar.getInstance();
        validDateDB.setTime(apsFreeResDB.getValidDate());
        expireDateDB.setTime(apsFreeResDB.getExpireDate());
        validDateRedis.setTime(std.parse(freeResMapRedis.get("validDate").toString()));
        expireDateRedis.setTime(std.parse(freeResMapRedis.get("expireDate").toString()));

        if ( apsFreeResDB.getUserId().equals(Long.valueOf(freeResMapRedis.get("userId").toString()))
                && apsFreeResDB.getItemId().equals(Long.valueOf(freeResMapRedis.get("itemId").toString()))
                && apsFreeResDB.getUsedValue().compareTo(new BigDecimal(freeResMapRedis.get("usedValue").toString())) == 0
                && apsFreeResDB.getAmount().compareTo(new BigDecimal(freeResMapRedis.get("amount").toString())) == 0
                && apsFreeResDB.getMeasureId().compareTo(new BigDecimal(freeResMapRedis.get("measureId").toString())) == 0
                && validDateDB.equals(validDateRedis) && expireDateDB.equals(expireDateRedis))
        {
            return true;
        }
        return false;
    }

    public boolean compareBalanceFee(ApsBalanceFee apsBalanceFeeDB,Map apsBalanceFeeRedis) throws ParseException {
        SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar updateDateDB = Calendar.getInstance();
        Calendar updateDateRedis = Calendar.getInstance();
        updateDateDB.setTime(apsBalanceFeeDB.getUpdateTime());
        updateDateRedis.setTime(std.parse(apsBalanceFeeRedis.get("updateTime").toString()));


        if ( apsBalanceFeeDB.getUserId().equals(Long.valueOf(apsBalanceFeeRedis.get("userId").toString()))
                && apsBalanceFeeDB.getRemainFee().compareTo(new BigDecimal(apsBalanceFeeRedis.get("remainFee").toString())) == 0
                && apsBalanceFeeDB.getMeasureId().compareTo(new BigDecimal(apsBalanceFeeRedis.get("measureId").toString())) == 0
                && updateDateDB.equals(updateDateRedis) )
        {
            return true;
        }
        return false;
    }

    public void freeResInsertRedis(String freeReskey ,ApsFreeRes apsFreeRes)
    {
        redisTemplate.opsForHash().put(freeReskey,"userId",apsFreeRes.getUserId().toString());
        redisTemplate.opsForHash().put(freeReskey,"itemId",apsFreeRes.getItemId().toString());
        redisTemplate.opsForHash().put(freeReskey,"usedValue",apsFreeRes.getUsedValue().toString());
        redisTemplate.opsForHash().put(freeReskey,"amount",apsFreeRes.getAmount().toString());
        redisTemplate.opsForHash().put(freeReskey,"measureId",apsFreeRes.getMeasureId().toString());
        redisTemplate.opsForHash().put(freeReskey,"validDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(apsFreeRes.getValidDate()));
        redisTemplate.opsForHash().put(freeReskey,"expireDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(apsFreeRes.getExpireDate()));

    }
    public void balanceFeeInsertRedis(String balanceTableKey,ApsBalanceFee apsBalanceFee){
        redisTemplate.opsForHash().put(balanceTableKey,"userId",apsBalanceFee.getUserId().toString());
        redisTemplate.opsForHash().put(balanceTableKey,"remainFee",apsBalanceFee.getRemainFee().toString());
        redisTemplate.opsForHash().put(balanceTableKey,"measureId",apsBalanceFee.getMeasureId().toString());
        redisTemplate.opsForHash().put(balanceTableKey,"updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(apsBalanceFee.getUpdateTime()));

    }


}
