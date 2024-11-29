import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useTitleStore = defineStore('title', () => {
  const titleCurrent = ref('') 
  const setTitle = (title:string)=>{
    titleCurrent.value = title
  }
  const getTitle = ()=>{
    return titleCurrent.value
  }

  return { titleCurrent, setTitle ,getTitle}
})
