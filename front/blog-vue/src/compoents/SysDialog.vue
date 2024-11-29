<template>
  <el-dialog
      v-model="props.visible"
      :title="props.title"
      :width="props.width + 'px'"
      :before-close="onClose"
      append-to-body
  >
    <div :style="{height:props.height + 'px'}">
      <!-- 相当于一个黑板 显示你父组件传来的东西 -->
      <slot name="content">
      </slot>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="danger" @click="onClose">取消</el-button>
        <el-button type="primary" @click="onConfirm">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import {defineProps} from 'vue';

interface DialogProps {
  title?: string,
  visible: boolean,
  width?: number,
  height?: number
}

// 设置默认值
// defineProps({
//     title:{
//         type:String,
//         default:'标题' 
//     }
// })
const props = withDefaults(defineProps<DialogProps>(), {
  title: '标题',
  visible: false,
  width: 630,
  height: 200
})
// 接收父组件传来的方法
const emit = defineEmits(['onClose', 'onConfirm'])
// 子组件调用这个方法可以先处理自己的业务逻辑，然后再回调父组件关闭弹框
const onClose = () => {
  console.log('子组件关闭弹框')
  // 通知父组件的方法
  emit('onClose')
}
// 子组件调用这个方法可以先处理自己的业务逻辑，然后再回调父组件关闭弹框
const onConfirm = () => {
  console.log('子组件提交弹框')
  // 通知父组件的方法
  emit('onConfirm')
}
</script>
<style scoped>
/* 对话框容器样式 */
.el-dialog {
  border-radius: 8px; /* 圆角 */
  overflow: hidden; /* 隐藏溢出内容 */
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

/* 对话框标题样式 */
.el-dialog__header {
  background-color: rgb(50, 218, 157) !important; /* 背景色 */
  padding: 15px 20px; /* 内边距 */
  border-bottom: 1px solid #ebeef5; /* 底部边框 */
  font-size: 18px; /* 字体大小 */
  font-weight: bold; /* 字体加粗 */
  color: #333; /* 字体颜色 */
}

/* 对话框内容区域样式 */
.el-dialog__body {
  padding: 20px; /* 内边距 */
  background-color: #fff; /* 背景色 */
  color: #666; /* 字体颜色 */
  font-size: 14px; /* 字体大小 */
}

/* 对话框页脚样式 */
.dialog-footer {
  display: flex; /* 弹性布局 */
  justify-content: flex-end; /* 右对齐 */
  padding: 10px 20px; /* 内边距 */
  background-color: #f5f7fa; /* 背景色 */
  border-top: 1px solid #ebeef5; /* 顶部边框 */
}

/* 按钮样式 */
.dialog-footer .el-button {
  margin-left: 10px; /* 左边距 */
  padding: 10px 20px; /* 内边距 */
  font-size: 14px; /* 字体大小 */
  border-radius: 4px; /* 圆角 */
}

/* 取消按钮样式 */
.dialog-footer .el-button[type="danger"] {
  background-color: #f56c6c; /* 背景色 */
  border-color: #f56c6c; /* 边框色 */
  color: #fff; /* 字体颜色 */
}

/* 确定按钮样式 */
.dialog-footer .el-button[type="primary"] {
  background-color: #409eff; /* 背景色 */
  border-color: #409eff; /* 边框色 */
  color: #fff; /* 字体颜色 */
}

/* 按钮悬停样式 */
.dialog-footer .el-button:hover {
  opacity: 0.9; /* 透明度 */
}
</style>
