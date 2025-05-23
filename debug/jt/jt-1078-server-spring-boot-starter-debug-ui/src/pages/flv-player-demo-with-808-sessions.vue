<script setup lang="ts">
import {computed, onMounted, reactive, Ref, ref} from "vue";
import FlvPlayer, {FlvPlayerConfig, FlvPlayerStatus} from "../components/FlvPlayer.vue";
import {jt1078ChannelConfig} from "../types/model.ts";
import {ArrowDown, QuestionFilled, Search} from "@element-plus/icons-vue";
import {
  requestJt1078ServerConfigApi,
  requestProxy9101CommandApi,
  requestProxy9102CommandApi,
  requestProxyJt808SessionsApi
} from "../api/proxy-api.ts";
import {Jt808Session} from "../model/jt808-models.ts";
import {
  Command9101CommandType,
  Jt1078DataType,
  Jt1078ServerConfig,
  Jt1078StreamType,
  MediaTypeToClose
} from "../model/jt1078-models.ts";

const pageState = reactive({
  command9101Dialog: {
    visible: false,
    loading: false,
    commandResult: "",
  },
  command9102Dialog: {
    visible: false,
    loading: false,
    commandResult: "",
  }
})
const jt1078ServerConfig = ref<Jt1078ServerConfig>({} as Jt1078ServerConfig)
const jt808ProxySession: {
  data?: Jt808Session[],
  total?: number,
  selectedRow?: Jt808Session | undefined
} = reactive({
  data: [],
  total: 0,
  selectedRow: undefined,
})
const handleRowClick = (row: any, column: any) => {
  if (column.property === 'ops') {
    return;
  }
  if (!editable.value) {
    return
  }
  jt808ProxySession.selectedRow = row
  formData.sim = row.terminalId
}

