import axios from 'axios'
import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import type { ApiResult } from '../types'

// 创建 axios 实例
const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 可以在请求头中添加 token 等认证信息
    // const token = localStorage.getItem('token')
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`
    // }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse<ApiResult>) => {
    const res = response.data
    // 后端约定 code 为 1 表示成功，0 表示失败
    if (res.code !== 1) {
      console.error('请求失败:', res.msg)
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return response
  },
  (error) => {
    console.error('网络错误:', error.message)
    return Promise.reject(error)
  }
)

/**
 * GET 请求
 * @param url 请求地址
 * @param params 查询参数
 * @returns 响应数据中的 data 字段
 */
export function get<T>(url: string, params?: Record<string, any>): Promise<ApiResult<T>> {
  return request.get(url, { params }).then((res) => res.data)
}

/**
 * POST 请求（JSON）
 * @param url 请求地址
 * @param data 请求体
 * @returns 响应数据中的 data 字段
 */
export function post<T>(url: string, data?: any): Promise<ApiResult<T>> {
  return request.post(url, data).then((res) => res.data)
}

/**
 * DELETE 请求
 * @param url 请求地址
 * @param params 查询参数
 * @returns 响应数据中的 data 字段
 */
export function del<T>(url: string, params?: Record<string, any>): Promise<ApiResult<T>> {
  return request.delete(url, { params }).then((res) => res.data)
}

/**
 * 上传文件（multipart/form-data）
 * @param url 请求地址
 * @param file 文件对象
 * @returns 响应数据中的 data 字段
 */
export function upload<T>(url: string, file: File): Promise<ApiResult<T>> {
  const formData = new FormData()
  formData.append('file', file)
  return request.post(url, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }).then((res) => res.data)
}

export default request
