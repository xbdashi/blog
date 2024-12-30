<script setup>
import { onMounted } from 'vue';
onMounted(() => {
    // 获取所有的 section 元素
    const sections = document.querySelectorAll('.main-content section');

    // 遍历所有 section，添加点击事件监听器
    sections.forEach(section => {
        section.addEventListener('click', function () {
            // 关闭所有已展开的 section
            sections.forEach(s => {
                if (s !== section) {
                    s.classList.remove('active');
                }
            });

            // 切换当前 section 的 'active' 类，控制展开/收起
            section.classList.toggle('active');
        });
    });
});
</script>
<template>
    <div style="margin-top: 130px;">
        <div class="container">
        <!-- 主要内容区域 -->
        <div class="main-content">
            <section id="tech">
                <h3>技术</h3>
                <div class="article-list">
                    <ul>
                        <li><a href="#">如何学习JavaScript</a></li>
                        <li><a href="#">前端开发的未来趋势</a></li>
                        <li><a href="#">React与Vue的对比</a></li>
                    </ul>
                </div>
            </section>

            <section id="life">
                <h3>生活</h3>
                <div class="article-list">
                    <ul>
                        <li><a href="#">平衡工作与生活的技巧</a></li>
                        <li><a href="#">如何提升自己的时间管理能力</a></li>
                        <li><a href="#">找到自己的兴趣爱好</a></li>
                    </ul>
                </div>
            </section>

            <section id="design">
                <h3>设计</h3>
                <div class="article-list">
                    <ul>
                        <li><a href="#">UI设计的基础</a></li>
                        <li><a href="#">如何设计一个完美的Logo</a></li>
                        <li><a href="#">网页设计的用户体验原则</a></li>
                    </ul>
                </div>
            </section>

            <section id="travel">
                <h3>旅行</h3>
                <div class="article-list">
                    <ul>
                        <li><a href="#">最值得去的旅行目的地</a></li>
                        <li><a href="#">如何规划一场完美的旅行</a></li>
                        <li><a href="#">背包旅行的心得体会</a></li>
                    </ul>
                </div>
            </section>

            <section id="food">
                <h3>美食</h3>
                <div class="article-list">
                    <ul>
                        <li><a href="#">家常菜的做法分享</a></li>
                        <li><a href="#">如何做一份健康的早餐</a></li>
                        <li><a href="#">探索不同国家的美食文化</a></li>
                    </ul>
                </div>
            </section>
        </div>
    </div>

    </div>
</template>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    padding: 20px;
    background-color: #f5f7fa;
}
/* 主要内容区域样式 */
.main-content {
    flex-grow: 1;
    padding: 0px 30px 30px 30px;
    background-color: #fff;

    border-radius: 10px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    width: 1200px;
    /* 设置宽度为1200px */
    margin-left: auto;
    /* 左边距自动 */
    margin-right: auto;
    /* 右边距自动，确保居中 */
}

.main-content h1 {
    font-size: 2.5em;
    margin-bottom: 30px;
    color: #2c3e50;
    font-weight: bold;
}

/* 第一个 section 顶部的下划线，变细且颜色为浅灰色 */
section:first-of-type {
    border-top: 1px solid #dcdcdc;
    /* 浅灰色，下划线变细 */
}

/* 每个 section 的下划线，变细且颜色为浅灰色 */
section {
    overflow: hidden;
    /* 防止下拉框展示时溢出 */
    padding: 0;
    margin: 0;
    /* 去除 section 外部间距 */
    position: relative;
    border-bottom: 1px solid #dcdcdc;
    /* 所有 section 的下划线为浅灰色 */
    display: flex;
    flex-direction: column;
    justify-content: center;
    /* 垂直居中 */
    padding-top: 20px;
    /* 给 section 顶部一点空间 */
    padding-bottom: 20px;
    /* 给 section 底部一点空间 */
}

section h3 {
    color: #2c3e50;
    /* 文字颜色 */
    font-weight: bold;
    text-align: left;
    /* 左对齐 */
    margin: 0;
    /* 去除外边距 */
    padding: 0;
    /* 去除内边距 */
    z-index: 1;
    /* 保证标题在上层 */
}

/* 改变 section 标题点击后的样式 */
section.active h3 {
    color: #3498db;
    /* 点击后字体颜色变为蓝色 */
}

/* 文章列表样式 */
.article-list {
    display: block;
    max-height: 0;
    /* 默认隐藏，设为0高度 */
    padding-top: 0;
    /* 默认没有内边距 */
    margin-top: 0;
    /* 默认没有外边距 */
    opacity: 0;
    /* 使其完全透明 */
    visibility: hidden;
    /* 使其不可见 */
    transition: max-height 0.5s ease-out, opacity 0.3s ease, visibility 0s ease 0.3s;
    /* 动画效果 */
}

section.active .article-list {
    max-height: 500px;
    /* 最大展开高度，确保下拉框显示 */
    padding-top: 15px;
    /* 展开时增加内边距 */
    opacity: 1;
    /* 展开时变得完全可见 */
    visibility: visible;
    /* 展开时可见 */
    transition: max-height 0.5s ease-out, opacity 0.3s ease;
    /* 确保动画平滑 */
}

section ul {
    list-style: none;
}

section ul li {
    margin: 8px 0;
    /* 缩小每项之间的垂直间距 */
}

section ul li a {
    text-decoration: none;
    color: #3498db;
    font-size: 1.2em;
    transition: color 0.3s ease;
}

section ul li a:hover {
    color: #2980b9;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .container {
        padding: 10px;
    }

    .sidebar ul li {
        margin: 8px 10px;
    }

    .main-content h1 {
        font-size: 2em;
    }

    section h3 {
        font-size: 1.8em;
        /* 更大字号 */
    }

    /* 在小屏幕下，限制 main-content 的宽度 */
    .main-content {
        width: 100%;
        padding: 15px;
    }
}
</style>