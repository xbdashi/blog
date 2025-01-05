<script setup lang="ts">
import { onMounted } from 'vue';
import { RouterView, useRouter } from 'vue-router'
import { useUserStore } from './stores/user';
import { useGetUser } from '@/api/user/user'

const {user} = useUserStore()
const router = useRouter()


</script>

<template>
 <RouterView/>
</template>

<style scoped>

</style>
