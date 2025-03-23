package com.example.project.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.example.project.common.annotation.AutoLog;
import cn.hutool.core.date.DateUtil;
import com.example.project.entity.User;
import com.example.project.mapper.UserMapper;
import com.example.project.utils.SessionUtils;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.project.utils.SpringContextUtil;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import cn.dev33.satoken.annotation.SaIgnore;
import com.example.project.service.IUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.project.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.project.service.IFeedbackService;
import com.example.project.entity.Feedback;

import org.springframework.web.bind.annotation.RestController;

/**
* <p>
* 用户反馈 前端控制器
* </p>
*
* @author wx:ericxu1116
* @since 2024-04-01
*/
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Resource
    private IFeedbackService feedbackService;

    @AutoLog("新增用户反馈")
    @PostMapping
    public Result save(@RequestBody Feedback feedback) {
	

        User dbUser = SessionUtils.getUser();
        feedback.setUserId(dbUser.getId());
		
        feedbackService.save(feedback);
        return Result.success();
    }

    @AutoLog("编辑用户反馈")
    @PutMapping
    public Result update(@RequestBody Feedback feedback) {
        feedbackService.updateById(feedback);


        return Result.success();
    }

    @AutoLog("删除用户反馈")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        feedbackService.removeById(id);
        return Result.success();
    }

    @AutoLog("批量删除用户反馈")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        feedbackService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(feedbackService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(feedbackService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<Feedback>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
		
		//User user = SessionUtils.getUser();  //获取登录用户
		

        //非管理员，数据过滤
        User currentUser = SessionUtils.getUser();
        String role = currentUser.getRole();
        if ("member".equals(role)) {
            queryWrapper.eq("user_id", currentUser.getId());
        }
		
        return Result.success(feedbackService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Feedback> list = feedbackService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Feedback信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
    * excel 导入
    * @param file
    * @throws Exception
    */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Feedback> list = reader.readAll(Feedback.class);

        feedbackService.saveBatch(list);
        return Result.success();
    }

	
	
}
