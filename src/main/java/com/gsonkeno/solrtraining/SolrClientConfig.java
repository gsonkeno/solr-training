//package com.gsonkeno.solrtraining;
//
//import org.apache.solr.client.solrj.impl.CloudSolrClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//
//@Configuration
//@EnableConfigurationProperties(SolrConfig.class)
//public class SolrClientConfig {
//    @Autowired
//    private SolrConfig solrConfig;
//    private CloudSolrClient solrClient;
//    @PreDestroy
//    public void close() {
//        if (this.solrClient != null) {
//            try {
//                this.solrClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    @Bean
//    public CloudSolrClient solrClient(){
//        if (StringUtils.hasText(this.solrConfig.getZkHost())) {
//            solrClient = new CloudSolrClient.Builder().withZkHost(this.solrConfig.getZkHost()).build();
//            solrClient.setDefaultCollection(this.solrConfig.getDefaultCollection());
//        }
//        return this.solrClient;
//    }
//
//}
