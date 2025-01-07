<template>
<!-- 横幅 -->
<div class="banner">
    <div class="banner-overlay"></div>
    <div class="banner-content">
        <div class="avatar">
            <img :src="user.photo" alt="用户头像">
            <div class="details">
                <div class="username">{{ user.username }}</div>
                <div class="stats">
                    关注：{{ user.focus }} | 
                    粉丝：{{ user.fans }} | 
                    获赞：{{ user.likes || 9 }} | 
                    播放量：{{ user.views || 594 }}
                </div>
            </div>
        </div>
        <button class="edit-button" @click="editUserInfo">编辑资料</button>
    </div>
</div>

<!-- 用户栏目导航 -->
<div class="user-nav">
    <ul>
        <li><a href="#" class="active">发表的博客</a></li> <!-- 高亮 "我发表的博客" -->
        <li><a href="sb4.html">我的点赞</a></li> <!-- 这里是指向sb4.html -->
        <li><a href="sb5.html">我的评论</a></li> <!-- 这里是指向sb5.html -->
    </ul>
<!--    <div class="stats">关注：28 | 粉丝：4 | 获赞：9 | 播放量：594</div>-->
</div>
<div class="main-content">
    <div class="blog-post" v-for="item in essayList">
        <div class="post-title">{{ item.title }}
            <el-tag v-if="item.status == 0" type="info">草稿</el-tag>
            <el-tag v-else-if="item.status == 1" type="warning">审核中</el-tag>
            <el-tag v-else-if="item.status == 2">已发布</el-tag>
            <el-tag v-else type="danger">未通过</el-tag>
        </div>
        <div class="post-meta">发表于 {{ formatDate(item.createTime) }} | 分类：<el-tag :type="sign.type" v-for="sign in item.signsList">{{ sign.label }}</el-tag></div>
        <div class="post-excerpt">
            {{ item.describes }}
        </div>
    </div>
</div>

<!-- 编辑用户的框框 -->
 <SysDialog
 :width="dialog.width"
 :height="dialog.height"
 :title="dialog.title"
 :visible="dialog.visible"
 @on-confirm="submit"
 @on-close="onClose"
 >
 <template #content>
    <el-form :model="form" label-width="80px" class="user-form">
      <el-form-item label="用户名">
        <el-input v-model="form.username" placeholder="请输入用户名" />
      </el-form-item>

      <el-form-item label="邮箱">
        <el-input v-model="form.email" placeholder="请输入邮箱" />
      </el-form-item>

      <el-form-item label="手机号">
        <el-input v-model="form.phone" placeholder="请输入手机号" />
      </el-form-item>

      <el-form-item label="QQ">
        <el-input v-model="form.qq" placeholder="请输入QQ" />
      </el-form-item>

      <el-form-item label="微信">
        <el-input v-model="form.wechat" placeholder="请输入微信" />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="省份">
            <el-select v-model="province" placeholder="选择省份" class="w-full">
              <el-option
                v-for="item in provinceArr"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-select v-model="city" placeholder="选择城市" class="w-full">
              <el-option
                v-for="item in cityArr"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
        </el-col>
        <el-col :span="8">
            <el-select v-model="area" placeholder="选择区域" class="w-full">
              <el-option
                v-for="item in areaArr"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
        </el-col>
      </el-row>

      <el-form-item label="头像">
        <el-upload
            class="avatar-uploader"
            :action="'/api/upload'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
        >
            <img v-if="form.photo" :src="form.photo" class="avatar"/>
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            <el-icon v-if="loading" class="loading-icon is-loading"><Loading /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
  </template>
 </SysDialog>
