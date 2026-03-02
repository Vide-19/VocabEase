<template>
  <div class="role-management">
    <el-button type="primary" @click="openDialog('add')">新增角色</el-button>

    <el-table :data="roles" style="width: 100%; margin-top: 20px" v-loading="loading">
      <el-table-column prop="roleId" label="ID" width="80" />
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleDesc" label="角色描述" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog('edit', row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.roleId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="form.roleDesc" type="textarea" placeholder="请输入角色描述（可选）" />
        </el-form-item>
        <el-form-item label="菜单权限" required>
          <el-tree
              ref="treeRef"
              :data="menuTree"
              show-checkbox
              node-key="menuId"
              :props="{ label: 'menuName', children: 'children' }"
              :default-expand-all="true"
              :check-strictly="false"
              style="border: 1px solid #ebeef5; padding: 10px; border-radius: 4px; max-height: 300px; overflow-y: auto;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <!-- 新增角色时：只显示“保存角色”（自动包含菜单） -->
        <el-button
            v-if="!isEditMode"
            type="primary"
            @click="handleSaveRole"
        >
          保存角色
        </el-button>
        <!-- 编辑模式：两个按钮 -->
        <template v-else>
          <el-button
              type="primary"
              @click="handleSaveRoleInfo"
          >
            保存角色信息
          </el-button>
          <el-button
              type="success"
              @click="handleSaveMenu"
          >
            保存菜单权限
          </el-button>
        </template>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, nextTick, onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import axios from 'axios'

// ====== 数据定义 ======
const roles = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEditMode = ref(false)
const currentId = ref(null)

const form = reactive({
  roleName: '',
  roleDesc: ''
})

const rules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
}

const formRef = ref()
const treeRef = ref()
const menuTree = ref([])
const dialogTitle = computed(() => isEditMode.value ? '编辑角色' : '新增角色')

// ====== 加载菜单树 ======
const loadMenuTree = async () => {
  try {
    const res = await axios.get('/settings/menuList')
    if (res.data.status === 'success') {
      const data = res.data.data || []
      const root = data.find(item => item.menuId === 0)
      menuTree.value = root?.children || []
    } else {
      ElMessage.error('加载菜单失败')
      menuTree.value = []
    }
  } catch (error) {
    console.error('加载菜单树失败:', error)
    ElMessage.error('网络错误，菜单加载失败')
    menuTree.value = []
  }
}

// ====== 获取角色列表 ======
const fetchData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/settings/loadRolesList')
    if (res.data.status === 'success') {
      roles.value = Array.isArray(res.data.data?.list) ? res.data.data.list : []
    } else {
      ElMessage.error(res.data.info || '获取角色失败')
      roles.value = []
    }
  } catch (error) {
    console.error('角色请求失败:', error)
    ElMessage.error('网络错误')
    roles.value = []
  } finally {
    loading.value = false
  }
}

// ====== 打开弹窗 ======
const openDialog = async (type, row = null) => {
  dialogVisible.value = true
  isEditMode.value = type === 'edit'

  form.roleName = ''
  form.roleDesc = ''

  if (isEditMode.value && row && typeof row.roleId === 'number') {
    currentId.value = row.roleId
    form.roleName = row.roleName || ''
    form.roleDesc = row.roleDesc || ''

    const detailRes = await axios.get('/settings/getRoleByRoleId', {
      params: { roleId: row.roleId }
    })
    if (detailRes.data.status === 'success') {
      const fullRole = detailRes.data.data
      await loadMenuTree()
      await nextTick(() => {
        treeRef.value?.setCheckedKeys(fullRole.menuIds || [])
      })
    }
  } else {
    currentId.value = null
    await loadMenuTree()
    await nextTick(() => {
      treeRef.value?.setCheckedKeys([])
    })
  }
}

// ====== 【新增】保存角色（含菜单）=====
const handleSaveRole = async () => {
  await formRef.value?.validate()
  const leafIds = treeRef.value.getCheckedKeys()
  if (leafIds.length === 0) {
    ElMessage.warning('请至少选择一个菜单权限')
    return
  }

  try {
    await axios.post('/settings/saveRole', {
      roleName: form.roleName,
      roleDesc: form.roleDesc,
      menuIdx: leafIds.join(',')
    })
    ElMessage.success('新增成功')
    dialogVisible.value = false
    await fetchData()
  } catch (error) {
    console.error('新增角色失败:', error)
    const msg = error.response?.data?.info || '保存失败'
    ElMessage.error(msg)
  }
}

// ====== 【编辑】仅保存角色基本信息 ======
const handleSaveRoleInfo = async () => {
  await formRef.value?.validate()

  if (!currentId.value) {
    ElMessage.error('角色ID缺失')
    return
  }

  try {
    // 注意：这里不传 menuIdx！只更新基本信息
    await axios.post('/settings/saveRole', {
      roleId: currentId.value,
      roleName: form.roleName,
      roleDesc: form.roleDesc
      // 不传 menuIdx → 后端不会更新菜单
    })
    ElMessage.success('角色信息更新成功')
    // 不关闭弹窗，方便继续改菜单
  } catch (error) {
    console.error('更新角色信息失败:', error)
    const msg = error.response?.data?.info || '更新失败'
    ElMessage.error(msg)
  }
}

// ====== 【编辑】仅保存菜单权限 ======
const handleSaveMenu = async () => {
  const leafIds = treeRef.value.getCheckedKeys()
  if (leafIds.length === 0) {
    ElMessage.warning('请至少选择一个菜单权限')
    return
  }

  if (!currentId.value) {
    ElMessage.error('角色ID缺失')
    return
  }

  try {
    // 调用专用接口 saveRole2Menu
    await axios.post('/settings/saveRole2Menu', {
      roleId: currentId.value,
      menuIds: leafIds.join(',') // 注意：字段名是 menuIds！
    })
    ElMessage.success('菜单权限更新成功')
    // 不关闭弹窗
  } catch (error) {
    console.error('更新菜单失败:', error)
    const msg = error.response?.data?.info || '菜单保存失败'
    ElMessage.error(msg)
  }
}

// ====== 删除角色 ======
const handleDelete = (roleId) => {
  ElMessageBox.confirm('确定删除该角色？', '提示', { type: 'warning' })
      .then(async () => {
        await axios.post('/settings/deleteRole', null, { params: { roleId } })
        ElMessage.success('删除成功')
        await fetchData()
      })
      .catch(() => {})
}

// ====== 初始化 ======
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.role-management {
  padding: 20px;
}
</style>