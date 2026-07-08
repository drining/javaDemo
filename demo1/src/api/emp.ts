import { get } from '../utils/request'
import type { ApiResult, Emp, PageResult, EmpPageParams } from '../types'

/** 分页查询员工列表 GET /emps/pageList */
export function getEmpPageList(params: EmpPageParams): Promise<PageResult<Emp>> {
  return get<PageResult<Emp>>('/emps/pageList', params as any).then(res => res.data)
}
