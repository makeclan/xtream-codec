<script lang="ts">
export interface FlvPlayerConfig {
  channel?: number
  location?: string
  hasVideo: boolean
  hasAudio: boolean
  mediaUrl?: string
}

export enum FlvPlayerStatus {
  OFFLINE = 'Offline',
  WAITING = 'Waiting',
  RUNNING = 'Running',
  PAUSE = 'Pause'
}
</script>

<script setup lang="ts">
import {computed, ref} from 'vue'
import mpegts from 'mpegts.js'
import {Refresh, VideoPause, VideoPlay} from '@element-plus/icons-vue'


const props = defineProps<{
  config: FlvPlayerConfig
}>()

const video = ref<HTMLMediaElement>()
const player = ref<mpegts.Player>()
const errorTips = ref('')
const status = ref(FlvPlayerStatus.OFFLINE)
const reset = () => {
  if (player.value) {
    player.value.pause()
    player.value.unload()
    player.value.detachMediaElement()
    player.value.destroy()
    player.value = undefined
  }
  publishStatusChangeEvent(status.value, FlvPlayerStatus.OFFLINE)
  status.value = FlvPlayerStatus.OFFLINE
  errorTips.value = ''
}
const playButtonDisabled = computed(() => {
  return status.value === FlvPlayerStatus.WAITING
})

const play = async () => {
  const oldStatus = status.value
  if (status.value === FlvPlayerStatus.OFFLINE) {
    await initPlayer()
    status.value = FlvPlayerStatus.WAITING
    player.value
        ?.play()
        ?.then((r: string | void) => {
          if (typeof r === 'string') {
            errorTips.value = r
          }
        })
        .catch((e: string) => {
          errorTips.value = e
          publishStatusChangeEvent(status.value, FlvPlayerStatus.OFFLINE, e)
          status.value = FlvPlayerStatus.OFFLINE
        })
  } else if (status.value === FlvPlayerStatus.WAITING) {
    // do nothing
  } else if (status.value === FlvPlayerStatus.RUNNING) {
    player.value?.pause()
    status.value = FlvPlayerStatus.PAUSE
  } else if (status.value === FlvPlayerStatus.PAUSE) {
    player.value
        ?.play()
        ?.then((r: string | void) => {
          if (typeof r === 'string') {
            errorTips.value = r
          }
        })
        .catch((e: string) => {
          errorTips.value = e
          publishStatusChangeEvent(status.value, FlvPlayerStatus.OFFLINE, e)
          status.value = FlvPlayerStatus.OFFLINE
        })
    status.value = FlvPlayerStatus.RUNNING
  } else {
    errorTips.value = '未知状态: ' + status.value
  }
  publishStatusChangeEvent(oldStatus, status.value)
}

