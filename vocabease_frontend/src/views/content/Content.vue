<!-- src/views/content/ContentManagement.vue -->
<template>
  <div class="content-management">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="分类管理" name="category">
        <CategoryManagement/>
      </el-tab-pane>
      <el-tab-pane label="单词管理" name="word">
        <WordManagement/>
      </el-tab-pane>
      <el-tab-pane label="文章管理" name="article">
        <ArticleManagement/>
      </el-tab-pane>
      <el-tab-pane label="问题管理" name="question">
        <QuestionManagement/>
      </el-tab-pane>
      <el-tab-pane label="笔记管理" name="share">
        <ShareManagement/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import {ref} from 'vue' // 按需引入各子组件（确保路径正确）
import CategoryManagement from '@/views/content/CategoryManagement.vue'
import ArticleManagement from '@/views/content/ArticleManagement.vue'
import QuestionManagement from '@/views/content/QuestionManagement.vue'
import WordManagement from '@/views/content/WordManagement.vue'
import ShareManagement from '@/views/content/ShareManagement.vue'


// 默认激活第一个 Tab
const activeTab = ref('category')
</script>

<style scoped>
.content-management {
  padding: 20px;
}
</style>