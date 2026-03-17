<template>
  <div class="feedback-management">
    <!-- 查询条件 (保持不变) -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline label-width="80px">
        <el-form-item label="反馈时间">
          <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              style="width: 240px"
              @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item label="反馈人">
          <el-input v-model="searchForm.nickNameFuzzy" placeholder="用户昵称" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="反馈内容">
          <el-input v-model="searchForm.contentFuzzy" placeholder="关键词搜索" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="未回复" :value="2" />
            <el-option label="已回复" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 列表区域 -->
    <el-card shadow="never" style="margin-top: 15px">
      <el-table :data="feedbackList" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="feedbackId" label="ID" width="80" />
        <el-table-column prop="nickName" label="反馈人" width="120" />
        <el-table-column prop="content" label="反馈内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="反馈时间" width="160" />

        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 3 ? 'success' : 'warning'">
              {{ row.status === 3 ? '已回复' : '未回复' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="lastSendTime" label="最后互动时间" width="160" />

        <el-table-column label="操作" fixed="right" width="120">
          <template #default="{ row }">
            <el-button type="primary" link @click="openChat(row)">
              {{ row.status === 3 ? '查看回复' : '去回复' }}
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
          :page-sizes="[10, 20, 50]"
          style="margin-top: 15px; justify-content: flex-end"
          @size-change="loadData"
          @current-change="loadData"
      />
    </el-card>

    <!-- ✅ 新增：聊天详情弹窗 -->
    <el-dialog
        v-model="chatDialogVisible"
        :title="'与用户 ' + currentUserName + ' 的沟通'"
        width="800px"
        top="5vh"
        :close-on-click-modal="false"
        destroy-on-close
        @closed="handleDialogClose"
    >
      <div class="chat-dialog-container">
        <!-- 聊天记录区域 -->
        <div class="chat-messages" ref="messageContainer">
          <div v-if="messageList.length === 0" class="empty-tip">暂无沟通记录</div>

          <div v-for="(msg, index) in messageList" :key="msg.feedbackId"
               :class="['message-row', msg.sendType === 1 ? 'message-admin' : 'message-user']">

            <!-- 头像 -->
            <div class="avatar">
              {{ msg.sendType === 1 ? '管' : '用' }}
            </div>

            <div class="message-body">
              <div class="message-info">
                <span class="name">{{ msg.sendType === 1 ? '管理员 ' : (msg.nickName + ' ' || '用户 ') }}</span>
                <span class="time">{{ formatTime(msg.createTime) }}</span>
              </div>
              <div class="message-content">
                {{ msg.content }}
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input-area">
          <el-input
              v-model="replyContent"
              type="textarea"
              :rows="3"
              placeholder="请输入回复内容... (Shift+Enter 换行)"
              maxlength="500"
              show-word-limit
              @keydown.enter.exact="sendReply"
          />
          <div class="input-footer">
            <span style="color: #999; font-size: 12px;">请勿发送敏感信息</span>
            <el-button type="primary" @click="sendReply" :loading="sending" :disabled="!replyContent.trim()">
              发送回复
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {nextTick, onMounted, reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'

const loading = ref(false)
const feedbackList = ref([])
const total = ref(0)
const dateRange = ref([])

// 聊天相关状态
const chatDialogVisible = ref(false)
const currentFeedbackId = ref(null)
const currentUserName = ref('')
const messageList = ref([])
const replyContent = ref('')
const sending = ref(false)
const messageContainer = ref(null)

const searchForm = reactive({
  pageNo: 1,
  pageSize: 15,
  nickNameFuzzy: '',
  contentFuzzy: '',
  status: null,
  createTimeStart: '',
  createTimeEnd: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.post('/appFeedback/loadFeedbackList', searchForm)
    if (res.data.status === 'success') {
      feedbackList.value = res.data.data.list
      total.value = res.data.data.totalCount
    }
  } catch (error) {
    ElMessage.error('加载反馈列表失败')
  } finally {
    loading.value = false
  }
}

const handleDateChange = (val) => {
  if (val && val.length === 2) {
    searchForm.createTimeStart = val[0]
    searchForm.createTimeEnd = val[1]
  } else {
    searchForm.createTimeStart = ''
    searchForm.createTimeEnd = ''
  }
}

const resetSearch = () => {
  dateRange.value = []
  searchForm.nickNameFuzzy = ''
  searchForm.contentFuzzy = ''
  searchForm.status = null
  searchForm.createTimeStart = ''
  searchForm.createTimeEnd = ''
  searchForm.pageNo = 1
  loadData()
}

// ✅ 打开聊天弹窗
const openChat = (row) => {
  currentFeedbackId.value = row.feedbackId
  currentUserName.value = row.nickName || '未知用户'
  chatDialogVisible.value = true

  // 打开后加载消息
  nextTick(() => {
    loadMessages()
  })
}

// 加载聊天记录
const loadMessages = async () => {
  if (!currentFeedbackId.value) return
  try {
    const res = await axios.post('/appFeedback/loadReplyList', null, {
      params: {pFeedbackId: currentFeedbackId.value}
    })
    if (res.data.status === 'success') {
      messageList.value = res.data.data || []
      await nextTick()
      scrollToBottom()
    }
  } catch (error) {
    ElMessage.error('加载聊天记录失败')
  }
}

// 发送回复
const sendReply = async (e) => {
  // 支持 Shift+Enter 换行，直接 Enter 发送
  if (e && !e.shiftKey) {
    e.preventDefault()
  }

  if (!replyContent.value.trim()) return

  sending.value = true
  try {
    await axios.post('/appFeedback/reply', null, {
      params: {
        content: replyContent.value.trim(),
        pFeedbackId: currentFeedbackId.value
      }
    })
    ElMessage.success('回复成功')
    replyContent.value = ''

    // 重新加载消息并滚动到底部
    await loadMessages()

    // 可选：刷新主列表状态（将未回复变为已回复）
    const currentRow = feedbackList.value.find(item => item.feedbackId === currentFeedbackId.value)
    if (currentRow) {
      currentRow.status = 3
      currentRow.lastSendTime = new Date()
    }

  } catch (error) {
    ElMessage.error('发送失败，请稍后重试')
  } finally {
    sending.value = false
  }
}

const scrollToBottom = () => {
  if (messageContainer.value) {
    messageContainer.value.scrollTop = messageContainer.value.scrollHeight
  }
}

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return `${date.getMonth() + 1}/${date.getDate()} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 弹窗关闭后的清理
const handleDialogClose = () => {
  currentFeedbackId.value = null
  currentUserName.value = ''
  messageList.value = []
  replyContent.value = ''
  // 可选：关闭后刷新列表，确保状态最新
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.feedback-management {
  padding: 20px;
}

.search-card {
  margin-bottom: 10px;
}

/* ✅ 聊天弹窗内部样式 */
.chat-dialog-container {
  display: flex;
  flex-direction: column;
  height: 600px; /* 固定高度 */
}

.chat-messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
  border-radius: 4px;
  margin-bottom: 15px;
}

.empty-tip {
  text-align: center;
  color: #909399;
  margin-top: 50px;
}

.message-row {
  display: flex;
  margin-bottom: 20px;
}

.message-user {
  flex-direction: row;
}

.message-admin {
  flex-direction: row-reverse;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #e4e7ed;
  color: #606266;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
  flex-shrink: 0;
}

.message-admin .avatar {
  background-color: #409eff;
  color: #fff;
}

.message-body {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.message-user .message-body {
  margin-left: 10px;
  align-items: flex-start;
}

.message-admin .message-body {
  margin-right: 10px;
  align-items: flex-end;
}

.message-info {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.message-content {
  padding: 10px 15px;
  border-radius: 6px;
  background-color: #fff;
  color: #303133;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-all;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.message-admin .message-content {
  background-color: #ecf5ff;
  color: #409eff;
}

.chat-input-area {
  border-top: 1px solid #ebeef5;
  padding-top: 15px;
}

.input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}
</style>