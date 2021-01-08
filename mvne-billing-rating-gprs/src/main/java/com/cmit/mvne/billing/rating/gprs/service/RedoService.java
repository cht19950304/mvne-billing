package com.cmit.mvne.billing.rating.gprs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.rating.gprs.dto.RedoDto;
import com.cmit.mvne.billing.rating.gprs.dto.ReratDto;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsDto;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsErrDto;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsReratDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprsError;
import com.cmit.mvne.billing.user.analysis.entity.RatingCdrGprsRerat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/7 9:39
 */
public interface RedoService {
    //IPage<CdrGprsError> queryGprsErr(RedoDto redoDto, Boolean isQuery);
    IPage<QueryCdrGprsErrDto> gprsErrForQuery(RedoDto redoDto);
    IPage<CdrGprsError> gprsErrForRedo(RedoDto redoDto);

    //IPage<CdrGprs> queryGprsCdr(ReratDto reratDto);
    IPage<QueryCdrGprsDto> gprsCdrForQuery(ReratDto reratDto);
    IPage<CdrGprs> gprsCdrForRerat(ReratDto reratDto);

    //IPage<RatingCdrGprsRerat> queryGprsRerat(ReratDto reratDto);
    IPage<QueryCdrGprsReratDto> gprsReratForQuery(ReratDto reratDto);
    IPage<RatingCdrGprsRerat> gprsReratForRerat(ReratDto reratDto);

    //IPage<CdrGprsError> queryGprsErr(String msisdn, Date finishDateS, Date finishDateE, String originalFile, String errCode, String redoFlag, List<Long> tableIdList, int page, int size);

    //IPage<CdrGprs> queryGprsCdr(String tableDate, Date createDateS, Date createDateE, Date finishDateS, Date finishDateE, String msisdn, Long productId, Long itemId, List<Long> idList, int page, int size);

    //List<CdrGprsError> queryGprsErr(String msisdn, Date finishTimeS, Date finishTimeE, String originalFile, String errCode, String redoFlag, List<Long> tableIdList);

    //List<CdrGprs> queryGprsCdr(String tableDate, Date createDateS, Date createDateE, Date finishDateS, Date finishDateE, String msisdn, Long productId, Long itemId, List<Long> idList);

    //List<RatingCdrGprsRerat> queryReratGprsCdr(String tableDate, Date createDateS, Date createDateE, Date finishDateS, Date finishDateE, String msisdn, Long productId, Long itemId, String reratStatus, List<Long> idList);

    HashMap<String, Integer> redoGprs(List<CdrGprsError> errList) throws MvneException, InterruptedException;

    HashMap<String, Integer> redoGprs(String whereSql) throws MvneException, InterruptedException;

    HashMap<String, Integer> reratGprs(String date, String reratSql) throws MvneException, InterruptedException;

    HashMap<String, Integer> reratGprs(String date, List<RatingCdrGprsRerat> ratingCdrGprsReratList) throws MvneException, InterruptedException;

    //Boolean moveSelectedGprsCdr(String date, List<Long> ids);

    Boolean moveGprsCdr(List<CdrGprs> cdrGprsList);

    Boolean rerat2rec();
}
