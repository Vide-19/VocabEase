<script setup lang="ts">
import {computed, ref} from 'vue'
import {ElAvatar, ElButton, ElIcon, ElMenu, ElMenuItem, ElMessageBox, ElScrollbar} from 'element-plus'
import {
  Connection,
  Expand,
  Fold,
  Management,
  OfficeBuilding,
  Setting,
  SwitchButton,
  User
} from '@element-plus/icons-vue'
import {useUserStore} from '@/stores/user'
import {useRoute, useRouter} from 'vue-router' // 👈 同时建议用 useRoute 而不是 $route

const userStore = useUserStore()
const router = useRouter()
const route = useRoute() // ✅ 推荐方式


// 菜单是否折叠
const isCollapse = ref(false)
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 菜单数据（可根据权限动态生成）
const menuItems = [
  {path: 'dashboard', icon: Connection, title: '系统数据'},
  {path: 'content', icon: Management, title: '内容管理'},
  {path: 'user', icon: OfficeBuilding, title: '用户管理'},
  {path: 'profile', icon: User, title: '个人中心'},
  {path: 'settings', icon: Setting, title: '系统管理'}
]

// 计算当前激活的子路径
const activeMenu = computed(() => {
  return route.path.replace('/index/', '')
})

// 退出登录
function handleLogout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 清除 token 和用户信息
    userStore.clear()
    // 跳转到登录页
    router.push('/login')
  })
}

</script>

<template>
  <div class="layout-container">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="logo" @click="router.push('/')" v-if="!isCollapse">
        <span>VocabEase 后台管理系统</span>
      </div>
      <div class="toggle-btn" @click="toggleCollapse">
        <el-icon :size="20">
          <component :is="isCollapse ? Expand : Fold"/>
        </el-icon>
      </div>
      <div class="user-info">
        <el-avatar :size="32" :src="userStore.avatar || ''">
          {{ userStore.username?.charAt(0).toUpperCase() }}
        </el-avatar>
        <span class="username">{{ userStore.username }}</span>
        <el-button text @click="handleLogout" size="default">
          <el-icon>
            <SwitchButton/>
          </el-icon>
        </el-button>
      </div>
    </header>

    <!-- 主体区域 -->
    <div class="main-container">
      <!-- 侧边菜单 -->
      <aside class="sidebar" :class="{ collapse: isCollapse }">
        <el-scrollbar>
          <el-menu
              :default-active="activeMenu"
              :collapse="isCollapse"
              :collapse-transition="false"
              background-color="#344a5f"
              text-color="#bfcbd9"
              active-text-color="#409eff"
              router
          >
            <el-menu-item
                v-for="item in menuItems"
                :key="item.path"
                :index="item.path"
            >
              <el-icon>
                <component :is="item.icon"/>
              </el-icon>
              <template #title>{{ item.title }}</template>
            </el-menu-item>
          </el-menu>
        </el-scrollbar>
      </aside>

      <!-- 内容区 -->
      <main class="content">
        <router-view/>
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  padding: 0 20px;
  justify-content: space-between;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
}

.toggle-btn {
  cursor: pointer;
  margin-right: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  font-size: 14px;
  color: #333;
}

.main-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.sidebar {
  width: 200px;
  transition: width 0.3s;
  background-color: #344a5f;
}

.sidebar.collapse {
  width: 64px;
}

.content {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
  overflow-y: auto;
}
</style>