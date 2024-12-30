<template>
    <div class="pubCommon main">
        <div>
            <div class="title" style="padding:40px 0;"><h1 style="font-size: 25px;">{{ essay.title }}</h1></div>
        </div>
        <div class="avtor">
            <!-- <div><img :src="essay.user.photo" width="50px"></div> -->
            <div style="margin-right:8px;width:50px;height: 50px;display: flex;justify-content: center;align-items: center;border-radius: 50%;overflow: hidden;">
                <img v-if="essay.user.photo &&essay.user.photo != ''" :src="essay.user.photo" width="60px" alt="">
                <img v-else src="@/assets/avtor.png" width="35px" alt="">
            </div>
            <div>
                <p>{{ essay.user.username }}</p>
                <p style="color: #333; font-size: 12px;"> {{ formatDate(essay.createTime) }} 阅读{{ essay.views}}评论{{ essay.commentNum }}</p>
            </div>
        </div>
        <!-- 内容展示部分 -->
         <div class="content">
            <v-md-preview :text="essay.content"/>
         </div>
         <!-- 结尾卡片展示部分 -->
         <div>
            <el-alert
            title="文章结尾"
            type="success"
            description="End"
            show-icon
            />
         </div>
         <!-- 标签展示 -->
          <div class="tag" style="display: flex;align-items: center;">
            <div style="width: 3px;height: 30px;background-color: rgb(197, 202, 195);"></div>
            &nbsp;&nbsp;标签：
            <el-button class="tag-btn" v-for="item in essay.signsList" >{{ item.label }}</el-button>
          </div>
          <!-- 评论 -->
          <div class="input_remark" style="margin-top: 20px;display: flex;justify-content: start;align-items: start;flex-wrap: wrap;">
            <!-- 头像 -->
            <div class="avtor" style="width: 10%;">
                <div style="width: 50px;height: 50px;display: flex;justify-content: center;align-items: center;border-radius: 50%;overflow: hidden;">
                    <img v-if="user?.photo &&user?.photo != ''" :src="user?.photo" width="60px" alt="">
                    <img v-else src="@/assets/avtor.png" width="35px" alt="">
                </div>
            </div>
            <!-- 评论 -->
            <div style="width: 90%;">
                <el-input
                    v-model="remarkModel.comment"
                    :autosize="{ minRows: 2, maxRows: 2 }"
                    type="textarea"
                    placeholder="发布你精彩的评论"
                />
             </div>
             <div style="margin-top: 10px;width: 100%;display: flex;justify-content: end;">
                <el-button type="primary" :icon="Edit" style="margin-left: 20px;" @click="addRemark">评论</el-button>
             </div>
          </div>
          <!-- 评论内容 -->
           <div class="remark">
                <h4 style="margin-bottom: 10px;">{{ commentList.count }}条评论</h4>
                <!-- 评论内容 -->
                <Comment 
                :commentList="commentList"
                @addLike="addLike"
                @showReplyBox="showReplyBox"
                @cancelReply="cancelReply"
                @submitReply="submitReply"
                />
           </div>
    </div>
</template>
<script setup>
import { useInfoStore } from '@/stores/essayInfo';
import { Edit } from '@element-plus/icons-vue';
import Comment from '@/compoents/Comment.vue';
import { computed, onMounted, reactive, ref, toRef} from 'vue';
import { useAddComment, useGetAllCommentByEssayId, useGetCommentByEssayId } from '@/api/layout';
import { ElNotification } from 'element-plus';
import { useUserStore } from '@/stores/user';
import useTimeFormat from '@/hook/useTimeFormat';
import { useRouter } from 'vue-router';
const router = useRouter()
const {formatDate} = useTimeFormat()
const {user} = useUserStore() // 获取到当前用户的数据
const {getInfo} = useInfoStore()
// 获取当前文章的数据
const essay = toRef(JSON.parse(getInfo()))
// 评论内容
const remarkModel = reactive({
    comment: '',
    essayId: essay.value.id,
    userId:''
})
// 获取到当前文章的评论
const commentList = ref([]);
// 最新的一条
// 添加评论
const addRemark =async ()=>{
    if(remarkModel.comment.length < 1){
        ElNotification({
        title: '错误',
        message: '评论内容不能为空',
        type: 'error',
    })
    return
    }
    if(!user){
        ElNotification({
            title: '错误',
            message: '请先登录后再评论',
            type: 'error',
        })
        router.push('/login')
        return  
    }
    remarkModel.userId = user.id
    const res = await useAddComment(remarkModel)
    if(res.code === 200){
        ElNotification({
        title: '成功',
        message: '评论成功',
        type: 'success',
    })
    remarkModel.comment = ''
    getCommentList(remarkModel.essayId);
    }
}

// 获取所有的评论数量
const getCommentCount = async ()=>{
    const res = await useGetAllCommentByEssayId(remarkModel.essayId)
    if(res.code === 200){
        commentList.value.count = res.data
    }
}
const getCommentList = async ()=>{
    console.log("执行了getCommentList")
    const res = await useGetCommentByEssayId(essay.value.id)
    console.log(res)
    commentList.value = res.data.map(comment => ({
        ...comment,
        showReply: false,
        replyContent: '',
        isLiked: false,
        isDisliked: false,

    }))
    getCommentCount()
} 

// 添加新的响应式数据和方法
const showReplyBox = (item) => {
    console.log(item)
    item.showReply = !item.showReply;
    item.replyContent = '';
}

const cancelReply = (item) => {
    item.showReply = false;
    item.replyContent = '';
}

const submitReply = async (item) => {
    // TODO: 实现回复提交逻辑
    remarkModel.comment = item.replyContent
    console.log('子组件提交过来的评论',item)
    remarkModel.parentId = item.id
    addRemark()
    item.showReply = false;
}

onMounted(()=>{
    getCommentList()
})
</script>
<style scoped>
.main{
    margin: 120px auto;
    width: 80%;
    padding: 0 100px;
    background-color: white;
    padding-bottom: 20px;
}
.tag-btn{
    margin:5px 0 0 5px;
    border-radius: 20px;
    border:1px solid rgb(191, 227, 201);
    color:rgb(95, 184, 120);
    transition: 0.2s all;
}
.tag-btn:hover{
    background-color:rgb(95, 184, 120) ;
    color:white
}

</style>