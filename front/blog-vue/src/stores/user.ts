import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { userModndel } from '@/api/userMondel'

export const useUserStore = defineStore('user', () => {
  const user = ref<userModndel>() 
  const setUser = (userMondel:userModndel)=>{
    user.value = userMondel
  }
  const getUser = ()=>{
    return user.value
  }

  return { user, setUser ,getUser}
},{
    persist: true
})