const tableRowClassName = ({row}: { row: any }) => {
  if (row.id === jt808ProxySession.selectedRow?.id) {
    return 'current-row';
  }
  return '';
};
const formData = reactive({
  protocol: 'ws',
  host: 'localhost',
  port: 9999,
  sim: '013800138999',
  channelNumber: 2,
  // 超过30秒没有音视频数据 断开连接
  timeout: 30,
  hasVideo: true,
  hasAudio: false,
  command9101: {
    dataType: Jt1078DataType.DATA_TYPE_AUDIO_VIDEO,
    streamType: Jt1078StreamType.STREAM_TYPE_MAIN,
    // 超过30秒 终端没有对0x9101进行回复
    timeout: 30,
  },
  command9102: {
    commandType: Command9101CommandType.TYPE_0,
    mediaTypeToClose: MediaTypeToClose.TYPE_0,
    streamTypeToSwitch: Jt1078StreamType.STREAM_TYPE_MAIN,
    // 超过30秒 终端没有对0x9102进行回复
    timeout: 30,
  },
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
const editable = computed(() => playerStatus.value === FlvPlayerStatus.OFFLINE)
const handleCommand = async (command: string) => {
  const selectedRow = jt808ProxySession.selectedRow
  console.log(command, selectedRow?.terminalId, selectedRow?.id)
  switch (command) {
    case '9101': {
      pageState.command9101Dialog.visible = true
      break
    }
    case '9102': {
      pageState.command9102Dialog.visible = true
      break
    }
    default: {
      console.log('command', command)
    }
  }
}

const sendCommand9101 = async () => {
  const params = {
    sim: formData.sim,
    jt1078ServerIp: jt1078ServerConfig.value.jt1078ServerHost,
    jt1078ServerPortTcp: jt1078ServerConfig.value.jt1078ServerTcpPort,
    jt1078ServerPortUdp: jt1078ServerConfig.value.jt1078ServerUdpPort,
    channelNumber: formData.channelNumber,
    dataType: formData.command9101.dataType,
    streamType: formData.command9101.streamType,
    timeout: formData.command9101.timeout,
  }
  pageState.command9101Dialog.loading = true
  const response = await requestProxy9101CommandApi(params).finally(() => {
    pageState.command9101Dialog.loading = false
  })
  pageState.command9101Dialog.commandResult = JSON.stringify(response, null, 2)

  if (response.result === 0) {
    await restartPlayer()
  }
}
const sendCommand9102 = async () => {
  const params = {
    sim: formData.sim,
    channelNumber: formData.channelNumber,
    command: formData.command9102.commandType,
    mediaTypeToClose: formData.command9102.mediaTypeToClose,
    streamType: formData.command9102.streamTypeToSwitch,
    timeout: formData.command9102.timeout,
  }
  pageState.command9102Dialog.loading = true
  const result = await requestProxy9102CommandApi(params).finally(() => {
    pageState.command9102Dialog.loading = false
    resetPlayer()
  })
  pageState.command9102Dialog.commandResult = JSON.stringify(result, null, 2)
}

async function loadJt808SessionList() {
  const {data, total} = await requestProxyJt808SessionsApi()
  jt808ProxySession.data = data
  jt808ProxySession.total = total
}

const demoPlayerRef = ref<typeof FlvPlayer>()
const restartPlayer = async () => {
  demoPlayerRef.value?.reset()
  await demoPlayerRef.value?.play()
}
const resetPlayer = () => {
  demoPlayerRef.value?.reset()
}

onMounted(async () => {
  jt1078ServerConfig.value = await requestJt1078ServerConfigApi()
  await loadJt808SessionList();
})
</script>

<template>
  <el-card class="form-box">
    <el-table :data="jt808ProxySession.data" :total="jt808ProxySession.total" :page-size="10" border
              @row-click="handleRowClick"
              :row-class-name="tableRowClassName"
              style="width: 100%;">
      <el-table-column fixed="left" width="55">
        <template #default="scope">
          <el-switch
              size="small"
              :disabled="!editable"
              :model-value="jt808ProxySession.selectedRow?.id === scope.row.id"/>
        </template>
      </el-table-column>
      <el-table-column label="ID" prop="id" fixed="left" show-overflow-tooltip width="510"/>
      <el-table-column label="协议" prop="protocolType" width="70"/>
      <el-table-column label="版本" prop="protocolVersion" width="130"/>
      <el-table-column label="SIM" prop="terminalId" width="200"/>
      <el-table-column label="最近一次通信时间" prop="lastCommunicateTime" width="210"/>
      <el-table-column label="创建时间" prop="creationTime" width="210"/>
      <el-table-column label="操作" fixed="right" width="120" prop="ops">
        <template #header>
          <el-button :icon="Search" @click="loadJt808SessionList">刷新</el-button>
        </template>
        <template #default="scope">
          <el-dropdown @command="handleCommand">
            <el-button type="primary">
              操作
              <el-icon class="el-icon--right">
                <arrow-down/>
              </el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                    command="9101"
                    :disabled="jt808ProxySession.selectedRow?.id !== scope.row.id">
                  下发0x9101 指令
                </el-dropdown-item>
                <el-dropdown-item
                    command="9102"
                    :disabled="jt808ProxySession.selectedRow?.id !== scope.row.id">
                  下发0x9102 指令
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <div class="demo-root">
    <el-card class="form-box">
      <el-form label-width="auto" size="small" :disabled="!editable">
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
      <flv-player
          ref="demoPlayerRef"
          class="player-box flv-player"
          :config="playerConfig"
          @on-player-status-change="onPlayerStatusChange"/>
    </el-card>
  </div>

  <el-dialog title="0x9101 指令下发" v-model="pageState.command9101Dialog.visible">
    <el-form class="container" label-width="auto" size="small">
      <el-form-item label="1078服务器IP(域名)">
        <el-input v-model="jt1078ServerConfig.jt1078ServerHost" readonly disabled/>
      </el-form-item>
      <el-form-item label="1078服务器端口(TCP)">
        <el-input v-model="jt1078ServerConfig.jt1078ServerTcpPort" readonly disabled/>
      </el-form-item>
      <el-form-item label="1078服务器端口(UDP)">
        <el-input v-model="jt1078ServerConfig.jt1078ServerUdpPort" readonly disabled/>
      </el-form-item>
      <el-form-item label="终端手机号(SIM)">
        <el-input v-model="formData.sim" readonly disabled/>
      </el-form-item>
      <el-form-item label="通道号">
        <el-input v-model="formData.channelNumber" readonly disabled/>
      </el-form-item>
      <el-form-item label="数据类型">
        <el-radio-group v-model="formData.command9101.dataType">
          <el-radio-button :value="0">音视频</el-radio-button>
          <el-radio-button :value="1">视频</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="码流类型">
        <el-radio-group v-model="formData.command9101.streamType">
          <el-radio-button :value="0">主码流</el-radio-button>
          <el-radio-button :value="1">子码流</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="超时时间">
        <el-input-number v-model="formData.command9101.timeout" placeholder="20" :min="0" :max="60 * 60">
          <template #suffix>秒</template>
        </el-input-number>
      </el-form-item>
      <el-form-item label="客户端回复" v-if="pageState.command9101Dialog.commandResult">
        <el-input v-model="pageState.command9101Dialog.commandResult" type="textarea" autosize readonly disabled/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div style="display: flex; justify-content: center; align-items: center;">
        <el-button type="primary" @click="sendCommand9101" :loading="pageState.command9101Dialog.loading">发送指令
        </el-button>
        <el-button @click="pageState.command9101Dialog.visible = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
  <el-dialog title="0x9102 指令下发" v-model="pageState.command9102Dialog.visible">
    <el-form class="container" label-width="auto" size="small">
      <el-form-item label="终端手机号(SIM)">
        <el-input v-model="formData.sim" readonly disabled/>
      </el-form-item>
      <el-form-item label="通道号">
        <el-select v-model="formData.channelNumber" filterable placeholder="请选择通道">
          <el-option v-for="item in channelOptions" :key="item.channel" :label="item.title" :value="item.channel">
            {{ item.title }}
            <el-tag type="danger" v-if="item.hasAudio && !item.hasVideo">暂不支持</el-tag>
            <el-tag type="primary" v-else-if="item.hasAudio && item.hasVideo">仅视频(H.264)</el-tag>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="控制指令">
        <el-select v-model="formData.command9102.commandType" filterable placeholder="请选择控制指令">
          <el-option label="(0) 关闭音视频传输指令" :value="0"/>
          <el-option label="(1) 切换码流(增加暂停和继续)" :value="1"/>
          <el-option label="(2) 暂停该通道所有流的发送" :value="2"/>
          <el-option label="(3) 恢复暂停前流的发送，与暂停前的流类型一致" :value="3"/>
          <el-option label="(4) 关闭双向对讲" :value="4"/>
        </el-select>
      </el-form-item>
      <el-form-item label="关闭音视频类型">
        <el-select v-model="formData.command9102.mediaTypeToClose" filterable placeholder="请选择关闭音视频类型">
          <el-option label="(0) 关闭该通道有关的音视频数据" :value="0"/>
          <el-option label="(1) 只关闭该通道有关的音频，保留该通道有关的视频" :value="1"/>
          <el-option label="(2) 只关闭该通道有关的视频，保留该通道有关的音频" :value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item label="切换码流类型">
        <el-radio-group v-model="formData.command9102.streamTypeToSwitch">
          <el-radio-button :value="0">主码流</el-radio-button>
          <el-radio-button :value="1">子码流</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="超时时间">
        <el-input-number v-model="formData.command9102.timeout" placeholder="20" :min="0" :max="60 * 60">
          <template #suffix>秒</template>
        </el-input-number>
      </el-form-item>
      <el-form-item label="客户端回复" v-if="pageState.command9102Dialog.commandResult">
        <el-input v-model="pageState.command9102Dialog.commandResult" type="textarea" autosize readonly disabled/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div style="display: flex; justify-content: center; align-items: center;">
        <el-button type="primary" @click="sendCommand9102" :loading="pageState.command9102Dialog.loading">发送指令
        </el-button>
        <el-button @click="pageState.command9102Dialog.visible = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
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

.container {
  box-sizing: border-box;
  border: 1px solid var(--el-border-color);
  border-radius: 10px;
  padding: 10px;
}
</style>
