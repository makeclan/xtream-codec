<script setup lang="ts">
import { ref } from 'vue'
import {ElButton, ElInput} from "element-plus";
import {request} from "../utils/request";

defineProps<{ msg: string }>()

const count = ref(0)
const response = ref<any>({});
const getBaseInfo = async () => {
  const res = await request({url: 'server-info', method: 'GET'});
  response.value = res
}
getBaseInfo();
</script>

<template>
    <div class="w-full">
        <div class="flex flex-col">
            <el-button @click="() => count++">debug1 {{msg}}</el-button>
            <div class="m-t-5">count: <el-input class="my-input" v-model="count"></el-input></div>
            <pre>{{ response }}</pre>
        </div>
    </div>
</template>

<style scoped lang="scss">
.my-input{
    width: 100px;
}
</style>
