<template>
    <div class="contain">
      <header>
        <div class="logo">
          <img src="@/assets/logo.png" height="70">
        </div>
        <div class="nav">
          <nav>
            <ul ref="navbarTextRef" style="display:flex;">
              <li><a @click="active('home')" :class="{ active: activeNav === '/home' }">首页</a></li>
              <li><a @click="active('categories')"  :class="{ active: activeNav === '/categories' }">文章分类</a></li>
              <li><a @click="active('tags')"  :class="{ active: activeNav === '/tags' }">标签</a></li>
              <li><a @click="active('guestbook')" :class="{ active: activeNav === '/guestbook' }">留言板</a></li>
            </ul>
          </nav>
        </div>
        <div class="login">
          <div style="margin-right: 55px;">
            <img v-if="user?.photo && user?.photo != ''" :src='user.photo ? user.photo :"@/assets/header.jpg"' width="35px" alt="">
            <img v-else src="@/assets/avtor.png" alt="">
          </div>
          <!-- 鼠标悬浮弹出菜单 -->
          <div class="drawer">
              <div class="user-info">
                <div class="img">
                  <img v-if="user?.photo && user?.photo != ''" :src='user.photo ? user.photo :"@/assets/header.jpg"' alt="">
                  <img v-else src="@/assets/avtor.png" alt="">
                </div>
                <div class="username">{{user?.username ? user?.username : '彭于晏'}}</div>
              </div>
              
              <div class="stats">
                <div class="stat-item">
                  <div class="stat-num">{{user?.fans ? user?.fans : '0'}}</div>
                  <div class="stat-label">粉丝</div>
                </div>
                <div class="stat-item">
                  <div class="stat-num">{{user?.focus ? user?.focus : '0'}}</div>
                  <div class="stat-label">关注</div>
                </div>
                <div class="stat-item">
                  <div class="stat-num">{{user?.likes ? user?.likes : '0'}}</div>
                  <div class="stat-label">获赞</div>
                </div>
              </div>
              
              <div class="menu-items">
                <div class="menu-item" @click="toPersonal"><i class="iconfont icon-yonghu"></i>个人中心</div>
                <div class="menu-item" @click="toContent"><i class="iconfont icon-neirong"></i>内容管理</div>
                <div class="menu-item" @click="toMessage"><i class="iconfont icon-xiaoxi"></i>消息列表</div>
                <div class="menu-item logout" @click="logout"><i class="iconfont icon-guanbi"></i>退出</div>
              </div>
            </div>
        </div>
      </header>
    </div>
  </template>
  
<script setup>
import { ref, watch } from 'vue';
import { useRouter,useRoute } from 'vue-router';
import {useTitleStore} from '@/stores/search'
import { useUserStore } from '@/stores/user';
const {user} = useUserStore()
const titleStore = useTitleStore();

  const router = useRouter();
  const route = useRoute();
  const activeNav = ref();

  watch(()=>route.path, ()=>{
    activeNav.value = route.path;
  },{immediate: true})

  const search = ref('');

  const searchBtn = ()=>{
    titleStore.setTitle(search.value)
  }
  const active = (path) => {
    activeNav.value = path;
    router.push('/'+path)
  };
  
const logout = ()=>{
  router.push('/login')
}
  </script>
  
<style scoped lang="less">
  header {
    box-shadow: 2px 2px 7px 1px rgba(0, 0, 0, 0.3);
    display: flex;
    background-color: white;
    width: 100%;
    position: fixed;
    top: 0;
    z-index: 10;
    height: 70px;
    justify-content: space-between;
    align-items: center;
    .nav {
      display: flex;
      a {
        color: #333;
        display: block;
        padding: 15px 20px;
        transition: 0.1s all;
        cursor: pointer;
      }
      a.active {
        color: rgb(95, 184, 120);
        border-bottom: 4px solid rgb(95, 184, 120);
      }
      a:hover {
        color: rgb(95, 184, 120);
        border-bottom: 4px solid rgb(95, 184, 120);
        box-sizing: border-box;
      }
    }
  }
.login{
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    .drawer{
      position: absolute;
      top: 40px;
      right: 0;
      width: 200px;
      height:0px;
      border-radius: 10px;
      background-color: white;
      // 展开的时候1s 关闭的时候0.3s
      transition-property: all;
      transition-duration: 0.3s, 1s;  
      overflow: hidden;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      .user-info {
    width: 100%;
    text-align: center;
    margin-bottom: 16px;
    
    .img {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      overflow: hidden;
      margin: 0 auto 8px;
     
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .username {
      font-weight: 600;
      font-size: 16px;
    }
  }

  .stats {
    width: 100%;
    display: flex;
    justify-content: space-around;
    padding: 12px 0;
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;
    margin-bottom: 16px;

    .stat-item {
      text-align: center;
      
      .stat-num {
        font-weight: 600;
        color: rgb(95, 184, 120);
      }
      
      .stat-label {
        font-size: 12px;
        color: #666;
      }
    }
  }

  .menu-items {
    width: 100%;
    .menu-item {
      padding: 12px 16px;
      cursor: pointer;
      transition: all 0.3s;
      border-radius: 6px;
      margin-bottom: 4px;
      
      i {
        margin-right: 8px;
      }
      
      &:hover {
        background-color: rgba(95, 184, 120, 0.1);
        color: rgb(95, 184, 120);
      }
      &.logout {
        color: #ff4d4f;
        &:hover {
          background-color: rgba(255, 77, 79, 0.1);
        }
      }
    }
  }
}

}
  .login:hover .drawer {
  height: 380px;
  border: 2px solid rgb(95, 184, 120);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
  </style>