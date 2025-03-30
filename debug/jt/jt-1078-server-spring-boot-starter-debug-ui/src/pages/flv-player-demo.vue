<script setup lang="ts">
import {computed, reactive, Ref, ref} from "vue";
import FlvPlayer, {FlvPlayerConfig, FlvPlayerStatus} from "../components/FlvPlayer.vue";
import {jt1078ChannelConfig} from "../types/model.ts";
import {QuestionFilled} from "@element-plus/icons-vue";

const formData = reactive({
  protocol: 'ws',
  host: 'localhost',
  port: 9999,
  sim: '013800138999',
  channelNumber: 2,
  timeout: 20,
  hasVideo: true,
  hasAudio: false
})
const channelOptions = jt1078ChannelConfig
const playerConfig = computed(() => {
  const option = channelOptions.find(it => it.channel === formData.channelNumber)
  return {
    location: option?.location || '未知',
    channel: formData.channelNumber,
    hasVideo: option?.hasVideo || false,
    // 目前还不支持音频(没有音频时这里必须写 false; 否则无法播放)
    hasAudio: false,
    mediaUrl: playerUrl.value
  } as FlvPlayerConfig
})

const playerUrl = computed(() => {
  const type = formData.protocol === 'http' || formData.protocol === 'https' ? 'http' : 'websocket';
  return `${formData.protocol}://${formData.host}:${formData.port}/debug-api/jt1078/stream-data/${type}/flv/${formData.sim}/${formData.channelNumber}?timeout=${formData.timeout}`
})
const playerStatus: Ref<FlvPlayerStatus> = ref(FlvPlayerStatus.OFFLINE)
const onPlayerStatusChange = (from: FlvPlayerStatus, to: FlvPlayerStatus, message?: string) => {
  console.log('onPlayerStatusChange', from, to, message)
  playerStatus.value = to
}
</script>

<template>
  <div class="demo-root">
    <el-card class="form-box">
      <el-form label-width="auto" size="small" :disabled="playerStatus != FlvPlayerStatus.OFFLINE">
        <el-form-item label="协议">
          <el-radio-group v-model="formData.protocol">
            <el-radio-button value="ws">WebSocket</el-radio-button>
            <el-radio-button value="http">HTTP</el-radio-button>
            <el-radio-button value="wss">WebSocket Secure</el-radio-button>
            <el-radio-button value="https">HTTP Secure</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="域名">
          <el-input v-model="formData.host" placeholder="localhost"/>
        </el-form-item>
        <el-form-item label="端口">
          <el-input-number v-model="formData.port" placeholder="9999" :min="0" :max="65535"/>
        </el-form-item>
        <el-form-item label="SIM">
          <el-input v-model="formData.sim" placeholder="013800138999"/>
        </el-form-item>
        <el-form-item label="通道">
          <el-select v-model="formData.channelNumber" filterable placeholder="请选择通道">
            <el-option v-for="item in channelOptions" :key="item.channel" :label="item.title" :value="item.channel">
              {{ item.title }}
              <el-tag type="danger" v-if="item.hasAudio && !item.hasVideo">暂不支持</el-tag>
              <el-tag type="primary" v-else-if="item.hasAudio && item.hasVideo">仅视频(H.264)</el-tag>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <template #label>
            <div style="display: flex;align-items: center;">
              <span>超时时间</span>
              <el-tooltip :content="`如果超过 ${formData.timeout} 秒之后任然没有终端上报数据，则自动放弃本次订阅。`"
                          placement="top">
                <el-icon class="el-icon-info" style="margin-left: 5px;">
                  <QuestionFilled/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <el-input-number v-model="formData.timeout" placeholder="20" :min="0" :max="60 * 60">
            <template #suffix>秒</template>
          </el-input-number>

        </el-form-item>
        <el-form-item label="播放地址">
          <el-input type="textarea" v-model="playerUrl" autosize readonly disabled></el-input>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="player-box">
      <flv-player class="player-box flv-player" :config="playerConfig" @on-player-status-change="onPlayerStatusChange"/>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.demo-root {
  display: flex;
  flex-direction: row;
  justify-content: space-around;

  .form-box, .player-box {
    border: 1px solid var(--el-border-color);
    border-radius: 10px;
    width: 100%;
    height: calc(100vh - 80px);
    overflow-y: scroll;
  }

  .player-box {
    .flv-player {
      max-width: 800px;
    }
  }
}
</style>
