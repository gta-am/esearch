package service;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import threadtest.ThreadTest2;

import java.io.IOException;

/**
 * desc:
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-11-11
 * Time: 下午2:43
 */
public class EServiceTest {

    //public static EService eService = new EService();
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        //eService = new EService();
    }

    @Test
    public void testCreateIndex() throws IOException {
        EService eService = new EService();
        eService.setMap();
        IndexResponse indexResponse = eService.createIndex();
        System.out.println("test");
    }

    @Test
    public void testGetIndex(){
        EService eService = new EService();
        GetResponse response = eService.getIndex();
        System.out.println("ok");
    }



}
