<template>
    <div style="margin-top: 130px;">
      <h1>留言板</h1>
      <div class="container">
        <div class="input-section">
          <div class="input-area">
            <div class="user-info">
              <img :src="user?.photo || '/src/assets/pet.svg'" alt="用户头像">
              <div>
                <span>{{ user?.username }}</span>
                <div class="hint">分享你的想法...</div>
              </div>
            </div>
            <textarea 
              v-model="messageInput" 
              rows="10" 
              placeholder="在这里输入您的留言..."
            ></textarea>
            <button @click="submitMessage">提交留言</button>
          </div>
        </div>
        <div class="message-board" @scroll="handleScroll" ref="messageContainer">
          <h2>留言列表</h2>
          <div class="divider"></div>
          <div class="message-container">
            
            <div v-for="message in messages" :key="message.id" class="message">
              
              <div class="message-info">
                <img :src="message.user?.photo || '/src/assets/pet.svg'" alt="用户头像">
                <span>{{ message.user?.username }}</span>
                <span class="timestamp">{{ formatDate1(message.createTime) }}</span>
              </div>
              <div>{{ message.content }}</div>
            </div>
            <div v-if="loading" class="loading">加载中...</div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { UserMessage } from '@/api/model/message/MessageMondel';
import { useGetMessage ,useAddMessage} from '@/api/message';
import { useUserStore } from '@/stores/user';
import type {MessagePage,Message} from '@/api/message' ;
import { ElNotification } from 'element-plus';
import Filter from "bad-words";
import router from '@/router';
const {user} = useUserStore()
  // 响应式数据
const messageInput = ref('')
const messages = ref<UserMessage[]>([])
const formatDate = (date: Date) => {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}


const formatDate1 = (dateStr: string) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  if (isNaN(date.getTime())) return ''; // Return empty string if invalid date
  
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  });
}

  // 提交留言方法
const submitMessage =async () => {
  if(!user){
        ElNotification({
            title: '错误',
            message: '请先登录后再评论',
            type: 'error',
        })
        router.push('/login')
        return  
    }
    if (messageInput.value) {
      const newMessage: Message = {
        userId: user.id ,
        content: messageInput.value
      };
      // 将新消息添加到数组开头
      const res  =await useAddMessage(newMessage)
      if(res.code === 200){
        ElNotification({
            title: '成功',
            message: '留言成功',
            type: 'success',
        })
        // 如果是新增的 重新new一个page对象 查询1 的数据
          const newPage = ref({
          pageNum: 1,
          pageSize: 1
        })


        const res = await useGetMessage(newPage.value)
        messages.value.unshift(res.data.rows[0])
      }
      // 清空输入
      messageInput.value = ''
    } else {
      alert('请填写留言内容')
    }
  }
const messageContainer = ref<HTMLElement | null>(null)
const loading = ref(false)
const hasMore = ref(true)
const page = ref({
  pageNum: 1,
  pageSize: 5
})

const getMesessage = async () => {
  if (loading.value || !hasMore.value) return
  
  loading.value = true
  try {
    const res = await useGetMessage(page.value)
    if (res.data.rows.length < page.value.pageSize) {
      hasMore.value = false
    }
    // 追加新消息而不是替换
    messages.value = [...messages.value, ...res.data.rows]
  } finally {
    loading.value = false
  }
}

const handleScroll = async (e: Event) => {
  const target = e.target as HTMLElement
  const scrollBottom = target.scrollHeight - target.scrollTop - target.clientHeight
  
  if (scrollBottom < 50 && !loading.value && hasMore.value) {
    page.value.pageNum++
    await getMesessage()
  }
}

onMounted(() => {
  getMesessage()
})
</script>

