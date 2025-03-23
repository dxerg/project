package com.example.project.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class IpUtils {
    private static final Map<String, String> IP_MAP = new ConcurrentHashMap<>();


public static Dict getIPAndCity() {
        Dict dict = Dict.create();
        try {
            String ip = "";
            dict.set("ip", ip);
            String city = "";
            if (IP_MAP.get(ip) != null) {
                city = IP_MAP.get(ip);
                dict.set("city", city);
                return dict;
            }
            String url = "https://api.map.baidu.com/location/ip?ip=" + ip + "&ak=bmvg8yeOopwOB4aHl5uvx52rgIa3VrPO";
            String res = HttpUtil.createRequest(Method.GET, url).execute().body();
            String json = UnicodeUtil.toString(res);
            JSON jsonObject = JSONUtil.parseObj(json);
            city = jsonObject.getByPath("content.address", String.class);
            dict.set("city", city);
        } catch (Exception e) {
            log.error("获取IP失败", e);
        }
        return dict;
    }
}
