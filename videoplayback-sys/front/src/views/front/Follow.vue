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
  const pageNum = ref(1)
    const pageSize = ref(5)
  const total = ref(0)

  //判断用户是否登录
  if(user.id==null){
    router.push('/login')
  }

  const content = ref('')
  const viewShow = ref(false)
  const view = (value) => {
    viewShow.value = true
    content.value = value
  }

  const id = router.currentRoute.value.query.id
  let name = router.currentRoute.value.query.name
  const state = reactive({
    tableData:[],
  })

  state.searchKey = ''
  const load = () => {
    if(state.searchKey!=null && state.searchKey!=''){
      name = state.searchKey
    }
    request.get('/front/follow/page', {
      params: {
        name: name,
        pageNum: pageNum.value,
        pageSize: pageSize.value,
      }
    }).then(res => {
        state.tableData = res.data.records
        total.value = res.data.total
    })
  }
  load()  // 调用 load方法拿到后台数据

  //轮播图
  request.get('/front/banner/list').then(res => {
    state.rotationList = res.data
    state.rotationList = state.rotationList.filter((item) => item.indexRadio === '否');
  })

  state.userOptions = []
  request.get('/front/user/list').then(res => state.userOptions = res.data)
  state.vedioOptions = []
  request.get('/front/vedio/list').then(res => state.vedioOptions = res.data)


  //搜索
  state.searchKey = ''
  const search = () =>{
    load()
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
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
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
          <span style="font-size: 14px;margin-right: 20px;">当前位置：首页 > 我的关注用户</span>
        </div>

        <div style="padding-bottom: 15px ;border-bottom: 3px solid #333333; text-align: left;display: flex;">
          <span style="font-weight: bold; font-size: 24px;float: left;flex: 3;color: #333333;">我的关注用户</span>

        </div>


        <el-table :data="state.tableData" style="width: 100%;margin-top: 20px;" stripe border :header-cell-class-name="'headerBg'">
      <el-table-column label="当前用户"><template #default="scope"><span v-if="scope.row.userId">{{ state.userOptions.find(v => v.id === scope.row.userId) ? state.userOptions.find(v => v.id === scope.row.userId).name : '' }}</span></template></el-table-column>
          <el-table-column label="关注用户"><template #default="scope"><span v-if="scope.row.followId">{{ state.userOptions.find(v => v.id === scope.row.followId) ? state.userOptions.find(v => v.id === scope.row.followId).name : '' }}</span></template></el-table-column>
          <el-table-column label="操作">
            <template #default="scope"><el-button type="success" size="small" @click="deleteFollow(scope.row.followId)">取消关注</el-button></template>
          </el-table-column>
        </el-table>

        <div class="page">
          <el-pagination
                  prev-text="上一页"
                  next-text="下一页"
                  @current-change="load"
                  @size-change="load"
                  v-model:current-page="pageNum"
                  v-model:page-size="pageSize"
                  :page-sizes="[4, 8, 12, 16]"
                  layout="prev, pager, next"
                  :total="total"
          />
        </div>

      <el-dialog v-model="viewShow" title="浏览内容" width="40%">
        <div  id="editor-content-view" class="editor-content-view" v-html="content" style="padding: 0 20px"></div>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="viewShow = false">关闭</el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<style>
  .navbar {
    background-color: #eeeeee;
    padding: 10px;
    justify-content: center;
  }

  .navbar a {
    font-size: 16px;
    color: #333;
    text-decoration: none;
    margin: 0 15px;
    padding: 5px;
    transition: color 0.3s;
  }

  .navbar a:hover {
    color: #ff6700;
  }

  .page {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 13px;
    color: #656565;
    margin-top: 20px;
    padding-bottom: 20px;
  }

  .page > div.homePage, .page > div.lastPage {
    height: 34px;
    line-height: 34px;
    width: 60px;
    text-align: center;
    border: 1px solid #EAEAEA;
    box-sizing: border-box;
    cursor: pointer;
  }

  .page > div.homePage {
    border-radius: 2px 0 0 2px;
  }

  .page > div.lastPage {
    border-radius: 0 2px 2px 0;
  }

  .el-pagination {
    text-align: center;
    padding: 0;
  }

  .el-pagination > button {
    padding: 0 !important;
    height: 34px !important;
  }

  .el-pagination > button.btn-prev {
    border-right: none !important;
  }

  .el-pagination span {
    color: #656565;
    width: 50px;
    border: 1px solid #EAEAEA;
    height: 34px !important;
    line-height: 34px !important;
    box-sizing: border-box;
  }

  .el-pagination .el-pager .number, .el-icon.more.el-icon-more {
    color: #656565 !important;
    border: 1px solid #EAEAEA;
    height: 34px !important;
    line-height: 34px !important;
    box-sizing: border-box;
  }

  .el-icon.more.btn-quicknext.el-icon-more {
    border-right: none;
  }

  .el-icon.more.btn-quickprev.el-icon-more {
    border-left: none;
  }

  .el-pagination .el-pager .number:not(:first-child) {
    border-right: none !important;
  }

  .el-pagination .el-pager .number.active {
    background: #FF9900;
    border: 1px solid #FF9900;
    color: #FFFFFF !important;
  }

  .headerBg {
    background: #333333!important;
    color: #ffffff!important;
  }


  .el-dialog {
    background-color: #fff; /* 设置对话框的背景颜色 */
    border: 1px solid #ccc; /* 设置对话框的边框颜色 */
    border-radius: 4px; /* 设置对话框的边框圆角 */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.15); /* 设置对话框的阴影效果 */
    padding: 20px; /* 设置对话框的内边距 */
    box-sizing: border-box; /* 防止内边距和边框超出容器 */
  }

  .el-dialog__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    background-color: #333333;
    border-bottom: 1px solid #ccc;
  }

  .el-dialog__header > * {
    margin-right: 10px;
    color: #ffffff;
  }

  .el-dialog__title {
    font-size: 18px;
    font-weight: bold;
  }

  .el-dialog__body {
    padding: 20px;
  }

  .el-dialog__footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    border-top: 1px solid #ccc;
  }

  .el-dialog__footer > * {
    margin-left: 10px;
  }
</style>