<!-- src/views/content/QuestionManagement.vue -->
<template>
  <div class="question-management">
    <!-- 查询条件 (保持不变) -->
    <el-form :model="searchForm" inline label-width="80px" style="margin-bottom: 20px">
      <el-form-item label="标题">
        <el-input v-model="searchForm.titleFuzzy" placeholder="模糊搜索" clearable />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable style="width: 150px">
          <el-option v-for="cat in categoryOptions" :key="cat.categoryId" :label="cat.categoryName" :value="cat.categoryId" />
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

    <!-- 操作按钮 (保持不变) -->
    <div style="margin-bottom: 15px">
      <el-button type="primary" @click="openAddDialog">新增问题</el-button>
      <el-button type="success" @click="handleBatchImport">批量导入</el-button>
      <el-button type="info" :disabled="!selectedIds.length" @click="batchPost(1)">批量发布</el-button>
      <el-button type="warning" :disabled="!selectedIds.length" @click="batchPost(0)">批量下架</el-button>
      <el-button type="danger" :disabled="!selectedIds.length" @click="batchDelete">批量删除</el-button>
    </div>

    <!-- 问题列表 (保持不变) -->
    <el-table :data="questionList" v-loading="loading" row-key="questionId" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="questionId" label="ID" width="50" />
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="categoryName" label="分类" width="120">
        <template #default="{ row }">
          <el-tag v-if="row.categoryName" size="small">{{ row.categoryName }}</el-tag>
          <span v-else style="color: #999; font-size: 12px;">未分类</span>
        </template>
      </el-table-column>
      <!-- ✅ 新增：显示题型列 -->
      <el-table-column prop="questionType" label="题型" width="100">
        <template #default="{ row }">
          <el-tag :type="getQuestionTypeTag(row.questionType)" size="small">
            {{ getQuestionTypeText(row.questionType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="level" label="难度" width="80">
        <template #default="{ row }">{{ getLevelText(row.level) }}</template>
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
          <el-button size="small" link :type="row.status === 1 ? 'warning' : 'success'" @click="togglePublish(row.questionId, row.status)">
            {{ row.status === 1 ? '取消发布' : '发布' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 (保持不变) -->
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="750px" top="5vh">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="150" show-word-limit />
        </el-form-item>

        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%" clearable>
            <el-option v-for="cat in categoryOptions" :key="cat.categoryId" :label="cat.categoryName" :value="cat.categoryId" />
          </el-select>
        </el-form-item>

        <!-- ✅ 新增：题型选择 -->
        <el-form-item label="题型" prop="questionType">
          <el-radio-group v-model="form.questionType" @change="handleQuestionTypeChange">
            <el-radio :label="0">判断题</el-radio>
            <el-radio :label="1">单选题</el-radio>
            <el-radio :label="2">多选题</el-radio>
            <el-radio :label="3">填空题</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="问题描述" prop="question">
          <el-input v-model="form.question" type="textarea" :rows="4" placeholder="请输入问题描述" />
        </el-form-item>

        <!-- ✅ 新增：判断题专用答案选择区 -->
        <el-form-item label="答案" v-if="form.questionType === 0">
          <el-radio-group v-model="form.answer" size="large">
            <el-radio-button label="1">✅ 正确</el-radio-button>
            <el-radio-button label="0">❌ 错误</el-radio-button>
          </el-radio-group>
          <div style="font-size: 12px; color: #999; margin-top: 5px;">
            请直接点击选择该判断题的正确答案
          </div>
        </el-form-item>

        <!-- ✅ 动态选项区域 (仅单选/多选显示) -->
        <el-form-item label="选项设置" v-if="isChoiceQuestion">
          <div v-for="(item, index) in form.optionList" :key="index" style="display: flex; align-items: center; margin-bottom: 10px;">
            <span style="width: 30px; font-weight: bold; color: #606266;">{{ getOptionLabel(index) }}</span>
            <el-input
                v-model="item.title"
                placeholder="请输入选项内容"
                style="flex: 1; margin: 0 10px;"
            />
            <!-- 正确答案选择 -->
            <el-checkbox
                v-if="form.questionType === 2"
                v-model="item.isCorrect"
                border
            >
              正确
            </el-checkbox>
            <el-radio
                v-else-if="form.questionType === 1"
                v-model="correctRadioIndex"
                :label="index"
                @change="handleRadioChange(index)"
            >
              正确
            </el-radio>

            <el-button
                type="danger"
                link
                icon="Delete"
                style="margin-left: 10px;"
                @click="removeOption(index)"
                :disabled="form.optionList.length <= 2"
            />
          </div>
          <el-button type="primary" link icon="Plus" @click="addOption">添加选项</el-button>
        </el-form-item>

        <!-- 填空题的简单答案提示 (判断题已移除提示，改用按钮) -->
        <el-alert
            v-if="form.questionType === 3"
            title="填空题：请在下方'标准答案'字段填写正确答案。"
            type="info"
            show-icon
            style="margin-bottom: 15px;"
        />

        <el-form-item label="标准答案" prop="answer" v-if="form.questionType === 3">
          <el-input v-model="form.answer" placeholder="填空题请填写标准答案" />
        </el-form-item>

        <el-form-item label="答案解析" prop="answerAnalysis">
          <el-input v-model="form.answerAnalysis" type="textarea" :rows="4" placeholder="请输入答案解析" />
        </el-form-item>

        <el-form-item label="难度" prop="level">
          <el-radio-group v-model="form.level">
            <el-radio :label="1">1 - 初级</el-radio>
            <el-radio :label="2">2 - 中级</el-radio>
            <el-radio :label="3">3 - 高级</el-radio>
            <el-radio :label="4">4 - 专家</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveQuestion">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量导入弹窗 (保持不变) -->
    <el-dialog title="批量导入问题" v-model="importDialogVisible" width="500px">
      <div class="model">
        <span style="font-size: 13px; color: #606266;">
          <el-icon style="vertical-align: middle; margin-right: 4px;"><Download /></el-icon>
          请先下载模板，填写后再上传
        </span>
        <el-button size="small" type="primary" link @click="downloadTemplate(1)">下载问题导入模板</el-button>
      </div>
      <el-upload ref="uploadRef" :auto-upload="false" :on-change="handleFileChange" accept=".xlsx, .xls" :limit="1" :on-exceed="() => ElMessage.warning('最多上传1个文件')">
        <template #trigger><el-button type="primary">选择 Excel 文件</el-button></template>
        <div v-if="importFile" style="margin-top: 10px">已选择：{{ importFile.name }}</div>
      </el-upload>
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="success" @click="confirmImport">开始导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import axios from 'axios'
import {Download} from '@element-plus/icons-vue'

// ================== 数据 ==================
const searchForm = reactive({
  pageNo: 1,
  pageSize: 15,
  titleFuzzy: '',
  categoryId: null,
  level: null,
  status: null,
  createrIdFuzzy: ''
})

const questionList = ref([])
const total = ref(0)
const loading = ref(false)
const selectedIds = ref([])
const categoryOptions = ref([])

// ================== 弹窗 ==================
const dialogVisible = ref(false)
const dialogTitle = ref('新增问题')
const formRef = ref()
const isEditMode = ref(false)

// 表单数据
const form = reactive({
  questionId: null,
  title: '',
  question: '',
  categoryId: null,
  questionType: 1, // 默认单选
  level: 1,
  answerAnalysis: '',
  answer: '', // 填空题/判断题答案
  optionList: [] // 前端临时存储选项 { title: '', isCorrect: false, itemId: null }
})

// 用于单选框绑定正确答案的索引
const correctRadioIndex = ref(-1)

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  question: [{ required: true, message: '请输入问题描述', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  questionType: [{ required: true, message: '请选择题型', trigger: 'change' }],
  level: [{ required: true, message: '请选择难度', trigger: 'change' }],
  answerAnalysis: [{ required: true, message: '请输入答案解析', trigger: 'blur' }],
  answer: [{ required: true, message: '必须填写标准答案', trigger: 'blur' }]
}

// 导入相关
const importDialogVisible = ref(false)
const uploadRef = ref()
const importFile = ref(null)

// ================== 计算属性 ==================
// 是否为选择题（单选或多选）
const isChoiceQuestion = computed(() => {
  return form.questionType === 1 || form.questionType === 2
})

// ================== 工具方法 ==================
const getLevelText = (level) => {
  const map = { 1: '初级', 2: '中级', 3: '高级', 4: '专家' }
  return map[level] || '未知'
}

const getQuestionTypeText = (type) => {
  const map = { 0: '判断题', 1: '单选题', 2: '多选题', 3: '填空题' }
  return map[type] || '未知'
}

const getQuestionTypeTag = (type) => {
  const map = { 0: 'info', 1: 'success', 2: 'warning', 3: 'primary' }
  return map[type] || 'info'
}

const getOptionLabel = (index) => {
  return String.fromCharCode(65 + index) // A, B, C...
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

const loadCategories = async () => {
  try {
    const res = await axios.post('/category/loadCategoryList', {}, { params: { type: 1 } })
    if (res.data.status === 'success') {
      categoryOptions.value = res.data.data || []
    }
  } catch (error) {
    ElMessage.error('加载分类失败')
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.post('/question/loadDataList', searchForm)
    if (res.data.status === 'success') {
      const data = res.data.data || {}
      questionList.value = data.list || []
      total.value = data.totalCount || 0
    }
  } catch (error) {
    ElMessage.error('加载问题失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  Object.assign(searchForm, {
    pageNo: 1,
    pageSize: 15,
    titleFuzzy: '',
    categoryId: null,
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
  selectedIds.value = selection.map(item => item.questionId)
}

// ================== 核心逻辑：题型切换处理 ==================
const handleQuestionTypeChange = () => {
  if (!isChoiceQuestion.value) {
    form.optionList = []
    // 如果切换到判断题且没有答案，默认设为正确
    if (form.questionType === 0 && !form.answer) {
      form.answer = '1'
    }
  } else if (form.optionList.length === 0) {
    addOption()
    addOption()
  }

  if (form.questionType === 1) {
    correctRadioIndex.value = -1
  }
}

const addOption = () => {
  form.optionList.push({
    title: '',
    isCorrect: false,
    itemId: null
  })
}

const removeOption = (index) => {
  form.optionList.splice(index, 1)
  if (form.questionType === 1 && correctRadioIndex.value === index) {
    correctRadioIndex.value = -1
  }
}

const handleRadioChange = (index) => {
  form.optionList.forEach((opt, idx) => {
    opt.isCorrect = (idx === index)
  })
}

// ================== 新增/编辑 ==================
const openAddDialog = () => {
  isEditMode.value = false
  dialogTitle.value = '新增问题'
  resetForm()
  dialogVisible.value = true
  setTimeout(() => formRef.value?.resetFields(), 0)
}

const resetForm = () => {
  Object.assign(form, {
    questionId: null,
    title: '',
    question: '',
    categoryId: null,
    questionType: 1,
    level: 1,
    answerAnalysis: '',
    answer: '1', // ✅ 默认给一个值，防止判断题初始化为空
    optionList: [
      { title: '', isCorrect: false, itemId: null },
      { title: '', isCorrect: false, itemId: null }
    ]
  })
  correctRadioIndex.value = -1
}

const openEditDialog = (row) => {
  isEditMode.value = true
  dialogTitle.value = '编辑问题'

  // ✅ 确保 answer 转为字符串，以便 el-radio-group 能正确匹配 ("1" vs 1)
  let initialAnswer = row.answer;
  if (initialAnswer !== null && initialAnswer !== undefined) {
    initialAnswer = String(initialAnswer);
  } else {
    initialAnswer = '';
  }

  Object.assign(form, {
    questionId: row.questionId,
    title: row.title,
    question: row.question,
    categoryId: null,
    questionType: row.questionType,
    level: row.level,
    answerAnalysis: row.answerAnalysis,
    answer: initialAnswer,
    optionList: []
  })

  dialogVisible.value = true

  Promise.all([
    getCategoryByQuestionId(row.questionId),
    loadQuestionItem(row.questionId)
  ]).then(([categoryId, items]) => {
    if (categoryId !== null && categoryId !== undefined) {
      form.categoryId = categoryId
    }

    if (items && items.length > 0) {
      form.optionList = items.map(item => ({
        itemId: item.itemId,
        title: item.title,
        isCorrect: item.isCorrect === 1
      }))

      if (form.questionType === 1) {
        const correctIdx = form.optionList.findIndex(opt => opt.isCorrect)
        correctRadioIndex.value = correctIdx >= 0 ? correctIdx : -1

        // 同步状态
        form.optionList.forEach((opt, idx) => {
          opt.isCorrect = (idx === correctRadioIndex.value)
        })
      }
    } else {
      if (isChoiceQuestion.value) {
        form.optionList = [
          { title: '', isCorrect: false, itemId: null },
          { title: '', isCorrect: false, itemId: null }
        ]
        correctRadioIndex.value = -1
      }
    }
  }).catch(() => {
    form.categoryId = null
  })

  setTimeout(() => formRef.value?.clearValidate(), 0)
}

const getCategoryByQuestionId = async (questionId) => {
  try {
    const res = await axios.post('/question2category/getCategoryIdByQuestionId', null, { params: { questionId } })
    const data = res.data.data
    if (data === null || data === undefined) return null
    return (typeof data === 'object' && data.categoryId !== undefined) ? data.categoryId : data
  } catch (error) {
    console.warn('获取问题分类失败', error)
    return null
  }
}

const loadQuestionItem = async (questionId) => {
  try {
    const res = await axios.post('/question/loadQuestionItem', null, { params: { questionId } })
    return res.data.data || []
  } catch {
    return []
  }
}

const saveQuestion = async () => {
  await formRef.value?.validate()

  // 2. 业务逻辑校验
  if (isChoiceQuestion.value) {
    if (form.optionList.length < 2) {
      ElMessage.warning('至少需要两个选项')
      return
    }
    const hasEmptyTitle = form.optionList.some(opt => !opt.title.trim())
    if (hasEmptyTitle) {
      ElMessage.warning('选项内容不能为空')
      return
    }
    const hasCorrect = form.optionList.some(opt => opt.isCorrect)
    if (!hasCorrect) {
      ElMessage.warning('请至少选择一个正确答案')
      return
    }
  }

  // ✅ 判断题校验
  if (form.questionType === 0) {
    if (!form.answer || (form.answer !== '1' && form.answer !== '0')) {
      ElMessage.warning('请选择判断题的正确答案（正确或错误）')
      return
    }
  }

  if (form.questionType === 3 && !form.answer) {
    ElMessage.warning('填空题请填写标准答案')
    return
  }

  try {
    let questionItemList = []
    let calculatedAnswer = ''

    // 3. 处理选择题逻辑
    if (isChoiceQuestion.value) {
      questionItemList = form.optionList.map((opt, index) => {
        const isCorrectVal = opt.isCorrect ? 1 : 0
        if (isCorrectVal === 1) {
          if (form.questionType === 1) {
            calculatedAnswer = String(index)
          } else if (form.questionType === 2) {
            calculatedAnswer = calculatedAnswer ? `${calculatedAnswer},${index}` : String(index)
          }
        }
        return {
          itemId: opt.itemId || null,
          title: opt.title.trim(),
          sort: index + 1,
          isCorrect: isCorrectVal
        }
      })

      if (!calculatedAnswer && form.questionType !== 3) {
        console.warn('警告：选择题未计算出答案索引，强制设为 "0"')
        calculatedAnswer = '0'
      }
    }

    // 4. 决定最终提交给后端的 answer 字段
    let finalAnswer = ''

    if (form.questionType === 3) {
      finalAnswer = form.answer || ''
    } else if (form.questionType === 0) {
      finalAnswer = form.answer
    } else if (isChoiceQuestion.value) {
      finalAnswer = calculatedAnswer
    } else {
      finalAnswer = form.answer || '0'
    }

    if (!finalAnswer && finalAnswer !== '0') {
      finalAnswer = ''
    }

    const questionData = {
      questionId: form.questionId || null,
      title: form.title,
      question: form.question,
      questionType: form.questionType,
      level: form.level,
      answerAnalysis: form.answerAnalysis || '',
      answer: finalAnswer,
      categoryId: form.categoryId,
      // ✅ 关键：非选择题强制传空数组，触发后端清理旧选项逻辑
      questionItemList: isChoiceQuestion.value ? JSON.stringify(questionItemList) : '[]'
    }

    await axios.post('/question/saveQuestion', questionData)

    ElMessage.success(isEditMode.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    await loadData()
  } catch (error) {
    console.error('保存失败错误详情:', error)
    const msg = error.response?.data?.info || error.response?.data?.msg || '保存失败，请检查控制台日志'
    ElMessage.error(msg)
  }
}

// ================== 发布/下架/删除 (保持不变) ==================
const togglePublish = (questionId, currentStatus) => {
  const newStatus = currentStatus === 1 ? 0 : 1
  const action = newStatus === 1 ? '发布' : '下架'
  ElMessageBox.confirm(`确定${action}该问题？`, '提示', { type: 'warning' }).then(async () => {
    await axios.post('/question/' + (newStatus === 1 ? 'postQuestion' : 'cancelPostQuestion'), null, {
      params: { questionIds: String(questionId) }
    })
    ElMessage.success(`${action}成功`)
    await loadData()
  })
}

const batchPost = async (status) => {
  const action = status === 1 ? '发布' : '下架'
  await axios.post('/question/' + (status === 1 ? 'postQuestion' : 'cancelPostQuestion'), null, {
    params: { questionIds: selectedIds.value.join(',') }
  })
  ElMessage.success(`批量${action}成功`)
  await loadData()
}

const batchDelete = () => {
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 个问题？`, '提示', { type: 'warning' }).then(async () => {
    try {
      const response = await axios.post('/question/deleteQuestionBatch', null, {
        params: { questionIds: selectedIds.value.join(',') }
      });
      if (response.data.status === 'success') {
        ElMessage.success('删除成功')
        await loadData()
      } else {
        ElMessage.error(response.data.info || '删除失败')
      }
    } catch (error) {
      ElMessage.error('请求失败，请稍后重试')
    }
  }).catch(() => {})
}

// ================== 批量导入 (保持不变) ==================
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
    const res = await axios.post('/question/importQuestionByExcel', formData, {
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
.question-management {
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