<template>
  <div class="emp-view">
    <div class="page-header">
      <h2>员工管理</h2>
      <el-button type="primary" @click="openAddDialog">新增员工</el-button>
    </div>

    <!-- 员工表格 -->
    <el-table :data="empList" v-loading="loading" stripe border style="width: 100%">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" min-width="100" />
      <el-table-column prop="name" label="姓名" min-width="100" />
      <el-table-column prop="gender" label="性别" width="70">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="job" label="职位" width="100">
        <template #default="{ row }">{{ jobMap[row.job as keyof typeof jobMap] || '-' }}</template>
      </el-table-column>
      <el-table-column prop="salary" label="薪资" width="90" />
      <el-table-column prop="entryDate" label="入职日期" width="120" />
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

    <!-- 新增员工弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="新增员工"
      width="640px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        @keyup.enter="submitForm"
      >
        <!-- 基础信息 -->
        <el-divider content-position="left">基础信息</el-divider>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="formData.username" placeholder="登录账号" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="formData.password" type="password" placeholder="至少6位" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="formData.name" placeholder="真实姓名" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="formData.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" placeholder="11位手机号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期" prop="entryDate">
              <el-date-picker
                v-model="formData.entryDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 工作信息 -->
        <el-divider content-position="left">工作信息</el-divider>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="职位" prop="job">
              <el-select v-model="formData.job" placeholder="请选择职位" style="width: 100%">
                <el-option v-for="[k, v] in Object.entries(jobMap)" :key="k" :label="v" :value="Number(k)" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资" prop="salary">
              <el-input-number
                v-model="formData.salary"
                :min="0"
                :step="1000"
                placeholder="月薪"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="部门ID" prop="deptId">
              <el-input-number
                v-model="formData.deptId"
                :min="1"
                placeholder="所属部门编号"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="头像" prop="image">
              <div class="avatar-upload">
                <el-upload
                  :show-file-list="false"
                  :action="uploadUrl"
                  name="file"
                  :on-success="handleUploadSuccess"
                  :on-error="handleUploadError"
                  :before-upload="beforeUpload"
                  accept="image/jpeg,image/png,image/webp"
                >
                  <img v-if="formData.image" :src="formData.image" class="avatar-preview" />
                  <div v-else class="avatar-placeholder">
                    <el-icon><Plus /></el-icon>
                  </div>
                </el-upload>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 工作经历 -->
        <el-divider content-position="left">
          工作经历
          <el-button size="small" type="primary" link @click="addExprRow" style="margin-left: 8px">
            + 添加经历
          </el-button>
        </el-divider>
        <div v-for="(expr, index) in formData.exprList" :key="index" class="expr-row">
          <el-row :gutter="16" align="middle">
            <el-col :span="18">
              <el-row :gutter="8">
                <el-col :span="8">
                  <el-date-picker
                    v-model="expr.beginDate"
                    type="date"
                    placeholder="开始日期"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-col>
                <el-col :span="8">
                  <el-date-picker
                    v-model="expr.endDate"
                    type="date"
                    placeholder="结束日期"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-col>
                <el-col :span="8">
                  <el-input v-model="expr.company" placeholder="公司名称" />
                </el-col>
              </el-row>
              <el-row style="margin-top: 8px">
                <el-input v-model="expr.job" placeholder="职位" />
              </el-row>
            </el-col>
            <el-col :span="6" style="text-align: center">
              <el-button type="danger" link @click="removeExprRow(index)">删除</el-button>
            </el-col>
          </el-row>
          <el-divider v-if="index < formData.exprList.length - 1" style="margin: 12px 0" />
        </div>
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
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getEmpPageList, addEmp } from '../api/emp'
import type { Emp, EmpExpr } from '../types'

/* ---------- 上传 ---------- */
const uploadUrl = '/api/upload'

function handleUploadSuccess(response: any) {
  if (response.code === 1) {
    formData.image = response.data
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

function handleUploadError() {
  ElMessage.error('上传失败，请检查网络')
}

function beforeUpload(file: File) {
  const isImage = ['image/jpeg', 'image/png', 'image/webp'].includes(file.type)
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) {
    ElMessage.error('只能上传 JPG/PNG/WebP 格式的图片')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB')
    return false
  }
  return true
}

/* ---------- 数据 ---------- */
const empList = ref<Emp[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

/** 职位映射 */
const jobMap = {
  1: '班主任',
  2: '讲师',
  3: '学工主管',
  4: '教研主管',
  5: '咨询师'
} as const

/* ---------- 弹窗 ---------- */
const dialogVisible = ref(false)
const submitting = ref(false)
const formRef = ref<FormInstance>()

interface EmpForm {
  username: string
  password: string
  name: string
  gender: number | null
  phone: string
  entryDate: string
  job: number | null
  salary: number | null
  deptId: number | null
  image: string
  exprList: Omit<EmpExpr, 'id' | 'empId'>[]
}

const formData = reactive<EmpForm>({
  username: '',
  password: '',
  name: '',
  gender: null,
  phone: '',
  entryDate: '',
  job: null,
  salary: null,
  deptId: null,
  image: '',
  exprList: []
})

const formRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2-30 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 50, message: '至少 6 位', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1\d{10}$/, message: '请输入正确的11位手机号', trigger: 'blur' }
  ]
}

/* ---------- 方法 ---------- */

/** 加载员工列表 */
async function loadEmpList() {
  loading.value = true
  try {
    const res = await getEmpPageList({ page: currentPage.value, pageSize: pageSize.value })
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

/** 打开新增弹窗 */
function openAddDialog() {
  // 重置表单
  formData.username = ''
  formData.password = ''
  formData.name = ''
  formData.gender = null
  formData.phone = ''
  formData.entryDate = ''
  formData.job = null
  formData.salary = null
  formData.deptId = null
  formData.image = ''
  formData.exprList = []
  dialogVisible.value = true
  formRef.value?.clearValidate()
}

/** 新增工作经历行 */
function addExprRow() {
  formData.exprList.push({ beginDate: '', endDate: '', company: '', job: '' })
}

/** 删除工作经历行 */
function removeExprRow(index: number) {
  formData.exprList.splice(index, 1)
}

/** 提交表单 */
async function submitForm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const payload: Partial<Emp> = {
      username: formData.username,
      password: formData.password,
      name: formData.name,
      gender: formData.gender ?? undefined,
      phone: formData.phone,
      entryDate: formData.entryDate,
      job: formData.job ?? undefined,
      salary: formData.salary ?? undefined,
      deptId: formData.deptId ?? undefined,
      image: formData.image || undefined,
      exprList: formData.exprList.filter(e => e.company || e.job) as EmpExpr[]
    }
    const res = await addEmp(payload)
    if (res.code === 1) {
      ElMessage.success('新增成功')
      dialogVisible.value = false
      await loadEmpList()
    }
  } finally {
    submitting.value = false
  }
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

.expr-row {
  padding: 0 0 0 12px;
}

.avatar-upload .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  transition: border-color 0.3s;
  display: inline-block;
}
.avatar-upload .el-upload:hover {
  border-color: #409eff;
}
.avatar-preview {
  width: 100px;
  height: 100px;
  object-fit: cover;
  display: block;
}
.avatar-placeholder {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8c939d;
  font-size: 28px;
}
</style>
