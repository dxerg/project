<script setup>
import {onMounted, reactive, ref, watch} from "vue"
import {User, Lock} from '@element-plus/icons-vue'
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {useUserStore} from "@/stores/user";
import router, {setRoutes} from "@/router";
import SIdentify from '../components/Sidentify.vue';
import config from "../../config";

// 图形验证码
let identifyCodes = "1234567890"
let identifyCode = ref('')
const failCount = ref(0)
const randomNum = (min, max) => {
  return Math.floor(Math.random() * (max - min) + min)
}
const makeCode = (o, l) => {
  for (let i = 0; i < l; i++) {
    identifyCode.value += o[randomNum(0, o.length)];
  }
}
const refreshCode = () => {
  identifyCode.value = "";
  makeCode(identifyCodes, 4);
}
// 生成验证码
onMounted(() => {
  identifyCode.value = "";
  makeCode(identifyCodes, 4);
})

const loginData = reactive({})
const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 3, max: 20, message: '密码长度在3-20位之间', trigger: 'blur'},
  ],
})
const ruleFormRef = ref()
const login = () => {
  ruleFormRef.value.validate(valid => {
    if (valid) {
      // 失败3次触发验证码
      if (failCount.value >= 3 && loginData.code !== identifyCode.value) {
        ElMessage.warning('验证码错误')
        return
      }
      // 发送表单数据给后台
      request.post('/login', loginData).then(res => {
        if (res.code === '200') {
          if (res.data.user.role === 'ADMIN') {
            router.push('/') // 后台的首页
          }
          		  else if(res.data.user.role === 'member'){
            router.push('/front') 
          }
          ElMessage.success('登录成功')
          const userStore = useUserStore()
          userStore.setManagerInfo(res.data)
        } else {
          ElMessage.error(res.msg)
          failCount.value ++  // 失败次数加1
        }
      })
    }
  })
}

</script>

<template>
  <div class="wrapper">
    <div style="margin: 150px auto; background-color: #333333; width: 400px; height: 330px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px;color: #ffffff;"><b>{{config.projectName}}</b></div>
      <el-form
              ref="ruleFormRef"
              :model="loginData"
              :rules="rules"
              size="large"
              status-icon
      >
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="loginData.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="loginData.password"></el-input>
        </el-form-item>
        <div style="display: flex; margin: 15px 0" v-if="failCount >= 3">
          <div style="flex: 1">
            <el-input v-model="loginData.code" placeholder="验证码"></el-input>
          </div>
          <div>
            <div @click="refreshCode" style="margin-left: 5px">
              <SIdentify :identifyCode="identifyCode" />
            </div>
          </div>
        </div>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="primary" autocomplete="off" @click="login">登录</el-button>
          <el-button type="info" autocomplete="off" @click="router.push('/register-member')">用户注册</el-button>
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

