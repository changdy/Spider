
import com.smzdm.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Changdy on 2018/5/7.
 */
@Slf4j
public class WeiXinTest {
    private String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx261b69e6a73769ac&secret=298d45ba696b49fc96f0e3a6ea78bbb0";
    private String accessToken;


    private String msg = "{\n" +
            "    \"touser\": \"oVJJCvx28HDsflYiPF-EwKsYeOJ8\",\n" +
            "    \"template_id\": \"XoEYmy-QTudlR3Ofx9dBeUxEFpl-3RYJfmClQCMEPUg\",\n" +
            "    \"url\": \"http://wxtest.saa.com.cn/order_det.html?orderId=HZYT001326677\",\n" +
            "    \"data\": {\n" +
            "        \"first\": {\n" +
            "            \"value\": \"您的【拖车】订单HZJNFZ0002018，任务已完成【救援成功】\"\n" +
            "        },\n" +
            "        \"keyword1\": {\n" +
            "            \"value\": \"福建北京中联车盟网络科技服务有限公司\"\n" +
            "        },\n" +
            "        \"keyword2\": {\n" +
            "            \"value\": \"东水路86号闽发世家\"\n" +
            "        },\n" +
            "        \"keyword3\": {\n" +
            "            \"value\": \"红牌楼街道龙腾西路3-附1号\"\n" +
            "        },\n" +
            "        \"keyword4\": {\n" +
            "            \"value\": \"闽A64684\"\n" +
            "        },\n" +
            "        \"keyword5\": {\n" +
            "            \"value\": \"郭大帅 18649735816\"\n" +
            "        },\n" +
            "        \"remark\": {\n" +
            "            \"value\": \"\"\n" +
            "        }\n" +
            "    }\n" +
            "}";



    @Test
    public void sendMsg() throws IOException {
        HttpUtil.sendJSONPost(msg, "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken);
    }
}