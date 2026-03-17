// src/stores/user.js
import {defineStore} from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        // 只存用于 UI 展示的信息，不存 token
        username: localStorage.getItem('username') || '',
        avatar: localStorage.getItem('avatar') || '',
        // 可以存一个标记，但不要作为鉴权唯一依据
        isLoggedIn: !!localStorage.getItem('username')
    }),
    getters: {
        // 只要有用户名，就认为已登录（因为 Cookie 是浏览器自动管的，JS 读不到）
        isLogin: (state) => state.isLoggedIn
    },
    actions: {
        setUserInfo(info) {
            this.username = info.username || info.userName // 兼容字段名
            this.avatar = info.avatar || ''
            this.isLoggedIn = true

            // 持久化到 localStorage，仅为了刷新后显示名字，不用于鉴权
            localStorage.setItem('username', this.username)
            if (this.avatar) localStorage.setItem('avatar', this.avatar)
            localStorage.setItem('isLoggedIn', 'true')
        },

        // 退出登录动作
        clear() {
            this.username = ''
            this.avatar = ''
            this.isLoggedIn = false
            localStorage.removeItem('username')
            localStorage.removeItem('avatar')
            localStorage.removeItem('isLoggedIn')
            // 注意：JS 无法删除 HttpOnly 的 Cookie，必须调用后端 /logout 接口
        }
    }
})