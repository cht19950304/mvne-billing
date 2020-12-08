package com.cmit.mvne.billing.preparation.service;

import java.util.Date;
import java.util.Set;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public interface CdrDupCheckService {
    String checkDup(String[] cdr, String originFilename, Date receiveDate, Set<String> cdrsThisTime, long lineNum);

    void cleanDupValue(String filename);

    void createRedoLog(String originFilename);

    void deleteRedoLog(String originFilename);

    void deleteDupLog(String originFilename);

    Boolean existRedoLog(String originFilename);

    String checkGprsDup(String[] cdr, String originFilename, Date receiveDate, Set<String> cdrsThisTime, long lineNum);

    String getGprsIdentify(String[] cdr);

    String getGprsIdentifyWithNum(String[] cdr, long lineNum);

    String checkSmsDup(String[] cdr, String originFilename, Date receiveDate, Set<String> cdrsThisTime, long lineNum);

    String getSmsIdentify(String[] cdr);

    String getSmsIdentifyWithNum(String[] cdr, long lineNum);

    Boolean dupOrNot(Boolean haveDealThisTime, Boolean haveDealBefore, Boolean isDup, long number);
}
