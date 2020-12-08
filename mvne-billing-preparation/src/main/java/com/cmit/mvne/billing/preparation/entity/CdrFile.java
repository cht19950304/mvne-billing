package com.cmit.mvne.billing.preparation.entity;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 话单文件
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/1/6
 */
@Data
public class CdrFile {

    private File originFile = null;
    private File workFile = null;

    private List<CdrGprsRating> cdrGprsRatingList = new ArrayList<>();
    private List<CdrGsmRating> cdrGsmRatingList = new ArrayList<>();
    private List<CdrMmsRating> cdrMmsRatingList = new ArrayList<>();
    private List<CdrSmsRating> cdrSmsRatingList = new ArrayList<>();

    private List<CdrGprsSettle> cdrGprsSettleList = new ArrayList<>();
    private List<CdrGsmSettle> cdrGsmSettleList = new ArrayList<>();
    private List<CdrMmsSettle> cdrMmsSettleList = new ArrayList<>();
    private List<CdrSmsSettle> cdrSmsSettleList = new ArrayList<>();

    private List<String> errorCdrList = new ArrayList<>();
    private List<String> successCdrList = new ArrayList<>();

    public CdrFile(File originFile, File workFile) {
        this.originFile = originFile;
        this.workFile = workFile;
    }
}



