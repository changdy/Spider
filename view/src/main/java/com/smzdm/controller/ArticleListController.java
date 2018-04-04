package com.smzdm.controller;

import com.smzdm.mapper.ArticleMapper;
import com.smzdm.model.ArticleSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Changdy on 2018/3/26.
 */
@RestController
@RequestMapping("/article")
public class ArticleListController {

    @Autowired
    private ArticleMapper articleMapper;

    @PostMapping("/list")
    public String getList(@Valid @RequestBody ArticleSearch articleSearch) {
        return "hello";
    }
}
