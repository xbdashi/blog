import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  const user = ref() 
  const setUser = (title:string)=>{
    user.value = title
  }
  const getUser = ()=>{
    return user.value
  }

  return { user, setUser ,getUser}
},{
    persist: true
})

