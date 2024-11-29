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
        <div class="search">
          <el-input placeholder="请输入搜索内容" v-model="search" clearable style="width: 200px;"/>
          <el-button type="primary" :icon="Search" @click="searchBtn">搜索</el-button>
        </div>
        <div class="login">
            <div>
              <img v-if="user.photo && user.photo != ''" :src="user.photo" width="35px" alt="">
              <img v-else src="@/assets/avtor.png" width="35px" alt="">
            </div>
            <div>
              <el-dropdown trigger="click">
                <span class="el-dropdown-link" style="padding: 0 20px;">
                  {{user.username}}<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item :icon="User">个人空间</el-dropdown-item>
                    <el-dropdown-item @click="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
            </el-dropdown>
            </div>
        </div>
      </header>
    </div>
  </template>
  
<script setup>
import { Close, Search, SwitchButton, User } from '@element-plus/icons-vue';
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
  
  <style scoped>
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
   

    div:nth-child(2){
      cursor: pointer;

    }
  }
  </style>