import type { ApiResponse } from './types'

export async function apiGet<T>(path: string): Promise<T> {
  const response = await fetch(path, {
    headers: {
      Accept: 'application/json',
    },
  })

  const payload = (await response.json()) as ApiResponse<T>
  if (!response.ok || payload.status >= 400) {
    throw new Error(payload.message || `Yêu cầu thất bại (${response.status})`)
  }
  return payload.data
}
