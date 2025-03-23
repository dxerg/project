package com.example.project.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.example.project.common.Result;
import cn.hutool.core.collection.CollectionUtil;
import com.example.project.common.*;
import com.example.project.common.annotation.AutoLog;
import com.example.project.controller.domain.LoginDTO;
import com.example.project.controller.domain.UserRequest;
import com.example.project.entity.*;
import com.example.project.service.IUserService;
import com.example.project.service.INoticeService;
import com.example.project.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.example.project.utils.SpringContextUtil;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import com.example.project.service.IMemberService;
import com.example.project.entity.Member;
import com.example.project.mapper.MemberMapper;
import com.example.project.service.ICategoryService;
import com.example.project.entity.Category;
import com.example.project.service.IVedioService;
import com.example.project.entity.Vedio;
import com.example.project.service.IFeedbackService;
import com.example.project.entity.Feedback;
import com.example.project.service.ICommentsService;
import com.example.project.entity.Comments;
import com.example.project.service.ICollectService;
import com.example.project.entity.Collect;
import com.example.project.service.ITagsService;
import com.example.project.entity.Tags;
import com.example.project.service.IVediotagsService;
import com.example.project.entity.Vediotags;
import com.example.project.service.IFollowService;
import com.example.project.entity.Follow;
import com.example.project.service.IBannerService;
import com.example.project.utils.RecommendUtils;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

@Api(tags = "无权限接口列表")
@RestController
@Slf4j
public class WebController {

    @Resource
    IUserService userService;

    @Resource
    INoticeService noticeService;

    @Resource
    IMemberService memberService;

    @Resource
    ICategoryService categoryService;
    @Resource
    IVedioService vedioService;
    @Resource
    IFeedbackService feedbackService;
    @Resource
    ICommentsService commentsService;
    @Resource
    ICollectService collectService;
    @Resource
    ITagsService tagsService;
    @Resource
    IVediotagsService vediotagsService;
    @Resource
    IFollowService followService;

    @Resource
    IBannerService bannerService;


    @GetMapping("/getCurrentUser")
    public User getLoginUser() {
        return SessionUtils.getUser();
    }

    @GetMapping(value = "/")
    @ApiOperation(value = "版本校验接口")
    public String version() {
        String ver = "partner-back-0.0.1-SNAPSHOT";  // 应用版本号
        Package aPackage = WebController.class.getPackage();
        String title = aPackage.getImplementationTitle();
        String version = aPackage.getImplementationVersion();
        if (title != null && version != null) {
            ver = String.join("-", title, version);
        }
        return ver;
    }

