// src/net/login.js
import axios from 'axios'
import {ElMessage} from 'element-plus'

// 默认失败回调
const defaultFailure = (message, code, url) => {
    console.warn(`请求地址: ${url}, 状态码: ${code}, 错误信息: ${message}`)
    ElMessage.warning(message || '操作失败')
}

// 默认错误回调
const defaultError = (err) => {
    console.error('网络请求异常:', err)
    ElMessage.error('网络错误，请检查连接')
}

/**
 * 支持 URL 查询参数的 POST 请求（用于 /api/login?phone=xxx&password=yyy）
 */
function postWithParams(url, params, success, failure = defaultFailure) {
    const searchParams = new URLSearchParams()
    for (const key in params) {
        if (params[key] != null) {
            searchParams.append(key, params[key])
        }
    }
    const fullUrl = `${url}?${searchParams.toString()}`

    axios
        .post(fullUrl, null, {
            withCredentials: true // ← 关键：允许跨域携带 Cookie
        })
        .then(({ data: resData }) => {
            if (resData.code === 200) {
                success(resData.data)
            } else {
                failure(resData.message, resData.code, url)
            }
        })
        .catch((err) => defaultError(err))
}

/**
 * 普通 POST（JSON Body）
 */
function post(url, data, success, failure = defaultFailure) {
    axios
        .post(url, data, {
            withCredentials: true // ← 关键
        })
        .then(({ data: resData }) => {
            if (resData.code === 200) {
                success(resData.data)
            } else {
                failure(resData.message, resData.code, url)
            }
        })
        .catch((err) => defaultError(err))
}

/**
 * GET 请求
 */
function get(url, success, failure = defaultFailure) {
    axios
        .get(url, {
            withCredentials: true // ← 关键
        })
        .then(({ data: resData }) => {
            if (resData.code === 200) {
                success(resData.data)
            } else {
                failure(resData.message, resData.code, url)
            }
        })
        .catch((err) => defaultError(err))
}

function logout(success, failure = defaultFailure) {
    get('/api/logout', () =>{
        ElMessage.success('退出成功')
        success()
    }, failure)
}

// 导出方法
export { get, post, postWithParams, logout }