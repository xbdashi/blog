<template>
    <div class="container">
        <div class="login-container">
            <div class="image-container">
                <img src="/src/assets/pet.svg" alt="美化图片">
                <div class="forgot-password">
                    <!-- <a href="/src/views/login/forgetPwd.vue">忘记密码？</a> -->
                    <RouterLink to="/forget">忘记密码？</RouterLink>
                    <RouterLink to="/">暂不登录</RouterLink>
                </div>
                <div class="hint-text">
                        未注册过的账户，我们将自动帮您注册。
                </div>
            </div>
            <div class="form-container">
                <div class="switch-mode">
                    <span 
                        :class="{ active: !isRegister }" 
                        @click="isRegister = false"
                    >登录</span>
                    <span 
                        :class="{ active: isRegister }" 
                        @click="isRegister = true"
                    >注册</span>
                </div>
                <form @submit.prevent>
                    <div class="form-group">
                        <label for="username">账号</label>
                        <input v-model="loginModel.username" type="text" id="username" name="username" placeholder="请输入您的账号" required>
                    </div>
                    <div class="form-group">
                        <label for="password">密码</label>
                        <input v-model="loginModel.password" type="password" id="password" name="password" autocomplete placeholder="请输入您的密码" required>
                    </div>
                    <!-- 注册时显示邮箱相关字段 -->
                    <template v-if="isRegister">
                        <div class="form-group">
                            <label for="email">邮箱</label>
                            <input v-model="loginModel.email" style="width: 50%;" type="email" id="email" name="email" placeholder="请输入您的邮箱" required>
                            <div @click="getEmailCode" class="code">
                                <el-icon v-if="isLoadingStatus" class="loading"><Loading/></el-icon>
                                验证码
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="emailCode">邮箱码</label>
                            <input v-model="loginModel.emailCode" placeholder="请输入邮箱验证码" required>
                        </div>
                    </template>
                    <div class="form-group">
                        <label for="code">验证码</label>
                        <input v-model="loginModel.code" style="width: 50%;" placeholder="请输入验证码" required>
                        <img @click="getCaptcha" width="50%" :src="captchaUrl" alt="">
                    </div>
                    <div class="button-group">
                        <button @click="login" style="width: 100%;" class="login">
                            {{ isRegister ? '注册' : '登录' }}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>
<script setup>
import {  Loading } from '@element-plus/icons-vue';
import { useGetCaptcha, useLogin } from '@/api/layout';
import { onMounted, reactive,ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import {useGetEmailCode} from '@/api/common/index';
import { watch } from 'vue';
const router = useRouter()
const userStore = useUserStore()
// 验证码图片地址
const captchaUrl = ref('')
const isRegister = ref(false)
const getCode =async () => {
    const res =await useGetCaptcha()
    // 接收验证码图片base64
    captchaUrl.value = res.data
}
const getCaptcha = ()=>{
    getCode()
}


watch(()=>isRegister.value, ()=>{
    console.log('变化了')
 // 清空loginModel
    loginModel.username = ''
    loginModel.password = ''
    loginModel.email = ''
    loginModel.emailCode = ''
    loginModel.code = ''
})

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
    
    try {
    const res = await useGetEmailCode(loginModel)
        ElMessage.success(res.msg)
    } catch(err) {
        
    } finally {
        isLoadingStatus.value = false
    }
}
// 效验
const login = async () => {
    try {
        const res = await useLogin(loginModel)
        if (res.code === 200) {
            // 1. 存储 token
            userStore.setToken(res.data.token)
            
            userStore.setUser(res.data.userinfo)
            // 3. 提示成功
            ElMessage.success(res.msg || '登录成功')
            sessionStorage.setItem('token',res.data.token)
            // 4. 跳转到首页
            router.push('/home')
            // 5. 设置ss
            
        } else {
            ElMessage.error(res.msg || '登录失败')
        }
    } catch (error) {
        
    } finally {
        // 重新获取验证码
        getCode()
    }
}
onMounted(() => {
    getCode()
})
</script>

<style scoped lang="scss">
.container {
    font-family: 'Microsoft YaHei', Arial, sans-serif;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0;
    background-image: url('/src/assets/pet.svg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    color: #333;
    height: 100vh;
    width: 100%;
    min-width: 1300px;
}

.login-container {
    display: flex;
    width: 40%;
    background: rgba(255, 255, 255, 0.9);
    height: 500px;
    border-radius: 16px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.image-container {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    padding-left: 20px;
    flex-direction: column;
}

.image-container img {
    max-width: 100%;
    max-height: 100%;
    border-radius: 16px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.form-container {
    flex: 2;
    padding: 30px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

h2 {
    text-align: center;
    margin-bottom: 25px;
    color: #4a4a4a;
    font-size: 24px;
}

.form-group {
    margin-bottom: 20px;
    display: flex;
    align-items: center;

    .code{
        width: 48%;
        height: 40px;
        background-color: #2575fc;
        display: flex;
        justify-content: center;
        align-items: center;
        color: white;
        padding: 10px;
        border-radius:5px;
        cursor: pointer;

        .loading {
            animation: rotate 1s linear infinite;
        }
    }
}
.switch-mode {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
    gap: 20px;
    
    span {
        padding: 5px 20px;
        cursor: pointer;
        border-bottom: 2px solid transparent;
        
        &.active {
            color: #007bff;
            border-bottom-color: #007bff;
        }
        
        &:hover {
            color: #0056b3;
        }
    }
}
.form-group label {
    margin-right: 5px;
    font-weight: bold;
    width: 80px;
}

.form-group input {
    width: calc(100% - 10px);
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 16px;
    box-sizing: border-box;
    transition: border-color 0.3s ease;
}

.form-group input:focus {
    border-color: #2575fc;
    outline: none;
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

/* 新增容器，包含忘记密码和提示词 */
.footer-links {
    display: flex;
    justify-content: flex-start;
    /* 使忘记密码与提示词靠左对齐 */
    margin-top: 15px;
    gap: 10px;
    /* 设置间距 */
}

.forgot-password {
    margin-top: 20px;
    font-size: 14px;
    text-align: left;
}

.forgot-password a {
    color: #007bff;
    text-decoration: none;
}

.forgot-password a:hover {
    text-decoration: underline;
}

/* 提示词样式 */
.hint-text {
    font-size: 10px;
    color: #888;
    text-align: left;
    align-self: flex-end;
    /* 使提示词与按钮垂直对齐 */
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>