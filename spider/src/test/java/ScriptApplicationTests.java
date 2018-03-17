
import com.smzdm.StartApplication;
import com.smzdm.mapper.CategoryMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class ScriptApplicationTests {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private CategoryMapper mapper;

    @Test
    public void test() {
        Map<String, String> map = new HashMap<>();
        mapper.getCategoryMap().forEach(x -> map.put((String) x.get("title"), String.valueOf((Integer) x.get("id"))));
        stringRedisTemplate.opsForHash().putAll("test", map);
    }
}