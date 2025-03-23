<script setup>
import { reactive, ref} from "vue"
import { User, Lock, Message } from '@element-plus/icons-vue'
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";
import config from "../../config";

  const loginData = reactive({})
  const rules = reactive({
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
    ],
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 3, max: 20, message: '密码长度在3-20位之间', trigger: 'blur' },
    ],
    confirmPassword: [
      { required: true, message: '请确认密码', trigger: 'blur' },
      { min: 3, max: 20, message: '密码长度在3-20位之间', trigger: 'blur' },
    ],
  })
  const ruleFormRef = ref()
  const register = () => {
    ruleFormRef.value.validate(valid => {
      if (valid) {
        if (loginData.password !== loginData.confirmPassword) {
          ElMessage.warning('两次密码不一致')
        }
		loginData.role = 'member'
        // 发送表单数据给后台
        request.post('/register', loginData).then(res => {
          if (res.code === '200') {
            ElMessage.success('注册成功')
            router.push('/login')
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    })
  }

</script>

<template>
  <div class="wrapper">
    <div style="margin: 150px auto; background-color: #333333; width: 400px; height: 430px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px;color: #ffffff;"><b>{{config.projectName}}</b></div>
      <el-form
              ref="ruleFormRef"
              :model="loginData"
              :rules="rules"
              size="large"
              status-icon
      >
        <el-form-item prop="username">
          <el-input v-model="loginData.username" placeholder="请输入用户名"  />
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="loginData.email" placeholder="请输入邮箱"  />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginData.password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="loginData.confirmPassword" show-password placeholder="请确认密码"/>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="primary" autocomplete="off" @click="register">注册</el-button>
          <el-button type="info" autocomplete="off" @click="router.push('/login')">登录</el-button>
        </el-form-item>
      </el-form>
        <div style="text-align: center;">
          <a style="text-decoration: none; color: #ffffff;margin-left: 20px;" href="/front/home">进入前台</a>
        </div>
    </div>
  </div>
</template>
<style>
  .wrapper {
    background-image: url("../assets/619613d4426298617.jpg");
    background-repeat: no-repeat;
    background-size: cover;
    height: 100vh;
    overflow: hidden;
  }
</style>
