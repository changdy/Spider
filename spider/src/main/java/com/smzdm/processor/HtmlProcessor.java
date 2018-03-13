package com.smzdm.processor;

import com.smzdm.pojo.ArticleInfo;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.time.LocalDateTime;

@Service("htmlProcessor")
public class HtmlProcessor implements PageProcessor {

    private static final Site SITE = Site.me().setRetryTimes(5).setSleepTime(1500).setUseGzip(true).setRetrySleepTime(5000);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Integer id = Integer.valueOf(page.getUrl().toString().replaceAll("[^0-9]", ""));
        ArticleInfo info = new ArticleInfo();
        info.setArticleId(id);
        info.setWorthy(Short.valueOf(html.xpath("//span[@id='rating_worthy_num']/text()").toString().trim()));
        info.setUnworthy(Short.valueOf(html.xpath("//span[@id='rating_unworthy_num']/text()").toString().trim()));
        info.setComment(Short.valueOf(html.xpath("//div[@id='panelTitle']/span/em/text()").toString().trim()));
        info.setCollection(Short.valueOf(html.xpath("//div[@class='operate_icon']//em/text()").toString().trim()));
        String title = html.xpath("//h1[@class='article_title']").toString();
        info.setTimeout(title.contains("过期"));
        info.setSoldOut(title.contains("售罄"));
        info.setUpdateTime(LocalDateTime.now());
        page.putField("ArticleInfo", info);
    }

    @Override
    public Site getSite() {
        return SITE;
    }
}