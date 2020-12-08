package com.cmit.mvne.billing.infomanage.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    /*@Value(value = "${spring.redis.cluster.nodes}")
    private String cluster;*/

    @Value(value = "${spring.redis.host}")
    private String host;

    @Value(value = "${spring.redis.port}")
    private String port;

    @Value(value = "${spring.redis.password}")
    private String password;

    @Value(value = "${spring.redis.timeout}")
    private int timeout;

    /*@Bean
    public RedissonClient redissonClusterClient() {
        String[] nodes = cluster.split(",");
        //redisson版本是3.11，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
        for(int i=0;i<nodes.length;i++){
            nodes[i] = "redis://"+nodes[i];
        }

        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000)  //设置集群状态扫描时间
                .addNodeAddress(nodes)
                .setPassword(password)
                .setConnectTimeout(timeout);
        return Redisson.create(config);
    }*/

    @Bean
    public RedissonClient redissonSingleClient() {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port)
                .setConnectTimeout(timeout)
                .setPassword(password)
                .setTimeout(timeout);

        return Redisson.create(config);
    }
}
