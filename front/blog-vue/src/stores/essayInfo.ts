import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useInfoStore = defineStore('essayInfo', () => {
  const into = ref('') 
  const setInfo = (item:string)=>{
    into.value = item
  }
  const getInfo = ()=>{
    return into.value
  }
  return {into, setInfo ,getInfo};
},{
  persist: true,
})
