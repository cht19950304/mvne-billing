package com.cmit.mvne.billing.rating.gprs.controller;

import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.cmit.mvne.billing.rating.gprs.util.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.rating.gprs.common.MvneCrmResponse;
import com.cmit.mvne.billing.rating.gprs.config.MyBatisPlusConfig;
import com.cmit.mvne.billing.rating.gprs.dto.RedoDto;
import com.cmit.mvne.billing.rating.gprs.dto.ReratDto;
import com.cmit.mvne.billing.rating.gprs.service.RedoService;
import com.cmit.mvne.billing.rating.gprs.util.DateUtils;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsDto;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsErrDto;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsReratDto;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprsError;
import com.cmit.mvne.billing.user.analysis.entity.RatingCdrGprsRerat;
import com.cmit.mvne.billing.user.analysis.service.CdrGprsErrorService;
import com.cmit.mvne.billing.user.analysis.service.CdrGprsService;
import com.cmit.mvne.billing.user.analysis.service.RatingCdrGprsReratService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description: 重处理及重批接口
 * @Date: Create in 2020/5/7 9:29
 */

@Slf4j
@RestController
@RequestMapping("/redoManagement")
public class RedoManagementController {
    @Autowired
    CdrGprsErrorService cdrGprsErrorService;
    @Autowired
    CdrGprsService cdrGprsService;
    @Autowired
    RedoService redoService;
    @Autowired
    RatingCdrGprsReratService ratingCdrGprsReratService;

    /**
     * 重处理共一个查询接口，三个重处理接口，一个导出接口
     */

    /**
     * 根据条件查询出错单，用于重处理
     * @param redoDto
     * @return
     */
    @RequestMapping(value = "/redoByCon/queryErr/gprs", method = RequestMethod.POST)
    MvneCrmResponse queryGprsRedo(@RequestBody @Validated RedoDto redoDto) {

        try {
            IPage<QueryCdrGprsErrDto> cdrGprsErrorIPage = redoService.gprsErrForQuery(redoDto);
            return new MvneCrmResponse().success().data(cdrGprsErrorIPage);
         } catch (Exception e) {
            log.error("Query gprs err failed :", e);
            return new MvneCrmResponse().fail().message("Query gprs err failed. Please check log!");
        }

    }

    /**
     * 1、全选的时候，需要根据条件，查询出错单进行重处理
     * @param redoDto
     * @return
     * @throws MvneException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/redoByCon/gprs", method = RequestMethod.POST)
    MvneCrmResponse redoGprsByCond(@RequestBody @Validated RedoDto redoDto) throws MvneException, InterruptedException {
        try {
            List<CdrGprsError> errList = redoService.gprsErrForRedo(redoDto).getRecords();
            HashMap<String, Integer> returnMap = redoService.redoGprs(errList);
            return new MvneCrmResponse().success().data(returnMap);
        } catch (Exception e) {
            log.error("Redo gprs err failed :", e);
            return new MvneCrmResponse().fail().message("Redo gprs err failed. Please check log!");
        }

    }

    /**
     * 2、勾选的情况，前台根据查询出来的错单及用户的勾选把tableId传到后台进行重处理
     * @param redoDto
     * @return
     * @throws MvneException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/redoByIds/gprs", method = RequestMethod.POST)
    MvneCrmResponse redoGprsByIds(@RequestBody RedoDto redoDto) throws MvneException, InterruptedException {
        try {
            List<CdrGprsError> errList = redoService.gprsErrForRedo(redoDto).getRecords();
            HashMap<String, Integer> returnMap = redoService.redoGprs(errList);
            return new MvneCrmResponse().success().data(returnMap);
        } catch (Exception e) {
            log.error("Query gprs err failed :", e);
            return new MvneCrmResponse().fail().message("Redo gprs err failed. Please check log!");
        }
    }

    /**
     * 3、根据传入的sql查询错单，用于错单的后台处理
     * @param whereSql
     * @return
     * @throws MvneException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/redoBySql/gprs", method = RequestMethod.POST)
    MvneCrmResponse redoGprsBySql(@RequestParam(value = "whereSql") String whereSql) throws MvneException, InterruptedException {
        // 防注入检测
        String selectSql = "select * from rating_cdr_gprs_error " + whereSql;
        List<CdrGprsError> errList = cdrGprsErrorService.errRedoSelect(selectSql);

        try {
            HashMap<String, Integer> returnMap = redoService.redoGprs(errList);
            return new MvneCrmResponse().success().data(returnMap);
        } catch (Exception e) {
            log.error("Query gprs err failed :", e);
            return new MvneCrmResponse().fail().message("Redo gprs err failed. Please check log!");
        }

    }

    /**
     * 4、根据条件查询错单，并导出成excel返回给前台
     * @param whereSql
     * @return
     * @throws MvneException
     * @throws InterruptedException
     */
    /*@RequestMapping(value = "/redoExport/gprs", method = RequestMethod.POST)
    MvneCrmResponse exportGprsRedo(@RequestParam(value = "whereSql") String whereSql) throws MvneException, InterruptedException {
        return new MvneCrmResponse();
    }*/

