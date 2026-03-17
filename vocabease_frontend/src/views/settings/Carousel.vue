<template>
  <div class="carousel-management">
    <!-- 顶部操作栏 -->
    <div class="header-actions">
      <el-button type="primary" @click="openAddDialog">新增轮播图</el-button>
    </div>

    <!-- 表格区域 -->
    <!-- ✅ 修改点：移除了 default-sort，改为在 JS 中手动排序，确保 100% 生效 -->
    <el-table
        :data="carouselList"
        style="width: 100%; margin-top: 20px"
        v-loading="loading"
        row-key="carouselId"
        border
    >
      <!-- 表格列：预览 -->
      <el-table-column label="预览" width="120" align="center">
        <template #default="{ row }">
          <!-- ✅ 只有当 imagPath 有值时才显示图片组件 -->
          <el-image
              v-if="row.imagPath"
              :src="getFullImageUrl(row.imagPath)"
              :preview-src-list="[getFullImageUrl(row.imagPath)]"
              fit="cover"
              style="width: 80px; height: 50px; border-radius: 4px; cursor: pointer;"
          >
            <!-- ✅ 加载失败时显示的文字 -->
            <template #error>
              <div class="image-error">
                <span>加载失败</span>
              </div>
            </template>
          </el-image>

          <!-- ✅ 当 imagPath 为空时显示的文字 -->
          <span v-else class="text-gray">无图片</span>
        </template>
      </el-table-column>

      <el-table-column prop="imagPath" label="图片路径" min-width="200" show-overflow-tooltip />

      <!-- 跳转类型 -->
      <el-table-column prop="objectTypeText" label="跳转类型" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getTypeTag(row.objectType)" size="small">
            {{ getTypeText(row.objectType) }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 关联内容 -->
      <el-table-column label="关联内容/链接" min-width="150" show-overflow-tooltip>
        <template #default="{ row }">
          <span v-if="row.objectType === 4">{{ row.outerLink }}</span>
          <span v-else>ID: {{ row.objectId || '-' }}</span>
        </template>
      </el-table-column>

      <!-- 排序列：添加 sortable 让用户也可以点击切换，但初始状态已被 JS 锁定为升序 -->
      <el-table-column prop="sort" label="排序" width="80" align="center" sortable />

      <!-- 操作列 -->
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row, $index }">
          <el-button size="small" link type="primary" @click="openEditDialog(row)">编辑</el-button>
          <el-button size="small" link type="danger" @click="handleDelete(row.carouselId)">删除</el-button>
          <el-divider direction="vertical" />
          <el-button size="small" link :disabled="$index === 0" @click="moveUp($index)">上移</el-button>
          <el-button size="small" link :disabled="$index === carouselList.length - 1" @click="moveDown($index)">下移</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">

        <!-- 图片上传组件 -->
        <el-form-item label="轮播图片" prop="imagPath">
          <el-upload
              class="avatar-uploader"
              action="/file/uploadFile"
              :show-file-list="false"
              :on-success="handleImageSuccess"
              :before-upload="beforeImageUpload"
              :headers="getUploadHeaders()"
              drag
          >
            <div v-if="form.imagPath">
              <el-image
                  :src="getFullImageUrl(form.imagPath)"
                  fit="cover"
                  style="width: 150px; height: 100px; border-radius: 4px;"
              />
            </div>
            <div v-else class="uploader-placeholder">
              <el-icon class="el-icon--upload"><upload-filled /></el-icon>
              <div class="el-upload__text">
                拖拽文件到此处 或 <em>点击上传</em>
              </div>
              <div class="el-upload__tip">支持 jpg/png 文件，不超过 5MB</div>
            </div>
          </el-upload>
        </el-form-item>

        <!-- 跳转类型 -->
        <el-form-item label="跳转类型" prop="objectType">
          <el-select
              v-model="form.objectType"
              placeholder="请选择点击后跳转类型"
              style="width: 100%"
              @change="handleTypeChange"
          >
            <el-option label="单词详情" :value="0" />
            <el-option label="文章详情" :value="1" />
            <el-option label="问题详情" :value="2" />
            <el-option label="笔记详情" :value="3" />
            <el-option label="外部链接" :value="4" />
          </el-select>
        </el-form-item>

        <!-- 动态字段 -->
        <el-form-item v-if="form.objectType !== 4" label="对象ID" prop="objectId">
          <el-input v-model="form.objectId" :placeholder="`请输入${getTypeText(form.objectType)}的ID`" />
          <div class="form-tip">填写数据库中对应的 ID</div>
        </el-form-item>

        <el-form-item v-if="form.objectType === 4" label="外部链接" prop="outerLink">
          <el-input v-model="form.outerLink" placeholder="请输入完整的 http/https 链接" />
        </el-form-item>

        <el-form-item label="排序值" prop="sort">
          <el-input-number v-model="form.sort" :min="1" :max="9999" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCarousel" :loading="saving">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {nextTick, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {UploadFilled} from '@element-plus/icons-vue'
