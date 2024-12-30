
export default function useTimeFormat(){
// 获取当前文章对应的评论
const formatDate = (dateString:string) => {
    const date = new Date(dateString)
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
  }
  return {
    formatDate
  }
}