    /** 以下为重批接口 **/

    /**
     * 根据传入的参数从详单表查询指定日期的详单
     * @param reratDto
     * @return
     */
    @RequestMapping(value = "/rerat/queryCdr/gprs", method = RequestMethod.POST)
    MvneCrmResponse queryGprsCdr(@RequestBody @Validated ReratDto reratDto) {

        try {
            IPage<QueryCdrGprsDto> cdrGprsIPage = redoService.gprsCdrForQuery(reratDto);

            return new MvneCrmResponse().success().data(cdrGprsIPage);
        } catch (Exception e) {
            log.error("Query gprs err failed :", e);
            return new MvneCrmResponse().fail().message("Query gprs cdr failed. Please check log!");
        }

    }

    /**
     * 1、根据用户勾选的详单进行迁移
     * @param reratDto
     * @return
     */
    @RequestMapping(value = "/rerat/moveSelected/gprs", method = RequestMethod.POST)
    MvneCrmResponse reratMoveSelectedGprsCdr(@RequestBody @Validated ReratDto reratDto) {
        List<CdrGprs> cdrGprsList = redoService.gprsCdrForRerat(reratDto).getRecords();

        try {
            if (redoService.moveGprsCdr(cdrGprsList)) {
                return new MvneCrmResponse().success().message("Move gprs cdr to rerat succeed!");
            } else {
                return new MvneCrmResponse().fail().message("Move gprs cdr to rerat failed. May be none selected or other reasons.");
            }
        } catch (Exception e) {
            log.error("Move gprs cdr to rerat failed.", e);
            return new MvneCrmResponse().fail().message("Move gprs cdr to rerat failed.Please check log!");
        }
    }

    /**
     * 2、根据条件查询详单进行迁移
     * @param reratDto
     * @return
     */
    @RequestMapping(value = "/rerat/moveAll/gprs", method = RequestMethod.POST)
    MvneCrmResponse reratMoveAlldGprsCdr(@RequestBody @Validated ReratDto reratDto) {

        // 根据条件从详单表查询
        List<CdrGprs> cdrGprsList = redoService.gprsCdrForRerat(reratDto).getRecords();

        try {
            redoService.moveGprsCdr(cdrGprsList);
            return new MvneCrmResponse().success().message("Move gprs cdr to rerat succeed!");
        } catch (Exception e) {
            log.error("Move gprs cdr to rerat failed.", e);
            return new MvneCrmResponse().fail().message("Move gprs cdr to rerat failed.Please check log!");
        }
    }

    /**
     * 根据条件从重批表中查询详单，重批表的查询不校验tableDate
     * @param reratDto
     * @return
     */
    @RequestMapping(value = "/rerat/queryReratCdr/gprs", method = RequestMethod.POST)
    MvneCrmResponse queryReratGprsCdr(@RequestBody ReratDto reratDto) {
        try {
            IPage<QueryCdrGprsReratDto> ratingCdrGprsReratIpage = redoService.gprsReratForQuery(reratDto);
            return new MvneCrmResponse().success().data(ratingCdrGprsReratIpage);
        } catch (Exception e) {
            log.error("Query gprs err failed :", e);
            return new MvneCrmResponse().fail().message("Query gprs rerat failed. Please check log.");
        }
    }

