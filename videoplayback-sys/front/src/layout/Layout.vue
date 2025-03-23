<script setup>
import { RouterView } from 'vue-router'
import router from "@/router";
import {useUserStore} from "@/stores/user";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
const userStore = useUserStore()
let user = userStore.getUser
const activePath = router.currentRoute.value.path.replace('/', '')

const logout = () => {
  request.get('/logout/' + user.uid).then(res => {
    if (res.code === '200') {
      userStore.logout()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const menus = userStore.getMenus

const getAvatarHandler = (avatar) => {
  user.avatar = avatar
}
</script>

<template>
  <div>
    <div style="height: 60px; line-height: 60px; border-bottom: 1px solid #ccc; background-color: #333333">
      <div style="display: flex">
        <div style="width: 300px; color: #FFFFFF; font-weight: bold;  text-align: center; font-size: 20px">
          视频播放平台
        </div>

        <div style="flex: 1; display: flex">
          <div style="flex: 1">
          </div>
          <div style="width: 500px; text-align: right; padding-right: 20px">
            <span style="margin-right: 5px; color: #FFFFFF">欢迎回来，{{ user.name }}</span>
            <a href="javascript:void(0)" @click="router.push('/front/home')" class="infoCls">进入前台</a>
            <a href="javascript:void(0)" @click="router.push('/person')" class="infoCls">个人信息</a>
            <a href="javascript:void(0)" @click="router.push('/password')" class="infoCls">修改密码</a>
            <a href="javascript:void(0)" @click="logout" class="infoCls">注销</a>
          </div>
        </div>
      </div>
    </div>

    <div style="display: flex;">
      <div style="width: 230px; min-height: calc(100vh - 60px); border-right: 1px solid #ccc;background-color: #333333">
        <el-menu
            :default-active="activePath"
            :default-openeds="menus.map(v => v.id + '')"
            class="el-menu-demo"
            style="border: none"
            router
        >
        <div v-for="item in menus" :key="item.id">
          <div v-if="item.type === 2">
            <el-menu-item :index="item.path" v-if="!item.hide">
              <el-icon v-if="item.icon">
                <component :is="item.icon"></component>
              </el-icon>
              <span>{{ item.name }}</span>
            </el-menu-item>
          </div>
          <div v-else>
            <el-sub-menu :index="item.id + ''" v-if="!item.hide">
              <template #title>
                <el-icon v-if="item.icon">
                  <component :is="item.icon"></component>
                </el-icon>
                <span>{{ item.name }}</span>
              </template>
              <div  v-for="subItem in item.children" :key="subItem.id">
                <el-menu-item :index="subItem.path" v-if="!subItem.hide">
                  <template #title>
                    <el-icon v-if="subItem.icon">
                      <component :is="subItem.icon"></component>
                    </el-icon>
                    <span>{{ subItem.name }}</span>
                  </template>
                </el-menu-item>
              </div>
            </el-sub-menu>
          </div>
        </div>
        </el-menu>
      </div>

      <div style="flex: 1; padding: 10px">
        <router-view v-slot="{ Component }">
          <component @getAvatar="getAvatarHandler" ref="childRef" :is="Component" />
        </router-view>
      </div>
    </div>

  </div>
</template>
<style>
  .el-menu-item{
    background-color: #333333;
    color: #FFFFFF;
  }
  .el-menu-item.is-active{
    background-color: #333333;
    color: #ffffff;
  }
  .el-menu-item:hover{
    background-color: #ffffff;
    color: #333333;
  }
  .el-sub-menu{
    background-color: #333333;
    color: #FFFFFF;
  }
  .el-sub-menu.is-active{
    background-color: #333333;
    color: #ffffff;
  }
  .el-sub-menu:hover{
    background-color: #ffffff;
    color: #333333;
  }

  .infoCls{
    margin-left: 10px;
    text-decoration: none;
    color: #ffffff;
  }
  .infoCls a {
    text-decoration: none;
    color: #ffffff;
    transition: color 0.3s;
  }

  .infoCls:hover {
    color: #f00;
  }

  .infoCls:visited {
    color: #999;
  }

  .infoCls::after {
    content: '';
    width: 0;
    height: 1px;
    background-color: #f00;
    transition: width 0.3s;
  }

  .infoCls:hover::after {
    width: 100%;
  }
</style>
