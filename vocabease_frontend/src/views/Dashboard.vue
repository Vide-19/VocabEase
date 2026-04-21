<template>
  <div class="dashboard-container">
    <h2 class="page-title">系统数据</h2>

    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6" v-for="item in statData" :key="item.statisticName">
        <el-card shadow="hover" class="stat-card">
          <div class="card-content">
            <div class="title">{{ item.statisticName }}</div>
            <div class="value">{{ item.totalCount }}</div>
            <div class="sub-info">
              今日 {{ item.todayCount }}
              <span class="diff"
                    :class="{ up: item.todayCount > item.yesterdayCount, down: item.todayCount < item.yesterdayCount }">
                {{ getDiffText(item) }}
              </span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 24px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>近7天用户趋势</span></template>
          <div ref="userChartRef" style="width:100%;height:300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>近7天内容统计</span></template>
          <div ref="contentChartRef" style="width:100%;height:300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'

interface StatisticDataDto {
  statisticName: string
  totalCount: number
  todayCount: number
  yesterdayCount: number
}

interface StatisticWeekItem {
  statisticName: string
  dataList: number[]
}

interface StatisticWeekDataDto {
  dataList: string[]
  itemDataList: StatisticWeekItem[]
}

const statData = ref<StatisticDataDto[]>([])
const userWeekData = ref<StatisticWeekDataDto | null>(null)
const contentWeekData = ref<StatisticWeekDataDto | null>(null)
const userChartRef = ref<HTMLDivElement | null>(null)
const contentChartRef = ref<HTMLDivElement | null>(null)

const getDiffText = (item: StatisticDataDto) => {
  const diff = item.todayCount - item.yesterdayCount
  if (diff === 0) return ' 持平'
  const sign = diff > 0 ? '+' : ''
  return ` ${sign}${diff}(较昨日)`
}

const fetchAllData = async () => {
  try {
    const res = await axios.get('/index/getAllData')
    statData.value = res.data.data || []
  } catch (err) {
    console.error('获取统计数据失败', err)
  }
}

const fetchUserWeekData = async () => {
  try {
    const res = await axios.get('/index/getWeekAllData')
    userWeekData.value = res.data.data
    renderUserChart()
  } catch (err) {
    console.error('获取用户周数据失败', err)
  }
}

const fetchContentWeekData = async () => {
  try {
    const res = await axios.get('/index/getWeekContentData')
    contentWeekData.value = res.data.data
    renderContentChart()
  } catch (err) {
    console.error('获取内容周数据失败', err)
  }
}

const renderChart = (el: any, data: any) => {
  if (!el || !data) return
  const chart = echarts.init(el)
  const option = {
    tooltip: {trigger: 'axis'},
    legend: {data: data.itemDataList.map((i: any) => i.statisticName)},
    xAxis: {type: 'category', data: data.dataList},
    yAxis: {type: 'value'},
    series: data.itemDataList.map((i: any) => ({
      name: i.statisticName,
      type: 'line',
      smooth: true,
      data: i.dataList
    }))
  }
  chart.setOption(option)
  window.addEventListener('resize', () => chart.resize())
}

const renderUserChart = () => renderChart(userChartRef.value, userWeekData.value)
const renderContentChart = () => renderChart(contentChartRef.value, contentWeekData.value)

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
  height: 130px;
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.title {
  font-size: 14px;
  color: #999;
  margin-bottom: 6px;
}

.value {
  font-size: 26px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.sub-info {
  font-size: 12px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 6px;
}

.diff {
  font-size: 12px;
}

.diff.up {
  color: #67c23a;
}

.diff.down {
  color: #f56c6c;
}
</style>