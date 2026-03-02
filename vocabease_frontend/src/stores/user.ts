import {defineStore} from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        username: '',
        avatar: '',
        token: localStorage.getItem('token') || ''
    }),
    getters: {
        isLogin: (state) => !!state.token
    },
    actions: {
        setUserInfo(info: { username: string; avatar?: string }) {
            this.username = info.username
            this.avatar = info.avatar || ''
        },
        clear() {
            this.username = ''
            this.avatar = ''
            this.token = ''
            localStorage.removeItem('token')
        }
    }
})