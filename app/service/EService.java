package service;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * desc: 这里提供的事ElasticSearch的服务
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-10-30
 * Time: 下午4:54
 */
public class EService {

    //设置项
    public final static Settings settings = ImmutableSettings.settingsBuilder()
            .put("action.auto_create_index",false)
            .put("index.mapper.dynamic",false)
            .put("number_of_shards",5) //分片
            .put("number_of_replicas",3)//副本
            .build();

    //Transport客户端
    public final static Client client = new TransportClient(settings)
            .addTransportAddress(new InetSocketTransportAddress("192.168.98.250", 9300));

    public Map<String,Object> map;

    //获取索引
    public GetResponse getIndex(){
        GetResponse response = client.prepareGet("twitter","tweet","1")
                .execute()
                .actionGet();
        return response;
    }

    public void setMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user","Kimchy");
        map.put("postDate",new Date());
        map.put("message","trying out Elastic Search");
        this.map = map;
    }

    //创建索引
    public IndexResponse createIndex() throws IOException{
        IndexResponse response = client.prepareIndex("twitterds","tweet","1")
                .execute()
                .actionGet();
        return response;
    }

}