    @AutoLog("用户登录")
    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody UserRequest user) {
        long startTime = System.currentTimeMillis();
        LoginDTO res = userService.login(user);
        log.info("登录花费时间 {}ms", System.currentTimeMillis() - startTime);
        return Result.success(res);
    }

    @AutoLog("用户退出登录")
    @ApiOperation(value = "用户退出登录接口")
    @GetMapping("/logout/{uid}")
    @SaIgnore
    public Result logout(@PathVariable String uid) {
        userService.logout(uid);
        return Result.success();
    }

    @AutoLog("用户注册")
    @ApiOperation(value = "用户注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody UserRequest user) {
        userService.register(user);
        return Result.success();
    }

    @AutoLog("用户重置密码")
    @ApiOperation(value = "密码重置接口")
    @PostMapping("/password/reset")
    public Result passwordReset(@RequestBody UserRequest userRequest) {
        String newPass = userService.passwordReset(userRequest);
        return Result.success(newPass);
    }


    @AutoLog("用户修改密码")
    @PostMapping("/password/change")
    public Result passwordChange(@RequestBody UserRequest userRequest) {
        userService.passwordChange(userRequest);
        return Result.success();
    }


    @AutoLog("编辑用户")
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        Object loginId = StpUtil.getLoginId();
        if (!loginId.equals(user.getUid())) {
            return Result.error("无权限");
        }
        User dbUser = SessionUtils.getUser();
        if (dbUser.getRole().equals("member")) {
            MemberMapper mapper = SpringContextUtil.getBean(MemberMapper.class);
            QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", dbUser.getId());
            Member member = mapper.selectOne(queryWrapper);
            member.setName(user.getName());
            mapper.updateById(member);
        }
        userService.updateById(user);
        return Result.success(user);
    }

    @AutoLog("修改头像")
    @PutMapping("/updateAvatar")
    public Result updateAvatar(@RequestBody User user) {
        Object loginId = StpUtil.getLoginId();
        if (!loginId.equals(user.getUid())) {
            return Result.error("无权限");
        }
        User dbUser = userService.getById(user.getId());
        dbUser.setAvatar(user.getAvatar());
        userService.updateById(dbUser);
        return Result.success(user);
    }

    @GetMapping("/front/user/list")
    @SaIgnore
    public Result findAllUser() {
        return Result.success(userService.list());
    }

    @AutoLog("公告浏览")
    @GetMapping("/front/notice/{id}")
    @SaIgnore
    public Result findOneNotice(@PathVariable Integer id) {
        return Result.success(noticeService.getById(id));
    }

    @AutoLog("所有公告")
    @GetMapping("/front/notice/list")
    @SaIgnore
    public Result findAllNotice() {
        return Result.success(noticeService.list());
    }


    @AutoLog("平台公告查询")
    @GetMapping("/front/notice/page")
    @SaIgnore
    public Result findPageNotice(
            @RequestParam(defaultValue = "") String name,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(noticeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @AutoLog("平台公告保存更新")
    @PutMapping("/front/notice/update")
    @SaIgnore
    public Result saveOrUpdateNotice(@RequestBody Notice notice) {
        return Result.success(noticeService.saveOrUpdate(notice));
    }

    @AutoLog("视频列表查询")
    @GetMapping("/front/vedio/page")
    @SaIgnore
    public Result findPageVedio(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String category_id,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String tags
    ) {
        QueryWrapper<Vedio> queryWrapper = new QueryWrapper<Vedio>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        queryWrapper.like(!"".equals(category_id), "category_id", category_id);
        queryWrapper.eq("state_radio","审核通过");
        if(StrUtil.isNotBlank(tags)){
            List<Vediotags> vediotagsList = vediotagsService.list(new QueryWrapper<Vediotags>().in("tags_id", Arrays.asList(tags.split(","))));
            if(CollectionUtil.isEmpty(vediotagsList)){
                return Result.success();
            }
            queryWrapper.in("id",vediotagsList.stream().map(Vediotags::getVedioId).collect(Collectors.toList()));
        }
        return Result.success(vedioService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @AutoLog("视频列表保存更新")
    @PutMapping("/front/vedio/update")
    @SaIgnore
    public Result saveOrUpdateVedio(@RequestBody Vedio vedio) {
        return Result.success(vedioService.saveOrUpdate(vedio));
    }

    @AutoLog("用户反馈查询")
    @GetMapping("/front/feedback/page")
    @SaIgnore
    public Result findPageFeedback(
            @RequestParam(defaultValue = "") String name,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<Feedback>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(feedbackService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @AutoLog("用户反馈保存更新")
    @PutMapping("/front/feedback/update")
    @SaIgnore
    public Result saveOrUpdateFeedback(@RequestBody Feedback feedback) {
        return Result.success(feedbackService.saveOrUpdate(feedback));
    }


    @AutoLog("f用户浏览")
    @GetMapping("/front/member/{id}")
    @SaIgnore
    public Result findOneMember(@PathVariable Integer id) {
        return Result.success(memberService.getById(id));
    }

    @AutoLog("根据userId查询用户")
    @GetMapping("/front/member/user/{id}")
    @SaIgnore
    public Result findOneMemberByUser(@PathVariable Integer id) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        return Result.success(memberService.getOne(queryWrapper));
    }

    @AutoLog("更新用户信息")
    @PutMapping("/front/member/update")
    @SaIgnore
    public Result updateMember(@RequestBody Member member) {
        return Result.success(memberService.saveOrUpdate(member));
    }

    @GetMapping("/front/member/list")
    @SaIgnore
    public Result findAllMember() {
        return Result.success(memberService.list());
    }

    @AutoLog("视频分类浏览")
    @GetMapping("/front/category/{id}")
    @SaIgnore
    public Result findOneCategory(@PathVariable Integer id) {
        return Result.success(categoryService.getById(id));
    }

    @AutoLog("视频分类列表")
    @GetMapping("/front/category/list")
    @SaIgnore
    public Result findAllCategory() {
        return Result.success(categoryService.list());
    }

    @AutoLog("视频浏览")
    @GetMapping("/front/vedio/{id}")
    @SaIgnore
    public Result findOneVedio(@PathVariable Integer id) {
        return Result.success(vedioService.getById(id));
    }

    @AutoLog("视频列表")
    @GetMapping("/front/vedio/list")
    @SaIgnore
    public Result findAllVedio() {
        return Result.success(vedioService.list());
    }

    @AutoLog("用户反馈浏览")
    @GetMapping("/front/feedback/{id}")
    @SaIgnore
    public Result findOneFeedback(@PathVariable Integer id) {
        return Result.success(feedbackService.getById(id));
    }

    @AutoLog("用户反馈列表")
    @GetMapping("/front/feedback/list")
    @SaIgnore
    public Result findAllFeedback() {
        return Result.success(feedbackService.list());
    }

    @AutoLog("视频评论浏览")
    @GetMapping("/front/comments/{id}")
    @SaIgnore
    public Result findOneComments(@PathVariable Integer id) {
        return Result.success(commentsService.getById(id));
    }

    @AutoLog("视频评论列表")
    @GetMapping("/front/comments/list")
    @SaIgnore
    public Result findAllComments() {
        return Result.success(commentsService.list());
    }

    @AutoLog("视频收藏浏览")
    @GetMapping("/front/collect/{id}")
    @SaIgnore
    public Result findOneCollect(@PathVariable Integer id) {
        return Result.success(collectService.getById(id));
    }

    @AutoLog("视频收藏列表")
    @GetMapping("/front/collect/list")
    @SaIgnore
    public Result findAllCollect() {
        return Result.success(collectService.list());
    }

    @AutoLog("标签浏览")
    @GetMapping("/front/tags/{id}")
    @SaIgnore
    public Result findOneTags(@PathVariable Integer id) {
        return Result.success(tagsService.getById(id));
    }

    @AutoLog("标签列表")
    @GetMapping("/front/tags/list")
    @SaIgnore
    public Result findAllTags() {
        return Result.success(tagsService.list());
    }

    @AutoLog("视频标签关联浏览")
    @GetMapping("/front/vediotags/{id}")
    @SaIgnore
    public Result findOneVediotags(@PathVariable Integer id) {
        return Result.success(vediotagsService.getById(id));
    }

    @AutoLog("视频标签关联列表")
    @GetMapping("/front/vediotags/list")
    @SaIgnore
    public Result findAllVediotags() {
        return Result.success(vediotagsService.list());
    }

    @AutoLog("用户关注浏览")
    @GetMapping("/front/follow/{id}")
    @SaIgnore
    public Result findOneFollow(@PathVariable Integer id) {
        return Result.success(followService.getById(id));
    }

    @AutoLog("用户关注列表")
    @GetMapping("/front/follow/list")
    @SaIgnore
    public Result findAllFollow() {
        return Result.success(followService.list());
    }

    @GetMapping("/front/banner/list")
    @SaIgnore
    public Result findAllBanner() {
        return Result.success(bannerService.list());
    }


    @AutoLog("查询视频评价列表")
    @GetMapping("/front/comments/tree")
    @SaIgnore
    public Result commentsTree(@RequestParam Integer vedioId) {
        List<User> userList = userService.list();
        List<Comments> list = commentsService.list(new QueryWrapper<Comments>().eq("vedio_id", vedioId));
        // 给comments里的每个对象设置一个user属性
        list = list.stream().peek(comments -> userList.stream()
                .filter(user -> user.getId().equals(comments.getUserId())).findFirst().ifPresent(comments::setUser))
                .collect(Collectors.toList());
        List<Comments> first = list.stream().filter(comments -> comments.getPid() == null).collect(Collectors.toList());// 一级评论
        for (Comments comments : first) {
            Integer pid = comments.getId();
            List<Comments> second = list.stream().filter(comments1 -> Objects.equals(pid, comments1.getPid())).collect(Collectors.toList());// 二级评论

            // 给second里的每个对象设置一个puser属性
            second = second.stream().peek(comments1 -> userList.stream()
                    .filter(user -> user.getId().equals(comments1.getPuserId())).findFirst()
                    .ifPresent(comments1::setPUser)).collect(Collectors.toList());
            comments.setChildren(second);  // 一级评论设置二级评论
        }
        return Result.success(first);
    }

    @AutoLog("更新单条视频评价")
    @PostMapping("/front/comments/update")
    @SaIgnore
    public Result updateComments(@RequestBody Comments comments) {
        return Result.success(commentsService.saveOrUpdate(comments));
    }


    @AutoLog("更新多条视频评价")
    @PostMapping("/front/comments/update/{goodids}")
    @SaIgnore
    public Result updateMultiComments(@RequestBody Comments comments, @PathVariable String goodids) {
        String[] array = goodids.split(",");
        for (String goodid : array) {
            comments.setVedioId(Integer.parseInt(goodid));
            commentsService.saveOrUpdate(comments);
            comments.setId(null);
        }
        return Result.success();
    }

    @AutoLog("删除视频评价")
    @DeleteMapping("/front/comments/{id}")
    @SaIgnore
    public Result deleteComments(@PathVariable Integer id) {
        return Result.success(commentsService.removeById(id));
    }

    /**
     * 协同过滤算法获取推荐数据-个性化推荐视频
     *
     * @return
     */
    @GetMapping("/front/recommend/vedio")
    @SaIgnore
    public Result findRecommendVedio() {
        User user = SessionUtils.getUser();

        List<Vedio> vedioList = null;
        if (user != null) {
            //如果已经登录，则推荐数据给用户
            //根据协同过滤算法获取推荐数据
            List<RecommendedItem> recommendedItems = RecommendUtils.recommendUserCF(user.getId(), 4);
            if (recommendedItems != null) {
                vedioList = recommendedItems.stream().map(e -> {
                    long itemID = e.getItemID();
                    Vedio vedio = vedioService.getById(itemID);
                    return vedio;
                }).collect(Collectors.toList());
            }

            //如果协同过滤没有推荐数据或数据不够，则获取访问量较高的前n名
            QueryWrapper<Vedio> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("views");
            List<Vedio> list = vedioService.list(queryWrapper);
            if (vedioList == null) {
                vedioList = new ArrayList<>();
                vedioList.addAll(list.subList(0, 4));
            } else if (vedioList.size() < 4) {
                int size = 4 - vedioList.size();
                vedioList.addAll(list.subList(0, size));
            }
        } else {
            //如果没有登录，则推荐访问量靠前的数据
            QueryWrapper<Vedio> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("views").last("limit 4");
            vedioList = vedioService.list(queryWrapper);

        }
        return Result.success(vedioList);
    }

    @AutoLog("添加视频收藏")
    @PostMapping("/front/collect/update")
    @SaIgnore
    public Result updateCollect(@RequestBody Collect collect) {
        return Result.success(collectService.saveOrUpdate(collect));
    }

    @AutoLog("删除视频收藏")
    @DeleteMapping("/front/collect/{vedioId}/{userId}")
    @SaIgnore
    public Result deleteCollect(@PathVariable Integer vedioId, @PathVariable Integer userId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<Collect>();
        queryWrapper.eq("vedio_id", vedioId);
        queryWrapper.eq("user_id", userId);
        return Result.success(collectService.remove(queryWrapper));
    }

    @AutoLog("查询视频收藏状态")
    @GetMapping("/front/collect/collect/{vedioId}/{userId}")
    @SaIgnore
    public Result checkCollect(@PathVariable Integer vedioId, @PathVariable Integer userId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<Collect>();
        queryWrapper.eq(vedioId != null, "vedio_id", vedioId);
        queryWrapper.eq(userId != null, "user_id", userId);
        return Result.success(collectService.getOne(queryWrapper) != null);
    }

    @AutoLog("更新访问量")
    @PostMapping("/front/vedio/views/update/{id}")
    @SaIgnore
    public Result updateVedioViews(@PathVariable Integer id) {
        Vedio vedio = vedioService.getById(id);
        if(vedio.getViews()==null){
            vedio.setViews(1);
        }
        vedio.setViews(vedio.getViews()+1);
        return Result.success(vedioService.saveOrUpdate(vedio));
    }


    @AutoLog("我的视频收藏查询")
    @GetMapping("/front/collect/page")
    @SaIgnore
    public Result findPageCollect(
            @RequestParam(defaultValue = "") String name,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<Collect>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        User user = SessionUtils.getUser();
        queryWrapper.eq("user_id",user.getId());
        return Result.success(collectService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @AutoLog("新增视频标签")
    @PostMapping("/front/vediotags")
    @SaIgnore
    public Result saveVediotags(@RequestBody Vediotags vediotags) {
        return Result.success(vediotagsService.saveOrUpdate(vediotags));
    }

    //删除指定视频的绑定的标签
    @AutoLog("删除指定视频的绑定的标签")
    @DeleteMapping("/front/tagsbyvedioid/del/{id}")
    @SaIgnore
    public Result deleteVediotags(@PathVariable Integer id) {
        return Result.success(vediotagsService.remove(new QueryWrapper<Vediotags>().eq("vedio_id",id)));
    }

    //查询指定视频下的标签
    @AutoLog("查询指定视频下的标签")
    @GetMapping("/front/tagsbyvedioid/{id}")
    @SaIgnore
    public Result getVediotags(@PathVariable Integer id) {
        List<Vediotags> vediotagsList = vediotagsService.list(new QueryWrapper<Vediotags>().eq("vedio_id", id));
        if(CollectionUtil.isEmpty(vediotagsList)){
            return Result.success();
        }
        return Result.success(vediotagsList.stream().map(Vediotags::getTagsId).collect(Collectors.toList()));
    }

    @AutoLog("添加用户关注")
    @PostMapping("/front/follow/update")
    @SaIgnore
    public Result updateFollow(@RequestBody Follow follow) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<Follow>();
        queryWrapper.eq("follow_id", follow.getFollowId());
        queryWrapper.eq("user_id", follow.getUserId());
        if(followService.getOne(queryWrapper)!=null){
            return Result.error("你已关注，无需重复关注");
        }
        return Result.success(followService.saveOrUpdate(follow));
    }

    @AutoLog("删除用户关注")
    @DeleteMapping("/front/follow/{followId}/{userId}")
    @SaIgnore
    public Result deleteFollow(@PathVariable Integer followId, @PathVariable Integer userId) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<Follow>();
        queryWrapper.eq("follow_id", followId);
        queryWrapper.eq("user_id", userId);
        return Result.success(followService.remove(queryWrapper));
    }

    @AutoLog("查询关注状态")
    @GetMapping("/front/follow/follow/{followId}/{userId}")
    @SaIgnore
    public Result checkFollow(@PathVariable Integer followId, @PathVariable Integer userId) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<Follow>();
        queryWrapper.eq(followId != null, "follow_id", followId);
        queryWrapper.eq(userId != null, "user_id", userId);
        return Result.success(followService.getOne(queryWrapper) != null);
    }

    @AutoLog("我的关注列表查询")
    @GetMapping("/front/follow/page")
    @SaIgnore
    public Result findPageFollow(
            @RequestParam(defaultValue = "") String name,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<Follow>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        User user = SessionUtils.getUser();
        queryWrapper.eq("user_id",user.getId());
        return Result.success(followService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @AutoLog("详情页视频推荐")
    @GetMapping("/front/vediorecommend/{id}")
    @SaIgnore
    public Result vediorecommend(@PathVariable Integer id) {
        Vedio vedio = vedioService.getById(id);
        QueryWrapper<Vedio> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",vedio.getCategoryId());
        queryWrapper.orderBy(true, new Random().nextBoolean(), "RAND()");
        queryWrapper.last("limit 4");
        return Result.success(vedioService.list(queryWrapper));
    }
}