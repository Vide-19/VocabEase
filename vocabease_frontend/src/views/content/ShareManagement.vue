<template>
  <div class="note-management">
    <!-- 查询条件 -->
    <el-form :model="searchForm" inline label-width="80px" style="margin-bottom: 20px">
      <el-form-item label="标题">
        <el-input v-model="searchForm.titleFuzzy" placeholder="模糊搜索" clearable />
      </el-form-item>

      <el-form-item label="封面类型">
        <el-select v-model="searchForm.coverType" placeholder="请选择封面类型" clearable style="width: 150px">
          <el-option
              v-for="type in coverTypeOptions"
              :key="type.value"
              :label="type.label"
              :value="type.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="未发布" :value="0" />
          <el-option label="已发布" :value="1" />
          <el-option label="已置顶" :value="2"/>
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
      <el-button type="primary" @click="openAddDialog">新增笔记</el-button>
      <el-button
          type="info"
          :disabled="!selectedIds.length"
          @click="batchPost(1)"
      >
        批量发布
      </el-button>
      <el-button
          type="warning"
          :disabled="!selectedIds.length"
          @click="batchPost(0)"
      >
        批量下架
      </el-button>
      <el-button
          type="danger"
          :disabled="!selectedIds.length"
          @click="batchDelete"
      >
        批量删除
      </el-button>
    </div>

    <!-- 笔记列表 -->
    <el-table
        :data="shareList"
        v-loading="loading"
        row-key="shareId"
        @selection-change="handleSelectionChange"
        style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="shareId" label="ID" width="50" />
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="coverType" label="封面类型" width="100">
        <template #default="{ row }">
          {{ getCoverTypeText(row.coverType) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : ( row.status === 2 ? 'danger' : 'info' )">
            {{ row.status === 1 ? '已发布' : ( row.status === 2 ? '已置顶' : '未发布' ) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createrId" label="创建人ID" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="170" />

      <el-table-column label="操作" fixed="right" width="195">
        <template #default="{ row }">
          <el-button size="small" link type="primary" @click="openEditDialog(row)">
            编辑
          </el-button>
          <el-button
              size="small"
              link
              :type="row.status === 1 ? 'warning' : 'success'"
              @click="togglePublish(row.shareId, row.status)"
          >
            {{ row.status === 1 ? '取消发布' : '发布' }}
          </el-button>
          <el-button
              size="small"
              link
              :type="row.status === 2 ? 'warning' : 'danger'"
              @click="toTop(row.shareId, row.status)"
          >
            {{ row.status === 2 ? '取消置顶' : '置顶' }}
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

        <el-form-item label="封面类型" prop="coverType">
          <el-select v-model="form.coverType" placeholder="请选择封面类型" style="width: 100%">
            <el-option
                v-for="type in coverTypeOptions"
                :key="type.value"
                :label="type.label"
                :value="type.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="正文" prop="content">
          <el-input
              v-model="form.content"
              type="textarea"
              :rows="6"
              placeholder="请输入正文内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveShare">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import axios from 'axios'

// ================== 数据 ==================
const searchForm = reactive({
  pageNo: 1,
  pageSize: 15,
  titleFuzzy: '',
  coverType: null,
  status: null,
  createrIdFuzzy: ''
})

const shareList = ref([])
const total = ref(0)
const loading = ref(false)
const selectedIds = ref([])

const coverTypeOptions = ref([
  { value: 0, label: '无封面' },
  { value: 1, label: '横幅' },
  { value: 2, label: '小图标' }
])

// ================== 弹窗 ==================
const dialogVisible = ref(false)
const dialogTitle = ref('新增笔记')
const formRef = ref()
const isEditMode = ref(false)

const form = reactive({
  shareId: null,
  title: '',
  coverType: 0,
  content: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  coverType: [{ required: true, message: '请选择封面类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入正文', trigger: 'blur' }]
}

// ================== 工具方法 ==================
const getCoverTypeText = (coverType) => {
  const map = { 0: '无封面', 1: '横幅', 2: '小图标' }
  return map[coverType] || '未知'
}

// ================== 初始化 ==================
onMounted(() => {
  loadData()
})

// 加载笔记列表
const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.post('/share/loadDataList', searchForm)
    if (res.data.status === 'success') {
      const data = res.data.data || {}
      shareList.value = data.list || []
      total.value = data.totalCount || 0
    }
  } catch (error) {
    ElMessage.error('加载笔记失败')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    pageNo: 1,
    pageSize: 15,
    titleFuzzy: '',
    coverType: null,
    status: null,
    createrIdFuzzy: ''
  })
  loadData()
}

// 分页变化
const handlePageSizeChange = (val) => {
  searchForm.pageSize = val
  loadData()
}

// 表格选择
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.shareId)
}

