<script setup>
import {reactive, defineEmits, ref, watch} from "vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import config from "../../config";
import {useUserStore} from "@/stores/user";

const userStore = useUserStore()
const user = userStore.getUser
const token = userStore.getBearerToken

const state = reactive({
  form: {}
})
const load = () => {
  request.get('/user/' + user.id).then(res => {
    state.form = res.data
  })
}
load()

const handleImportSuccess = (res) => {
  state.form.avatar = res.data
  ElMessage.success("上传头像成功")
}

let $myEmit = defineEmits(['getAvatar'])
const save = () => {
  request.put("/updateUser", state.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      userStore.setUser(res.data)
      $myEmit('getAvatar', res.data.avatar)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>

<template>
  <div style="width: 40%;margin-top: 30px;">
    <span style="font-size: 22px;text-align: center;line-height: 80px;"><h3>修改个人信息</h3></span>
    <el-form style="width: 80%; margin: 0 auto" label-width="60px">
      <div style="text-align: center">
        <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :action='`http://${config.serverUrl}/file/upload`'
                :on-success="handleImportSuccess"
                :headers="{ Authorization: token}"
        >
          <img v-if="state.form.avatar" :src="state.form.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>

      <el-form-item label="修改账户" style="margin-top: 20px;" label-width="250px;">
        <el-input v-model="state.form.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="修改Email" label-width="250px;">
        <el-input v-model="state.form.email"></el-input>
      </el-form-item>
      <el-form-item label="修改姓名" label-width="250px;">
        <el-input v-model="state.form.name"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align: center; width: 100%">
      <el-button type="primary" @click="save">确认修改</el-button>
    </div>
	
	
  </div>
</template>

<style>
  .avatar-uploader .avatar {
    width: 200px;
    height: 200px;
    display: block;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #ccc;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #ccc;
  }

  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 200px;
    height: 200px;
    text-align: center;
  }
  
</style>

