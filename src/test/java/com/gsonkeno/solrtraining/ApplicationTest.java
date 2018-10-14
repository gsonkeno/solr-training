package com.gsonkeno.solrtraining;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    // 自动注入client,见配置项
    // spring.data.solr.host=http://127.0.0.1:8983/solr
    // spring.data.solr.zk-host=127.0.0.1:2181/rc
    @Autowired
    private CloudSolrClient client;

    // springboot + solr 测试新增
    // 更新也是该方法，若文档已存在，则覆盖而已
    @Test
    public void testAdd() throws IOException, SolrServerException {

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id",5);
        doc.addField("item_title","周杰伦的黑色毛衣");
        doc.addField("shop_name","周杰伦时尚店");
        doc.addField("img_url","http://www.baidu.com/197739.jpg");
        doc.addField("item_score",0.83);
        doc.addField("shop_score",0.83);
        doc.addField("sales_score",0.83);
        doc.addField("create_time",new Date());
        doc.addField("type",1);

        client.add("item_v2",doc);
        UpdateResponse response = client.commit("item_v2");
        System.out.println(response);
    }

    // springboot + solr 测试查询
    @Test
    public void testQuery() throws IOException, SolrServerException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("q","item_title:大衣");
        QueryResponse response = client.query("item_v2", params);
        System.out.println(response);
    }

    // springboot + solr 测试删除
    @Test
    public void testDel() throws IOException, SolrServerException {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        ids.add("3");
        ids.add("4");
        UpdateResponse updateResponse = client.deleteById("item_v2",ids);
        //这句很重要，否则不会删除
        client.commit("item_v2");
        System.out.println("删除结果:" + updateResponse);

    }

    // springboot + solr 测试删除
    @Test
    public void testDelByQuery() throws IOException, SolrServerException {
        UpdateResponse response = client.deleteByQuery("item_v2", "*:*");
        //这句很重要，否则不会删除
        client.commit("item_v2");
        System.out.println("根据查询删除:" + response);
    }



    // springboot + solr 全量索引
    @Test
    public void testFullIndex() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //默认情况下是/select,其值与solrconfig.xml内容有关,全量索引时，赋值/dataimport
        solrQuery.setRequestHandler("/dataimport");
        solrQuery.set("command", "full-import");

        QueryResponse response = client.query("item_v2", solrQuery);
        System.out.println(response);
    }

    // springboot + solr 增量索引
    @Test
    public void testDeltaIndex() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        //默认情况下是/select,其值与solrconfig.xml内容有关,增量索引时，赋值/dataimport
        solrQuery.setRequestHandler("/dataimport");
        //增量索引
        solrQuery.set("command", "delta-import");

        QueryResponse response = client.query("item_v2", solrQuery);
        System.out.println(response);
    }

    // springboot + solr 测试分词结果
    // 对应请求url http://localhost:8983/solr/item_v2/analysis/field?&analysis.fieldtype=text_ik&analysis.query=%E9%BB%91%E8%89%B2%E6%AF%9B%E8%A1%A3&wt=json
    @Test
    public void testAnalysis() throws IOException, SolrServerException {
        FieldAnalysisRequest analysisRequest = new FieldAnalysisRequest();
        ArrayList<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("text_ik");
        analysisRequest.setFieldTypes(fieldTypes);
        analysisRequest.setQuery("黑色毛衣");
        analysisRequest.setFieldValue("黑色毛衣");

        FieldAnalysisResponse response = analysisRequest.process(client, "item_v2");
        System.out.println(response);

        FieldAnalysisResponse.Analysis fieldTypeAnalysisRes = response.getFieldTypeAnalysis("text_ik");

        Iterable<AnalysisResponseBase.AnalysisPhase> analysisPhaseIterable = fieldTypeAnalysisRes.getQueryPhases();
        Iterator<AnalysisResponseBase.AnalysisPhase> analysisPhaseIterator = analysisPhaseIterable.iterator();

        while (analysisPhaseIterator.hasNext()){
            AnalysisResponseBase.AnalysisPhase analysisPhase = analysisPhaseIterator.next();
            System.out.println(analysisPhase.getClassName());
            List<AnalysisResponseBase.TokenInfo> tokens = analysisPhase.getTokens();
            for (AnalysisResponseBase.TokenInfo token:tokens) {
                System.out.println(token.getText() + "  " + token.getType() + "  " );
            }
        }
    }



}
