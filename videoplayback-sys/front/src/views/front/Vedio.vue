<script setup>
  import {nextTick, onBeforeUnmount, reactive, ref, shallowRef} from "vue";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";
  import {useUserStore} from "@/stores/user";
  import router from "@/router"
  import config from "../../../config";

  const userStore = useUserStore()
  const user = userStore.getUser

  
  const pageNum = ref(1)
  const pageSize = ref(8)
  const total = ref(0)

  let name = router.currentRoute.value.query.name
  const state = reactive({
    tableData: [],
    form:{},
  })

  state.searchKey = ''
  const load = () => {
    if(state.searchKey!=null && state.searchKey!=''){
      name = state.searchKey
    }
    let tags = null
    if(state.form.selectTags!=null) {
       tags = state.form.selectTags.join(",")
    }
    request.get('/front/vedio/page', {
      params: {
        name: name,
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        category_id:state.category_id,
        tags:tags,
      }
    }).then(res => {
      state.tableData = res.data.records
      total.value = res.data.total
    })
  }
  load()

  //轮播图
  request.get('/front/banner/list').then(res => {
    state.rotationList = res.data
    state.rotationList = state.rotationList.filter((item) => item.indexRadio === '否');
  })

  state.categoryOptions = []
  request.get('/front/category/list').then(res => state.categoryOptions = res.data)
  state.userOptions = []
  request.get('/front/user/list').then(res => state.userOptions = res.data)

  state.category_id = ''
  const getCategory = (category_id) =>{
    state.category_id = category_id
    if(category_id=='all'){
      state.category_id = ''
    }
    load()
  }

  const truncatedContent = (content) => {
    const maxLength = 100;
    if (content.length > maxLength) {
      return content.substring(0, maxLength) + '...';
    }
    return content;
  }

  //搜索
  state.searchKey = ''
  const search = () =>{
    load()
  }

  state.tagsOptions = []
  request.get('/front/tags/list').then(res => state.tagsOptions = res.data)

  const getTags = ()=>{


    load()
  }
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


    <div style="width:85%;margin: 0 auto;margin-bottom: 50px;">
        <div style="padding-bottom: 15px ;margin-top: 20px;text-align: left;">
          <span style="font-size: 14px;margin-right: 20px;">当前位置：首页 > 视频列表</span>
        </div>

        <div style="padding-bottom: 15px ;border-bottom: 3px solid #333333; text-align: left;display: flex;">
          <span style="font-weight: bold; font-size: 24px;float: left;flex: 3;color: #333333;">视频列表</span>

          <div style="margin-top: 10px;float: right;flex: 1;">
            <el-input style="width: 200px" placeholder="查询视频列表" v-model="state.searchKey" clearable></el-input>
            <el-button style="margin-left: 5px" @click="search" size="large">查询</el-button>
          </div>
        </div>

        <div style="padding-bottom: 15px ;margin-top: 20px;text-align: left;line-height: 40px;">
          <span style="font-weight: bold;margin-left: 10px;">视频分类：</span>
          <span style="margin-right: 10px;" class="menubar">
            <a href="javascript:void(0)" @click="getCategory('all')">全部</a>
          </span>
          <span v-for="item in state.categoryOptions" :key="item.id" style="margin-right: 10px;" class="menubar">
            <a href="javascript:void(0)" @click="getCategory(item.id)">{{item.name}}</a>
          </span>
        </div>
      <div style="padding-bottom: 15px ;margin-top: 20px;text-align: left;line-height: 40px;">
        <span style="font-weight: bold;margin-left: 10px;">视频标签：</span>
        <el-select clearable v-model="state.form.selectTags" placeholder="选择标签"  style="width: 50%" filterable multiple @change="getTags">
          <el-option v-for="item in state.tagsOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </div>

        <div style="margin-top: 20px;">
            <div style="margin-top: 20px;">
              <div class="item" v-for="item in state.tableData" :key="index" >
                <img :src="item.img" style="margin-left: 30px;"/>
                <div class="right-container">
                  <div class="top">
                    <div style="cursor: pointer;width:80%; "><a href="javascript:void(0)" class="title" @click="router.push('/front/vedio-detail?id=' + item.id)">{{ item.name }}</a></div>
                    <!-- （可选）显示价格 -->
                    <div style="color: #fa5741;width:20%; " v-if="item.price!=null">{{ item.price }} 元</div>
                  </div>
                  <div style="color: #6d6d73;font-weight: bold; font-size: 14px;" v-html="truncatedContent(item.content)"></div>
                  <div class="time">发布时间：{{ item.createTime }}</div>
                </div>
              </div>
            </div>
        </div>

        <div style="margin: 20px 0;">
          <el-pagination
                  @current-change="load"
                  @size-change="load"
                  v-model:current-page="pageNum"
                  v-model:page-size="pageSize"
                  background
                  :page-sizes="[4, 8, 12, 16]"
                  layout="total, prev, pager, next"
                  :total="total"
          />
        </div>

    </div>

  </div>
</template>
<style scoped>
  .menubar {
    background-color: #eeeeee;
    padding: 10px;
    justify-content: center;
  }

  .menubar a {
    font-size: 16px;
    color: #333;
    text-decoration: none;
    margin: 0 15px;
    padding: 5px;
    transition: color 0.3s;
  }

  .menubar a:hover {
    color: #ff6700;
  }


  /* 标题样式 */
  .title {
    font-size: 18px;
    font-weight: bold;
    color: #333;  /* 标题默认颜色 */
    text-decoration: none;  /* 移除下划线 */
    /* 悬停效果 */
    transition: color 0.3s ease;
  }

  .title:hover {
    color: orangered !important;  /* 鼠标悬停时的颜色 */
  }


  .item {
    padding: 20px 0;
    display: flex;
    border-radius: 10px;
  }

  .item img {
    width: 160px;
    height: 120px;
  }

  .right-container {
    position: relative;
    margin-left: 20px;
    width: 100%;

  }

  .right-container div {
    margin-bottom: 7px;
  }

  .top {
    display: flex;
    color: #101d37;
    font-size: 20px;
    font-weight: bold;
  }

  .time {
    position: absolute;
    left: 0;
    bottom: 0;
    margin-bottom: 0 !important;
    color: rgba(16, 29, 55, .3);
    font-size: 14px;
  }
</style>