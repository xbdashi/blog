<template>
<div class="msg-container">
  <!-- 主要内容容器 -->
  <div class="main-box">
    <!-- 导航栏 -->
    <div class="nav">
     <h2 style="color: #333;font-size: 17px;height: auto;line-height: 30px;padding: 10px 0px;"><i class="iconfont icon-zhifeiji"></i> &nbsp;消息中心</h2>
     <ul class="nav-list">
      <li class="activeLi"><span>·</span>我的消息</li>
      <li><span>·</span>回复我的</li>
     </ul>
     <!-- 用户头像功能展示 -->
     <div class="nav-user-box">
         <div class="">
              <el-avatar :src="user.photo" :size="50" style="margin-right: 10px;"></el-avatar>
         </div> 
     </div>
    </div>
    <div class="nav-main">
      <!-- 右边头导航栏 -->
      <div class="main-top-nav">
          {{'我的消息'}}
      </div>
      <!-- 主要内容 -->
    
      <div class="main-content">
        <router-view />
      </div>
    
    </div>
  </div>
</div>
</template>
<script setup>
import { useUserStore } from '@/stores/user';
import Nav from '@/views/layout/header/index.vue'

const {user} = useUserStore()
</script>
<style scoped lang="less">
.msg-container{
  width: 100%;
  height: 100vh;
  min-width: 1200px;
  background: url("/src/assets/messageBg.png") no-repeat;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  .main-box{
    width: 70%;
    height:100vh;
    background-color: rgb(235, 245, 244);
    display: flex;
   


    
    // 侧边栏样式
    .nav{
      position: relative;
      width: 20%;
      text-align: center;
      background-color: white;
      .nav-list{
        li{
          height: 40px;
          line-height: 40px;
          cursor: pointer;
          display: flex;
          justify-content: center;
          align-items: center;
          padding: 10px 0px;
          span{
            font-size: 25px;
            margin:0 10px
          }
        }
        li:hover{
          background-color: #eee;
        }
        .activeLi{
          color: rgb(0, 165, 224);
          font-weight: bold;
        }
      }
      // 头像
      .nav-user-box{
        position: absolute;
        bottom: 10px;
        width: 100%;
        height: auto;
      }
    }
    // 主要的样式
    .nav-main{
      width: 80%;
      display: flex;
      flex-direction: column;
      margin-left: 10px;
      .main-top-nav{
        width: 98%;
        height: 40px;
        background-color:white ;
        padding: 0px 10px ;
        border-radius: 10px;
        margin: 10px 5px;
        display: flex;
        color: #606060;
        justify-content: start;
        align-items: center;
        box-shadow: 0 0 10px rgb(190, 208, 221);
      }
      .main-content {
        width: 98%;
        height: calc(100vh - 80px); // 减去顶部导航的高度
        overflow: hidden; // 防止内容溢出
        
      }
    }
  }
}
</style>