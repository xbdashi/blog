<template>
    <div class="container">
        <div class="login-card">
            <h2 class="title">欢迎登录</h2>
            <el-form ref="loginFormRef" :rules="rules" :model="loginModel" label-width="0" class="login-form">
                <el-form-item prop="username">
                    <el-input 
                        placeholder="请输入用户名" 
                        v-model="loginModel.username"
                        prefix-icon="User"
                    />
                </el-form-item>
                <el-form-item prop="password">
                    <el-input 
                        v-model="loginModel.password" 
                        type="password"
                        placeholder="请输入密码"
                        prefix-icon="Lock" 
                        show-password
                    />
                </el-form-item>
                <el-form-item>
                    <div class="captcha-container">
                        <el-input 
                            v-model="loginModel.code"
                            placeholder="请输入验证码"
                            prefix-icon="Key"
                        />
                        <img @click="getCaptcha" :src="captchaUrl" alt="验证码" class="captcha-img">
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" class="login-btn" @click="login">登 录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<script setup>
import { User, Lock, Key } from '@element-plus/icons-vue'
import { useGetCaptcha, useLogin } from '@/api/layout';
import { onMounted, reactive,ref } from 'vue'
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
const {setUser}=useUserStore()
const router = useRouter()
// 验证码图片地址
const captchaUrl = ref('')
const loginFormRef = ref(null)
const rules = reactive({
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
})

const getCode =async () => {
    const res =await useGetCaptcha()
    // 接收验证码图片base64
    captchaUrl.value = res.data
    console.log(res)
}
const getCaptcha = ()=>{
    getCode()
}
const loginModel = reactive({
    username: '',
    password: '',
    code:''
})
// 效验
const login = ()=>{
    loginFormRef.value.validate(async valid => {
        if (valid) {
            const res = await useLogin(loginModel)
            if(res.code == 200){
                ElMessage.success('登录成功')
                // 存入sessionStorage
                sessionStorage.setItem('token',res.data.token)
                // 将当前用户信息存入pinia
                setUser(res.data.user)
                router.push('/home')
            }else{
                ElMessage.error(res.message)
            }
        } 
   });
}
onMounted(() => {
    getCode()
})
</script>

<style scoped lang="scss">
.container {
    width: 100%;
    height: 100vh;
    background: url('@/assets/bg.jpg') no-repeat center;
    background-size: cover;
    display: flex;
    justify-content: center;
    align-items: center;
    
    .login-card {
        width: 420px;
        padding: 40px;
        border-radius: 16px;
        background: rgba(255, 255, 255, 0.9);
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        backdrop-filter: blur(10px);
        
        .title {
            color: #303133;
            font-size: 28px;
            margin-bottom: 40px;
            text-align: center;
            font-weight: 600;
        }
        
        .login-form {
            :deep(.el-input) {
                --el-input-height: 45px;
                
                .el-input__wrapper {
                    border-radius: 8px;
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
                }
            }
            
            .captcha-container {
                display: flex;
                gap: 12px;
                align-items: center;
                
                .el-input {
                    flex: 1;
                }
                
                .captcha-img {
                    height: 45px;
                    border-radius: 8px;
                    cursor: pointer;
                    transition: transform 0.2s;
                    
                    &:hover {
                        transform: scale(1.02);
                    }
                }
            }
            
            .login-btn {
                width: 100%;
                height: 45px;
                font-size: 16px;
                border-radius: 8px;
                margin-top: 20px;
                
                &:hover {
                    transform: translateY(-1px);
                    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
                }
            }
        }
    }
}
</style>