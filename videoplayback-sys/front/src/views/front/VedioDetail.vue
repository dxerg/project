<script setup>
  import router from "@/router";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";
  import {onMounted, reactive, ref} from "vue";
  import {useUserStore} from "@/stores/user";
  import '@wangeditor/editor/dist/css/style.css' // 引入 css
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

  const userStore = useUserStore()
  const user = userStore.getUser

  
  const id = router.currentRoute.value.query.id // 参数id
  const state = reactive({
    data: {},
  })

  const load = () => {
    request.get('/front/vedio/' + id).then(res => {
      state.data = res.data
      loadVedio(state.data.vedio)
      checkFollow(state.data.userId)
    })

  }
  onMounted(() => {
    load()
  })

  //轮播图
  request.get('/front/banner/list').then(res => {
    state.rotationList = res.data
    state.rotationList = state.rotationList.filter((item) => item.indexRadio === '否');
  })


  state.activeTab = 'content'

  //加载评论
  const loadComments = () =>{
    request.get('/front/comments/tree?vedioId=' + id).then(res => {
      state.comments = res.data
    })
  }
  loadComments();

  const dialogFormVisible = ref(false)
  //弹出评论框
  const handleComment = (row) => {
    //判断是否登录
    if(user.id==null){
      ElMessage.error('请先登录')
      return
    }
    dialogFormVisible.value = true
    state.form = {}
    if (row && row.id) {  // row是父节点的数据，如果存在，则为评论
      state.parent = row
    }
  }

  //保存评论
  state.parent = {}
  const save = () => {
    if (state.parent.id) {  // 回复的时候触发
      state.form.pid = state.parent.id
      state.form.puserId = state.parent.user.id  // 传父级评论的用户id
    }
    state.form.vedioId = id  // 当前模块的id
    state.form.userId = user.id //用户id
    // 发送数据
    request.post('/front/comments/update', state.form).then(res => {
      if (res.code === '200') {
        ElMessage.success("评论成功")
        dialogFormVisible.value = false
        loadComments()
        state.parent = {}
      } else {
        ElMessage.error(res.msg)
      }
    })
  }

  //删除评论
  const del = (id) => {
    request.delete('/front/comments/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        loadComments()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }

  state.userOptions = []
  request.get('/front/user/list').then(res => state.userOptions = res.data)
  state.categoryOptions = []
  request.get('/front/category/list').then(res => state.categoryOptions = res.data)

  state.collect = {}
  state.collectStatus = false
  //查询视频收藏状态
  const checkCollect = () =>{
    if(user.id==null){
      state.collectStatus = false
      return
    }
    request.get('/front/collect/collect/'+id+'/'+user.id).then(res => {
      state.collectStatus = res.data
    })
  }
  checkCollect()
  //添加视频收藏
  const addCollect = () =>{
    //判断是否登录
    if(user.id==null){
      ElMessage.error('请先登录')
      return
    }
    state.collect.userId = user.id
    state.collect.vedioId = id
    request.post('/front/collect/update', state.collect).then(res => {
      if (res.code === '200') {
        ElMessage.success("收藏成功")
        checkCollect()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
  //取消视频收藏
  const deleteCollect = () =>{
    //判断是否登录
    if(user.id==null){
      ElMessage.error('请先登录')
      return
    }
    request.delete('/front/collect/' + id +'/'+user.id).then(res => {
      if (res.code === '200') {
        ElMessage.success("取消收藏成功")
        checkCollect()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }


  state.follow = {}
  state.followStatus = false
  //查询关注状态
  const checkFollow = (followId) =>{
    if(user.id==null){
      state.followStatus = false
      return
    }
    request.get('/front/follow/follow/'+followId+'/'+user.id).then(res => {
      state.followStatus = res.data
    })
  }

  //添加作者关注
  const addFollow = (followId) =>{
    //判断是否登录
    if(user.id==null){
      ElMessage.error('请先登录')
      return
    }
    if(user.id==followId){
      ElMessage.error('不能关注自己')
      return
    }
    state.follow.userId = user.id
    state.follow.followId = followId
    request.post('/front/follow/update', state.follow).then(res => {
      if (res.code === '200') {
        ElMessage.success("关注成功")
        checkFollow(state.follow.followId)
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
  //取消作者关注
  const deleteFollow = (followId) =>{
    //判断是否登录
    if(user.id==null){
      ElMessage.error('请先登录')
      return
    }
    request.delete('/front/follow/' + followId +'/'+user.id).then(res => {
      if (res.code === '200') {
        ElMessage.success("取消关注成功")
        checkFollow(followId)
      } else {
        ElMessage.error(res.msg)
      }
    })
  }

  //视频设置
  state.activeVedioTab = 'vedio'
  const getUrl = (name) => {
    return new URL(`../../../../files/${name}`, import.meta.url).href
  }
  state.vedioUrl = ''
  state.vedioOptions = ''
  const loadVedio = (value) => {
    if(value==null || value==''){
      ElMessage.error('没有发现视频，请上传视频')
      return
    }
    state.vedioOptions = {
      width: '800px', //播放器高度
      height: '450px', //播放器高度
      color: "#409eff", //主题色
      muted: false, //静音
      webFullScreen: false,
      speedRate: ["0.75", "1.0", "1.25", "1.5", "2.0"], //播放倍速
      autoPlay: false, //自动播放
      loop: false, //循环播放
      mirror: false, //镜像画面
      ligthOff: false,  //关灯模式
      volume: 0.3, //默认音量大小
      control: true, //是否显示控制器
      title: '', //视频名称
      src: value, //视频源
      poster: '', //封面

    }
  }
  //增加浏览量
  const updateVedioViews = () =>{
    request.post('/front/vedio/views/update/'+id).then(res => {})
  }
  updateVedioViews()

  state.recommend = []
  request.get('/front/vediorecommend/'+id).then(res => {
    state.recommend = res.data
  })
</script>

<template>
  <div>

      <!-- 轮播图 -->
      <div>
        <div style="width: 100%">
          <el-carousel :interval="5000" arrow="always" height="200px">
            <el-carousel-item v-for="item in state.rotationList" :key="item">
              <a :href="item.url" target="_blank"><img :src="item.img" alt="" style="width: 100%; height: 100%"></a>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>


    <div class="mc detail-container">
        <div style="padding-bottom: 15px ;text-align: left;">
          <span style="font-size: 14px;margin-right: 20px;">当前位置：首页 > {{ state.data.name }}</span>
        </div>

        <div class="detail-content">
          <div class="detail-left">
            <img :src="state.data.img" style="height: 420px;width: 100%;"/>
          </div>
          <div class="detail-right">
            <div class="big-name"> {{ state.data.name }}</div>
            <div class="detail-info-list">
              <div>
                <span>播放量：</span>{{ state.data.views }}
              </div>
              <div>
                <span>上传时间：</span>{{ state.data.createTime }}
              </div>
              <div>
                <span>上传用户：</span>{{ state.userOptions.find(v => v.id === state.data.userId) ? state.userOptions.find(v => v.id === state.data.userId).name : '' }}
              </div>
              <div>
                <span>视频分类：</span>{{ state.categoryOptions.find(v => v.id === state.data.categoryId) ? state.categoryOptions.find(v => v.id === state.data.categoryId).name : '' }}
              </div>
              <div class="detail-btn" style="margin-top: 20px">
                <div @click="addCollect" v-if="!state.collectStatus"><i class="el-icon-date"></i>收藏</div>
                <div @click="deleteCollect" v-if="state.collectStatus"><i class="el-icon-date"></i>取消收藏</div>
                <div @click="addFollow(state.data.userId)" v-if="!state.followStatus"><i class="el-icon-date"></i>关注作者</div>
                <div @click="deleteFollow(state.data.userId)" v-if="state.followStatus"><i class="el-icon-date"></i>取消关注</div>
              </div>
            </div>
          </div>
        </div>

        <el-tabs v-model="state.activeVedioTab" style="margin-top: 20px;min-height: 500px;">
          <el-tab-pane label="视频播放" name="vedio">
            <div style="display: flex; justify-content: center; align-items: center; height: 100%;">
              <vue3VideoPlay class="vue-video" v-bind="state.vedioOptions" />
            </div>
          </el-tab-pane>
        </el-tabs>

        <el-tabs v-model="state.activeTab" style="margin-top: 20px;min-height: 300px;">
          <el-tab-pane label="简介详情" name="content">
            <span class="markdown-body" v-html="state.data.content"></span>
          </el-tab-pane>
          <el-tab-pane label="评论列表" name="comments">
            <div v-if="state.comments && state.comments.length">
              <div v-for="item in state.comments" :key="item.id" style="padding: 10px 20px;">
                <div style="display: flex; align-items: center">
                  <img v-if="item.user" style="width: 40px; height: 40px; border-radius: 50%"  :src="item.user.avatar" alt="">
                  <span v-if="item.user" style="margin-left: 10px; font-weight: bold">{{ item.user.name }}<el-rate v-model="item.score" style="margin-left: 10px;" :disabled="true"></el-rate></span>
                </div>

                <div style="line-height: 30px; padding-left: 50px; color: #666; margin: 5px 0">{{ item.content }}</div>

                <div style="padding-left: 50px; font-size: 12px; color: #999; display: flex">
                  <div style="flex: 1">
                    <el-icon style="top: 2px"><Clock /></el-icon> <span>{{item.createTime }}</span>
                  </div>

                  <div style="flex: 1; text-align: right">
                    <el-button type="danger" link style="color: red;" v-if="item.userId === user.id"  @click="del(item.id)">删除</el-button>
                  </div>
                </div>

                <div style="margin-top: 20px; margin-left: 50px; border-bottom: 1px solid #ddd;">
                  <!-- 回复的子节点 -->
                  <div v-for="subItem in item.children" :key="subItem.id" style="padding: 10px 0">
                    <div style="display: flex; align-items: center">
                      <img v-if="subItem.user" style="width: 40px; height: 40px; border-radius: 50%"  :src="subItem.user.avatar" alt="">
                      <span v-if="subItem.user" style="margin-left: 10px; font-weight: bold">{{ subItem.user.name }}<el-rate v-model="subItem.score" style="margin-left: 10px;" :disabled="true"></el-rate></span>
                    </div>

                    <div style="line-height: 30px; padding-left: 50px; color: #666; margin: 5px 0">
                      <span style="color: #999">回复<span style="color: orange" v-if="subItem.puser">@{{ subItem.puser.name }}</span></span>
                      <span style="margin-left: 10px">{{ subItem.content }}</span>
                    </div>

                    <div style="padding-left: 50px; font-size: 12px; color: #999; display: flex">
                      <div style="flex: 1">
                        <el-icon style="top: 2px"><Clock /></el-icon> <span>{{subItem.createTime }}</span>
                      </div>

                      <div style="flex: 1; text-align: right">
                        <el-button type="danger" link style="color: red;" @click="del(subItem.id)" v-if="subItem.userId === user.id">删除</el-button>
                      </div>
                    </div>
                  </div>

                </div>
              </div>
            </div>

            <div v-else style="margin: 10px 0; color: #999">
              暂时还没有评论哦！
            </div>

            <div style="display: flex; align-items: center">
              <el-input style="flex: 1" placeholder="请您文明发言" @click="handleComment"></el-input>
            </div>

          </el-tab-pane>
        </el-tabs>

    </div>

    <div style="width:85%;margin: 0 auto;margin-bottom: 50px;">
      <div style="padding-bottom: 10px ;padding-top: 5px ;border-bottom: 3px solid #333333;border-top: 3px solid #333333; background-color: #e0e0e0; margin-top: 20px;text-align: center;">
        <span style="font-weight: bold; font-size: 24px;color:#333333;">推荐相似视频</span>
      </div>
      <div style="margin-top: 20px;">
        <el-row :gutter="10">
          <el-col :span="6" v-for="item in state.recommend" :key="item.id" style="margin-top: 20px;">
            <el-card style="color: #666" >
              <div ><img @click="router.push('/moviedetail?id=' + item.id)" :src="item.img" alt="" style="width: 100%; height: 220px;cursor: pointer;"></div>
              <div><span style="font-weight: bold">{{ item.name}}</span></div>
              <div style="width: 90%;margin-top: 20px;line-height: 40px;height: 40px;">
                <el-button type="primary" size="small" @click="router.push('/moviedetail?id=' + item.id)" style="float: left;">>>>详情</el-button>
                <span style="color:red;font-size: 18px;font-weight: bold;float: right;" v-if="item.price">￥{{item.price}}</span>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <el-dialog v-model="dialogFormVisible" title="评论" width="30%">
      <el-form :model="state.form" label-width="50px" style="padding: 0 20px" status-icon>
        <el-form-item label="评分">
          <el-rate v-model="state.form.score">
          </el-rate>
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" :rows="5" v-model="state.form.content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">
          确定
        </el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
  .detail-container {
    padding: 20px 0;
    width:85%;margin: 0 auto;margin-bottom: 50px;
  }

  .big-name {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
    font-size: 28px;
    font-weight: 700;
    line-height: 40px;
    color: #101d37;
  }


  .detail-content {
    display: flex;
  }

  .detail-left {
    width: 48%;
    margin-right: 2%;

  }

  .detail-right {
    width: 50%;
    border: 1px solid #e4e6f0;
    padding: 17px 30px 0;
    background-color: #eeeeee;
    box-sizing: border-box;
    height: 420px;
  }

  .detail-tag div {
    background-color: rgba(132, 154, 174, .1);
    border-radius: 2px;
    display: inline-block;
    padding: 2px 10px;
    height: 23px;
    line-height: 23px;
    text-align: center;
    font-size: 12px;
    color: #849aae;
    margin-right: 5px;
    margin-bottom: 5px;
  }

  .detail-info-list {
    padding: 10px 0;
    border-bottom: 1px solid #e4e6f0;
  }

  .detail-info-list div {
    margin-bottom: 5px;
    font-size: 18px;
  }

  .detail-info-list div span {
    color: #9399a5;

  }

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }

  .detail-btn {
    display: flex;

  }

  .detail-btn div {
    width: 160px;
    height: 50px;
    line-height: 14px;
    padding: 18px 0;
    font-size: 16px;
    box-sizing: border-box;
    background: #333333;
    color: #fff;
    text-align: center;
    margin-right: 15px;
    cursor: pointer;
    border-radius: 20px;
  }


  /* el-tabs */
  ::v-deep .el-tabs__nav {
    margin: 0 20px;
    /* 使用rpx没有效果 */
  }
  ::v-deep .el-tabs--top .el-tabs__item.is-top:nth-child(2) {
    padding-left: 20px;
  }
  ::v-deep .el-tabs--top .el-tabs__item.is-top:last-child {
    padding-right: 20px;
  }
  /*悬浮样式*/
  ::v-deep .el-tabs__item:hover {
    color: #e1251b;
  }
  /*选中样式*/
  ::v-deep .el-tabs__item.is-active {
    color: #fff;
    font-weight: bold;
    background-color: #333333;
  }
  /*隐藏tab下面的一横*/
  ::v-deep .el-tabs__active-bar {
    display: none;
  }

</style>