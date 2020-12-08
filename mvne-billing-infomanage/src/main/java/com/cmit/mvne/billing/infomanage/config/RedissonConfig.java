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

    /*@Value(value = "${spring.redis.host}")
    private String host;

    @Value(value = "${spring.redis.port}")
    private String port;

    @Value(value = "${spring.redis.password}")
    private String password;

    @Value(value = "${spring.redis.timeout}")
    private int timeout;*/

    @Value(value = "${lettuce.sentinel.master}")
    private String sentinelMaster;
    @Value(value = "${lettuce.sentinel.node1}")
    private String node1;
    @Value(value = "${lettuce.sentinel.port1}")
    private Integer port1;
    @Value(value = "${lettuce.sentinel.node2}")
    private String node2;
    @Value(value = "${lettuce.sentinel.port1}")
    private Integer port2;
    @Value(value = "${lettuce.sentinel.node3}")
    private String node3;
    @Value(value = "${lettuce.sentinel.port1}")
    private Integer port3;
    @Value(value = "${spring.redis.password}")
    private String password;

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

    /*@Bean
    public RedissonClient redissonSingleClient() {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port)
                .setConnectTimeout(timeout)
                .setPassword(password)
                .setTimeout(timeout);
                //.setPingConnectionInterval(60)

        return Redisson.create(config);
    }*/

    @Bean
    public RedissonClient redissonMainClient() {

        Config config = new Config();
        config.useSentinelServers()
                .setMasterName(sentinelMaster)
                .setPassword(password)
                .setMasterConnectionMinimumIdleSize(10)
                .setSlaveConnectionMinimumIdleSize(10)
                .addSentinelAddress("redis://" + node1 + ":" + port1)
                .addSentinelAddress("redis://" + node2 + ":" + port2)
                .addSentinelAddress("redis://" + node3 + ":" + port3);

        return Redisson.create(config);
    }
}
