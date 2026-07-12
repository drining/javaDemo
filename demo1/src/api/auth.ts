import { post } from '../utils/request'
import type { ApiResult } from '../types'

/** 登录接口返回的数据 */
export interface LoginResult {
  token: string
  username: string
  name: string
}

/** 登录 POST /login */
export function loginApi(username: string, password: string): Promise<ApiResult<LoginResult>> {
  return post<LoginResult>('/login', { username, password })
}
