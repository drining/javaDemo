<template>
  <div class="emp-view">
    <div class="page-header">
      <h2>员工管理</h2>
    </div>

    <!-- 员工表格 -->
    <el-table :data="empList" v-loading="loading" stripe border style="width: 100%">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="name" label="姓名" min-width="120" />
      <el-table-column prop="gender" label="性别" width="70">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="job" label="职位" width="100">
        <template #default="{ row }">{{ jobMap[row.job] || '-' }}</template>
      </el-table-column>
      <el-table-column prop="salary" label="薪资" width="100" />
      <el-table-column prop="entryDate" label="入职日期" width="130" />
      <el-table-column prop="deptId" label="部门ID" width="80" />
    </el-table>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="total"
        :disabled="loading"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="loadEmpList"
        @size-change="onSizeChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getEmpPageList } from '../api/emp'
import type { Emp } from '../types'

/* ---------- 数据 ---------- */
const empList = ref<Emp[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

/** 职位映射 */
const jobMap: Record<number, string> = {
  1: '班主任',
  2: '讲师',
  3: '学工主管',
  4: '教研主管',
  5: '咨询师'
}

/* ---------- 方法 ---------- */

/** 加载员工列表 */
async function loadEmpList() {
  loading.value = true
  try {
    const res = await getEmpPageList({
      page: currentPage.value,
      pageSize: pageSize.value
    })
    empList.value = res.rows
    total.value = res.total
  } catch (err) {
    console.error('获取员工列表失败:', err)
  } finally {
    loading.value = false
  }
}

/** 每页条数改变时，回到第一页重新查询 */
function onSizeChange() {
  currentPage.value = 1
  loadEmpList()
}

onMounted(() => {
  loadEmpList()
})
</script>

<style scoped>
.emp-view {
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

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
