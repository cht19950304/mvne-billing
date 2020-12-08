/*package com.cmit.mvne.billing.preparation.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

*//**
 * 话单下载配置
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2019/7/30
 *//*
@Configuration
@ConditionalOnProperty(prefix = "mvne.collect", name = "enabled", havingValue = "true", matchIfMissing = true)
@ImportResource(locations = "classpath:META-INF/spring/integration/FtpIntegration.xml")
public class FtpIntegrationConfiguration {

//    @Autowired
//    private FtpProperties ftpProperties;
//
//    @Autowired
//    private PathProperties pathProperties;
//
//    @Bean
//    public SessionFactory<FTPFile> ftpFileSessionFactory() {
//        DefaultFtpSessionFactory ftpSessionFactory = new DefaultFtpSessionFactory();
//        ftpSessionFactory.setHost(ftpProperties.getHost());
//        ftpSessionFactory.setPort(ftpProperties.getPort());
//        ftpSessionFactory.setUsername(ftpProperties.getUsername());
//        ftpSessionFactory.setPassword(ftpProperties.getPassword());
//        ftpSessionFactory.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE);
//        ftpSessionFactory.setBufferSize(1024 * 1024);
//        ftpSessionFactory.setControlEncoding("UTF-8");
//
//        return new CachingSessionFactory<>(ftpSessionFactory);
//
//    }
//
//    @Bean
//    public FtpInboundFileSynchronizer ftpInboundFileSynchronizer() {
//
//        FtpInboundFileSynchronizer fileSynchronizer = new FtpInboundFileSynchronizer(ftpFileSessionFactory());
//
//        fileSynchronizer.setDeleteRemoteFiles(Boolean.TRUE);
//        fileSynchronizer.setRemoteDirectory(ftpProperties.getPath());
//        CompositeFileListFilter compositeFileListFilter = new CompositeFileListFilter();
//        compositeFileListFilter.addFilters(
//                Arrays.asList(
//                        new AcceptOnceFileListFilter(),
//                        new FtpSimplePatternFileListFilter("CD*")
//
//                ));
//        fileSynchronizer.setFilter(compositeFileListFilter);
//        fileSynchronizer.setTemporaryFileSuffix(".writing");
//
//        return fileSynchronizer;
//    }
//
//    @Bean
//    @InboundChannelAdapter(channel = "ftpChannel", poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "5"))
//    public MessageSource<File> ftpMessageSource() {
//        FtpInboundFileSynchronizingMessageSource source =
//                new FtpInboundFileSynchronizingMessageSource(ftpInboundFileSynchronizer());
//        source.setLocalDirectory(new File(pathProperties.getProcessPath()));
//        source.setAutoCreateLocalDirectory(true);
//        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
//
//        return source;
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "ftpChannel")
//    public MessageHandler decodeHandler() {
//
//        return (Message<?> message) -> log.info("download file {} successful!", (File)message.getPayload());
//    }
//


//    public static void main(String[] args) {
//        ConfigurableApplicationContext ctx =
//                new ClassPathXmlApplicationContext("META-INF/spring/integration/FtpIntegration.xml");
//
//    }
}*/
