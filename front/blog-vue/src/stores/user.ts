import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { User } from '@/api/model/model'


export const useUserStore = defineStore('user', () => {
  const user = ref<User|null>()
  const token = ref<string|null>(null)

  // 设置 token
  const setToken = (newToken: string) => {
    token.value = newToken
    sessionStorage.setItem('token', newToken)
  }

  // 获取 token
  const getToken = () => {
    return token.value || sessionStorage.getItem('token')
  }

  const setUser = (userMondel: User|null) => {
    user.value = userMondel
  }

  const removeUser = () => {
    user.value = null
    token.value = null
    sessionStorage.removeItem('token')
    localStorage.clear()
  }
  const addFocus = (focus :string)=>{
    user.value!.focus = focus
  }
  const getUser = ()=>{
    return user.value
  }

  return { 
    user,
    token,
    setToken,
    getToken,
    addFocus,

    setUser,
    getUser,
    removeUser
  }
}, {
  persist: true
})