// ================== 新增/编辑 ==================
const openAddDialog = () => {
  isEditMode.value = false
  dialogTitle.value = '新增笔记'
  Object.assign(form, {
    shareId: null,
    title: '',
    coverType: 0,
    content: ''
  })
  dialogVisible.value = true
  formRef.value?.resetFields()
}

const openEditDialog = (row) => {
  isEditMode.value = true
  dialogTitle.value = '编辑笔记'
  Object.assign(form, {
    shareId: row.shareId,
    title: row.title,
    coverType: row.coverType,
    content: row.content
  })
  dialogVisible.value = true
}

// 保存笔记
const saveShare = async () => {
  await formRef.value?.validate()
  try {
    const postData = {
      shareId: form.shareId,
      title: form.title,
      coverType: form.coverType,
      content: form.content
    }
    await axios.post('/share/saveShare', postData)
    ElMessage.success(isEditMode.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    await loadData()
  } catch (error) {
    const msg = error.response?.data?.info || '保存失败'
    ElMessage.error(msg)
  }
}

// ================== 发布/下架 ==================
const togglePublish = (shareId, currentStatus) => {
  const newStatus = currentStatus === 1 ? 0 : 1
  const action = newStatus === 1 ? '发布' : '下架'
  ElMessageBox.confirm(`确定${action}该笔记？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await axios.post('/share/' + (newStatus === 1 ? 'postShare' : 'cancelPostShare'), null, {
      params: { shareIds: String(shareId) }
    })
    ElMessage.success(`${action}成功`)
    await loadData()
  })
}

const batchPost = async (status) => {
  const action = status === 1 ? '发布' : '下架'
  await axios.post('/share/' + (status === 1 ? 'postShare' : 'cancelPostShare'), null, {
    params: { shareIds: selectedIds.value.join(',') }
  })
  ElMessage.success(`批量${action}成功`)
  await loadData()
}

// ================== 置顶 ==================
const toTop = (shareId, currentStatus) => {
  const newStatus = currentStatus === 2 ? 1 : 2
  const action = newStatus === 2 ? '置顶' : '取消置顶'
  ElMessageBox.confirm(`确定${action}该笔记？`, '提示', {type: 'warning'}).then(async () => {
    await axios.post('/share/' + (newStatus === 2 ? 'topShare' : 'postShare'), null, {
      params: {shareIds: String(shareId)}
    })
    ElMessage.success(`${action}成功`)
    await loadData()
  })
}

// ================== 删除 ==================
const batchDelete = () => {
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 篇笔记？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const response = await axios.post('/share/deleteShareBatch', null, {
        params: { shareIds: selectedIds.value.join(',') }
      });
      if (response.data.status === 'success') {
        ElMessage.success('删除成功');
        await loadData();
      } else {
        // 业务错误（如已发布）
        ElMessage.error(response.data.info || '删除失败');
      }
    } catch (error) {
      // 网络错误、500、404 等真正的 HTTP 错误
      ElMessage.error('请求失败，请稍后重试');
    }
  }).catch(() => {
    // 用户取消
  });
};
</script>

<style scoped>
.note-management {
  padding: 20px;
}
</style>