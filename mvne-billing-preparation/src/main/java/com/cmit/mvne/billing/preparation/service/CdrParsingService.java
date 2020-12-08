package com.cmit.mvne.billing.preparation.service;

import com.cmit.mvne.billing.preparation.entity.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/19
 */
public interface CdrParsingService {
    void parsing(CdrFile cdrFile) throws IOException;

    String[] parseToArray(String line);

    Date getLocalTime(String[] cdr);

    void doTransformCdr(String[] cdr, long lineNum, String originFilename, Date receiveTime, List<CdrGprsRating> cdrGprsRatingList, List<CdrGprsSettle> cdrGprsSettleList, List<CdrSmsRating> cdrSmsRatingList, List<CdrSmsSettle> cdrSmsSettleList);
}
