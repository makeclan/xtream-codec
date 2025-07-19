<script setup lang="ts">
import {computed, onMounted, reactive, Ref, ref} from "vue";
import FlvPlayer, {FlvPlayerConfig, FlvPlayerStatus} from "../components/FlvPlayer.vue";
import {jt1078ChannelConfig} from "../types/model.ts";
import {QuestionFilled} from "@element-plus/icons-vue";
import {Jt1078ServerConfig} from "../model/jt1078-models.ts";
import {requestJt1078ServerConfigApi} from "../api/proxy-api.ts";

const jt1078ServerConfig = ref<Jt1078ServerConfig>({} as Jt1078ServerConfig)
const formData = reactive({
  protocol: 'ws',
  host: 'localhost',
  port: 9999,
  sim: '013800138999',
  channelNumber: 2,
  timeout: 20,
  hasVideo: true,
  hasAudio: true,
  naluDecoderRingBufferSizeExponent: 18,
})
const onChannelChange = (ch: number) => {
  const option = channelOptions.find(it => it.channel === ch)
  if (option) {
    formData.hasVideo = option.hasVideo;
    formData.hasAudio = option.hasAudio;
  }
}

const naluDecoderRingBufferSize = computed(() => {
  return 2 ** formData.naluDecoderRingBufferSizeExponent
})

const channelOptions = jt1078ChannelConfig
const playerConfig = computed(() => {
  const option = channelOptions.find(it => it.channel === formData.channelNumber)
  return {
    location: option?.location || '未知',
    channel: formData.channelNumber,
    hasVideo: formData.hasVideo,
    hasAudio: formData.hasAudio,
    mediaUrl: playerUrl.value
  } as FlvPlayerConfig
})

const playerUrl = computed(() => {
  const type = formData.protocol === 'http' || formData.protocol === 'https' ? 'http' : 'websocket';
  const bufferSize = naluDecoderRingBufferSize.value;
  const {hasVideo, hasAudio} = formData
  // return `${formData.protocol}://${formData.host}:${formData.port}/debug-api/jt1078/stream-data/${type}/flv/${formData.sim}/${formData.channelNumber}?timeout=${formData.timeout}&hasVideo=${hasVideo}&hasAudio=${hasAudio}&naluDecoderRingBufferSize=${bufferSize}`
  return `${formData.protocol}://${formData.host}:${formData.port}/dashboard-api/jt1078/v1/stream-data/${type}/flv/${formData.sim}/${formData.channelNumber}?timeout=${formData.timeout}&hasVideo=${hasVideo}&hasAudio=${hasAudio}&naluDecoderRingBufferSize=${bufferSize}`
})
const playerStatus: Ref<FlvPlayerStatus> = ref(FlvPlayerStatus.OFFLINE)
const onPlayerStatusChange = (from: FlvPlayerStatus, to: FlvPlayerStatus, message?: string) => {
  console.log('onPlayerStatusChange', from, to, message)
  playerStatus.value = to
}

onMounted(async () => {
  jt1078ServerConfig.value = await requestJt1078ServerConfigApi()
  formData.port = jt1078ServerConfig.value.jt1078ServerWebPort
  formData.host = window.location.hostname
})
</script>

<template>
  <div class="demo-root">
    <el-card class="form-box">
      <template #header>
        <div style="display: flex; justify-content: center; align-items: center; height: 15px;">
          <div style="font-weight: bold;">H.264 -&gt; FLV</div>
          <el-tag type="primary" style="margin-left: 10px;">手动推流</el-tag>
        </div>
      </template>
      <div>
        <el-form label-width="auto" size="small" :disabled="playerStatus != FlvPlayerStatus.OFFLINE">
          <el-form-item label="协议">
            <el-radio-group v-model="formData.protocol">
              <el-radio-button value="ws">WebSocket</el-radio-button>
              <el-radio-button value="http">HTTP</el-radio-button>
              <el-radio-button value="wss">WebSocket Secure</el-radio-button>
              <el-radio-button value="https">HTTP Secure</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="播放地址域名">
            <template #label>
              <div style="display: flex;align-items: center;">
                <span>播放地址域名</span>
                <el-tooltip content="FLV视频播放地址域名(浏览器能访问即可，不一定要公网地址)"
                            placement="top">
                  <el-icon class="el-icon-info" style="margin-left: 5px;">
                    <QuestionFilled/>
                  </el-icon>
                </el-tooltip>
              </div>
            </template>
            <el-input v-model="formData.host" placeholder="localhost"/>
          </el-form-item>
          <el-form-item label="端口">
            <el-input-number v-model="formData.port" placeholder="9999" :min="0" :max="65535"/>
          </el-form-item>
          <el-form-item label="SIM">
            <el-input v-model="formData.sim" placeholder="013800138999"/>
          </el-form-item>
          <el-form-item label="通道">
            <el-select v-model="formData.channelNumber" filterable placeholder="请选择通道" @change="onChannelChange">
              <el-option v-for="item in channelOptions" :key="item.channel" :label="item.title" :value="item.channel">
                {{ item.title }}
                <el-tag :type="!item.hasVideo ? 'danger': 'success'">{{ item.remark }}</el-tag>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="音视频">
            <el-checkbox v-model="formData.hasAudio">
              音频
            </el-checkbox>
            <el-checkbox v-model="formData.hasVideo">
              视频
            </el-checkbox>
          </el-form-item>
          <el-form-item label="缓冲区大小">
            <template #label>
              <div style="display: flex;align-items: center;">
                <span>缓冲区大小</span>
                <el-tooltip content="Nalu解码器环形数组容量:必须是 2 的 N 次幂"
                            placement="top">
                  <el-icon class="el-icon-info" style="margin-left: 5px;">
                    <QuestionFilled/>
                  </el-icon>
                </el-tooltip>
              </div>
            </template>
            <el-input-number v-model="formData.naluDecoderRingBufferSizeExponent" :min="8" :max="31">
              <template #prefix>2 ^</template>
            </el-input-number>
            &nbsp;=&nbsp; {{ naluDecoderRingBufferSize }}
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
            <el-alert type="success" style="margin-bottom: 10px;">
              为方便调试，本示例服务端允许跨域访问
            </el-alert>
            <el-input type="textarea" v-model="playerUrl" autosize readonly disabled></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <div style="width: 100%; margin-left: 5px;">
      <flv-player
          class="player-box flv-player"
          style="border: 0;"
          :config="playerConfig"
          @on-player-status-change="onPlayerStatusChange"/>
    </div>
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
    height: calc(100vh - 60px);
    overflow-y: scroll;
  }

  .player-box {
    .flv-player {
      max-width: 800px;
    }
  }
}
</style>
