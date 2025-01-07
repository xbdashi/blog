<template>
    <div class="container">
        <!-- 展示最新或者是最热的文章 -->
         <div class="sort">
            <!-- 如果当前激活就会有绿色样式 -->
            <span :class="pageData.sort == '2' ? 'isActive':''" @click="active('2')">最新</span> <span style="color: #ccc;">&nbsp;| &nbsp;</span>
            <span :class="pageData.sort == '1' ? 'isActive':''" @click="active('1')">最热</span>
         </div>
        <!-- 布局 -->
        <div class="comment_grid" v-infinite-scroll="load" 
            :infinite-scroll-distance="10"
            :infinite-scroll-immediate="false"
            :infinite-scroll-disabled="loading"
           >
            <!-- 卡片 -->
            <div class="comments"  v-for="item in dataList">
                <div class="card">
                        <!-- 标题 评论 浏览量 -->
                        <div style="display: flex; justify-content: space-between;">
                            <div class="title"><a style="cursor: pointer;" @click="info(item)"><h2>{{ item.title }}</h2></a></div>
                            <div class="content" style="margin-right: 30px;">
                                <span><el-icon><View /></el-icon>&nbsp;{{ item.views }}</span> &nbsp;&nbsp;&nbsp;
                                <span><el-icon><ChatDotSquare /></el-icon>&nbsp;{{ item.commentNum }}</span>
                            </div>
                        </div>
                        <!-- 描述 -->
                        <div class="desc">
                            <p style="height: 40px; overflow: hidden;">
                                {{ item.describes }}
                            </p>
                        </div>
                        <div class="userInfo">
                            <div class="userName">
                                <div class="userAvatar" @mousemove="showUserInfo(item)" @mouseout="hideUserInfo()">
                                    <div style="display: flex;justify-content: center;align-items: center;border-radius: 50%;width:35px;height:35px;">
                                        <img v-if="item.user.photo && item.user.photo != ''" :src="item.user.photo" width="35px" alt="">
                                        <img v-else src="@/assets/avtor.png" width="35px" alt="">
                                    </div>
                                    &nbsp;&nbsp;
                                    <span>{{item.user.username}}</span>

                                    <!-- 悬浮显示用户信息 -->
                                     <div class="userinfo" :class="{ 'fade-in': hoverUser == item.userId }">
                                        <div class="user-header">
                                            <img :src="item.user.photo || '@/assets/avtor.png'" alt="">
                                            <h3>{{item.user.username}}</h3>
                                        </div>
                                        <div class="user-stats">
                                            <div class="stat-item">
                                                <div class="stat-value">{{item.user.focus || 9999}}</div>
                                                <div class="stat-label">关注</div>
                                            </div>
                                            <div class="stat-item">
                                                <div class="stat-value">{{item.user.fans || 9999}}</div>
                                                <div class="stat-label">粉丝</div>
                                            </div>
                                        </div>
                                        <div v-if="user && user.id != item.user.id" >
                                            <div @click="toFollow(item)" v-if="!item.user.fanIds.includes(user.id)" :class="!item.user.fanIds.includes(user.id) ? 'focus' :'closeFocus'">关注 <i class="iconfont icon-yiguanzhu"></i></div>
                                            <div v-else class='closeFocus'>取消关注 <i class="iconfont icon-yiguanzhu"></i></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="sign">
                                <!-- 标签 -->
                                <el-tag style="margin-right: 5px;" :type="tag.type" v-for="tag in item.signsList" :key="tag.id">{{ tag.label}}</el-tag>
                            </div>
                            <!-- 发布时间 -->
                            <div class="createTime" style="text-align: right;margin-right: 20px;">
                                <el-icon><Timer/></el-icon>
                                <!-- 格式化时间 -->
                                <span>{{ formatDate(item.createTime) }}</span>
                            </div>
                        </div>
                </div>
            <!--  -->
            </div>
            <!-- 展示部分 -->
            <div class="show">
                <!-- 登录展示框 -->
                <div class="item" v-show="user">
                    <div style="
                    border-bottom: 1px solid rgb(95, 184, 120);
                    width: 90%;
                    margin-bottom: 20px;
                    padding-bottom: 20px;
                    ">
                        <h2>{{ user?.username ? user?.username : '彭于晏'}}</h2>
                    </div>
                    <div>
                        <p>
                            <i><Location style="width: 20px; height: 20px;"/></i>&nbsp;&nbsp;湖南&长沙&nbsp;&nbsp;
                            <i><Handbag style="width: 20px; height: 20px;"/></i>&nbsp;java开发工程师
                        </p>
                        <p class="icon">
                            <br/><i class="iconfont icon-QQ"></i>
                            <i class="iconfont icon-weixin" style="font-size: 40px;"></i>
                        </p>
                    </div>
                </div>
                <!-- 最热标签 -->
                <div class="item">
                    <div class="pubTitle">
                        <h2>最热标签</h2>
                        <div>查看全部</div>
                    </div>
                    <div class="tag">
                        <button v-for="item in tagTop8">{{ item.label }}</button>
                    </div>
                </div>
                <!-- 最热文章 -->
                <div class="item">
                    <div class="pubTitle">
                        <h3>最热文章</h3>
                        
                    </div>
                    <div class="list">
                       <ul style="padding: 20px 0;margin-left: 10px;line-height: 1.8em;">
                         <li v-for="item in hotList"><a href="#">{{ item.title }}</a></li>
                       </ul>
                    </div>
                </div>
                <!-- 最新文章 -->
                <div class="item">
                    <div class="pubTitle">
                        <h3>最新文章</h3>
                    </div>
                    <div class="list">
                       <ul style="padding: 20px 0;margin-left: 10px;line-height: 1.8em;">
                        <li v-for="item in newList"><a href="#">{{ item.title }}</a></li>
                       </ul>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 编辑按钮 -->
         <div class="edit" @click="addBtn"  @mouseover="showTooltip" @mouseout="hideTooltip">
            <div class="tooltip" v-if="isTooltipVisible">新增文章</div>
            <i class="iconfont icon-bianji"></i>
         </div>
    </div>
