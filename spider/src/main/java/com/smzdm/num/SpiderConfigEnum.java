package com.smzdm.num;

/**
 * Created by Changdy on 2018/3/8.
 */
public enum SpiderConfigEnum {
    homeConfig(false, "timesort:home", "http://www.smzdm.com/homepage/json_more"),
    latestConfig(true, "timesort:latest", "http://faxian.smzdm.com/json_more?page=2", "http://faxian.smzdm.com/json_more?page=1"),
    hotUrl(true, null, "http://faxian.smzdm.com/json_more?filter=h3s0t0f0c0&page=3", "http://faxian.smzdm.com/json_more?filter=h3s0t0f0c0&page=2", "http://faxian.smzdm.com/json_more?filter=h3s0t0f0c0&page=1");


    private String[] url;
    private boolean discovery;
    private String redisKey;

    SpiderConfigEnum(boolean discovery, String redisKey, String... url) {
        this.discovery = discovery;
        this.redisKey = redisKey;
        this.url = url;
    }

    public String[] getUrl() {
        return url;
    }


    public boolean isDiscovery() {
        return discovery;
    }


    public String getRedisKey() {
        return redisKey;
    }

}