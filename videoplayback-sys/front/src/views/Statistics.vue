<script setup>
import * as echarts from 'echarts'
import {onMounted, reactive} from "vue";
import request from "@/utils/request";

const state = reactive({
    categoryCount:'',
  vedioCount:'',
  tagsCount:'',
  memberCount:'',
})

onMounted(() => {
  //获取数量
  request.get('/statistics/categoryCount').then(res => {
    state.categoryCount = res.data
  })
  request.get('/statistics/vedioCount').then(res => {
    state.vedioCount = res.data
  })
  request.get('/statistics/tagsCount').then(res => {
    state.tagsCount = res.data
  })
  request.get('/statistics/memberCount').then(res => {
    state.memberCount = res.data
  })



  let goodsCategoryOption = {
    title: {
      text: '不同视频分类数量统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        type: 'pie',
        radius: '50%',
        data: [],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  let chart_goodsCategory = echarts.init(document.getElementById("chart_goodsCategory"))
  request.get('/statistics/goodsCategory').then(res => {
    goodsCategoryOption.series[0].data = res.data
    // 绘制图表
    chart_goodsCategory.setOption(goodsCategoryOption)
  })







  let vedioStaticsOption = {
    title: {
      text: '最近30天视频上传量统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    xAxis: {
      type: 'category',
      data: []
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [],
        type: 'bar',
        itemStyle: {
          color: function() {
            var letters = "0123456789ABCDEF";
            var color = "#";
            for (var i = 0; i < 6; i++) {
              color += letters[Math.floor(Math.random() * 16)];
            }
            return color;
          }
        }
      }
    ]
  };
  let chart_vedioStatics = echarts.init(document.getElementById("chart_vedioStatics"))
  request.get('/statistics/vedioStatics').then(res => {
    vedioStaticsOption.series[0].data = res.data.map(v => v.value)
    vedioStaticsOption.xAxis.data = res.data.map(v => v.name)
    // 绘制图表
    chart_vedioStatics.setOption(vedioStaticsOption)
  })




})
</script>

<template>
  <div>
    <div>
      <el-row :gutter="10">

        <el-col :span="6">
          <el-card style="height: 100px;">
            <div style="color: #888">视频分类数量</div>
            <div style="font-size: 24px; font-weight: bold; text-align: center">{{ state.categoryCount.categoryCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="height: 100px;">
            <div style="color: #888">上传视频数量</div>
            <div style="font-size: 24px; font-weight: bold; text-align: center">{{ state.vedioCount.vedioCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="height: 100px;">
            <div style="color: #888">视频标签数量</div>
            <div style="font-size: 24px; font-weight: bold; text-align: center">{{ state.tagsCount.tagsCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="height: 100px;">
            <div style="color: #888">注册用户数</div>
            <div style="font-size: 24px; font-weight: bold; text-align: center">{{ state.memberCount.memberCount }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div style="margin: 20px 0;">
      <el-row :gutter="10">

        <el-col :span="24">
          <div style="width:100%; height: 500px" id="chart_goodsCategory"></div>
        </el-col>
        <el-col :span="24">
          <div style="width:100%; height: 500px" id="chart_vedioStatics"></div>
        </el-col>
      </el-row>
    </div>


  </div>
</template>
