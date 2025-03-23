<script setup>
import { RouterView } from 'vue-router'
import router from "@/router";
import {useUserStore} from "@/stores/user";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {nextTick, ref} from "vue";
import config from "../../config";

const userStore = useUserStore()
let user = userStore.getUser
const activePath = router.currentRoute.value.path
const name = ref('')
name.value = router.currentRoute.value.query.name || ''
const childRef = ref()
const menus = ref([])


const logout = () => {
  request.get('/logout/' + user.uid).then(res => {
    if (res.code === '200') {
      userStore.logout()
	  window.location.href="/front"
    } else {
      ElMessage.error(res.msg)
    }
  })
}


const getAvatarHandler = (avatar) => {
  user.avatar = avatar
}

const search = () => {
  router.push('/front/vedio?name=' + name.value)
}

</script>

<template>
  <div>
    <div style="display: flex; height: 100px; line-height: 100px; border-bottom: 1px solid #eee;">
      <div style="flex: 2; font-size: 26px; color: #333333; font-weight: bold;margin-left: 180px;">{{config.projectName}}</div>
      <div style="flex: 2;">
        <!-- 搜索条 -->
        <el-input style="width: 250px" placeholder="搜索你想看的视频" v-model="name" clearable size="large"></el-input>
        <el-button style="margin-left: 5px;background-color: #333333;color: #FFFFFF;" @click="search" size="large">搜索</el-button>
      </div>
      <div style="flex: 3; font-size: 16px; color: #333333;" v-if="user.id">
        <el-avatar :size="30" :src="user.avatar" style="margin-top: 10px;" />
        你好，{{ user.name }}
        <a href="javascript:void(0)" @click="router.push('/person')" class="loginCls">修改信息</a>
        <a href="javascript:void(0)" @click="router.push('/password')" class="loginCls">更改密码</a>
        <a href="javascript:void(0)" @click="logout" class="loginCls">退出登录</a>
      </div>
      <div style="flex: 3; font-size: 16px; color: #333333; margin-left: 180px;" v-else>
        <a href="javascript:void(0)" @click="router.push('/login')" class="loginCls">登录</a>
        <a href="javascript:void(0)" @click="router.push('/register-member')" class="loginCls">用户注册</a>
      </div>
    </div>

    <div style="display: flex; height: 60px; line-height: 60px; border-bottom: 1px solid #eee;background-color: #333333;">
      <div style="width: 200px; display: flex; padding-left: 30px">
      </div>
      <div style="flex: 1; display: flex">
        <el-menu :default-active="activePath" mode="horizontal" router style="border: none; height: 100%; width: 100%;background-color: #333333;">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/notice">平台公告</el-menu-item>
          <el-menu-item index="/front/vedio">视频列表</el-menu-item>
          <el-menu-item index="/front/feedback">用户反馈</el-menu-item>
          <el-dropdown v-if="user.id!=null && user.role=='member'">
            <el-menu-item>个人中心</el-menu-item>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item><div @click="router.push('/vedio')">上传视频</div></el-dropdown-item>
                <el-dropdown-item><div @click="router.push('/front/collect')">我的视频收藏</div></el-dropdown-item>
                <el-dropdown-item><div @click="router.push('/front/follow')">我的关注用户</div></el-dropdown-item>
                <el-dropdown-item><div @click="router.push('/feedback')">添加用户反馈</div></el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-menu-item index="/home" v-if="user.id!=null && user.role=='ADMIN'">进入后台管理</el-menu-item>
        </el-menu>
      </div>
    </div>

    <div style="width: 100%; margin: 0 auto;min-height: 600px;">
      <router-view v-slot="{ Component }">
        <component @getAvatar="getAvatarHandler" @getUnread="getUnRead" ref="childRef" :is="Component" />
      </router-view>
    </div>


    <div style="height: 100px; line-height: 100px; border-top: 1px solid rgba(208,208,208,0.08);text-align: center;background-color: #333333;color: #FFFFFF;">
      <span>{{config.projectName}}</span>
    </div>
  </div>
</template>

<style scoped>
  .badge {
    margin-top: 10px;
    margin-right: 40px;
  }
  :deep(.el-badge__content.is-fixed) {
    top: 10px !important;
  }

  .content {
    text-align: center;
  }

  .el-menu-item{
    background-color: #333333;
    color: #FFFFFF;
  }

  .el-menu--horizontal>.el-menu-item.is-active{
    color: #ffffff !important;
    border-bottom: 2px solid #ffffff !important;
  }

  .loginCls{
    margin-left: 25px;
    text-decoration: none;
    color: #333333;
  }
  .loginCls a {
    text-decoration: none;
    color: #333;
    transition: color 0.3s;
  }

  .loginCls:hover {
    color: #f00;
  }

  .loginCls:visited {
    color: #999;
  }

  .loginCls::after {
    content: '';
    width: 0;
    height: 1px;
    background-color: #f00;
    transition: width 0.3s;
  }

  .loginCls:hover::after {
    width: 100%;
  }
</style>
