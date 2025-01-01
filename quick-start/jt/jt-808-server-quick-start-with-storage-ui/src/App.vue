<script setup lang="ts"></script>

<template>
  <el-menu
      :router="true"
      :ellipsis="false"
      class="header-bar"
      mode="horizontal"
  >
    <el-menu-item index="/">首页</el-menu-item>
    <el-menu-item index="/trace-log">请求日志</el-menu-item>
    <el-menu-item index="/alarm-info">报警信息</el-menu-item>
  </el-menu>
  <main class="main">
    <RouterView></RouterView>
  </main>
</template>

<style scoped>
.header-bar {
  box-sizing: content-box;
  position: fixed;
  top: 0;
  left: 0;
  height: var(--el-menu-horizontal-height);
  width: 100%;
}

.main {
  box-sizing: content-box;
  margin: calc(var(--el-menu-horizontal-height) + 10px) 0 10px 0;
  padding: 10px 10px;
  border: 1px solid var(--el-border-color);
  border-radius: 10px;
  height: calc(100vh - var(--el-menu-horizontal-height) - 42px);
}


</style>
