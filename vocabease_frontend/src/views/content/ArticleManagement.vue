<!-- src/views/content/ArticleManagement.vue -->
<template>
  <div class="article-management">
    <!-- 查询条件 -->
    <el-form :model="searchForm" inline label-width="80px" style="margin-bottom: 20px">
      <el-form-item label="标题">
        <el-input v-model="searchForm.titleFuzzy" placeholder="模糊搜索" clearable />
      </el-form-item>

      <!-- ✅ 新增：分类查询 -->
      <el-form-item label="分类">
        <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable style="width: 150px">
          <el-option
              v-for="cat in categoryOptions"
              :key="cat.categoryId"
              :label="cat.categoryName"
              :value="cat.categoryId"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="难度">
        <el-select v-model="searchForm.level" placeholder="请选择难度" clearable style="width: 120px">
          <el-option :label="'1 - 初级'" :value="1" />
          <el-option :label="'2 - 中级'" :value="2" />
          <el-option :label="'3 - 高级'" :value="3" />
          <el-option :label="'4 - 专家'" :value="4" />
        </el-select>
      </el-form-item>

      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="未发布" :value="0" />
          <el-option label="已发布" :value="1" />
        </el-select>
      </el-form-item>

      <el-form-item label="创建人ID">
        <el-input v-model="searchForm.createrIdFuzzy" placeholder="用户ID" clearable style="width: 120px" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <div style="margin-bottom: 15px">
      <el-button type="primary" @click="openAddDialog">新增文章</el-button>
      <el-button type="success" @click="handleBatchImport">批量导入</el-button>

      <el-button type="info" :disabled="!selectedIds.length" @click="batchPost(1)">批量发布</el-button>
      <el-button type="warning" :disabled="!selectedIds.length" @click="batchPost(0)">批量下架</el-button>
      <el-button type="danger" :disabled="!selectedIds.length" @click="batchDelete">批量删除</el-button>
    </div>

    <!-- 文章列表 -->
    <el-table
        :data="articleList"
        v-loading="loading"
        row-key="articleId"
        @selection-change="handleSelectionChange"
        style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="articleId" label="ID" width="50" />
      <el-table-column prop="title" label="标题" min-width="150" />

      <!-- ✅ 新增：分类列 -->
      <el-table-column prop="categoryName" label="分类" width="120">
        <template #default="{ row }">
          <el-tag v-if="row.categoryName" size="small">{{ row.categoryName }}</el-tag>
          <span v-else style="color: #999; font-size: 12px;">未分类</span>
        </template>
      </el-table-column>

      <el-table-column prop="level" label="难度" width="80">
        <template #default="{ row }">
          {{ getLevelText(row.level) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '已发布' : '未发布' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createrId" label="创建人ID" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="180" />

      <el-table-column label="操作" fixed="right" width="180">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openEditDialog(row)">编辑</el-button>
          <el-button
              size="small"
              link
              :type="row.status === 1 ? 'warning' : 'success'"
              @click="togglePublish(row.articleId, row.status)"
          >
            {{ row.status === 1 ? '取消发布' : '发布' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="searchForm.pageNo"
        v-model:page-size="searchForm.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 15, 20, 50]"
        style="margin-top: 15px; text-align: right"
        @size-change="handlePageSizeChange"
        @current-change="loadData"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px" top="5vh">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="150" show-word-limit />
        </el-form-item>

        <!-- ✅ 分类选择 (编辑时会自动回填) -->
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%" clearable>
            <el-option
                v-for="cat in categoryOptions"
                :key="cat.categoryId"
                :label="cat.categoryName"
                :value="cat.categoryId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="难度" prop="level">
          <el-radio-group v-model="form.level">
            <el-radio :label="1">1 - 初级</el-radio>
            <el-radio :label="2">2 - 中级</el-radio>
            <el-radio :label="3">3 - 高级</el-radio>
            <el-radio :label="4">4 - 专家</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="正文" prop="body">
          <el-input
              v-model="form.body"
              type="textarea"
              :rows="6"
              placeholder="请输入正文内容"
          />
        </el-form-item>

        <el-form-item label="翻译" prop="translation">
          <el-input
              v-model="form.translation"
              type="textarea"
              :rows="4"
              placeholder="请输入翻译"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveArticle">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量导入弹窗 -->
    <el-dialog title="批量导入文章" v-model="importDialogVisible" width="500px">
      <div class="model">
        <span style="font-size: 13px; color: #606266;">
          <el-icon style="vertical-align: middle; margin-right: 4px;"><Download /></el-icon>
          请先下载模板，填写后再上传
        </span>
        <el-button size="small" type="primary" link @click="downloadTemplate(0)">
          下载文章导入模板
        </el-button>
      </div>

      <el-upload
          ref="uploadRef"
          :auto-upload="false"
          :on-change="handleFileChange"
          accept=".xlsx, .xls"
          :limit="1"
          :on-exceed="() => ElMessage.warning('最多上传1个文件')"
      >
        <template #trigger>
          <el-button type="primary">选择 Excel 文件</el-button>
        </template>
        <div v-if="importFile" style="margin-top: 10px">
          已选择：{{ importFile.name }}
        </div>
      </el-upload>
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="success" @click="confirmImport">开始导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import axios from 'axios'
import {Download} from '@element-plus/icons-vue'

// ================== 数据 ==================
const searchForm = reactive({
  pageNo: 1,
  pageSize: 15,
  titleFuzzy: '',
  categoryId: null, // ✅ 新增查询条件
  level: null,
  status: null,
  createrIdFuzzy: ''
})

const articleList = ref([])
const total = ref(0)
const loading = ref(false)
const selectedIds = ref([])
const categoryOptions = ref([]) // ✅ 存储分类列表

// ================== 弹窗 ==================
const dialogVisible = ref(false)
const dialogTitle = ref('新增文章')
const formRef = ref()
const isEditMode = ref(false)

const form = reactive({
  articleId: null,
  title: '',
  categoryId: null, // ✅ 表单用分类ID
  level: 1,
  body: '',
  translation: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  level: [{ required: true, message: '请选择难度', trigger: 'change' }],
  body: [{ required: true, message: '请输入正文', trigger: 'blur' }],
  translation: [{ required: true, message: '请输入翻译', trigger: 'blur' }]
}

// 导入
const importDialogVisible = ref(false)
const uploadRef = ref()
const importFile = ref(null)

// ================== 工具方法 ==================
const getLevelText = (level) => {
  const map = { 1: '初级', 2: '中级', 3: '高级', 4: '专家' }
  return map[level] || '未知'
}

const downloadTemplate = (type) => {
  const link = document.createElement('a');
  link.href = `/file/downloadTemplate?type=${type}`;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

// ================== 初始化 ==================
onMounted(() => {
  loadCategories()
  loadData()
})

// 加载分类（type=3 表示文章，请根据实际后端枚举调整）
const loadCategories = async () => {
  try {
    const res = await axios.post('/category/loadCategoryList', {}, { params: { type: 3 } })
    if (res.data.status === 'success') {
      categoryOptions.value = res.data.data || []
    }
  } catch (error) {
    ElMessage.error('加载分类失败')
  }
}

// 加载文章列表
const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.post('/article/loadDataList', searchForm)
    if (res.data.status === 'success') {
      const data = res.data.data || {}
      articleList.value = data.list || []
      total.value = data.totalCount || 0
    }
  } catch (error) {
    ElMessage.error('加载文章失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  Object.assign(searchForm, {
    pageNo: 1,
    pageSize: 15,
    titleFuzzy: '',
    categoryId: null, // ✅ 重置分类
    level: null,
    status: null,
    createrIdFuzzy: ''
  })
  loadData()
}

const handlePageSizeChange = (val) => {
  searchForm.pageSize = val
  loadData()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.articleId)
}

// ================== 新增/编辑 ==================
const openAddDialog = () => {
  isEditMode.value = false
  dialogTitle.value = '新增文章'
  Object.assign(form, {
    articleId: null,
    title: '',
    categoryId: null,
    level: 1,
    body: '',
    translation: ''
  })
  dialogVisible.value = true
  setTimeout(() => formRef.value?.resetFields(), 0)
}

// ✅ 核心修改：编辑时预先加载分类
const openEditDialog = (row) => {
  isEditMode.value = true
  dialogTitle.value = '编辑文章'

  // 先填充基础信息
  Object.assign(form, {
    articleId: row.articleId,
    title: row.title,
    categoryId: null, // 先置空
    level: row.level,
    body: row.body,
    translation: row.translation
  })

  dialogVisible.value = true

  // 异步获取分类ID并回填
  getCategoryByArticleId(row.articleId).then(categoryId => {
    if (categoryId !== null && categoryId !== undefined) {
      form.categoryId = categoryId
    }
  }).catch(() => {
    form.categoryId = null
  })

  setTimeout(() => formRef.value?.clearValidate(), 0)
}

const getCategoryByArticleId = async (articleId) => {
  try {
    const res = await axios.post('/article2category/getCategoryIdByArticleId', null, {
      params: { articleId }
    })
    const data = res.data.data
    if (data === null || data === undefined) return null
    return (typeof data === 'object' && data.categoryId !== undefined) ? data.categoryId : data
  } catch (error) {
    console.warn('获取文章分类失败', error)
    return null
  }
}

const saveArticle = async () => {
  await formRef.value?.validate()
  try {
    const articleData = {
      articleId: form.articleId,
      title: form.title,
      level: form.level,
      body: form.body,
      translation: form.translation,
      categoryId: form.categoryId
    }

    // 1. 保存文章主体
    await axios.post('/article/saveArticle', articleData)

    ElMessage.success(isEditMode.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    await loadData()
  } catch (error) {
    const msg = error.response?.data?.info || error.response?.data?.msg || '保存失败'
    ElMessage.error(msg)
  }
}

// ================== 发布/下架 ==================
const togglePublish = (articleId, currentStatus) => {
  const newStatus = currentStatus === 1 ? 0 : 1
  const action = newStatus === 1 ? '发布' : '下架'
  ElMessageBox.confirm(`确定${action}该文章？`, '提示', { type: 'warning' }).then(async () => {
    await axios.post('/article/' + (newStatus === 1 ? 'postArticle' : 'cancelPostArticle'), null, {
      params: { articleIds: String(articleId) }
    })
    ElMessage.success(`${action}成功`)
    await loadData()
  })
}

const batchPost = async (status) => {
  const action = status === 1 ? '发布' : '下架'
  await axios.post('/article/' + (status === 1 ? 'postArticle' : 'cancelPostArticle'), null, {
    params: { articleIds: selectedIds.value.join(',') }
  })
  ElMessage.success(`批量${action}成功`)
  await loadData()
}

// ================== 删除 ==================
const batchDelete = () => {
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 篇文章？`, '提示', { type: 'warning' }).then(async () => {
    try {
      const response = await axios.post('/article/deleteArticleBatch', null, {
        params: { articleIds: selectedIds.value.join(',') }
      });
      if (response.data.status === 'success') {
        ElMessage.success('删除成功');
        await loadData();
      } else {
        ElMessage.error(response.data.info || '删除失败');
      }
    } catch (error) {
      ElMessage.error('请求失败，请稍后重试');
    }
  }).catch(() => {});
};

// ================== 批量导入 ==================
const handleFileChange = (file) => {
  importFile.value = file.raw
}

const confirmImport = async () => {
  if (!importFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }
  const formData = new FormData()
  formData.append('file', importFile.value)

  try {
    const res = await axios.post('/article/importArticleByExcel', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.data.status === 'success') {
      const errors = res.data.data || []
      if (errors.length > 0) {
        ElMessage.warning(`导入完成，但有 ${errors.length} 行错误，请检查`)
      } else {
        ElMessage.success('导入成功')
      }
      importDialogVisible.value = false
      await loadData()
    }
  } catch (error) {
    ElMessage.error('导入失败')
  }
}

const handleBatchImport = () => {
  importFile.value = null
  uploadRef.value?.clearFiles()
  importDialogVisible.value = true
}
</script>

<style scoped>
.article-management {
  padding: 20px;
}
.model {
  margin-bottom: 15px;
  padding: 10px;
  background-color: #f4f4f5;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>