    /**
     * 根据勾选的重批表详单进行重批
     * @param reratDto
     * @return
     */
    @RequestMapping(value = "/rerat/reratByIds/gprs", method = RequestMethod.POST)
    MvneCrmResponse reratGprsByIds(@RequestBody ReratDto reratDto) {
        String tableDate = reratDto.getTableDate();
        List<RatingCdrGprsRerat> ratingCdrGprsReratList = redoService.gprsReratForRerat(reratDto).getRecords();

        try {
            HashMap<String, Integer> resultMap = redoService.reratGprs(tableDate, ratingCdrGprsReratList);
            return new MvneCrmResponse().success().data(resultMap);
        } catch (Exception e) {
            log.error("Rerat gprs failed!", e);
            return new MvneCrmResponse().fail().message("Rerat gprs failed! Please check log.");
        }
    }

    /**
     * 全选，根据条件查询重批表详单进行重批
     * @param reratDto
     * @return
     */
    @RequestMapping(value = "/rerat/reratByCon/gprs", method = RequestMethod.POST)
    MvneCrmResponse reratGprsByCon(@RequestBody ReratDto reratDto) {
        String tableDate = reratDto.getTableDate();
        List<RatingCdrGprsRerat> ratingCdrGprsReratList = redoService.gprsReratForRerat(reratDto).getRecords();
        try {
            HashMap<String, Integer> resultMap = redoService.reratGprs(tableDate, ratingCdrGprsReratList);
            return new MvneCrmResponse().success().data(resultMap);
        } catch (Exception e) {
            log.error("Redo catch MVNEException!", e);
            return new MvneCrmResponse().fail().message("Rerat gprs failed! Please check log.");
        }

    }


    /**
     * 考虑到性能的话，可以起一个不处理话单的进程，专门用来重批
     * 对开始日期到结束日期（左闭右闭）符合条件的详单进行回捞
     * 不建议一次性处理太多详单
     * @param startDate 开始日期，格式必须为‘yyyy-mm-dd'
     * @param endDate 结束日期，格式必须为'yyyy-mm-dd'
     * @param whereSql 条件语句，不包括';'
     * @return
     */
    @RequestMapping(value = "/reratBySql/gprs", method = RequestMethod.POST)
    MvneCrmResponse reratGprsBySql(@RequestParam(value = "startDate") String startDate,
                     @RequestParam(value = "endDate") String endDate,
                     @RequestParam(value = "whereSql") String whereSql) {
        // 防注入检测

        DateTimeFormatter dateDtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate start = LocalDate.parse(startDate, dateDtf);
        LocalDate end = LocalDate.parse(endDate, dateDtf);

        long distance = ChronoUnit.DAYS.between(start, end);
        if (distance < 0) {
            return new MvneCrmResponse().message("endDate should be later than startDate");
        }

        HashMap<String, Integer> resultMap = null;
        try {
            for (LocalDate cur = start; cur.compareTo(end) <= 0; cur = cur.plusDays(1)) {
                System.out.println(cur);
                String date = cur.format(dateDtf);
                LocalDateTime now = LocalDateTime.now();
                String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            /*
            ！！！这里不要使用*，用字段！！！
             */
                String cdrSql = "select * from rating_cdr_gprs_" + date + " " + whereSql;
                log.info("Rerat query sql: '{}'", cdrSql);
                // 查询
                List<CdrGprs> cdrGprsList = cdrGprsService.cdrReratSelect(cdrSql);
                // 挪到重批表
                MyBatisPlusConfig.tableNameHolder.set(date);
                Boolean haveMoved = redoService.moveGprsCdr(cdrGprsList);

                if (haveMoved) {
                    // 从重批表查询
                    String reratSql = "select * from rating_cdr_gprs_rerat " + whereSql;
                    List<RatingCdrGprsRerat> ratingCdrGprsReratList = ratingCdrGprsReratService.reratSelect(reratSql);

                    resultMap = redoService.reratGprs(date, ratingCdrGprsReratList);

                }
            }
        } catch (Exception e) {
            log.error("Redo catch Exception!", e);
            return new MvneCrmResponse().fail().message("Rerat gprs failed! Please check log.");
        }

        if (resultMap == null) {
            return new MvneCrmResponse().fail().message("Rerat gprs failed! Please check log.");
        } else {
            return new MvneCrmResponse().success().data(resultMap);
        }

        /*// 日期遍历
        Stream.iterate(start, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            String date = f.format(dateDtf);
            LocalDateTime now = LocalDateTime.now();
            String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            *//*
            ！！！这里不要使用*，用字段！！！
             *//*
            String cdrSql = "select * from rating_cdr_gprs_" + date + " " + whereSql;
            log.info("Rerat query sql: '{}'", cdrSql);
            // 查询
            List<CdrGprs> cdrGprsList = cdrGprsService.cdrReratSelect(cdrSql);
            // 挪到重批表
            MyBatisPlusConfig.tableNameHolder.set(date);
            Boolean haveMoved = redoService.moveGprsCdr(cdrGprsList);

            if (haveMoved) {
                // 从重批表查询
                String reratSql = "select * from rating_cdr_gprs_rerat " + whereSql;
                List<RatingCdrGprsRerat> ratingCdrGprsReratList = ratingCdrGprsReratService.reratSelect(reratSql);

                try {
                    this.resultMap = redoService.reratGprs(date, ratingCdrGprsReratList);
                } catch (MvneException e) {
                    log.error("Redo catch MVNEException!", e);
                } catch (InterruptedException e) {
                    log.error("Redo catch InterruptedException!", e);
                }

            }
        });

        if (this.resultMap == null) {
            return new MvneCrmResponse().fail().message("Rerat gprs failed! Please check log.");
        } else {
            return new MvneCrmResponse().success().data(resultMap);
        }*/

    }

