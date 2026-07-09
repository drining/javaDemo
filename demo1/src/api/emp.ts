import { del, get, post } from '../utils/request'
import type { ApiResult, Emp, PageResult, EmpPageParams } from '../types'

/** 分页查询员工列表 GET /emps/pageList */
export function getEmpPageList(params: EmpPageParams): Promise<PageResult<Emp>> {
  return get<PageResult<Emp>>('/emps/pageList', params as any).then(res => res.data)
}

/** 查询员工详情（含工作经历） GET /emps/{id} */
export function getEmpById(id: number): Promise<ApiResult<Emp>> {
  return get<Emp>(`/emps/${id}`)
}

/** 新增员工 POST /emps/add */
export function addEmp(emp: Partial<Emp>): Promise<ApiResult<null>> {
  return post<null>('/emps/add', emp)
}

/** 修改员工 POST /emps/update */
export function updateEmp(emp: Partial<Emp>): Promise<ApiResult<null>> {
  return post<null>('/emps/update', emp)
}

/** 删除员工 DELETE /emps?id= */
export function deleteEmp(id: number): Promise<ApiResult<null>> {
  return del<null>('/emps', { id })
}
