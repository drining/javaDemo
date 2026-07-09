import { upload } from '../utils/request'
import type { ApiResult } from '../types'

/** 上传文件 /upload */
export function uploadImage(file: File): Promise<ApiResult<string>> {
  return upload<string>('/upload', file)
}