const initPlayer = async () => {
  if (player.value !== undefined) {
    return
  }

  player.value = mpegts.createPlayer(
      {
        isLive: true,
        type: 'flv',
        url: props.config.mediaUrl,
        hasAudio: props.config.hasAudio,
        hasVideo: props.config.hasVideo
        // enableWorker: true,
        // enableStashBuffer: false,
        // stashInitialSize: 128
      },
      {
        enableWorker: false
      }
  )
  if (video.value) {
    player.value.attachMediaElement(video.value)
  }

  player.value.load()

  player.value.on(mpegts.Events.LOADING_COMPLETE, () => {
    publishStatusChangeEvent(status.value, FlvPlayerStatus.OFFLINE)
    status.value = FlvPlayerStatus.OFFLINE
    errorTips.value = '视频播放结束'
  })

  player.value.on(mpegts.Events.METADATA_ARRIVED, () => {
    publishStatusChangeEvent(status.value, FlvPlayerStatus.RUNNING)
    status.value = FlvPlayerStatus.RUNNING
  })

  // player.value.on(mpegts.Events.MEDIA_INFO, (res) => {
  //   console.log('==> MEDIA_INFO')
  //   console.log(res)
  // })

  player.value.on(mpegts.Events.ERROR, (errorType, errorDetail, errorInfo) => {
    console.log('播放器异常')
    console.log('errorType:', errorType)
    console.log('errorDetail:', errorDetail)
    console.log('errorInfo:', errorInfo)
    errorTips.value = errorInfo
  })
}
const playerStatusBlockBackground = computed(() => {
  switch (status.value) {
    case FlvPlayerStatus.OFFLINE: {
      return '#e00c3d'
    }
    case FlvPlayerStatus.WAITING: {
      return '#e4f602'
    }
    case FlvPlayerStatus.RUNNING: {
      return '#05ec05'
    }
    case FlvPlayerStatus.PAUSE: {
      return '#3702f6'
    }
      // 应该不会执行到这里
    default: {
      return '#909399'
    }
  }
})
const buttonCss = computed(() => {
  switch (status.value) {
    case FlvPlayerStatus.OFFLINE: {
      return {
        play: VideoPlay,
        reset: Refresh
      }
    }
    case FlvPlayerStatus.WAITING: {
      return {
        play: VideoPause,
        reset: Refresh
      }
    }
    case FlvPlayerStatus.RUNNING: {
      return {
        play: VideoPause,
        reset: Refresh
      }
    }
    case FlvPlayerStatus.PAUSE: {
      return {
        play: VideoPlay,
        reset: Refresh
      }
    }
    default: {
      return {
        play: VideoPlay,
        reset: Refresh
      }
    }
  }
})
const streamDesc = () => {
  const audio = props.config.hasAudio
  const video = props.config.hasVideo
  if (audio && video) {
    return '音视频'
  } else if (audio) {
    return '音频'
  } else if (video) {
    return '视频'
  } else {
    return '未知'
  }
}
const emit = defineEmits<{
  (e: 'onPlayerStatusChange', from: FlvPlayerStatus, to: FlvPlayerStatus, message?: string): void
}>()

const publishStatusChangeEvent = (from: FlvPlayerStatus, to: FlvPlayerStatus, message?: string) => {
  if (from !== to) {
    emit('onPlayerStatusChange', from, to, message)
  }
}
defineExpose({play, reset, status})

</script>

<template>
  <div class="player-root">
    <el-card style="border-radius: 10px;">
      <template #header>
        <div class="player-header">
          <div class="player-header-left">
            <el-tooltip :content="status" placement="top">
              <div class="player-status" :style="{backgroundColor: playerStatusBlockBackground}"></div>
            </el-tooltip>
            <span>通道{{ config.channel }}</span>
            <el-divider direction="vertical"></el-divider>
            <span>{{ config.location }}</span>
            <el-divider direction="vertical"></el-divider>
            <span>{{ streamDesc() }}</span>

          </div>
          <div>
            <el-tooltip content="重置播放器" placement="top">
              <el-button :icon="buttonCss.reset" circle @click="reset"/>
            </el-tooltip>
            <el-tooltip content="播放/暂停" placement="top">
              <el-button circle @click="play" :icon="buttonCss.play" :disabled="playButtonDisabled"></el-button>
            </el-tooltip>
          </div>
        </div>
      </template>
      <el-alert type="error" v-if="errorTips" style="margin-bottom: 10px;">
        {{ errorTips }}
      </el-alert>
      <video
          ref="video"
          w-full
          style="height: 100%"
          width="100%"
          height="100%"
          controls
          autoplay
      ></video>
      <div v-if="config.hasVideo">
      </div>
    </el-card>
  </div>
</template>
<style lang="scss" scoped>
.player-root {
  .player-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .player-header-left {
      display: flex;
      justify-content: center;
      align-items: center;

      .player-status {
        margin-right: 8px;
        width: 16px;
        height: 16px;
        border-radius: 50%;
      }
    }
  }


}
</style>