import axios from 'axios'
import {useUserStore} from '@/stores/user'

const userStore = useUserStore()

// --- 状态定义 ---
const carouselList = ref([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const isEditMode = ref(false)
const formRef = ref()
const dialogTitle = ref('')

// 表单数据
const form = reactive({
  carouselId: null,
  imagPath: '',
  objectType: 0,
  objectId: '',
  outerLink: '',
  sort: 1
})

const IMAGE_BASE_URL = 'http://localhost:9091/file/getImage/'

// --- 辅助方法 ---

const getFullImageUrl = (relativePath) => {
  if (!relativePath) return ''
  if (relativePath.startsWith('http')) return relativePath
  return `/file/getImage/${relativePath}`
}

const typeMap = { 0: '单词', 1: '文章', 2: '问题', 3: '笔记', 4: '外部' }
const getTypeText = (type) => typeMap[type] || '未知'
const getTypeTag = (type) => ['success', 'primary', 'warning', 'info', 'danger'][type] || 'info'

const handleTypeChange = () => {
  if (form.objectType === 4) form.objectId = ''
  else form.outerLink = ''
  if (formRef.value) formRef.value.clearValidate(['objectId', 'outerLink'])
}

const getUploadHeaders = () => {
  return {}
}

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

const handleImageSuccess = (response, uploadFile) => {
  if (response.code === 200 || response.status === 'success') {
    form.imagPath = response.data
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.info || response.msg || '上传失败')
    form.imagPath = ''
  }
}

// --- 业务逻辑 ---

const loadCarousels = async () => {
  loading.value = true
  try {
    const res = await axios.post('/appCarousel/loadCarouselList', {})
    // 兼容不同的返回结构
    let list = res.data.data || res.data.result || []

    // ✅ 核心修改：手动执行升序排序 (1 -> 2 -> 3)
    // 无论后端返回什么顺序，这里强制排好
    list.sort((a, b) => {
      // 转为数字比较，防止 "10" < "2" 的字符串排序错误
      const sortA = Number(a.sort) || 9999;
      const sortB = Number(b.sort) || 9999;
      return sortA - sortB; // 升序
    });

    // 赋值给表格
    carouselList.value = list.map(item => ({
      ...item,
      imagPath: item.imagPath || ''
    }))
  } catch (error) {
    console.error('加载失败:', error)
    ElMessage.error('加载轮播图失败')
  } finally {
    loading.value = false
  }
}

const openAddDialog = () => {
  isEditMode.value = false
  dialogTitle.value = '新增轮播图'

  form.carouselId = null
  form.imagPath = ''
  form.objectType = 0
  form.objectId = ''
  form.outerLink = ''

  // 新增时默认排在最后
  const maxSort = carouselList.value.length > 0
      ? Math.max(...carouselList.value.map(i => Number(i.sort) || 0))
      : 0
  form.sort = maxSort + 1

  dialogVisible.value = true

  nextTick(() => {
    if (formRef.value) {
      formRef.value.clearValidate()
    }
  })
}

const openEditDialog = (row) => {
  isEditMode.value = true
  dialogTitle.value = '编辑轮播图'

  form.carouselId = row.carouselId
  form.imagPath = row.imagPath || ''
  form.objectType = row.objectType
  form.objectId = row.objectId || ''
  form.outerLink = row.outerLink || ''
  form.sort = row.sort

  dialogVisible.value = true

  nextTick(() => {
    if (formRef.value) {
      formRef.value.clearValidate()
    }
  })
}

const saveCarousel = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    if (!form.imagPath && !isEditMode.value) {
      ElMessage.warning('请先上传图片')
      return
    }

    saving.value = true
    try {
      const payload = {...form}
      if (payload.objectType === 4) payload.objectId = null
      else payload.outerLink = null

      await axios.post('/appCarousel/saveCarousel', payload)

      ElMessage.success(isEditMode.value ? '更新成功' : '新增成功')
      dialogVisible.value = false
      await loadCarousels() // 重新加载并自动排序
    } catch (error) {
      console.error('保存失败:', error)
      const msg = error.response?.data?.msg || error.response?.data?.info || '保存失败'
      ElMessage.error(msg)
    } finally {
      saving.value = false
    }
  })
}

