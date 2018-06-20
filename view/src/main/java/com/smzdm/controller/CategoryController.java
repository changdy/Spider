package com.smzdm.controller;

import com.smzdm.model.ResponseResult;
import com.smzdm.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Changdy on 2018/4/11.
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Value("${custom.category-key}")
    private String categoryKey;
    @Autowired
    private ValueOperations<String, String> valueOperations;

    @GetMapping("/get-last/{sha1}")
    public ResponseResult<String[]> getCategory(@PathVariable(required = false) String sha1) {
        String s = valueOperations.get("category:sha1");
        if (s.equals(sha1)) {
            return ResultUtil.success();
        } else {
            String[] arr = new String[2];
            arr[0] = s;
            arr[1] = valueOperations.get(categoryKey);
            return ResultUtil.success(arr);
        }
    }
}