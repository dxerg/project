package com.example.project.service.impl;

import com.example.project.entity.Feedback;
import com.example.project.mapper.FeedbackMapper;
import com.example.project.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户反馈 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-04-01
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

}
