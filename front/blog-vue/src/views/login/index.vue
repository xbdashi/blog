<template>
    <div class="container">
        <div class="login">
            <h2 style="color: black;margin: 20px 0;">登录</h2>
            <el-form ref="loginFormRef" :rules="rules" :model="loginModel" label-width="auto" style="max-width: 700px">
                <el-form-item prop="username" label="用户名">
                    <el-input placeholder="请输入用户名" v-model="loginModel.username" />
                </el-form-item>
                <el-form-item prop="password" label="密码">
                    <el-input v-model="loginModel.password" type="password" />
                </el-form-item>
                <el-form-item label="验证码">
                    <div style="display: flex;align-items:top;">
                        <div style="width: 40%;"><el-input v-model="loginModel.code" /></div>
                        <div style="color: black; width: 40%;"><img @click="getCaptcha" :src="captchaUrl" alt=""></div>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button style="width: 100%;" type="primary" @click="login">登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<script setup>
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

<style scoped>
.container{
    width: 100%;
    height: 100vh;
    background: url('@/assets/bg.jpg') no-repeat;
    background-size: cover;
    display: flex;
    justify-content: end;
    
    align-items: center;
    .login{
        margin-right: 200px;
        width: 500px;
        height: 400px;
        border-radius: 20px;
        background-color: rgba(255, 255, 255, 0.7);
        color: white;
        text-align: center;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
    }
}

</style>