<style scoped >
.container {
     display: flex;
     width: 100%;
     max-width: 1000px;
     margin: 20px auto;
     /* 居中容器 */
     background: rgba(255, 255, 255, 0);
     /* 容器白色背景，100%透明 */
     border-radius: 8px;
     /* 圆角 */
     overflow: hidden;
     /* 防止内容溢出 */
     gap: 10px;
     /* 添加间距 */
     box-shadow: none;
     /* 移除整体阴影 */
 }

 .input-section,
 .message-board {
     box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
     /* 添加单独的阴影 */
     border-radius: 8px;
     /* 圆角 */
     background: #fff;
     /* 背景颜色 */
 }

 .input-section {
     width: 250px;
     padding: 20px;
     margin: 0;
     /* 重置 margin */
     height: 300px;
     background: rgba(255, 255, 255, 0.7);
     margin-right: 20px;
 }

 .input-area {
     display: flex;
     flex-direction: column;
     height: 100%;
     /* 使输入区域占满整个高度 */
 }

 .user-info {
     display: flex;
     align-items: flex-start;
     /* 垂直顶部对齐 */
     margin-bottom: 10px;
     /* 用户信息与输入框之间的间距 */
 }

 .user-info img {
     width: 40px;
     /* 头像宽度 */
     height: 40px;
     /* 头像高度 */
     border-radius: 50%;
     /* 圆形头像 */
     margin-right: 10px;
     /* 头像与用户名之间的间距 */
 }

 .user-info span {
     font-size: 18px;
     /* 用户名字体大小 */
     margin-bottom: 5px;
     /* 用户名与小字之间的间距 */
     font-weight: bold;
     /* 加粗用户名 */
 }

 .user-info .hint {
     font-size: 12px;
     /* 小字字体大小 */
     color: #888;
     /* 小字颜色 */
 }

 .message-board {
     width: 600px;
     padding: 20px;
     height: 550px;
    
     /* 启用垂直滚动条 */
     overflow-x: hidden;
     /* 禁用横向滚动条 */
     background: rgba(249, 249, 249, 0.7);
     /* 留言框背景颜色 */
     margin: 0;
     /* 重置 margin */
     box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
     /* 独立阴影 */
     border-radius: 8px;
     /* 圆角 */
     margin-left: 20px;
 }


 .message-board h2 {
     margin: 0 0 15px;
     /* 标题上下间距 */
     font-size: 20px;
     /* 标题字体 大小 */
     color: #333;
     /* 标题颜色 */
 }

 .divider {
     border-top: 1px solid #ddd;
     /* 分割线 */
     margin: 10px 0;
     /* 分割线上下间距 */
 }

 .message {
     border: 1px solid #ddd;
     /* 边框 */
     border-radius: 8px;
     /* 圆角 */
     padding: 15px;
     /* 内边距 */
     margin-bottom: 15px;
     /* 留言之间的间距 */
     background: #fff;
     /* 留言背景颜色 */
     box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
     /* 阴影效果 */
     word-wrap: break-word;
     /* 长内容换行，避免溢出 */
     overflow-wrap: break-word;
     /* 确保文本自动换行 */
     width: 100%;
 }

 .message:hover {
     box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
     /* 悬停时的阴影效果 */
 }

 .message .message-info {
     display: flex;
     align-items: center;
     /* 垂直居中 */
     margin-bottom: 5px;
     /* 留言信息与内容之间的间距 */
 }

 .message .message-info img {
     width: 30px;
     /* 留言用户头像宽度 */
     height: 30px;
     /* 留言用户头像高度 */
     border-radius: 50%;
     /* 圆形头像 */
     margin-right: 10px;
     /* 头像与用户名之间的间距 */
 }

 .message .timestamp {
     font-size: 12px;
     /* 时间字体大小 */
     color: #888;
     /* 时间颜色 */
     margin-left: auto;
     /* 右对齐 */
 }

 textarea {
     width: 100%;
     box-sizing: border-box;
     margin-bottom: 10px;
     /* 输入框与按钮之间的间距 */
     padding: 10px;
     /* 内边距 */
     border: 1px solid #ccc;
     /* 边框 */
     border-radius: 4px;
     /* 圆角 */
     resize: none;
     /* 禁止调整大小 */
     font-size: 16px;
     /* 字体大小 */
 }

 textarea:focus,
 input:focus {
     outline: none;
     /* 移除聚焦时的黑色边框 */
     border: 1px solid #ccc;
     /* 保持正常边框样式 */
 }


 button {
     width: 100%;
     /* 按钮宽度与输入框一致 */
     padding: 10px;
     /* 内边距 */
     background-color: #28a745;
     /* 按钮背景颜色 */
     color: white;
     /* 按钮字体颜色 */
     border: none;
     /* 去掉边框 */
     border-radius: 4px;
     /* 圆角 */
     font-size: 16px;
     /* 字体大小 */
     cursor: pointer;
     /* 鼠标指针 */
     transition: background-color 0.3s;
     /* 背景颜色过渡效果 */
 }

 button:hover {
     background-color: #218838;
     /* 悬停时的背景颜色 */
 }

 h1 {
     text-align: center;
     /* 标题居中 */
     color: #333;
     /* 标题颜色 */
     margin: 20px 0;
     /* 标题上下间距 */
     font-size: 24px;
     /* 标题字体大小 */
     text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
     /* 标题阴影效果 
     */
 }

.loading {
  text-align: center;
  padding: 10px;
  color: #666;
}

</style>@/api/model/message@/api/model/message