package com.example.project.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import com.example.project.common.Result;
import com.example.project.mapper.CommonSQLMapper;
import com.example.project.utils.SpringContextUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "数据统计接口列表")
@RestController
@RequestMapping("/statistics")
@Slf4j
public class StatisticsController {

        /**
     * 视频分类数量
     */
    @GetMapping("/categoryCount")
    public Result categoryCount() {
        CommonSQLMapper sqlMapper = SpringContextUtil.getBean(CommonSQLMapper.class);
        Map map = new HashMap();
        map.put("sql","select count(*) as categoryCount from category");
        List select = sqlMapper.select(map);
        if(CollectionUtil.isNotEmpty(select)){
            return Result.success(select.get(0));
        }else{
            return Result.success();
        }
    }
    /**
     * 上传视频数量
     */
    @GetMapping("/vedioCount")
    public Result vedioCount() {
        CommonSQLMapper sqlMapper = SpringContextUtil.getBean(CommonSQLMapper.class);
        Map map = new HashMap();
        map.put("sql","select count(*) as vedioCount from vedio");
        List select = sqlMapper.select(map);
        if(CollectionUtil.isNotEmpty(select)){
            return Result.success(select.get(0));
        }else{
            return Result.success();
        }
    }
    /**
     * 视频标签数量
     */
    @GetMapping("/tagsCount")
    public Result tagsCount() {
        CommonSQLMapper sqlMapper = SpringContextUtil.getBean(CommonSQLMapper.class);
        Map map = new HashMap();
        map.put("sql","select count(*) as tagsCount from tags");
        List select = sqlMapper.select(map);
        if(CollectionUtil.isNotEmpty(select)){
            return Result.success(select.get(0));
        }else{
            return Result.success();
        }
    }
    /**
     * 注册用户数
     */
    @GetMapping("/memberCount")
    public Result memberCount() {
        CommonSQLMapper sqlMapper = SpringContextUtil.getBean(CommonSQLMapper.class);
        Map map = new HashMap();
        map.put("sql","select count(*) as memberCount from member");
        List select = sqlMapper.select(map);
        if(CollectionUtil.isNotEmpty(select)){
            return Result.success(select.get(0));
        }else{
            return Result.success();
        }
    }

    /**
    * 不同视频分类数量统计
    */
    @GetMapping("/goodsCategory")
    public Result goodsCategory() {
        CommonSQLMapper sqlMapper = SpringContextUtil.getBean(CommonSQLMapper.class);
        Map map = new HashMap();
        map.put("sql","SELECT c.name AS `name`,COUNT(r.id) AS `value` FROM vedio r INNER JOIN category c WHERE c.id = r.category_id GROUP BY c.name");
        List select = sqlMapper.select(map);
        if(CollectionUtil.isNotEmpty(select)){
            return Result.success(select);
        }else{
            return Result.success();
        }
    }
    /**
    * 最近30天视频上传量统计
    */
    @GetMapping("/vedioStatics")
    public Result vedioStatics() {
        CommonSQLMapper sqlMapper = SpringContextUtil.getBean(CommonSQLMapper.class);
        Map map = new HashMap();
        map.put("sql","SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS `name`,COUNT(id) AS `value` FROM vedio WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') ORDER BY DATE_FORMAT(create_time, '%Y-%m-%d')");
        List select = sqlMapper.select(map);
        if(CollectionUtil.isNotEmpty(select)){
            return Result.success(select);
        }else{
            return Result.success();
        }
    }

}