</template>
<script setup>
import {useUserStore} from '@/stores/user'
import { ref ,onMounted,computed,reactive,watch,nextTick} from 'vue';
import {useGetEssayByUserId} from '@/api/essay/index.ts'
import SysDialog from '@/compoents/SysDialog.vue';
import useDialog from '@/hook/useDialog';
import { useGetUser,useUpdateUser } from '@/api/user/user';
import { ElMessage } from 'element-plus/es/components/index.mjs';
// 详细地址(省市区 详细地址)
import areaObj from '@/assets/pca.json'
import { Plus } from '@element-plus/icons-vue';
import { Loading } from '@element-plus/icons-vue'
const {user} = useUserStore()
const { dialog, show, onClose } = useDialog()
const essayList = ref()
// 省
const provinceArr = Object.keys(areaObj)
const province = ref(provinceArr[6])
// 市
const cityArr = computed(() => {
    return Object.keys(areaObj[province.value])
})
const city = ref(cityArr.value[0])
// 监听省份变化
// 修改 watch 配置，添加 immediate 选项
watch(province, (newVal) => {
    if (newVal) {
        city.value = Object.keys(areaObj[newVal])[0]
    }
}, { immediate: true })
// 区
const areaArr = computed(() => {
    return areaObj[province.value][city.value]
})
const area = ref(areaArr.value[0])
// 监听市变化
watch(city, (newVal) => {
    if (newVal && province.value) {
        area.value = areaObj[province.value][newVal][0]
    }
}, { immediate: true })
const form = reactive({
    username:'',
    email:'',
    photo:'',
    phone:'',
    province: province.value,
    city: city.value,
    distract: area.value,
    qq:'',
    wechat:'',
})
const loading = ref(false)
// 根据用户id查询对应的数据
const getUserInfo = async () => {
    const {data} = await useGetUser(user.id)
    form.username = data.username
    form.email = data.email
    form.photo = data.photo
    form.phone = data.phone
    form.province = data.province
    form.city = data.city
    form.distract = data.distract
    form.qq = data.qq
    form.wechat = data.wechat
    form.id = data.id

    // 先设置省，触发 watch 更新市的数据
    province.value = data.province
    // 需要等待 cityArr 更新后再设置 city
    await nextTick()
    city.value = data.city
    // 需要等待 areaArr 更新后再设置 area
    await nextTick()
    area.value = data.distract
}
const getEssay = async () => {
    const res = await useGetEssayByUserId(user.id)
    essayList.value = res.data
}
onMounted(()=>{
    getEssay();
})
const submit = async()=>{
     // 确保提交前更新最新的省市区值
    form.province = province.value
    form.city = city.value
    form.distract = area.value
    const res = await useUpdateUser(form)
    if(res.code === 200){
        ElMessage.success('修改成功')
        onClose()
    }
}

