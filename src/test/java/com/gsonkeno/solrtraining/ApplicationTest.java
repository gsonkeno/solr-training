package com.gsonkeno.solrtraining;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private CloudSolrClient client;

    @Test
    public void testQuery() throws IOException, SolrServerException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("q","item_title:大衣");
        QueryResponse response = client.query("item_v2", params);
        System.out.println(response);
    }

}