</template>
<script setup>
import { ChatDotSquare, Handbag, House, Location, Phone, Timer, View } from '@element-plus/icons-vue';
import { onMounted, reactive, ref, watch } from 'vue';
import {useGetAssay,useGetAllTag, useAddViews,useGetSort} from '@/api/layout/index';
import {useTitleStore} from '@/stores/search'
import { useRouter } from 'vue-router';
import { loadTop8Params} from '@/tools/page';
import { useInfoStore } from '@/stores/essayInfo';
import { useUserStore } from '@/stores/user';
const {setInfo} = useInfoStore()
const {user,addFocus} = useUserStore()

const router = useRouter()
// 添加文字的 消息提示
const isTooltipVisible = ref(false);

const showTooltip = () => {
  isTooltipVisible.value = true;
};

const hideTooltip = () => {
  isTooltipVisible.value = false;
};
const hoverUser = ref()

// 获取到当前用户信息
const showUserInfo = (item)=>{
    if(item.id == hoverUser.value) return // 如果和上一次id一样就不执行
    hoverUser.value = item.userId 
}
const hideUserInfo = ()=>{
    hoverUser.value = ''
}

// .............
// 获取paina标题
const {getTitle} = useTitleStore()
const pageData = reactive({
    pageNum: 1,
    pageSize: 6,
    title:'',
    sort:"2" // 默认为最新
})

const dataList = ref([])

// 获取top8
const tagTop8 = ref([])
const top8 =  async()=>{
    const res = await useGetAllTag(loadTop8Params)
    tagTop8.value = res.data
}
// 新增文章跳转
const addBtn = ()=>{
    router.push('/add')
}
// 最热文章
const hotList = ref([])
const hot = async ()=>{
    const res = await useGetSort('HOT')
    hotList.value = res.data
}
// 最新文章
const newList = ref([])
const getLatest = async ()=>{
    const res = await useGetSort('FRESH')
    newList.value = res.data
}
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
}

