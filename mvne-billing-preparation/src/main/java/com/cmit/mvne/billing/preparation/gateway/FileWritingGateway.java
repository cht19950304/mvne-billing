package com.cmit.mvne.billing.preparation.gateway;

import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/1
 */

public interface FileWritingGateway {

    File writeToFile(@Header(FileHeaders.FILENAME) String filename,
                     @Header("directory") File directory, String data);

}
