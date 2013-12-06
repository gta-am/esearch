package service;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * desc:
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-11-18
 * Time: 下午2:56
 */
public class EServiceTest {
    private EService eService = null;
    @Before
    public void setUp() throws Exception {
           eService = EService.getInstance();
    }
    @Test
    public void testGetIndex() throws Exception {
        GetResponse response = eService.getIndex("twitter","","M7dergw1QA6WJFjCNICDJw");
        assertEquals("twitter", response.getIndex());
    }

    @Test
    public void testCreateIndex() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user","frees");
        map.put("postDate","2013");
        map.put("message","trying out Elastic Search");

        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elastic Search\"," +
                "}";

        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("user", "kimchy")
                .field("postDate", "2013")
                .field("message", "trying out Elastic Search")
                .endObject();

        IndexResponse indexResponse = eService.addIndex("twitter","tweets","1",map);
    }

    @Test
    public void testCreateBlankIndices() throws Exception {//不能创建重复的索引库
        eService.createBlankIndices("free");
    }

    @Test
    public void testPutMapping() throws Exception{
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("productindex")
                .startObject("properties")
                .startObject("title").field("type", "string").field("store", "yes").endObject()
                .startObject("description").field("type", "string").field("index", "not_analyzed").endObject()
                .startObject("price").field("type", "double").endObject()
                .startObject("onSale").field("type", "boolean").endObject()
                .startObject("type").field("type", "integer").endObject()
                .startObject("createDate").field("type", "date").endObject()
                .endObject()
                .endObject()
                .endObject();
        PutMappingRequest pmr = eService.putMapping(mapping);
    }

    @Test
    public void deleteIndex(){
        DeleteResponse response = eService.deleteIndex("twitter", "tweets", "1");
    }

    @Test
    public void testDeleteIndexByQuery(){
        QueryBuilder query = QueryBuilders.fieldQuery("name", "Free");
        DeleteByQueryResponse dbr = eService.deleteIndexByQuery(query);
    }
    @Test
    public void testIsIndicesExist(){
         assertEquals(true, eService.isIndicesExist("twitter"));
    }
    @Test
    public void testBulkOperation() throws Exception{
        BulkRequestBuilder bulkRequest = EService.client.prepareBulk();

// either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(EService.client.prepareIndex("twitter", "tweet", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elastic Search")
                        .endObject()
                )
        );

        bulkRequest.add(EService.client.prepareIndex("twitter", "tweet", "2")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "another post")
                        .endObject()
                )
        );
        BulkResponse response = eService.bulkOperation(bulkRequest);
    }

    @Test
    public void testSearch(){
        FilterBuilder filter = FilterBuilders.termFilter("user","frees");
        SearchResponse response = eService.serch(filter);
        //System.out.println(response.getHits().getTotalHits());
    }


    @After
    public void tearDown() throws Exception {
        EService.client.close();
    }
}
