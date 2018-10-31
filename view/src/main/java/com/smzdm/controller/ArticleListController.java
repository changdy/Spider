package com.smzdm.controller;

import com.smzdm.mapper.ArticleMapper;
import com.smzdm.model.*;
import com.smzdm.util.ResultUtil;
import com.smzdm.util.SendRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Changdy on 2018/3/26.
 */
@RestController
@RequestMapping("/article")
public class ArticleListController {

    @Value("${custom.search-url}")
    private String url;

    @Autowired
    private ArticleMapper articleMapper;


    @PostMapping("/proxy-list")
    public ResponseResult<List<ArticleModel>> getList(@RequestBody ApiSearchModel apiSearchModel) throws IOException, URISyntaxException {
        String type = apiSearchModel.isSearchByCategory() ? "faxian" : "home";
        List<ArticleModel> list = new ArrayList<>();
        for (Integer i = 0; i < apiSearchModel.getPage(); i++) {
            list.addAll(SendRequestUtil.sendRequest(MessageFormat.format(url, apiSearchModel.getKeyWords(), type, i * 100)));
        }
        return ResultUtil.success(list);
    }


    @PostMapping("/search-article")
    public ResponseResult<SimplePageContent<ArticleModel>> getList(@RequestBody DataBaseSearchModel model) {
        return ResultUtil.success(new SimplePageContent<>(articleMapper.getCount(model), articleMapper.queryArticle(model)));
    }
}
