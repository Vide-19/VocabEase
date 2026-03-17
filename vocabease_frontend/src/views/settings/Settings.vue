<template>
  <div class="settings">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="菜单管理" name="menu">
        <MenuManagement />
      </el-tab-pane>
      <el-tab-pane label="角色管理" name="role">
        <RoleManagement />
      </el-tab-pane>
      <el-tab-pane label="轮播图管理" name="carousel">
        <Carousel />
      </el-tab-pane>
      <el-tab-pane label="反馈管理" name="feedback">
        <Feedback />
      </el-tab-pane>
<!--      <el-tab-pane label="系统设置" name="system">
        <h1>系统设置</h1>
        <el-form label-width="120px" style="max-width: 460px; margin-top: 20px;">
          <el-form-item label="通知设置">
            <el-switch v-model="notificationEnabled"></el-switch>
          </el-form-item>
          <el-form-item label="语言">
            <el-select v-model="language" placeholder="请选择语言">
              <el-option label="中文" value="zh"></el-option>
              <el-option label="English" value="en"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary">保存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>-->
    </el-tabs>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import MenuManagement from '@/views/settings/Menu.vue'
import RoleManagement from '@/views/settings/Role.vue'
import Carousel from '@/views/settings/Carousel.vue'
import Feedback from "@/views/settings/Feedback.vue";

const activeTab = ref('menu')

const notificationEnabled = ref(true)
const language = ref('zh')
</script>

<style scoped>
.settings {
  padding: 20px;
}
</style>