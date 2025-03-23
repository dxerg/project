package com.example.project.service.impl;

import com.example.project.entity.Tags;
import com.example.project.mapper.TagsMapper;
import com.example.project.service.ITagsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-04-01
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

}