const handleDelete = (carouselId) => {
  ElMessageBox.confirm('确定删除该轮播图吗？', '提示', {type: 'warning'}).then(async () => {
    try {
      await axios.post('/appCarousel/deleteCarousel', null, {params: {carouselId}})
      ElMessage.success('删除成功')
      await loadCarousels()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const moveUp = (index) => {
  if (index === 0) return
  const list = [...carouselList.value]
  ;[list[index - 1], list[index]] = [list[index], list[index - 1]]
  submitNewOrder(list)
}

const moveDown = (index) => {
  if (index === carouselList.value.length - 1) return
  const list = [...carouselList.value]
  ;[list[index + 1], list[index]] = [list[index], list[index + 1]]
  submitNewOrder(list)
}

const submitNewOrder = async (newList) => {
  const ids = newList.map(item => item.carouselId).join(',')
  try {
    await axios.post('/appCarousel/updateSort', null, {params: {carouselIds: ids}})
    ElMessage.success('排序更新成功')
    await loadCarousels()
  } catch (error) {
    ElMessage.error('排序更新失败')
    await loadCarousels()
  }
}

const rules = {
  imagPath: [{required: true, message: '请上传轮播图片', trigger: 'change'}],
  objectType: [{required: true, message: '请选择跳转类型', trigger: 'change'}],
  objectId: [{
    required: true, message: '请输入对象ID', trigger: 'blur', validator: (rule, value, callback) => {
      if (form.objectType !== 4 && !value) callback(new Error('请输入对象ID')); else callback()
    }
  }],
  outerLink: [{
    required: true, message: '请输入外部链接', trigger: 'blur', validator: (rule, value, callback) => {
      if (form.objectType === 4 && !value) callback(new Error('请输入外部链接'));
      else if (form.objectType === 4 && !value.startsWith('http')) callback(new Error('链接必须以 http 开头'));
      else callback()
    }
  }],
  sort: [{required: true, message: '请输入排序值', trigger: 'blur'}]
}

// 初始化
loadCarousels()
</script>

<style scoped>
.carousel-management {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
}

.header-actions {
  display: flex;
  justify-content: flex-start;
}

.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80px;
  height: 50px;
  background: #f5f7fa;
  color: #909399;
  font-size: 12px;
  border-radius: 4px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
}

.text-gray {
  color: #909399;
  font-size: 12px;
}

/* 上传组件样式 */
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 100%;
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.uploader-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100px;
  color: var(--el-text-color-secondary);
}

.el-icon--upload {
  font-size: 28px;
  color: var(--el-text-color-secondary);
  margin-bottom: 8px;
}
</style>