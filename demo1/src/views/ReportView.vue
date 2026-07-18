<template>
  <div class="report-view">
    <div class="page-header">
      <h2>数据统计</h2>
    </div>

    <div class="chart-row">
      <el-card class="chart-card">
        <template #header>
          <span class="card-title">👤 性别分布</span>
        </template>
        <div ref="genderChartRef" class="chart-container"></div>
      </el-card>

      <el-card class="chart-card">
        <template #header>
          <span class="card-title">💼 职位分布</span>
        </template>
        <div ref="jobChartRef" class="chart-container"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { getGenderReport, getJobReport } from '../api/report'
import type { ReportItem } from '../api/report'

/* ---------- 图表容器 ---------- */
const genderChartRef = ref<HTMLDivElement>()
const jobChartRef = ref<HTMLDivElement>()
let genderChart: echarts.ECharts | null = null
let jobChart: echarts.ECharts | null = null

/* ---------- 颜色 ---------- */
const colors = ['#4361ee', '#f72585', '#4cc9f0', '#f8961e', '#43aa8b']

/** 初始化单个图表 */
function initChart(el: HTMLDivElement): echarts.ECharts {
  const chart = echarts.init(el)
  return chart
}

/** 渲染性别饼图 */
function renderGenderChart(data: ReportItem[]) {
  if (!genderChartRef.value) return
  if (!genderChart) genderChart = initChart(genderChartRef.value)

  genderChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}人 ({d}%)'
    },
    legend: {
      bottom: '0',
      textStyle: { fontSize: 14 }
    },
    series: [
      {
        type: 'pie',
        radius: ['30%', '65%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}\n{c}人',
          fontSize: 14
        },
        emphasis: {
          label: { show: true, fontSize: 18, fontWeight: 'bold' }
        },
        data: data.map((item, idx) => ({
          ...item,
          itemStyle: { color: colors[idx % colors.length] }
        }))
      }
    ]
  })
}

/** 渲染职位柱状图 */
function renderJobChart(data: ReportItem[]) {
  if (!jobChartRef.value) return
  if (!jobChart) jobChart = initChart(jobChartRef.value)

  jobChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      formatter: '{b}<br/>人数: {c}人'
    },
    grid: {
      left: '12%',
      right: '8%',
      bottom: '15%',
      top: '10%'
    },
    xAxis: {
      type: 'category',
      data: data.map((item) => item.name),
      axisLabel: { fontSize: 14 }
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      name: '人数',
      nameTextStyle: { fontSize: 13 }
    },
    series: [
      {
        type: 'bar',
        data: data.map((item, idx) => ({
          value: item.value,
          itemStyle: {
            color: colors[idx % colors.length],
            borderRadius: [6, 6, 0, 0]
          }
        })),
        barWidth: '50%',
        label: {
          show: true,
          position: 'top',
          formatter: '{c}人',
          fontSize: 14
        }
      }
    ]
  })
}

/** 窗口尺寸变化时自适应 */
function onResize() {
  genderChart?.resize()
  jobChart?.resize()
}

onMounted(async () => {
  try {
    const [genderRes, jobRes] = await Promise.all([
      getGenderReport(),
      getJobReport()
    ])
    if (genderRes.code === 1) renderGenderChart(genderRes.data)
    if (jobRes.code === 1) renderJobChart(jobRes.data)
  } catch (e) {
    console.error('加载统计数据失败:', e)
  }

  window.addEventListener('resize', onResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
  genderChart?.dispose()
  jobChart?.dispose()
})
</script>

<style scoped>
.report-view {
  padding: 24px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  font-size: 22px;
  color: #1a1a2e;
}

.chart-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.chart-card {
  border-radius: 12px;
}

.chart-card :deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #eee;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a2e;
}

.chart-container {
  width: 100%;
  height: 380px;
}
</style>
