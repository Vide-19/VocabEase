<!-- src/views/content/CategoryManagement.vue -->
<template>
  <div class="category-management">
    <el-button type="primary" @click="openAddDialog">新增分类</el-button>

    <el-table
        :data="categoryList"
        style="width: 100%; margin-top: 20px"
        v-loading="loading"
        row-key="categoryId"
    >
      <el-table-column prop="categoryName" label="分类名称" width="200" />
      <el-table-column prop="typeText" label="类型" width="100" align="center" />
      <el-table-column prop="sort" label="排序值" width="100" align="center" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row, $index }">
          <el-button size="small" link type="primary" @click="openEditDialog(row)">
            编辑
          </el-button>
          <el-button size="small" link type="danger" @click="handleDelete(row.categoryId)">
            删除
          </el-button>
          <!-- 上移：不是第一个才能上移 -->
          <el-button
              size="small"
              link
              :disabled="$index === 0"
              @click="moveUp($index)"
          >
            上移
          </el-button>
          <!-- 下移：不是最后一个才能下移 -->
          <el-button
              size="small"
              link
              :disabled="$index === categoryList.length - 1"
              @click="moveDown($index)"
          >
            下移
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="420px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>

        <el-form-item label="分类类型" prop="type">
          <el-select
              v-model="form.type"
              placeholder="请选择分类类型"
              style="width: 100%"
          >
            <el-option label="考题" :value="1" />
            <el-option label="单词" :value="2" />
            <el-option label="文章" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="排序" prop="sort">
          <el-input-number
              v-model="form.sort"
              :min="1"
              :max="9999"
              controls-position="right"
              style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCategory">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import axios from 'axios'

const categoryList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEditMode = ref(false)
const formRef = ref()

// 表单数据（不含 categoryDesc，因后端无此字段）
const form = reactive({
  categoryId: null,
  categoryName: '',
  sort: 1,
  type: 1 // 1=考题, 2=单词, 3=文章
})

// 表单校验规则
const rules = {
  categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序值', trigger: 'blur' }],
  type: [{ required: true, message: '请选择分类类型', trigger: 'change' }]
}

const dialogTitle = ref('新增分类')

// 类型映射（用于表格显示中文）
const typeMap = {
  1: '考题',
  2: '单词',
  3: '文章'
}

// 计算带 typeText 的列表（用于表格展示）
const displayCategoryList = computed(() => {
  return categoryList.value.map(item => ({
    ...item,
    typeText: typeMap[item.type] || '未知'
  }))
})

// 加载分类列表
const loadCategories = async () => {
  loading.value = true
  try {
    const res = await axios.post('/category/loadCategoryList', {})
    if (res.data.status === 'success') {
      categoryList.value = res.data.data || []
    }
  } catch (error) {
    ElMessage.error('加载分类失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 打开新增
const openAddDialog = () => {
  isEditMode.value = false
  dialogTitle.value = '新增分类'
  // ✅ 逐个赋值，确保响应式
  form.categoryId = null
  form.categoryName = ''
  form.sort = Math.max(...categoryList.value.map(item => item.sort), 0) + 1
  form.type = 1
  dialogVisible.value = true
  formRef.value?.resetFields()
}

// 打开编辑
const openEditDialog = (row) => {
  isEditMode.value = true
  dialogTitle.value = '编辑分类'
  // ✅ 逐个赋值
  form.categoryId = row.categoryId
  form.categoryName = row.categoryName
  form.sort = row.sort
  form.type = row.type
  dialogVisible.value = true
}

// 保存分类
const saveCategory = async () => {
  await formRef.value?.validate()
  try {
    await axios.post('/category/saveCategory', form)
    ElMessage.success(isEditMode.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    await loadCategories()
  } catch (error) {
    // 可选：更详细的错误提示
    const msg = error.response?.data?.info || '保存失败'
    ElMessage.error(msg)
  }
}

// 删除分类
const handleDelete = (categoryId) => {
  ElMessageBox.confirm('确定删除该分类？', '提示', { type: 'warning' }).then(async () => {
    try {
      await axios.post('/category/deleteCategory', null, { params: { categoryId } })
      ElMessage.success('删除成功')
      await loadCategories()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 上移
const moveUp = (index) => {
  const list = [...categoryList.value]
  ;[list[index - 1], list[index]] = [list[index], list[index - 1]]
  submitNewOrder(list)
}

// 下移
const moveDown = (index) => {
  const list = [...categoryList.value]
  ;[list[index], list[index + 1]] = [list[index + 1], list[index]]
  submitNewOrder(list)
}

// 提交新排序
const submitNewOrder = async (newList) => {
  const ids = newList.map(item => item.categoryId).join(',')
  try {
    await axios.post('/category/updateSort', null, { params: { categoryIds: ids } })
    ElMessage.success('排序更新成功')
    await loadCategories()
  } catch (error) {
    ElMessage.error('排序更新失败')
    await loadCategories() // 回滚
  }
}

// 初始化
loadCategories()
</script>

<style scoped>
.category-management {
  padding: 20px;
}
</style>