const getEssay = async ()=>{
    pageData.pageNum = 1
    const res = await useGetAssay(pageData)
    dataList.value = res.data.rows
}
// 查看文章的最新或最热
const isActive = ref(false)
const active = (sort)=>{
    pageData.sort = sort
    getEssay()
}
// 监视数据变化
watch(()=>getTitle(), async()=>{
    // 发生变化就执行
    pageData.title = getTitle()
    await getEssay()

    console.log(dataList.value)
})
// 添加 loading 状态
const loading = ref(false)

// 修改 load 函数
const load = async () => {
    try {
        loading.value = true
        pageData.pageNum++
        const res = await useGetAssay(pageData)
        if (res.data.rows && res.data.rows.length > 0) {
            dataList.value.push(...res.data.rows)
        }
    } catch (error) {
        console.error('加载更多数据失败:', error)
    } finally {
        loading.value = false
    }
}
// 关注用户 userId --> 被关注用户的id
// 存入这次页面关注的用户
const focusIds = ref([])
const toFollow = (essay)=>{
    // 循环遍历文章的列表 将和这个user的id一样的用户的粉丝量+1
    dataList.value.forEach(item=>{
        if(item.id === essay.id){
            // 将点击的用户粉丝+1
            item.fans = (Number(item.fans)+1).toString()
            // 粉丝列表添加当前用户
            item.user.fanIds.push(user?.id)
            focusIds.value.push(essay?.userId)
            item.user.fans = (Number(item.user.fans)+1).toString()
            // 将当前用户的关注数+1
            addFocus((Number(user?.focus)+1).toString())
            
        }
    })

    // 增加被关注用户的粉丝数

}
// 跳转详细页面
const info = async(item)=>{
    // 通过id查出对应的文章，将他的阅读量+1
    await useAddViews(item.id)
    // 将对象转换为字符串存入paina
    setInfo(JSON.stringify(item))
    router.push({
        name: 'info', // 假设你的路由配置中有 name 为 'info' 的路由
    });
}

onMounted(()=>{
    getEssay();
    top8();
    hot();
    getLatest();
})

</script>  
<style scoped lang="less">
.isActive{
    color: #5fb878;
}
.pubTitle{
    display: flex;
    justify-content: space-between;
    align-items: center;   
    border-bottom: 1px solid #5fb878;
    padding: 20px 5px;
}

