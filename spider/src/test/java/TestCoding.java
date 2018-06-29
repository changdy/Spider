import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smzdm.model.SubNoticeMsg;
import com.smzdm.pojo.ArticleSubscription;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Calendar;


/**
 * Created by Changdy on 2018/4/10.
 */
public class TestCoding {

    @Test
    public void updateCategory() {
        String raw = "hello";
        String str = String.format("%1$-7s", raw);
        System.out.println(str + "======");


        int num = 1;
        str = String.format("%04d", num);
        System.out.println(str);


        int n2 = 9999999;
        System.out.printf("%+(,d %n", n2);


        Calendar calendar = Calendar.getInstance();
        //%tH:%tM的缩写
        System.out.println(String.format("'R':将时间格式化为：HH:MM（24小时制）。输出：%tR", calendar));
        //%tH:%tM:%tS的缩写
        System.out.println(String.format("'T':将时间格式化为：HH:MM:SS（24小时制）。输出：%tT", calendar));
        //%tI:%tM:%tS %Tp的缩写，输出形如：
        System.out.println(String.format("'r':将时间格式化为：09:23:15 下午，跟设置的语言地区有关。输出：%tr", calendar));
        //%tm/%td/%ty的缩写，输出形如
        System.out.println(String.format("'D':将时间格式化为：10/19/16。输出：%tD", calendar));
        //%tY-%tm-%td，输出形如：
        System.out.println(String.format("'F':将时间格式化为：2016-10-19。输出：%tF", calendar));
        //%ta %tb %td %tT %tZ %tY，输出形如：Sun Jul 20 16:17:00 EDT 1969
        System.out.println(String.format("'c':将时间格式化为\"Sun Jul 20 16:17:00 EDT 1969\"。输出：%tc", calendar));
    }




}