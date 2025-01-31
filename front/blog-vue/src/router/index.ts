import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('/src/views/login/index.vue')
    },
    {
      path: '/forget',
      name: 'forget',
      component: () => import('@/views/login/forgetPwd.vue')
    },
    {
      path: '/',
      name: 'layout',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/layout/index.vue'),
      redirect:'/home',
      children:[
        {
          path: '/home',
          name: 'home',
          component: () => import('@/views/layout/main/home.vue'),
        },
        {
          path: '/categories',
          name: 'categories',
          component: () => import('@/views/layout/main/categories.vue'),
        },
        {
          path: '/tags',
          name: 'tags',
          component: () => import('@/views/layout/main/sign.vue'),
        },
        {
          path: '/guestbook',
          name: 'guestbook',
          component: () => import('@/views/layout/main/massage.vue'),
        },
        {
          path: '/add',
          name: 'add',
          component: () => import('@/views/layout/main/addEssay.vue'),
        },
        {
          path: '/info',
          name: 'info',
          component: () => import('@/views/layout/main/essayInfo.vue'),
        },
      ]
    },
    {
      path:'/userinfo',
      name:'userInfo',
      component:()=>import('@/views/userInfo/layout.vue'),
    },
    {
      path:'/message',
      name:'message',
      component:()=>import('@/views/message/index.vue'),
      redirect:'/message/private',
      children:[
        {
          path:'private',
          name:'private',
          component:()=>import('@/views/message/private/index.vue'),
        }
      ]
    },
  ],
})

export default router
