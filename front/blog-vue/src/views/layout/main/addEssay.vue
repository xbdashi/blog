<template>
    <div style="margin-top: 120px;">
        <el-form ref="ruleFormRef" :rules="rules" :model="addMondel" label-width="auto">
            <el-form-item label="标题" prop="title">
                <el-input v-model="addMondel.title" placeholder="请输入标题" ></el-input>
             </el-form-item>
             <el-form-item label="描述" prop="describes">
                <el-input v-model="addMondel.describes" placeholder="请输入描述"></el-input>
             </el-form-item>
             <el-form-item label="内容" prop="content">
                <v-md-editor v-model="addMondel.content" height="800px"></v-md-editor>
             </el-form-item>
             <el-form-item label="选择标签">
                    <div style="display: flex;justify-content: center; width: 100%;">
                        <!-- 下拉列表 -->
                     <div style="width: 70%;">
                        <el-select v-model="addMondel.labelIds" multiple placeholder="请选择标签">
                        <el-option
                            v-for="item in optTag"
                            :key="item.id"
                            :label="item.label"
                            :value="item.id">
                            <el-tag :type="item.type">{{item.label}}</el-tag>
                        </el-option>
                    </el-select></div>

                    <div style="width: 30%;">
                        <el-button type="warning" plain :icon="Search" @click="addSign">自定义标签</el-button>
                    </div>
                    </div>
             </el-form-item>
              <!-- <v-md-preview :text="content"></v-md-preview> -->
            <div style="display: flex;justify-self: end;">
                <el-button type="success" @click="commit" style="margin-left: 50px;">保存</el-button>
                <el-button type="primary" @click="commit">发布</el-button>
            </div>
        </el-form>
        <!-- 添加标签对话框 -->
        <SysDialog
        :title="dialog.title"
        :width="dialog.width"
        :visible="dialog.visible"
        @on-confirm="commitSign"
        @on-close="onClose"
        >
            <template v-slot:content>
                <el-form ref="signFormRef" :rules="ruleSign" :model="signMondel" label-width="auto">
             <el-form-item label="标签名" prop="label">
                <el-input v-model="signMondel.label" placeholder="请输入描述"></el-input>
             </el-form-item>
             <el-form-item label="样式" prop="type">
                <el-select @change="handleChange" v-model="signMondel.type" style="width: 240px">
                        <el-option
                            v-for="item in colors"
                            :key="item.label"
                            :label="item.label"
                            :value="item.label"
                        >
                        <div class="flex items-center">
                            <el-tag :color="item.value" style="margin-right: 8px" size="small" />
                            <span :style="{ color: item.value }">{{ item.label }}</span>
                        </div>
                        </el-option>
                    </el-select>
             </el-form-item>
             </el-form>
            </template>
        </SysDialog>
    </div>
</template>
  
<script setup>
import { useAddEssay, useAddTag, useGetAllTag } from '@/api/layout';
import { Search } from '@element-plus/icons-vue';
import { ElMessage, ElNotification } from 'element-plus';
import { onMounted, reactive, ref } from 'vue'
import SysDialog from '@/compoents/SysDialog.vue'; 
import useDialog from '@/hook/useDialog';
import { loadAllParams } from '@/tools/page';
import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router';
const router = useRouter()
const {user} = useUserStore()
const {dialog,show,onClose,onConfirm} = useDialog() 
const addMondel = reactive({
    id: '',
    userId: '',
    title: '',
    describes: '',
    content: '',
    signIds:''
})
// 标签模型
const signMondel = reactive({
    label: '',
    type: ''
})
// 获取出所有的标签
const optTag = ref([])
const getAllTag = async ()=>{
    const res =  await useGetAllTag(loadAllParams)
    optTag.value = res.data
}

onMounted(()=>{
    getAllTag();
    console.log(optTag.value)
})

// 获取到表单
const ruleFormRef = ref()
// 表单效验
const rules = reactive({
    title: [
      { required: true, message: '请输入标题', trigger: 'blur' },
    ],
    describes: [
      { required: true, message: '请输入描述', trigger: 'blur' },
    ],
    content: [
      { required: true, message: '请输入内容', trigger: 'blur' },
    ],
})
// 自定义标签
const addSign = ()=>{
    dialog.title = '自定义标签'
    dialog.width = 400
    dialog.height = 200
    show()
}
const commit = () => {
    ruleFormRef.value.validate(async valid => {
            if(!user){
            ElNotification({
                title: '错误',
                message: '请先登录后再发布文章',
                type: 'error',
            })
            router.push('/login')
            return  
        }
        if (valid) {
            // 提交表单
            addMondel.userId = user.id
            console.log(addMondel)
            const res = await useAddEssay(addMondel)
            if(res.code === 200){
                ElMessage.success(res.msg)
            }else{
                ElMessage.error(res.msg)
            }
        } else {
            return false;
        }
    })
    console.log(addMondel)
}
// 颜色选择器
const value = ref([])
const colors = [
  {
    value: 'rgb(64, 158, 255)',
    label: 'primary',
  },
  {
    value: 'rgb(103, 194, 58)',
    label: 'success',
  },
  {
    value: 'rgb(144, 147, 153)',
    label: 'info',
  },
  {
    value: 'rgb(230, 162, 60)',
    label: 'warning',
  },
  {
    value: 'rgb(245, 108, 108)',
    label: 'danger',
  },
]

// 如果超过一个就替换为新的那个
const handleChange = (newValues) => {
    if (newValues.length > 1) {
    // 如果选择的数量超过1个，保留最后一个选择的值
    value.value = [newValues[newValues.length - 1]];
  }
   colors.forEach(item =>{
    if(item.value === value.label){
       signMondel.type = value.label
    }
   })
}
// 添加一个新的标签的效验
const signFormRef = ref()
// 新标签效验
const ruleSign = reactive({
    label: [
      { required: true, message: '请输入描述', trigger: 'blur' },
    ],
    type: [
      { required: true, message: '请选择样式', trigger: 'blur' },
    ],
})
const commitSign = ()=>{
    signFormRef.value.validate(async valid => {
        if (valid) {
            const res = await useAddTag(signMondel)
            if(res.code === 200){
                ElMessage.success(res.msg)
            }else{
                ElMessage.error(res.msg)
            }
            dialog.visible = false
            await getAllTag();
        } else {
            return false;
        }
    })
}
</script>
<style scoped>
/* 修改选中项的背景颜色 */
.el-select .el-tag {
    background-color: #409EFF; /* 选中项的背景颜色 */
    color: #FFFFFF; /* 选中项的文字颜色 */
}

/* 修改下拉菜单中选中项的背景颜色 */
.el-select-dropdown__item.selected {
    background-color: #409EFF; /* 选中项的背景颜色 */
    color: #FFFFFF; /* 选中项的文字颜色 */
}
</style>