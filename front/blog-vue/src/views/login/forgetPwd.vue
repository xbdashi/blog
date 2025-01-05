<template>
    <div class="body">
        <div class="reset-password-container">
        <h2>重置密码</h2>
        <form @submit.prevent>
            <!-- 邮箱输入框 -->
            <div class="form-group">
                <label for="email">邮箱</label>
                <input v-model="loginModel.email" type="email" id="email" name="email" placeholder="请输入注册邮箱" required>
            </div>

            <!-- 验证码输入框 -->
            <div class="form-group">
                <label for="verification-code">验证码</label>
                <div style="display: flex;">
                    <input v-model="loginModel.emailCode" type="text" id="verification-code" name="verification-code" placeholder="请输入验证码" required
                        style="flex-grow: 1;">
                        
                    <button @click="getEmailCode" type="button">
                        <el-icon v-if="isLoadingStatus" class="loading"><Loading/></el-icon>
                        获取验证码
                    </button>
                </div>
            </div>

            <!-- 新密码输入框 -->
            <div class="form-group">
                <label for="new-password">新密码</label>
                <input v-model="loginModel.password" type="password" id="new-password" name="new-password" placeholder="请输入新密码" required>
            </div>

            <!-- 提交按钮 -->
            <div class="button-group">
                <button @click="resetPwd">重置密码</button>
            </div>
        </form>

        <!-- 返回登录链接 -->
        <div class="link-container">
            <RouterLink to="/login">返回登录</RouterLink>
        </div>
    </div>
    </div>
</template>
<script setup lang="ts">
import { useGetEmailCode,useResetPwd } from '@/api/common';
import router from '@/router';
import { Loading } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { ref,reactive } from 'vue';
const loginModel = reactive({
    username: '',
    password: '',
    email:'',
    emailCode:'',
    code:'',
})
const isLoadingStatus = ref(false)
// 获取验证码
const getEmailCode =async ()=>{
    if(isLoadingStatus.value) return
    isLoadingStatus.value = true
    ElMessage.success('正在发送请耐心等待大约需要20s...')
    try {
    const res = await useGetEmailCode(loginModel)
        ElMessage.success(res.msg)
    } catch(err) {
        console.log('捕获到错误:', err)
    } finally {
        isLoadingStatus.value = false
    }
}
// 重置密码
const resetPwd = async ()=>{
    const res = await useResetPwd(loginModel)
    if(res.code == 200){
        ElMessage.success(res.msg)
        router.push('/login')
    }
    

}
</script>
<style scoped>
.body {
    font-family: 'Microsoft YaHei', Arial, sans-serif;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    margin: 0;
    background: url('/src/assets/pet.svg') no-repeat center center fixed;
    /* 添加背景图片 */
    background-size: cover;
    /* 让背景图片覆盖整个屏幕 */
    color: #333;
}

.reset-password-container {
    width: 60vh;
    background: rgba(255, 255, 255, 0.9);
    /* 半透明白色背景 */
    padding: 30px;
    border-radius: 16px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}


h2 {
    text-align: center;
    margin-bottom: 20px;
    color: #4a4a4a;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
}

.form-group input {
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 16px;
    box-sizing: border-box;
    width: 100%;
    /* 其他输入框恢复到原宽度 */
}

.form-group input:focus {
    border-color: #2575fc;
    outline: none;
}

/* 特别设置验证码输入框的宽度 */
.form-group input#verification-code {
    width: 60%;
    /* 缩短验证码输入框的宽度 */
}

.form-group button {
    width: 30%;
    /* 增加按钮宽度，保持按钮的大小 */
    padding: 8px 16px;
    font-size: 14px;
    cursor: pointer;
    background-color: #28a745;
    color: white;
    border-radius: 8px;
    margin-left: 10px;
    /* 保持按钮与输入框之间的间距 */
}

.form-group button:hover {
    background-color: #218838;
}

.button-group {
    display: flex;
    justify-content: center;
    margin-top: 25px;
}

.button-group button {
    padding: 12px 20px;
    border: none;
    border-radius: 8px;
    font-size: 16px;
    cursor: pointer;
    background-color: #007bff;
    color: #fff;
    transition: background-color 0.3s ease;
}

.button-group button:hover {
    background-color: #0056b3;
}

.link-container {
    text-align: center;
    margin-top: 15px;
}

.link-container a {
    color: #007bff;
    text-decoration: none;
}

.link-container a:hover {
    text-decoration: underline;
}
.loading {
            animation: rotate 1s linear infinite;
        }
@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>