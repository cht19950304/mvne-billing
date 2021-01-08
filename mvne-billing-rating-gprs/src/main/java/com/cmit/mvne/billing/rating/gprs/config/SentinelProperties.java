package com.cmit.mvne.billing.rating.gprs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/3/11 9:02
 */
@Component
public class SentinelProperties {
    @Value("${lettuce.sentinel.master}")
    private String master;
    @Value("${lettuce.sentinel.nodes}")
    private String nodes;

    private Set<String> hosts;

    @PostConstruct
    public void hosts() {
        hosts = new HashSet<>();
        hosts.addAll(Arrays.asList(nodes.split(",")));
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public Set<String> getHosts() {
        return hosts;
    }

    public void setHosts(Set<String> hosts) {
        this.hosts = hosts;
    }

}
