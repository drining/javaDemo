/** 通用响应结果，对应后端 Result.java */
export interface ApiResult<T = any> {
  code: number
  msg: string
  data: T
}

/** 分页结果，对应后端 PageResult.java */
export interface PageResult<T = any> {
  total: number
  rows: T[]
}

/** 部门，对应后端 Dept.java */
export interface Dept {
  id: number
  name: string
  createTime: string
  updateTime: string
}

/** 员工，对应后端 Emp.java */
export interface Emp {
  id: number
  username: string
  password: string
  name: string
  gender: number       // 1-男, 2-女
  phone: string
  job: number
  salary: number
  image: string
  entryDate: string
  createTime: string
  updateTime: string
  deptId: number
  exprList: EmpExpr[]
}

/** 员工工作经历 */
export interface EmpExpr {
  id: number
  empId: number
  begin: string
  end: string
  company: string
  job: string
}

/** 员工分页查询参数，对应后端 EmpPageListParams.java */
export interface EmpPageParams {
  page: number
  pageSize: number
  name?: string
  gender?: number
  /** 入职开始日期（含），格式 yyyy-MM-dd */
  beginDate?: string
  /** 入职结束日期（含），格式 yyyy-MM-dd */
  endDate?: string
}
