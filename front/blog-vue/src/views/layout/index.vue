<template>
    <div class="contain">
        <header>
            <Header/>
        </header>
        <section class="pubContain">
        <transition name="cc">
            <RouterView/>
        </transition>   
            
        </section>
    </div>
</template>
<script setup>
import Header from '@/views/layout/header/index.vue'
</script>
<style scoped>
.cc-enter-active,  .cc-leave-active{  /* 进入 / 离开 过度生效状态 */
        transition: all .5s;
    }
    .cc-enter,  .cc-leave-to{             /* 进入状态 & 结束状态 opacity为0 */
        opacity: 0;
        transform: translateX(10px);
    }
</style>