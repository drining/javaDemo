<template>
  <div class="emp-view">
    <div class="page-header">
      <h2>员工管理</h2>
      <el-button type="primary" @click="openAddDialog">新增员工</el-button>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable @clear="search" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="searchForm.gender" placeholder="全部" clearable style="width:100px">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker
            v-model="searchForm.beginDate"
            type="date"
            placeholder="开始日期"
            value-format="YYYY-MM-DD"
            style="width:150px"
          />
          <span class="date-separator">~</span>
          <el-date-picker
            v-model="searchForm.endDate"
            type="date"
            placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width:150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 员工表格 -->
    <el-card shadow="never">
      <el-table :data="empList" v-loading="loading" stripe border style="width: 100%">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="username" label="用户名" min-width="110" />
        <el-table-column prop="name" label="姓名" min-width="100" />
        <el-table-column prop="gender" label="性别" width="70">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="job" label="职位" width="100">
          <template #default="{ row }">{{ jobMap[row.job] || '-' }}</template>
        </el-table-column>
        <el-table-column prop="salary" label="薪资" width="100" />
        <el-table-column prop="entryDate" label="入职日期" width="120" />
        <el-table-column prop="deptId" label="部门ID" width="80" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openEditDialog(row.id)">编辑</el-button>
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
          @current-change="loadEmpList"
          @size-change="onSizeChange"
        />
      </div>
    </el-card>

    <!-- ============ 新增/编辑员工弹窗 ============ -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑员工' : '新增员工'"
      width="700px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="90px"
        label-position="top"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="formData.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="formData.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="formData.password"
                type="password"
                :placeholder="isEdit ? '不填则不修改' : '默认123456'"
              />
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

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入手机号" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门" prop="deptId">
              <el-select v-model="formData.deptId" placeholder="请选择部门" style="width:100%">
                <el-option
                  v-for="d in deptList"
                  :key="d.id"
                  :label="d.name"
                  :value="d.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位" prop="job">
              <el-select v-model="formData.job" placeholder="请选择职位" style="width:100%">
                <el-option label="班主任" :value="1" />
                <el-option label="讲师" :value="2" />
                <el-option label="学工主管" :value="3" />
                <el-option label="教研主管" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资" prop="salary">
              <el-input-number v-model="formData.salary" :min="0" :step="1000" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入职日期" prop="entryDate">
              <el-date-picker
                v-model="formData.entryDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width:100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="头像" prop="image">
              <div class="upload-wrapper">
                <el-upload
                  ref="uploadRef"
                  :show-file-list="false"
                  :before-upload="handleBeforeUpload"
                  :http-request="handleUpload"
                  accept="image/*"
                >
                  <template #trigger>
                    <el-button type="primary" :loading="uploading">选择图片</el-button>
                  </template>
                </el-upload>
                <div v-if="formData.image" class="image-preview">
                  <el-image
                    :src="formData.image"
                    fit="cover"
                    style="width:60px;height:60px;border-radius:4px"
                    :preview-src-list="[formData.image]"
                    preview-teleported
                  />
                  <el-button
                    size="small"
                    type="danger"
                    circle
                    :icon="Delete"
                    class="img-remove-btn"
                    @click="formData.image = ''"
                  />
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 工作经历子表单 -->
        <el-divider content-position="left">工作经历</el-divider>
        <div
          v-for="(expr, index) in formData.exprList"
          :key="index"
          class="expr-item"
        >
          <el-row :gutter="16">
            <el-col :span="6">
              <el-form-item
                :label="'开始'"
                :prop="'exprList.' + index + '.beginDate'"
                :rules="[{ required: true, message: '请选择开始日期', trigger: 'change' }]"
              >
                <el-date-picker
                  v-model="expr.beginDate"
                  type="date"
                  placeholder="开始日期"
                  value-format="YYYY-MM-DD"
                  style="width:100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                :label="'结束'"
                :prop="'exprList.' + index + '.endDate'"
                :rules="[{ required: true, message: '请选择结束日期', trigger: 'change' }]"
              >
                <el-date-picker
                  v-model="expr.endDate"
                  type="date"
                  placeholder="结束日期"
                  value-format="YYYY-MM-DD"
                  style="width:100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                :label="'公司'"
                :prop="'exprList.' + index + '.company'"
                :rules="[{ required: true, message: '请输入公司名', trigger: 'blur' }]"
              >
                <el-input v-model="expr.company" placeholder="公司名称" />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                :label="'职位'"
                :prop="'exprList.' + index + '.job'"
                :rules="[{ required: true, message: '请输入职位', trigger: 'blur' }]"
              >
                <el-input v-model="expr.job" placeholder="职位" />
              </el-form-item>
            </el-col>
            <el-col :span="1" class="expr-remove">
              <el-button type="danger" :icon="Delete" circle size="small" @click="removeExpr(index)" />
            </el-col>
          </el-row>
        </div>
        <el-button type="warning" plain @click="addExpr" style="margin-bottom:16px">
          + 添加工作经历
        </el-button>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitForm">
          {{ isEdit ? '保存修改' : '确定' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { getEmpPageList, addEmp, getEmpById, updateEmp, deleteEmp } from '../api/emp'
import { getDeptList } from '../api/dept'
import { uploadImage } from '../api/upload'
import type { Emp, EmpExpr, Dept } from '../types'

/* ========== 数据 ========== */
const empList = ref<Emp[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const deptList = ref<Dept[]>([])

const jobMap: Record<number, string> = {
  1: '班主任',
  2: '讲师',
  3: '学工主管',
  4: '教研主管'
}

/* ========== 搜索 ========== */
const searchForm = reactive({
  name: '',
  gender: undefined as number | undefined,
  beginDate: '',
  endDate: ''
})

function search() {
  currentPage.value = 1
  loadEmpList()
}

function resetSearch() {
  searchForm.name = ''
  searchForm.gender = undefined
  searchForm.beginDate = ''
  searchForm.endDate = ''
  currentPage.value = 1
  loadEmpList()
}

/* ========== 弹窗 ========== */
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref<number | null>(null)
const submitting = ref(false)
const formRef = ref<FormInstance>()
const uploadRef = ref()
const uploading = ref(false)

interface EmpForm {
  username: string
  password: string
  name: string
  gender: number
  phone: string
  job: number | undefined
  salary: number
  image: string
  entryDate: string
  deptId: number | undefined
  exprList: EmpExpr[]
}

const formData = reactive<EmpForm>({
  username: '',
  password: '123456',
  name: '',
  gender: 1,
  phone: '',
  job: undefined,
  salary: 0,
  image: '',
  entryDate: '',
  deptId: undefined,
  exprList: []
})

const formRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  job: [{ required: true, message: '请选择职位', trigger: 'change' }],
  entryDate: [{ required: true, message: '请选择入职日期', trigger: 'change' }]
}

/* ========== 方法 ========== */

async function loadEmpList() {
  loading.value = true
  try {
    const params: any = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    if (searchForm.name) params.name = searchForm.name
    if (searchForm.gender !== undefined) params.gender = searchForm.gender
    if (searchForm.beginDate) params.beginDate = searchForm.beginDate
    if (searchForm.endDate) params.endDate = searchForm.endDate

    const res = await getEmpPageList(params)
    empList.value = res.rows
    total.value = res.total
  } catch (err: any) {
    ElMessage.error(err.message || '获取员工列表失败')
  } finally {
    loading.value = false
  }
}

function onSizeChange() {
  currentPage.value = 1
  loadEmpList()
}

/** 图片上传前校验：格式 + 大小 */
function handleBeforeUpload(file: File): boolean {
  const allowed = ['image/jpeg', 'image/png', 'image/gif', 'image/webp']
  if (!allowed.includes(file.type)) {
    ElMessage.error('只支持 JPG/PNG/GIF/WebP 格式')
    return false
  }
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片不能超过 5MB')
    return false
  }
  return true
}

/** 自定义上传：调用后端 /upload 接口 */
async function handleUpload(options: any) {
  uploading.value = true
  try {
    const res = await uploadImage(options.file)
    if (res.code === 1) {
      formData.image = res.data
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(res.msg || '上传失败')
    }
  } catch (err: any) {
    ElMessage.error(err.message || '上传失败')
  } finally {
    uploading.value = false
  }
}

/** 重置表单数据（新增用） */
function resetForm() {
  formData.username = ''
  formData.password = '123456'
  formData.name = ''
  formData.gender = 1
  formData.phone = ''
  formData.job = undefined
  formData.salary = 0
  formData.image = ''
  formData.entryDate = ''
  formData.deptId = undefined
  formData.exprList = []
}

/** 填充表单数据（编辑用） */
function fillForm(emp: Emp) {
  formData.username = emp.username
  formData.password = emp.password || ''
  formData.name = emp.name
  formData.gender = emp.gender
  formData.phone = emp.phone
  formData.job = emp.job
  formData.salary = emp.salary
  formData.image = emp.image || ''
  formData.entryDate = emp.entryDate
  formData.deptId = emp.deptId
  formData.exprList = (emp.exprList || []).map(e => ({ ...e }))
}

function openAddDialog() {
  isEdit.value = false
  editId.value = null
  resetForm()
  dialogVisible.value = true
  formRef.value?.clearValidate()
}

async function openEditDialog(id: number) {
  isEdit.value = true
  editId.value = id
  try {
    const res = await getEmpById(id)
    if (res.code === 1 && res.data) {
      fillForm(res.data)
      dialogVisible.value = true
      formRef.value?.clearValidate()
    } else {
      ElMessage.error(res.msg || '获取员工信息失败')
    }
  } catch (err: any) {
    ElMessage.error(err.message || '获取员工信息失败')
  }
}

function addExpr() {
  formData.exprList.push({
    id: 0,
    empId: 0,
    beginDate: '',
    endDate: '',
    company: '',
    job: ''
  })
}

function removeExpr(index: number) {
  formData.exprList.splice(index, 1)
}

async function submitForm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const payload: Partial<Emp> = {
      username: formData.username,
      name: formData.name,
      gender: formData.gender,
      phone: formData.phone,
      job: formData.job,
      salary: formData.salary,
      image: formData.image || null,
      entryDate: formData.entryDate,
      deptId: formData.deptId,
      exprList: formData.exprList
    }

    if (isEdit.value && editId.value) {
      // 编辑模式
      payload.id = editId.value
      if (formData.password) {
        payload.password = formData.password
      }
      await updateEmp(payload)
      ElMessage.success('修改成功')
    } else {
      // 新增模式
      payload.password = formData.password || '123456'
      await addEmp(payload)
      ElMessage.success('新增成功')
    }

    dialogVisible.value = false
    await loadEmpList()
  } catch (err: any) {
    ElMessage.error(err.message || '操作失败')
  } finally {
    submitting.value = false
  }
}

/** 删除员工 */
async function handleDelete(id: number) {
  try {
    await ElMessageBox.confirm('确认删除该员工吗？删除后不可恢复。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await deleteEmp(id)
    if (res.code === 1) {
      ElMessage.success('删除成功')
      await loadEmpList()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch {
    // 用户取消不处理
  }
}

onMounted(async () => {
  await loadEmpList()
  try {
    const res = await getDeptList()
    deptList.value = res.data
  } catch {
    // 部门列表加载失败不影响主流程
  }
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

.search-card {
  margin-bottom: 16px;
}

.date-separator {
  display: inline-block;
  margin: 0 8px;
  color: #999;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.expr-item {
  background: #f9fafb;
  border-radius: 6px;
  padding: 12px 12px 0;
  margin-bottom: 8px;
  position: relative;
}

.expr-remove {
  display: flex;
  align-items: flex-start;
  padding-top: 22px;
}
</style>