    /**
     * 将重批表的数据迁到重批记录表
     */
    @RequestMapping(value = "/rerat/rerat2rec/gprs", method = RequestMethod.POST)
    public MvneCrmResponse rerat2rec() {
        try {
            if (redoService.rerat2rec()) {
                return new MvneCrmResponse().success().message("Move rerat gprs to rec succeed!");
            }
        } catch (Exception e) {
            log.error("Move rerat gprs to rec failed!", e);
        }

        return new MvneCrmResponse().fail().message("Move rerat gprs to rec failed!");
    }

    @RequestMapping(value = "/redo/exportSelectedErr/gprs", method = RequestMethod.POST)
    public void exportSelectedErr(HttpServletResponse response, @RequestBody RedoDto redoDto) {
        String date = DateUtils.Date2Str(new Date(), "yyyyMMddHHmmss");
        List<Long> tableIdList = redoDto.getTableIdList();
        // 查询
        List<CdrGprsError> cdrGprsErrorList = redoService.gprsErrForRedo(redoDto).getRecords();

        try {
            String fileName = URLEncoder.encode("Billing_Error_Record_" + date + ".xlsx", "UTF-8");
            exportExcel(response, fileName, cdrGprsErrorList, CdrGprsError.class);
        } catch (Exception e) {
            log.error("Export Error!", e);
        }
    }

    @RequestMapping(value = "/redo/exportAllErr/gprs", method = RequestMethod.POST)
    public void exportAllErr(HttpServletResponse response, @RequestBody @Validated RedoDto redoDto) {
        String date = DateUtils.Date2Str(new Date(), "yyyyMMddHHmmss");
        // 查询
        List<CdrGprsError> cdrGprsErrorList = redoService.gprsErrForRedo(redoDto).getRecords();

        try {
            String fileName = URLEncoder.encode("Billing_Error_Record_" + date + ".xlsx", "UTF-8");
            exportExcel(response, fileName, cdrGprsErrorList, CdrGprsError.class);
        } catch (Exception e) {
            log.error("Export Error!", e);
        }

    }

    @RequestMapping(value = "/rerat/exportSelectedCdr/gprs", method = RequestMethod.POST)
    public void exportSelectedCdr(HttpServletResponse response, @RequestBody @Validated ReratDto reratDto) {
        String date = DateUtils.Date2Str(new Date(), "yyyyMMddHHmmss");
        // 查询
        List<CdrGprs> cdrGprsList = redoService.gprsCdrForRerat(reratDto).getRecords();

        try {
            String fileName = URLEncoder.encode("Billing_Records_" + date + ".xlsx", "UTF-8");
            exportExcel(response, fileName, cdrGprsList, CdrGprs.class);
        } catch (Exception e) {
            log.error("Export Error!", e);
        }

    }

