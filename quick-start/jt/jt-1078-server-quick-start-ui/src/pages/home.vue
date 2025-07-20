<script setup lang="ts">
import {onMounted, ref} from "vue";
import {Jt1078ServerConfig} from "../model/jt1078-models.ts";
import {requestJt1078ServerConfigApi} from "../api/proxy-api.ts";

const jt1078ServerConfig = ref<Jt1078ServerConfig>({} as Jt1078ServerConfig)

onMounted(async () => {
  jt1078ServerConfig.value = await requestJt1078ServerConfigApi()
})
</script>

<template>
  <div>这个前端项目仅仅用来调试</div>
  <pre>{{ JSON.stringify(jt1078ServerConfig, null, 2) }}</pre>
</template>

<style scoped lang="scss">

</style>
