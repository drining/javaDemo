<template>
  <div class="dept-view">
    <div class="page-header">
      <h2>部门管理</h2>
      <el-button type="primary" @click="openAddDialog">新增部门</el-button>
    </div>

    <!-- 部门表格 -->
    <el-table :data="deptList" v-loading="loading" stripe border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="部门名称" min-width="180" />
      <el-table-column prop="createTime" label="创建时间" min-width="180" />
      <el-table-column prop="updateTime" label="更新时间" min-width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openEditDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
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
        @current-change="loadDeptList"
        @size-change="onSizeChange"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑部门' : '新增部门'"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
        @keyup.enter="submitForm"
      >
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入部门名称" maxlength="50" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getDeptPageList, deleteDept, addDept, updateDept } from '../api/dept'
import type { Dept } from '../types'

/* ---------- 数据 ---------- */
const deptList = ref<Dept[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

/* ---------- 弹窗 ---------- */
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const editId = ref<number | null>(null)
const formRef = ref<FormInstance>()

interface DeptForm {
  name: string
}

const formData = reactive<DeptForm>({ name: '' })

const formRules: FormRules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ]
}

/* ---------- 方法 ---------- */

/** 加载部门列表（分页） */
async function loadDeptList() {
  loading.value = true
  try {
    const res = await getDeptPageList({ page: currentPage.value, pageSize: pageSize.value })
    deptList.value = res.rows
    total.value = res.total
  } finally {
    loading.value = false
  }
}

/** 每页条数改变时，回到第一页重新查询 */
function onSizeChange() {
  currentPage.value = 1
  loadDeptList()
}

/** 打开新增弹窗 */
function openAddDialog() {
  isEdit.value = false
  editId.value = null
  formData.name = ''
  dialogVisible.value = true
  // 清除校验
  formRef.value?.clearValidate()
}

/** 打开编辑弹窗 */
function openEditDialog(dept: Dept) {
  isEdit.value = true
  editId.value = dept.id
  formData.name = dept.name
  dialogVisible.value = true
  formRef.value?.clearValidate()
}

/** 提交表单（新增/编辑） */
async function submitForm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value && editId.value) {
      const res = await updateDept({ id: editId.value, name: formData.name.trim() })
      if (res.code === 1) {
        ElMessage.success('更新成功')
      }
    } else {
      const res = await addDept({ name: formData.name.trim() })
      if (res.code === 1) {
        ElMessage.success('新增成功')
      }
    }
    dialogVisible.value = false
    await loadDeptList()
  } finally {
    submitting.value = false
  }
}

/** 删除部门 */
async function handleDelete(id: number) {
  try {
    await ElMessageBox.confirm('确认删除该部门吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await deleteDept(id)
    if (res.code === 1) {
      ElMessage.success('删除成功')
      await loadDeptList()
    }
  } catch {
    // 用户取消不做处理
  }
}

onMounted(() => {
  loadDeptList()
})
</script>

<style scoped>
.dept-view {
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
