import { get, post, del } from '../utils/request'
import type { ApiResult, Dept } from '../types'

/** 获取所有部门列表 GET /depts */
export function getDeptList(): Promise<ApiResult<Dept[]>> {
  return get<Dept[]>('/depts')
}

/** 根据ID查询部门详情 GET /depts/{id} */
export function getDeptById(id: number): Promise<ApiResult<Dept>> {
  return get<Dept>(`/depts/${id}`)
}

/** 新增部门 POST /depts/add */
export function addDept(dept: Partial<Dept>): Promise<ApiResult<null>> {
  return post<null>('/depts/add', dept)
}

/** 更新部门 POST /depts/update */
export function updateDept(dept: Partial<Dept>): Promise<ApiResult<null>> {
  return post<null>('/depts/update', dept)
}

/** 删除部门 DELETE /depts?id= */
export function deleteDept(id: number): Promise<ApiResult<null>> {
  return del<null>('/depts', { id })
}