const editUserInfo = ()=>{
    dialog.title = '修改个人信息'
    dialog.height = 500
    getUserInfo()
    show()
}
// 上传前的验证
const beforeAvatarUpload= (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('Avatar picture must be JPG format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  loading.value = true
  return true
}
// 上传后的调用
const handleAvatarSuccess = (res) => {
  loading.value = false
  form.photo = res
}
// Add this function before the component logic
const formatDate = (date) => {
  const d = new Date(date)
  const pad = (n) => n.toString().padStart(2, '0')
  
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}
</script>
<style scoped lang="less">
/* 顶部导航栏 */
.top-navbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            background-color: #007bff;
            padding: 10px 20px;
            color: white;
            position: sticky;
            top: 0;
            z-index: 10;
        }

        /* 顶部大横幅 */
        .banner {
            height: 200px;
            background: url('/src/assets/1.webp') no-repeat center/cover;
            display: flex;
            align-items: flex-end; /* 让内容靠近横幅的下方 */
            justify-content: flex-start;
            color: white;
            position: relative;
            padding: 0 120px;  /* 左右边距改为120px */
            overflow: hidden;
        }

        .banner-overlay {
            background-color: rgba(0, 0, 0, 0.4);
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
        }

        .banner-content {
            position: relative;
            z-index: 1;
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 100%;
            padding-bottom: 20px;  /* 增加底部间距，使内容更接近底部 */
        }

        .banner-content h1 {
            font-size: 24px;  /* 减小标题字体 */
            color: white;
        }

        .banner-content .avatar {
            display: flex;
            align-items: center;
            gap: 15px;
            width: 50px;
        }

        .banner-content .avatar img {
            width: 80px;  /* 调整头像大小 */
            height: 80px;
            border-radius: 50%;
            border: 4px solid #fff;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
            transition: transform 0.3s ease;
        }

        .banner-content .avatar img:hover {
            transform: scale(1.1);
        }

        .banner-content .details {
            font-size: 18px;  /* 减小字体大小 */
            color: white;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            width: 300px;
         
        }

        .banner-content .details .username {
            font-size: 26px;  /* 减小用户名的字体 */
            font-weight: bold;
            color: #ffdd40;
            margin-bottom: 5px;
            text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.4);
        }

        .banner-content .details .stats {
            
            font-size: 14px;
            color: #f0f0f0;
            text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.3);
            /* 移除了可能导致问题的flex布局属性 */
            white-space: nowrap; /* 防止换行 */
        }

        .edit-button {
            padding: 10px 18px;  /* 缩小按钮的大小 */
            background-color: #ffdd40;
            color: #333;
            border: none;
            border-radius: 30px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .edit-button:hover {
            background-color: #ffcc00;
            transform: translateY(-3px);
        }

        .edit-button:active {
            background-color: #ff9900;
        }

        /* 用户栏目导航 - 横向布局 */
        .user-nav {
            display: flex;
            justify-content: flex-start;
            background-color: #fff;
            margin-top: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            padding: 12px 0;
            align-items: center;
            padding-left: 120px; /* 左侧间距改为120px */
            padding-right: 120px; /* 右侧间距改为120px */
        }

        .user-nav ul {
            display: flex;
            gap: 15px;
            padding: 0;
        }

        .user-nav ul li {
            padding: 10px 20px;
        }

        .user-nav ul li a {
            font-size: 18px;
            color: #007bff;
            transition: color 0.3s, transform 0.3s, background-color 0.3s, box-shadow 0.3s;
        }

        /* 高亮效果：默认给 "我发表的博客" 添加高亮 */
        .user-nav ul li a.active {
            color: #fff;
            background: linear-gradient(45deg, #ffb74d, #ffcc80);  /* 更浅的渐变背景 */
            font-weight: bold;
            border-radius: 5px;
            padding: 8px 20px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);  /* 添加阴影 */
            transform: scale(1.05);  /* 添加轻微放大效果 */
        }

        /* 高亮效果：鼠标悬停时 */
        .user-nav ul li a:hover {
            text-decoration: none;
            background: linear-gradient(45deg, #ffcc80, #ffb74d);  /* 悬停时改变渐变方向 */
            color: #fff;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
            transform: scale(1.1);  /* 鼠标悬停时放大 */
        }

        .stats {
            font-size: 14px;
            color: #777;
            margin-left: auto;
            padding-right: 20px;
        }
/* 主体内容区域 */
.main-content {
    width: 85%;
    margin: 20px auto;
    padding: 20px;
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.main-content .section-title {
            font-size: 22px;
            font-weight: bold;
            margin-bottom: 15px;
            border-bottom: 2px solid #eee;
            padding-bottom: 8px;
        }

        .blog-post {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 15px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
            transition: background-color 0.3s, transform 0.3s;
            cursor: pointer;
        }

        .blog-post:hover {
            background-color: #e8f4ff;
            transform: translateY(-5px);
        }

        .blog-post .post-title {
            font-size: 20px;
            font-weight: bold;
            color: #007bff;
            margin-bottom: 10px;
        }

        .blog-post .post-meta {
            font-size: 14px;
            color: #777;
            margin-bottom: 10px;
        }

        .blog-post .post-excerpt {
            font-size: 16px;
            color: #333;
        }
/* 添加表单样式 */
.user-form {
  padding: 20px;
}

.user-form :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  transition: all 0.2s;
}

.user-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.user-form :deep(.el-select) {
  width: 100%;
}

.user-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.user-form :deep(.el-form-item__label) {
  font-weight: 500;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  border:1px dashed var(--el-border-color);
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.loading-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 20px;
  color: var(--el-color-primary);
}

.is-loading {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  0% {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  100% {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}
</style>