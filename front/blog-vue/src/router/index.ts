import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'layout',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/layout/index.vue'),
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
    
    }
  ],
})

export default router
