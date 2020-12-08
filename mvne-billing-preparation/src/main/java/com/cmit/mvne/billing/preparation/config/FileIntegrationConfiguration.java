package com.cmit.mvne.billing.preparation.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 话单解码、查重、校验、上发环节配置
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2019/7/30
 */
@Configuration
@ConditionalOnProperty(prefix = "mvne.process", name = "enabled", havingValue = "true", matchIfMissing = true)
@ImportResource(locations = "classpath:META-INF/spring/integration/FileIntegration.xml")
public class FileIntegrationConfiguration {

//    /**
//     * 正则表达式
//     * 以CD开始并且不以.writing结尾
//     */
//    private String pattern = "^CD((?!\\.writing$).)*$";
//
//    @Autowired
//    private PathProperties pathProperties;
//
//    @Bean
//    public MessageChannel fileInputChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
//    public MessageSource<File> fileReadingMessageSource() {
//        FileReadingMessageSource source = new FileReadingMessageSource();
//        source.setDirectory(new File(pathProperties.getProcessPath()));
//
//        CompositeFileListFilter compositeFileListFilter = new CompositeFileListFilter();
//        compositeFileListFilter.addFilters(
//                Arrays.asList(
//                    new AcceptOnceFileListFilter(),
//                    new RegexPatternFileListFilter(pattern)
//
//        ));
//        source.setFilter(compositeFileListFilter);
//
//        return source;
//    }
//
//    /**
//     * 话单处理,包括校验、查重、分拣、上发
//     * @return message handler
//     */
//    @Bean
//    @ServiceActivator(inputChannel = "fileInputChannel")
//    public MessageHandler proccessHandler() {
//        return new ProccessHandler();
//    }


//    @Bean
//    @ServiceActivator(inputChannel = "writeToFileChannel")
//    public MessageHandler fileWritingMessageHandler() {
//        Expression directoryExpression = new SpelExpressionParser().parseExpression("headers.directory");
//        FileWritingMessageHandler handler = new FileWritingMessageHandler(directoryExpression);
//        handler.setFileExistsMode(FileExistsMode.REPLACE);
//        return handler;
//    }
//
//    @MessagingGateway(defaultRequestChannel = "writeToFileChannel")
//    public interface MyGateway {
//
//        void writeToFile(@Header(FileHeaders.FILENAME) String fileName,
//                         @Header(FileHeaders.FILENAME) File directory, String data);
//
//    }

}
