package com.gsonkeno.solrtraining;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件映射类
 * @author gaosong
 * @since 2018-10-12
 */
@ConfigurationProperties(prefix="spring.solr")
public class SolrConfig {
    private String host;
    private String zkHost;
    private String defaultCollection;
    public String getDefaultCollection() {
        return defaultCollection;
    }
    public void setDefaultCollection(String defaultCollection) {
        this.defaultCollection = defaultCollection;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getZkHost() {
        return zkHost;
    }
    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }
}
