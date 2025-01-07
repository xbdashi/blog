<template>
    <div class="contain">
        <header>
            <Nav/>
        </header>
        <section class="pubContain">
            <Transition name="slide-fade">
                <RouterView/>
            </Transition>
        </section>
    </div>
</template>
<script setup>
import Nav from '@/views/layout/header/index.vue'
</script>
<style scoped>
.slide-fade-enter-active {
  transition: all 0.3s ease-out;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(20px);
  opacity: 0;
}
</style>