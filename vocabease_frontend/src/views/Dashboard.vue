<template>
  <div class="dashboard-container">
    <h2 class="page-title">系统数据</h2>

    <!-- 数据卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6" v-for="item in statData" :key="item.statisticName">
        <el-card shadow="hover" class="stat-card">
          <div class="card-content">
            <div class="title">{{ item.statisticName }}</div>
            <div class="value">{{ item.count }}</div>
            <div class="diff" :class="{ 'up': item.preCount < item.count, 'down': item.preCount > item.count }">
              {{ getDiffText(item) }}
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 用户行为趋势 -->
    <el-row :gutter="20" style="margin-top: 24px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span class="value">近7天用户趋势</span>
          </template>
          <div ref="userChartRef" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>

      <!-- 内容趋势 -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span class="value">近7天内容统计</span>
          </template>
          <div ref="contentChartRef" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'

// 类型定义
interface StatisticDataDto {
  statisticName: string
  count: number
  preCount: number
}

interface StatisticWeekItem {
  statisticName: string
  dataList: number[]
}

interface StatisticWeekDataDto {
  dataList: string[] // 日期列表
  itemDataList: StatisticWeekItem[]
}

// 响应式数据
const statData = ref<StatisticDataDto[]>([])
const userWeekData = ref<StatisticWeekDataDto | null>(null)
const contentWeekData = ref<StatisticWeekDataDto | null>(null)

// 图表引用
const userChartRef = ref<HTMLDivElement | null>(null)
const contentChartRef = ref<HTMLDivElement | null>(null)

// 工具函数
const getDiffText = (item: StatisticDataDto): string => {
  const diff = item.count - item.preCount
  if (diff === 0) return '—'
  const sign = diff > 0 ? '+' : ''
  return `${sign}${diff} (昨日)`
}

// 获取总览数据
const fetchAllData = async () => {
  try {
    const res = await axios.get('/index/getAllData')
    statData.value = res.data.data || []
  } catch (err) {
    console.error('获取统计数据失败', err)
  }
}

// 获取用户周数据
const fetchUserWeekData = async () => {
  try {
    const res = await axios.get('/index/getWeekAllData')
    userWeekData.value = res.data.data
    renderUserChart()
  } catch (err) {
    console.error('获取用户周数据失败', err)
  }
}

// 获取内容周数据
const fetchContentWeekData = async () => {
  try {
    const res = await axios.get('/index/getWeekContentData')
    contentWeekData.value = res.data.data
    renderContentChart()
  } catch (err) {
    console.error('获取内容周数据失败', err)
  }
}

// 渲染用户图表
const renderUserChart = () => {
  if (!userWeekData.value || !userChartRef.value) return
  const chart = echarts.init(userChartRef.value)
  const { dataList: dates, itemDataList: series } = userWeekData.value

  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: series.map(s => s.statisticName) },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value' },
    series: series.map(s => ({
      name: s.statisticName,
      type: 'line',
      data: s.dataList,
      smooth: true
    }))
  }
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 渲染内容图表
const renderContentChart = () => {
  if (!contentWeekData.value || !contentChartRef.value) return
  const chart = echarts.init(contentChartRef.value)
  const { dataList: dates, itemDataList: series } = contentWeekData.value

  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: series.map(s => s.statisticName) },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value' },
    series: series.map(s => ({
      name: s.statisticName,
      type: 'line',
      data: s.dataList,
      smooth: true
    }))
  }
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

// 生命周期
onMounted(() => {
  fetchAllData()
  fetchUserWeekData()
  fetchContentWeekData()
})
</script>

<style scoped>
.dashboard-container {
  padding: 0;
}

.page-title {
  margin-bottom: 5px;
  font-size: 24px;
  color: #333;
}

.stat-cards {
  margin-bottom: 24px;
}

.stat-card {
  height: 120px;
}

.card-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.title {
  font-size: 14px;
  color: #999;
  margin-bottom: 8px;
}

.value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.diff {
  font-size: 12px;
  margin-top: 4px;
}

.diff.up {
  color: #f56c6c; /* 上升：红色 */
}

.diff.down {
  color: #67c23a; /* 下降：绿色 */
}
</style>