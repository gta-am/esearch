package service;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import play.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * desc: 提供的ElasticSearch的服务
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-10-30
 * Time: 下午4:54
 */
public class EService {

    //设置项
    public final static Settings settings = ImmutableSettings.settingsBuilder()
            .put("action.auto_create_index",false)
            .put("index.mapper.dynamic",false)
            .put("number_of_shards",10) //分片
            .put("number_of_replicas",3)//副本
            .build();

    //Transport客户端
    public final static Client client = new TransportClient(settings)
            .addTransportAddress(new InetSocketTransportAddress("192.168.98.250", 9300));
    private static EService eService = null;
    private EService() {}
    public static EService getInstance(){
        if(eService ==null){
            eService = new EService();
        }
        return eService;
    }

    /**
     * 索引库是否存在
     * @param IndicesName 索引库名称
     * @return
     */
    public boolean isIndicesExist(String IndicesName){
        return client.admin().indices().exists( new IndicesExistsRequest(IndicesName)).actionGet().isExists();
    }

    /**
     * 创建一个空索引库 (索引库名不能重复、不能是大写字母)
     * @param name
     * @return
     */
    public CreateIndexResponse createBlankIndices(String name){
        CreateIndexResponse response = null;
        try {
            client.admin().indices().prepareCreate(name)
                    .execute()
                    .actionGet();
        } catch (Exception e) {
            Logger.error("创建空索引库出错！");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 用map添加
     * @param indexName
     * @param type
     * @param id
     * @param map
     * @return
     */
    public IndexResponse addIndex(String indexName, String type, String id, Map<String,Object> map){
        IndexResponse response = null;
        try{
            client.prepareIndex(indexName,type,id)
                    .setSource(map)
                    .execute()
                    .actionGet();
        }catch (Exception e){
            Logger.error("创建索引出错！");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 用json添加索引
     * @param indexName
     * @param type
     * @param id
     * @param json
     * @return
     */
    public IndexResponse addIndex(String indexName, String type, String id, String json){
        IndexResponse response = null;
        try{
            client.prepareIndex(indexName,type,id)
                    .setSource(json)
                    .execute()
                    .actionGet();
        }catch (Exception e){
            Logger.error("创建索引出错！");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 用builder添加索引
     * @param indexName
     * @param type
     * @param id
     * @param builder
     * @return
     */
    public IndexResponse addIndex(String indexName, String type, String id, XContentBuilder builder){
        IndexResponse response = null;
        try{
            client.prepareIndex(indexName,type,id)
                    .setSource(builder)
                    .execute()
                    .actionGet();
        }catch (Exception e){
            Logger.error("创建索引出错！");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 添加索引（自动生成ID）
     * @param indexName
     * @param type
     * @return
     */
    public IndexResponse addIndex(String indexName, String type,Map<String,Object> map){
        return addIndex(indexName,type,null,map);
    }
    /**
     * 添加索引（自动生成ID）
     * @param indexName
     * @param type
     * @return
     */
    public IndexResponse addIndex(String indexName, String type,String json){
        return addIndex(indexName,type,null,json);
    }
    /**
     * 添加索引（自动生成ID）
     * @param indexName
     * @param type
     * @return
     */
    public IndexResponse addIndex(String indexName, String type,XContentBuilder builder){
        return addIndex(indexName,type,null,builder);
    }

    /**
     * 获取索引
     * @param indexName
     * @param type
     * @param id
     * @return
     */
    public GetResponse getIndex(String indexName, String type, String id){
        GetResponse response = client.prepareGet(indexName,type,id)
                .execute()
                .actionGet();
        return response;
    }


    public PutMappingRequest putMapping(XContentBuilder mapping) throws Exception{
        PutMappingRequest mappingRequest = Requests.putMappingRequest("twitter")
                .type("twitter")
                .source(mapping);
        client.admin().indices().putMapping(mappingRequest).actionGet();
        return mappingRequest;
    }

    public DeleteResponse deleteIndex(String indexName, String type, String id){
        DeleteResponse response = client.prepareDelete(indexName, type, id)
                .execute()
                .actionGet();
        return response;
    }

    public DeleteByQueryResponse deleteIndexByQuery(QueryBuilder query){
        return client.prepareDeleteByQuery("twitter").setQuery(query).execute().actionGet();
    }

    /**
     * 批量操作
     * @param bulkRequest
     * @return
     */
    public BulkResponse bulkOperation(BulkRequestBuilder bulkRequest){
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
        }
        return bulkResponse;
    }

    /**
     * 查询
     * @param qb
     * @param fb
     * @return
     */
    public SearchResponse serch(QueryBuilder qb,FilterBuilder fb){
        SearchResponse response = null;
        client.prepareSearch()
                .setQuery(qb)
                .setFilter(fb)
                .execute()
                .actionGet();
        return response;
    }

    /**
     * 查询器查询
     * @param qb
     * @return
     */
    public SearchResponse serch(QueryBuilder qb){
        SearchResponse response = null;
        client.prepareSearch()
                .setQuery(qb)
                .execute()
                .actionGet();
        return response;
    }

    /**
     * 过滤器查询
     * @param fb
     * @return
     */
    public SearchResponse serch(FilterBuilder fb){
        SearchResponse response = null;
        client.prepareSearch()
                .setFilter(fb)
                .setFrom(0).setSize(4).setExplain(true)
                .execute()
                .actionGet();
        return response;
    }

}
