<script setup lang="ts">
import { ref } from 'vue'
import {ElButton, ElInput} from "element-plus";

defineProps<{ msg: string }>()

const count = ref(0)
</script>

<template>
    <div class="w-full">
        <div class="flex flex-col">
            <el-button @click="() => count++">Add {{msg}}</el-button>
            <div class="m-t-5">count: <el-input class="my-input" v-model="count"></el-input></div>
        </div>
    </div>
</template>

<style scoped lang="scss">
.my-input{
    width: 100px;
}
</style>