.container{
    margin:130px auto;
    height: 100vh;
    border-radius: 10px;

    .sort{
        span{
            cursor: pointer;
        }
    }
    .comment_grid{
        position: relative;
    max-height: calc(100vh - 150px); /* 添加最大高度 */
        
    }
    .comments{
        padding: 20px;
        box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.1);
        height: 208px;
        background-color: white;
        border-radius: 4px;
        margin-top: 20px;
        width: 70%;

        .card{
            display: block;
            height: 100%;
            display: grid;
            grid-template-rows: repeat(1, 1fr);
            color: black;

            .title a{
                color: #333;
                transition: 0.2s all;
            }

            .title a:hover{
                color: #409eff;
            }

           .userInfo{
            display: grid;
            grid-template-columns: repeat(3, 1fr);

            /* username 占3格 */
            .userName{
                grid-column: 1/4;
            }
            /* 标签 占2格 */
            .sign{
                grid-column: 1/3;
            }
           }
        }
    }
    .show{
        display: flex;
        flex-direction: column;
        
        width: 28%;
        position: absolute;
        right: 0;
        top: 0;
        .item:nth-child(1){ 
            background-color: white;
            box-shadow: 1px 1px 1px 0 rgba(0, 0, 0, 0.1);
            text-align: center;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            padding: 30px 0;
            .icon i{
                text-align: center;
                font-size: 45px;
                transition: 0.4s all;
                cursor: pointer;
                margin: 20px 0;
            }
            .icon-QQ:hover{
                color: #409eff;
            }
            .icon-weixin:hover{
                color: #34ec59;
            }
        }
        .item:nth-child(2){
            margin-top: 10px;
            background-color: white;
            box-shadow: 1px 1px 1px 0 rgba(0, 0, 0, 0.1);
            .tag{
                margin-top: 5px;
                display: grid;
                grid-template-columns: repeat(4, 1fr);
                grid-row-gap: 10px;
                margin-left: 10px;
                padding: 10px 0;
                button{
                    width: 70px;
                    height: 25px;
                    padding: 15px 7px;
                    border: 1px solid rgb(211, 211, 211);
                    text-align: center;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    color: #5fb878;
                    background: #eff8f2;
                    border: 1px solid #bfe3c9;
                    border-radius: 10px;
                    transition: 0.4s all;
                }
                button:hover{
                    color: white;
                    background: #5fb878;
    
                }
            }
        }
        .item:nth-child(3),.item:nth-child(4) {
            margin-top: 10px;
            background-color: white;
            box-shadow: 1px 1px 1px 0 rgba(0, 0, 0, 0.1);
            
            li{
                width: 100%;
                overflow: hidden;
            
            }
            a{
                color: #5fb878;
                /* 如果超过就显示省略号 */
                overflow: hidden;
                white-space: nowrap; /* 不换行 */
                overflow: hidden; /* 超出部分隐藏 */
                text-overflow: ellipsis; /* 超出部分显示为省略号 */
                max-width: 100%; /* 设置最大宽度，可以根据需要调整 */
                display: block; /* 确保 a 标签的行为像块级元素 */
            }
            a:hover{
                /* 显示下划线 */
                text-decoration: underline;

              
            }
        }
    }
    /* 头像 */
    .userAvatar{
        width:80px;
        height: 60px;
        display: flex;
        align-items: center;
        position: relative;

        .userinfo {
        background: white;
        border-radius: 12px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
        padding: 20px;
        width: 350px;
        opacity: 0;
        transform: translateY(10px);
        transition: all 0.3s ease;

        &.fade-in {
            opacity: 1;
            transform: translateY(0);
        }

        .user-header {
            display: flex;
            align-items: center;
            gap: 15px;
            margin-bottom: 20px;

            img {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid #f0f0f0;
            }

            h3 {
            margin: 0;
            font-size: 18px;
            color: #333;
            font-weight: 600;
            }
        }

        .user-stats {
            display: flex;
            justify-content: space-around;
            padding: 10px 0;
            border-top: 1px solid #f0f0f0;

            .stat-item {
            text-align: center;
            padding: 0 10px;

            .stat-value {
                font-size: 20px;
                font-weight: 600;
                color: #333;
                margin-bottom: 4px;
            }

            .stat-label {
                font-size: 12px;
                color: #666;
            }
            }
        }
        .focus{
            width: 100%;
            height: 30px;
            background: #409eff;
            display: flex;
            justify-content: center;
            align-items: center;
            color: white;
            border-radius: 30px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .closeFocus{
            width: 100%;
            height: 30px;
            background: rgb(229, 74, 74);
           
            color: blue;
        
            display: flex;
            justify-content: center;
            align-items: center;
            color: white;
            border-radius: 30px;
            cursor: pointer;
        }
        }
    }
}

.edit{
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    bottom: 10%;
    right: 5%;
    width: 70px;
    height: 70px;
    border: 3px solid #5fb878;
    border-radius: 50%;
    transition: 0.3s all;
    cursor: pointer;
    background-color: #eff8f2;

    i{
        font-size: 30px;
        color: #333;
    }
}
.edit:hover{
    background-color:#5fb878 ;
}
.edit:hover i{
    color: #fff;
}
.tooltip {
  position: absolute;
  top: -30px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #333;
  color: #fff;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  z-index: 1000;
}
</style>@/api/model@/api/model/userMondel@/api/model/model