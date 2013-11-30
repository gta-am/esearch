package service;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * desc:
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-11-28
 * Time: 上午9:19
 */
public class HttpClientServiceImplTest {
    @Before
    public void setUp() throws Exception {

    }
    @Test
    public void testService(){

//        String url2="http://oa.gtadata.com/C6/jhsoft.web.workflat/index.aspx";
//
//        String url3="http://oa.gtadata.com/C6/JHSoft.Web.Login/PassWordNew.aspx";
//        String url4="http://oa.gtadata.com/C6/jhsoft.web.workflat/UserDispose.aspx?userid=10652";
        //1 登陆
        String url0="http://oa.gtadata.com/C6/JHSoft.Web.Login/AjaxForLogin.aspx";
        //2
        String url1="http://oa.gtadata.com/C6/JHsoft.Web.Workflow/WorkFlowTemplate/InstanceEntry.aspx";
        //3
        String url2="http://oa.gtadata.com/C6/JHsoft.Web.Workflow/WorkFlowTemplate/AjaxImpl.aspx?AjaxFlag=FlowInstanceImpl&TemplateID=365cc8176a614bd3b8b7d4ba3ee936ee&InstanceName=&UserID=10652&ran=0.6959722726605833";
        //4
        String url3="http://oa.gtadata.com/C6/JHSoft.Web.Module/ToolBar/toolbarwf.aspx?djsn=kqjlggsq_9614639a-abbe-4d30-9413-0c2f6ce7cce6&djtype=TT&DjName=%u8003%u52e4%u8bb0%u5f55%u66f4%u6539%u7533%u8bf7&ModuleID=kqjlggsq_9614639a-abbe-4d30-9413-0c2f6ce7cce6&IsNew=0&opentype=1&httpAppID=-1&_FlowInstanceID=0&_FlowTemplateID=365cc8176a614bd3b8b7d4ba3ee936ee&Version=2.0";



        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("loginCode", "d2VpZ2w="));
        nvps.add(new BasicNameValuePair("pwd", "MTIzNDU2"));
        nvps.add(new BasicNameValuePair("type", "login"));

        try {
            System.out.println(HttpClientServiceImpl.getInstance(url0, nvps).post());
            String html1 = HttpClientServiceImpl.getInstance(url1).get();
            Document doc = Jsoup.parse(html1);
            Element li = doc.select(".normal").first().child(0);
            String param = li.attr("onclick");
            Pattern p = Pattern.compile("CreateInstance(\"*\")");
            //String ps = p.matcher(param).group();
            System.out.println(param);

            //System.out.println(HttpClientServiceImpl.getInstance(url3).get());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {//保证释放链接
            HttpClientServiceImpl.close();
        }
    }
}
