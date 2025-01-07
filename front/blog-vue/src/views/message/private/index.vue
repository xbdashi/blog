<template>
    <div class="private-container">
        <div class="main">
            <!-- 好友列表 -->
            <div class="friend-list">
                <div class="latest" style="color: red; font-size: 13px;margin-bottom: 10px;">近期消息</div>
                <!-- 好友列表 -->
                <div class="friend" @click="">
                    <!-- 头像 -->
                    <div class="avtor">
                        <el-avatar :size="40" :src="userAvatar" />
                    </div>
                    <!-- 用户名和最后一条消息 -->
                    <div class="message-latest">
                        <div class="neckname">{{ '彭于晏' }}</div>
                        <div class="message-latest">
                            {{ messageList.length > 0 ? messageList[messageList.length - 1].content : '' }}
                        </div>
                    </div>
                </div>
            </div>
            <!-- 主要内容 -->
            <div class="private-friend">
                <div class="neckname">{{ '彭于晏' }}</div>
                <div class="message-content">
                    <!-- 接收的消息 -->
                    <div v-for="(msg, index) in messageList" :key="index" 
                         :class="['message-item', msg.type == 1 ? 'receive' : 'send']">
                        <el-avatar v-if="msg.type == 1" :size="30" :src="userAvatar" />
                        <div class="message-bubble">{{ msg.content }}
                        </div>
                        
                        <el-avatar v-if="msg.type == 0 " :size="30" :src="userAvatar" />
                        
                    </div>
                </div>
                <div class="input-area">
                    <el-input
                        v-model="inputMessage"
                        type="textarea"
                        :rows="4"
                        placeholder="请输入消息..."
                        @keyup.enter.native.prevent="sendMessage"
                    />
                    
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref,onUnmounted,onMounted } from 'vue'
import userAvatar from '@/assets/header.jpg'
import type { User } from '@/api/model/user';
interface Chat {
  senderId: string;
  receiverId: string;
  content: string;
  msgType: string | number; // 0 - 'TEXT',1- 'IMAGE',2-'VIDEO',3-'FILE'
  status: string | number; // 0-'SENT' 已经发送,1-'READ' 已读
  type: string | number; // 0 - send(发送) ，1receive 接收
  create_time: string; // 或者使用 string，具体取决于你的后端数据格式
}

interface ChatVo extends Chat{
    senderUser?: User | null,
    receiverUser?: User | null,
}
/**
 * 规定的json格式为
 * {
 *  senderId: 'currentUserId', // 应该是当前用户的实际ID
            receiverId: 'targetUserId', // 应该是目标用户的实际ID
            content: inputMessage.value.trim(),
            msgType: 0,
            status: 0,
            type: 0,
            create_time: new Date().toISOString()
 * }
 * 
 */
// 添加时间格式化函数
const formatTime = (date: Date): string => {
    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}
// 测试数据
const messageList = ref<ChatVo[]>([
    {
        senderId: "user1",
        receiverId: "user2",
        senderUser: null,
        receiverUser: null,
        content: "你好，最近在忙什么？",
        msgType: "0", // TEXT
        status: "0", // SENT
        type: "1", // receive
        create_time: ''
    },
    {
        senderId: "user2",
        receiverId: "user1",
        senderUser: null,
        receiverUser: null,
        content: "我在学习Vue和TypeScript，你呢？",
        msgType: "0", // TEXT
        status: "1", // READ
        type: "0", // send
        create_time: ''
    },
    {
        senderId: "user1",
        receiverId: "user2",
        senderUser: null,
        receiverUser: null,
        content: "我也在学习前端开发，感觉很有趣！",
        msgType: "0", // TEXT
        status: "0", // SENT
        type: "1", // receive
       create_time: ''
    },
    {
        senderId: "user2",
        receiverId: "user1",
        senderUser: null,
        receiverUser: null,
        content: "是的，开发聊天界面确实很有意思。这条消息故意写得长一点，测试一下样式会不会自动换行。",
        msgType: "0", // TEXT
        status: "0", // SENT
        type: "0", // send
        create_time: ''
    }
])

