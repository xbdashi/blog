<template>
<div class="remark_card" v-for="(item,index) in commentList">
                    <!-- 当前用户的头像和 -->
                    <div class="avtor">
                         <!-- <div><img :src="essay.user.photo" width="50px"></div> -->
                         <div style="margin-right:8px;width:50px;height: 50px;display: flex;justify-content: center;align-items: center;border-radius: 50%;overflow: hidden;">
                            <img v-if="item.user.photo &&item.user.photo != ''" :src="item.user.photo" width="60px" alt="">
                            <img v-else src="@/assets/avtor.png" width="35px" alt="">
                         </div>
                        <div>
                            <p>{{ item.user.username }} <span style="color: #333;font-size: 12px;">{{ index+1 }}楼</span></p>
                            <p style="color: #333; font-size: 12px;"> {{ formatDate(item.createTime) }}</p>
                        </div>
                    </div>
                    <div class="content">
                        <!-- 通过parentId查询到对应的评论用户 -->
                        <div><span><!-- {{ queryByParentId(item.parentId).data }} --></span>{{item.comment}}</div>
                        <div style="text-align: right;">
                            <span class="reply-btn" @click="showReplyBox(item)">回复</span>
                        </div>
                    </div>
                    <!-- 添加回复框 -->
                    <div v-if="item.showReply" class="reply-box">
                        <el-input
                            v-model="item.replyContent"
                            :autosize="{ minRows: 2, maxRows: 2 }"
                            type="textarea"
                            :placeholder="'回复@'+item.user.username"
                        />
                        <div class="reply-actions">
                            <el-button size="small" @click="cancelReply(item)">取消</el-button>
                            <el-button type="primary" size="small" @click="submitReply(item)">评论</el-button>
                        </div>
                    </div>
                    <Comment 
                    :commentList="item.children"
                     v-if="item.children.length > 0"
                     @addLike="addLike"
                     @showReplyBox="showReplyBox"
                     @cancelReply="cancelReply"
                     @submitReply="submitReply"
                     />
</div>
<!-- 添加展开更多按钮 -->
<div v-if="commentList && commentList.length > initialDisplayCount" class="show-more">
    <el-button 
        type="text" 
        @click="toggleShowMore"
    >
        {{ showAll ? '收起' : `展开更多评论 (${commentList.length - initialDisplayCount})` }}
    </el-button>
</div>
</template>

<script setup lang="ts">
import useTimeFormat from '@/hook/useTimeFormat';
import Comment from '@/compoents/Comment.vue';
const { formatDate } = useTimeFormat();
import { onMounted ,ref, watch,computed} from 'vue';
import { useGetUsernameByParentId } from '@/api/layout';
// 接收父组件传来的方法
const emit = defineEmits(['addLike', 'showReplyBox','cancelReply','submitReply'])
const addLike = (item:comment)=>{
    emit('addLike',item)
}
const showReplyBox = (item:comment)=>{
    // 点击之后将当前评论的showReply设置为false
    emit('showReplyBox',item)
}
const cancelReply = (item:comment)=>{
    emit('cancelReply',item)
}
const submitReply = (item:comment)=>{
    emit('submitReply',item)
}
const props = defineProps({
    commentList:Array<comment>
})
watch(()=>props.commentList, ()=>{
   // 修改list里面的内容，如果又parentid 就根据id查询对应的用户，在评论前面加（对应的parent名称）@
   props.commentList?.forEach( async item=>{
    if(item.parentId){
        // 查询对应的评论用户
        let username = await useGetUsernameByParentId(item.parentId)
        
        // 将parentUser的name添加到item的content前面
        item.comment = `回复@${username.data}:${item.comment}`
    }
   })
},{immediate:true})
// 通过parentId查询到对应的评论用户
/* const queryByParentId = async(parentId: string | number) => { 
    if(parentId != null){
        let res = await useGetUsernameByParentId(parentId.toString())
        console.log("对应的",res)
        return res.data
    }
} */
interface comment{
    user:{
        photo:string,
        username:string
    },
    createTime:string,
    comment:string,
    isLiked:boolean,
    likes:number,
    showReply:boolean,
    replyContent:string,
    parentId:string,
    children:Array<comment>,
    id:string
}
const initialDisplayCount = 3; // 初始显示的评论数
const showAll = ref(false);

// 计算要显示的评论列表
const displayedComments = computed(() => {
    if (!props.commentList) return [];
    return showAll.value 
        ? props.commentList 
        : props.commentList.slice(0, initialDisplayCount);
});

// 切换显示全部/收起
const toggleShowMore = () => {
    showAll.value = !showAll.value;
};

</script>
<style scoped>
.avtor{
        display: flex;
        width: 300px;
        align-items: center;

        div:nth-child(1){
            width: 20%;
            border-radius: 50px;
            overflow: hidden;
          
        }
        div:last-child{
            width: 80%;
        
        }
    }
.remark_card{
    padding: 20px 0;
    border-bottom: 1px solid #ccc;

    .content{
        display: flex;
        justify-content: space-between;
    
        div:nth-child(1){
            width:55%;
            cursor: pointer;
        }
        div:nth-child(2){
            width: 15%;
            cursor: pointer;
            transition: 0.3s all;
            .like{
                color: red;
            }
        }
    }
}
.reply-btn {
    cursor: pointer;
    color: #409EFF;
    font-size: 14px;
}

.reply-btn:hover {
    color: #66b1ff;
}

.reply-box {
    margin: 10px 0 10px 50px;
}

.reply-actions {
    margin-top: 10px;
    text-align: right;
}

.like {
    color: #ff4757 !important;
}

.dislike {
    color: #2f3542 !important;
}

.remark_card .content div:nth-child(2) i {
    cursor: pointer;
    transition: 0.3s all;
}

.remark_card .content div:nth-child(2) i:hover {
    transform: scale(1.1);
}
</style>