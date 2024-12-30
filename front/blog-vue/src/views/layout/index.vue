<template>
    <div class="contain">
        <header>
            <Header/>
        </header>
        <section class="pubContain">
        <Transition name="fade" mode="out-in">
            <RouterView/>
        </Transition>   
            
        </section>
    </div>
</template>
<script setup>
import Header from '@/views/layout/header/index.vue'
</script>
<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 1s ease;
}

.fade-enter-from
{
    transform: translateX(-60px);
  opacity: 0.1;
 
}
.fade-leave-to {
    opacity: 0.1;
    transform: translateY(30px);
}
</style>