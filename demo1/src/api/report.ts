import { get } from '../utils/request'
import type { ApiResult } from '../types'

/** 统计单条数据 */
export interface ReportItem {
  name: string
  value: number
}

/** 获取性别分布统计 GET /report/gender */
export function getGenderReport(): Promise<ApiResult<ReportItem[]>> {
  return get<ReportItem[]>('/report/gender')
}

/** 获取职位分布统计 GET /report/job */
export function getJobReport(): Promise<ApiResult<ReportItem[]>> {
  return get<ReportItem[]>('/report/job')
}
