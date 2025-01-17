<script setup lang="ts">
import {onMounted, ref} from "vue";
import {requestServerConfig} from "../api/demo-query-api.ts";
import {QuickstartServerConfig} from "../types/model.ts";

const dashboardAddress = ref('');
const serverConfig = ref({} as QuickstartServerConfig);

onMounted(async () => {
  serverConfig.value = await requestServerConfig()
  dashboardAddress.value = `${window.location.protocol}//${window.location.hostname}:${serverConfig.value.server.port}/dashboard-ui/`
})

</script>

<template>
  <div>
    <el-alert type="info" show-icon class="my-card">
      <template #title>
        说明
      </template>
      <template #default>
        <div>
          <p>该示例演示了如何基于
            <a
                href="https://github.com/hylexus/xtream-codec"
                target="_blank"><strong>xtream-codec</strong></a> 实现
            <strong style="color: #1a1a1a;">JT/T 808</strong> 协议服务端
          </p>
          <p>
            同时引入了 <strong style="color: #1a1a1a;">Dashboard</strong> 依赖，
            尝试访问下面的地址查看监控面板：
          </p>
          <ul>
            <li v-for="(item,idx) in ['127.0.0.1',...(serverConfig?.server?.availableIpAddresses || [])]"
                :key="idx">
              <a
                  style="font-weight: bold;"
                  :href="`http://${item}:${serverConfig?.server?.port}/dashboard-ui/`"
                  target="_blank"
              >http://{{ item }}:{{ serverConfig?.server?.port }}/dashboard-ui/</a></li>
          </ul>
        </div>
      </template>
    </el-alert>

    <el-card class="my-card">
      <template #header>
        服务端配置
      </template>
      <div style="display: flex;flex-wrap: wrap;">
        <div class="box-item" style="min-width: 200px;">
          <el-descriptions :column="1" border title="Web服务器 - HTTP" style="width: 100%;height: 100%;">
            <el-descriptions-item label="类型">
              <el-tag :type="serverConfig?.server?.type.toLocaleLowerCase() === 'reactive' ? 'success' : 'warning'">
                {{ serverConfig?.server?.type }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="端口">
              {{ serverConfig?.server?.port }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="box-item" style="min-width: 200px;">
          <el-descriptions :column="1" border title="指令服务器 - TCP" style="width: 100%;">
            <el-descriptions-item label="状态">
              <el-tag :type="serverConfig?.jt808?.instructionServer?.tcpServer?.enabled ? 'success' : 'danger'">
                {{ serverConfig?.jt808?.instructionServer?.tcpServer?.enabled ? '已启用' : '未启用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="IP">
              {{ serverConfig?.jt808?.instructionServer?.tcpServer?.host }}
            </el-descriptions-item>
            <el-descriptions-item label="端口">
              {{ serverConfig?.jt808?.instructionServer?.tcpServer?.port }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="box-item" style="min-width: 200px;">
          <el-descriptions :column="1" border title="指令服务器 - UDP" style="width: 100%;">
            <el-descriptions-item label="状态">
              <el-tag :type="serverConfig?.jt808?.instructionServer?.udpServer?.enabled ? 'success' : 'danger'">
                {{ serverConfig?.jt808?.instructionServer?.udpServer?.enabled ? '已启用' : '未启用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="IP">
              {{ serverConfig?.jt808?.instructionServer?.udpServer?.host }}
            </el-descriptions-item>
            <el-descriptions-item label="端口">
              {{ serverConfig?.jt808?.instructionServer?.udpServer?.port }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="box-item" style="min-width: 200px;">
          <el-descriptions :column="1" border title="附件服务器 - TCP" style="width: 100%;">
            <el-descriptions-item label="状态">
              <el-tag :type="serverConfig?.jt808?.attachmentServer?.tcpServer?.enabled ? 'success' : 'danger'">
                {{ serverConfig?.jt808?.attachmentServer?.tcpServer?.enabled ? '已启用' : '未启用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="IP">
              {{ serverConfig?.jt808?.attachmentServer?.tcpServer?.host }}
            </el-descriptions-item>
            <el-descriptions-item label="端口">
              {{ serverConfig?.jt808?.attachmentServer?.tcpServer?.port }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="box-item" style="min-width: 200px;">
          <el-descriptions :column="1" border title="附件服务器 - UDP" style="width: 100%;">
            <el-descriptions-item label="状态">
              <el-tag :type="serverConfig?.jt808?.attachmentServer?.udpServer?.enabled ? 'success' : 'danger'">
                {{ serverConfig?.jt808?.attachmentServer?.udpServer?.enabled ? '已启用' : '未启用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="IP">
              {{ serverConfig?.jt808?.attachmentServer?.udpServer?.host }}
            </el-descriptions-item>
            <el-descriptions-item label="端口">
              {{ serverConfig?.jt808?.attachmentServer?.udpServer?.port }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-card>

    <el-card class="my-card">
      <template #header>
        <div class="card-header">
          <span>数据存储 - 请求日志</span>
        </div>
      </template>
      <div class="outer-box">
        <div v-for="(item,idx) in serverConfig.database" :key="idx" class="box-item">
          <div style="margin-bottom: 10px;">{{ item.label }}</div>
          <el-tag :type="item.enabled ? 'success' : 'danger'">{{ item.enabled ? '已启用' : '未启用' }}</el-tag>
        </div>
      </div>
    </el-card>

    <el-card class="my-card">
      <template #header>
        <div>OSS(Object Storage Service) - 苏标附件存储</div>
      </template>
      <div class="outer-box">
        <div v-for="(item,idx) in serverConfig.oss" :key="idx" class="box-item">
          <div style="margin-bottom: 10px;">{{ item.label }}</div>
          <el-tag :type="item.enabled ? 'success' : 'danger'">{{ item.enabled ? '启用' : '未启用' }}</el-tag>
        </div>
      </div>
    </el-card>

  </div>
</template>

<style scoped lang="scss">
.my-card {

  &:not(:first-child) {
    margin-top: 20px;
  }

  border-radius: 10px;
}

.outer-box {
  display: flex;
  flex-flow: row wrap;
  min-width: 300px;
}

.box-item {
  border: 1px solid var(--el-border-color);
  border-radius: 10px;
  padding: 20px;
  margin: 10px 10px 10px 0;
  min-width: 100px;

  &:last-child {
    margin-right: 0;
  }

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-items: center;
}
</style>

