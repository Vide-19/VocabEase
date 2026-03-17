import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/login/Welcome.vue'),
            children: [
                {path: '/', redirect: '/login' },
                {
                    path: '/login',
                    name: '登录',
                    component: () => import('@/views/login/Login.vue')
                }, {
                    path: '/register',
                    name: '注册',
                    component: () => import('@/views/login/Register.vue')
                }, {
                    //先不管👇功能待修改
                    path: '/reset',
                    name: '忘记密码',
                    component: () => import('@/views/login/Reset.vue')
                }
            ]
        }, {
            path: '/index',
            component: () => import('@/views/Index.vue'), // 注意：path 也是 '/'
            meta: {requiresAuth: true},
            redirect: '/index/dashboard', // 👈 关键：访问 /index 时自动重定向到 /index/dashboard
            children: [
                {
                    path: 'dashboard',
                    name: '仪表盘',
                    component: () => import('@/views/Dashboard.vue')
                }, {
                    path: 'content',
                    name: '内容管理',
                    component: () => import('@/views/content/Content.vue')
                }, {
                    path: 'user',
                    name: '用户管理',
                    component: () => import('@/views/User.vue')
                }, {
                    path: 'profile',
                    name: '个人中心',
                    component: () => import('@/views/Profile.vue')
                }, {
                    path: 'settings',
                    name: '系统管理',
                    component: () => import('@/views/settings/Settings.vue')
                }
            ]
        }
    ]
})


export default router