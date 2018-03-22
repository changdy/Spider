
import com.alibaba.fastjson.JSON;
import com.smzdm.HandlerFunction;
import com.smzdm.StartApplication;
import com.smzdm.mapper.CategoryMapper;
import com.smzdm.pojo.Category;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 更新目录列表
 * 懒得单独在写个项目了，就放这里了
 * url：https://www.smzdm.com/fenlei/ajax_category_tree/
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class UpdateCategory {
    private static List<CategoryTemp> list = new ArrayList<>(2500);

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void test() throws IOException {
        String temp = new String(Files.readAllBytes(Paths.get("c:\\category.json")));
        add(JSON.parseArray(temp, CategoryTemp.class));
        List<Category> collect = list.stream().map(x -> {
            Category category = new Category();
            category.setId(x.getId());
            category.setTitle(x.getTitle());
            category.setNickTitle(x.getUrlNicktitle());
            String parentIds = x.getParentIds();
            if (!parentIds.equals("") && !parentIds.equals("0")) {
                category.setParentIds((Short[]) HandlerFunction.PARENTS.getFunction().apply(parentIds));
            }
            return category;
        }).collect(toList());
        categoryMapper.insertList(collect);
    }

    private static void add(List<CategoryTemp> categories) {
        list.addAll(categories);
        categories.forEach(x -> {
            List<CategoryTemp> child = x.getChild();
            if (child != null && child.size() > 0) {
                add(child);
            }
        });
    }

    @Data
    class CategoryTemp {
        private Short id;
        private String title;
        private String parentIds;
        private String urlNicktitle;
        private List<CategoryTemp> child;
    }
}