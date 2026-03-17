<template>
  <div class="menu-management">
    <el-button type="success" @click="handleAddMenu">新增菜单</el-button>

    <!-- 菜单列表（扁平化展示，忽略树形） -->
    <el-table :data="flatMenuList" style="width: 100%; margin-top: 20px" v-loading="loading">
      <el-table-column prop="menuName" label="菜单名称" />
      <el-table-column prop="menuUrl" label="URL" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.menuId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px">
      <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" />
        </el-form-item>
        <el-form-item label="URL">
          <el-input v-model="form.menuUrl" placeholder="/path" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="1" :max="999" controls-position="right" />
        </el-form-item>
        <el-form-item label="上级菜单">
          <el-select v-model="form.pId" placeholder="请选择">
            <el-option :label="'顶级菜单'" :value="0" />
            <el-option
                v-for="item in allMenus"
                :key="item.menuId"
                :label="item.menuName"
                :value="item.menuId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveMenu">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import axios from 'axios'

const menuTree = ref([]) // 原始树形数据
const loading = ref(false)
const dialogVisible = ref(false)
const isEditMode = ref(false)

const form = reactive({
  menuId: null,
  menuName: '',
  menuUrl: '',
  sort: 1,
  pId: 0 // 0 表示顶级
})

const formRef = ref()
const rules = {
  menuName: [{required: true, message: '请输入菜单名称', trigger: 'blur'}],
  sort: [{required: true, message: '请输入排序', trigger: 'blur'}]
}

const dialogTitle = computed(() => isEditMode.value ? '编辑菜单' : '新增菜单')

// 扁平化菜单（用于表格展示）
const flatMenuList = computed(() => {
  const flatten = (list) => {
    let result = []
    list.forEach(item => {
      result.push(item)
      if (item.children && item.children.length) {
        result = result.concat(flatten(item.children))
      }
    })
    return result
  }
  return flatten(menuTree.value)
})

// 获取所有菜单（用于上级菜单下拉）
const allMenus = computed(() => {
  return flatMenuList.value.filter(item => item.menuId !== form.menuId)
})

// ✅ 获取菜单列表
const fetchData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/settings/menuList')
    if (res.data.status === 'success') {
      menuTree.value = Array.isArray(res.data.data) ? res.data.data : []
    } else {
      ElMessage.error(res.data.info || '获取菜单失败')
      menuTree.value = []
    }
  } catch (error) {
    console.error('菜单请求失败:', error)
    ElMessage.error('网络错误')
    menuTree.value = []
  } finally {
    loading.value = false
  }
}

const handleAddMenu = () => {
  dialogVisible.value = true
  isEditMode.value = false
  Object.assign(form, {
    menuId: null,
    menuName: '',
    menuUrl: '',
    sort: 1,
    pId: 0
  })
  formRef.value?.resetFields()
}

const handleEdit = (row) => {
  dialogVisible.value = true
  isEditMode.value = true
  Object.assign(form, {...row})
}

const handleDelete = (menuId) => {
  ElMessageBox.confirm('确定删除该菜单？', '提示', {type: 'warning'})
      .then(async () => {
        await axios.post('/settings/deleteMenu', null, {params: {menuId}})
        ElMessage.success('删除成功')
        await fetchData()
      })
      .catch(() => {
      })
}

const saveMenu = async () => {
  await formRef.value?.validate()
  try {
    await axios.post('/settings/saveMenu', form)
    ElMessage.success(isEditMode.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    await fetchData()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.menu-management {
  padding: 20px;
}
</style>