const inputMessage = ref('')

// WebSocket connection
const ws = ref<WebSocket | null>(null)
const wsUrl = 'ws://your-websocket-server-url' // Replace with your actual WebSocket server URL

// 连接websocket
const initWebSocket = () => {
    ws.value = new WebSocket(wsUrl)
    
    ws.value.onopen = () => {
        console.log('WebSocket connected')
    }
    
    ws.value.onmessage = (event) => {
        const message = JSON.parse(event.data)
        messageList.value.push({
            senderId: 'string', // 发送者的id
            receiverId: '',  // 当前用户id
            content: 'message',
            msgType: 0, // 0 - 'TEXT',1- 'IMAGE',2-'VIDEO',3-'FILE'
            status: 0, // 0-'SENT' 已经发送,1-'READ' 已读
            type: 1, // 0 - send(发送) ，1receive 接收
            create_time: 'Date'// 或者使用 string，具体取决于你的后端数据格式
        })
    }
    
    ws.value.onclose = () => {
        console.log('WebSocket disconnected')
    }
    
    ws.value.onerror = (error) => {
        console.error('WebSocket error:', error)
    }
}

// 发送消息
const sendMessage = () => {
    if (inputMessage.value.trim() && ws.value?.readyState === WebSocket.OPEN) {
        const message = {
            senderId: 'currentUserId', // 应该是当前用户的实际ID
            receiverId: 'targetUserId', // 应该是目标用户的实际ID
            content: inputMessage.value.trim(),
            msgType: 0,
            status: 0,
            type: 0,
            create_time: new Date().toISOString()
        }
        ws.value.send(JSON.stringify(message))
        messageList.value.push(message)
        inputMessage.value = ''
    }
}

// 初始化连接websocket
onMounted(() => {
    initWebSocket()
})

// 组件销毁的时候关闭
onUnmounted(() => {
    ws.value?.close()
})
</script>

<style scoped lang="less">
.private-container {
    width: 98%;
    max-width: 100%;
    background-color: white;
    height: auto;
    margin-left: 10px;
    padding: 10px;
    border-radius: 5px;
    box-shadow: 0 0 10px rgb(190, 208, 221);

    .main{
        display: flex;
    }
    .friend-list{
        width: 20%;
        min-width: 230px;
        overflow: auto;
        .friend{
            width: 100%;
            padding: 10px 5px;
            height: auto;
            overflow-y: auto;
            display: flex;
            overflow-x: hidden;
            cursor: pointer;
            transition: 0.3s all;
            .avtor{
                width: 20%;
            }
            .message-latest{
                width:80%;
                display: flex;
                flex-direction: column;
                text-overflow: ellipsis;  
                .message-latest {
                    white-space: nowrap;
                    text-overflow: ellipsis;
                }
            }
        }
    }
    .private-friend{
        width: 80%;
        display: flex;
        flex-direction: column;
        height: calc(100vh - 80px); // 设置整体高度，减去容器的padding
    
        .neckname{
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .message-content {
            flex: 1;
            padding: 20px;
            overflow: auto;
            
            .message-item {
                display: flex;
                align-items: flex-start;
                margin-bottom: 20px;
                margin-top: 10px;
                .message-time {
                font-size: 12px;
                color: #999;
                margin: 0 5px;
                align-self: flex-end;
            }
                .message-bubble {
                    max-width: 60%;
                    padding: 10px 15px;
                    border-radius: 10px;
                    margin: 0 10px;
                    word-break: break-word;
                }
            }

            .receive {
                justify-content: flex-start;
                .message-bubble {
                    background-color: #f0f0f0;
                }
            }

            .send {
                justify-content: flex-end;
                .message-bubble {
                    background-color: #95ec69;
                }
            }
        }

        .input-area {
            padding: 20px;
            border-top: 1px solid #eee;
            
            .el-button {
                margin-top: 10px;
                float: right;
            }
        }
    }
}

.friend:hover{
    background-color: rgb(204, 201, 201);
}
</style>