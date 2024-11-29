<template>
    <div class="pubCommon main">
        <div>
            <div class="title" style="padding:40px 0;"><h1 style="font-size: 25px;">{{ essay.title }}</h1></div>
        </div>
        <div class="avtor">
            <!-- <div><img :src="essay.user.photo" width="50px"></div> -->
            <div><img src="@/assets/avtor.png" width="50px"></div>
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
                <div><img src="@/assets/avtor.png" width="50px"></div>
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
                <h4 style="margin-bottom: 10px;">{{ essay.commentNum }}条评论</h4>
                <div class="remark_card" v-for="item in commentList">
                    <!-- 当前用户的头像和 -->
                    <div class="avtor">
                         <!-- <div><img :src="essay.user.photo" width="50px"></div> -->
                        <div><img src="@/assets/avtor.png" width="50px"></div>
                        <div>
                            <p>{{ item.user.username }}</p>
                            <p style="color: #333; font-size: 12px;"> {{ formatDate(item.createTime) }}</p>
                        </div>
                    </div>
                    <div class="content">
                        <div>{{item.comment}}</div>
                        <div>
                            <i class="iconfont icon-heart-fill" @click="addLike"></i><span>{{ item.likes }}</span>&nbsp;&nbsp;
                            <i class="iconfont icon-xinsui"  @click="addLike"></i><span>{{ item.disLikes }}</span>
                        </div>
                    </div>
                </div>
           </div>
    </div>
</template>
<script setup>
import { useInfoStore } from '@/stores/essayInfo';
import { Edit } from '@element-plus/icons-vue';
import { computed, onMounted, reactive, ref, toRef} from 'vue';
import { useAddComment, useGetCommentByEssayId } from '@/api/layout';
import { ElNotification } from 'element-plus';
const {getInfo} = useInfoStore()
// 获取当前文章的数据
const essay = toRef(JSON.parse(getInfo()))
console.log(essay)
// 评论内容
const remarkModel = reactive({
    comment: '',
    essayId: essay.value.id,
    userId:'1'
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
    const res = await useAddComment(remarkModel)
    if(res.code === 200){
        ElNotification({
        title: '成功',
        message: '评论成功',
        type: 'success',
    })

    }
}
// 获取当前文章对应的评论
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
}

const getCommentList = async ()=>{
    const res = await useGetCommentByEssayId(essay.value.id)
    commentList.value = res.data
} 
onMounted(()=>{
    getCommentList()
})
// 点击之后就变红
const addLike = ()=>{
 // 给点击的i上like类
console.log()
}
</script>
<style scoped>
.main{
    margin: 120px auto;
    width: 80%;
    padding: 0 100px;
    background-color: white;
    padding-bottom: 20px;
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
.remark_card{
    padding: 20px 0;
    border-bottom: 1px solid #ccc;

    .content{
        display: flex;
        justify-content: space-around;
    
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
</style>