    @RequestMapping(value = "/rerat/exportAllCdr/gprs", method = RequestMethod.POST)
    public void exportAllCdr(HttpServletResponse response, @RequestBody @Validated ReratDto reratDto) {
        String date = DateUtils.Date2Str(new Date(), "yyyyMMddHHmmss");
        // 查询
        List<CdrGprs> cdrGprsList = redoService.gprsCdrForRerat(reratDto).getRecords();

        try {
            String fileName = URLEncoder.encode("Billing_Records_" + date + ".xlsx", "UTF-8");
            exportExcel(response, fileName, cdrGprsList, CdrGprs.class);
        } catch (Exception e) {
            log.error("Export Error!", e);
        }

    }

    @RequestMapping(value = "/rerat/exportSelectedRerat/gprs", method = RequestMethod.POST)
    public void exportSelectedRerat(HttpServletResponse response, @RequestBody ReratDto reratDto) {
        String date = DateUtils.Date2Str(new Date(), "yyyyMMddHHmmss");
        // 查询
        List<RatingCdrGprsRerat> ratingCdrGprsReratList = redoService.gprsReratForRerat(reratDto).getRecords();

        try {
            String fileName = URLEncoder.encode("To_Rerat_Records_" + date + ".xlsx", "UTF-8");
            exportExcel(response, fileName, ratingCdrGprsReratList, RatingCdrGprsRerat.class);
        } catch (Exception e) {
            log.error("Export Error!", e);
        }

    }

    @RequestMapping(value = "/rerat/exportAllRerat/gprs", method = RequestMethod.POST)
    public void exportAllRerat(HttpServletResponse response, @RequestBody ReratDto reratDto) {
        String date = DateUtils.Date2Str(new Date(), "yyyyMMddHHmmss");
        // 查询
        List<RatingCdrGprsRerat> ratingCdrGprsReratList = redoService.gprsReratForRerat(reratDto).getRecords();

        try {
            String fileName = URLEncoder.encode("To_Rerat_Records_" + date + ".xlsx", "UTF-8");
            exportExcel(response, fileName, ratingCdrGprsReratList, RatingCdrGprsRerat.class);
        } catch (Exception e) {
            log.error("Export Error!", e);
        }

    }

    /*@RequestMapping(value = "/rerat/testExport", method = RequestMethod.GET)
    public void testExport(HttpServletResponse response) {
        String date = DateUtils.Date2Str(new Date(), "yyyyMMddHHmmss");
        // 查询
        ReratDto reratDto = new ReratDto();
        reratDto.setTableDate("20200325");
        List<RatingCdrGprsRerat> ratingCdrGprsReratList = redoService.gprsReratForRerat(reratDto).getRecords();

        try {
            String fileName = URLEncoder.encode("Re-rating_and_the_result_Query_" + date + ".xlsx", "UTF-8");
            exportExcel(response, fileName, ratingCdrGprsReratList, RatingCdrGprsRerat.class);
        } catch (Exception e) {
            log.error("Export Error!", e);
        }

    }*/

    /*private <T> void exportExcel(HttpServletResponse response, String fileName, List<T> exportList, Class clazz) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系

        String str = "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso-8859-1");
        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        response.addHeader("Content-disposition", str);

        EasyExcel.write(response.getOutputStream(), clazz).sheet("sheet1").doWrite(exportList);
    }*/

    private <T> void exportExcel(HttpServletResponse response, String fileName, List<T> exportList, Class clazz) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系

        String str = "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso-8859-1");
        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        response.addHeader("Content-disposition", str);

        //单元格宽度自动适应数据长度
        EasyExcel.write(response.getOutputStream(), clazz).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("sheet1").doWrite(exportList);

//        //单元格宽度默认
//        EasyExcel.write(response.getOutputStream(), clazz).sheet("sheet1").doWrite(exportList);
    }


}
