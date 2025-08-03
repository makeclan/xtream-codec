<script setup lang="ts">
import {computed, onMounted, reactive, Ref, ref} from "vue";
import FlvPlayer, {FlvPlayerConfig, FlvPlayerStatus} from "../components/FlvPlayer.vue";
import {jt1078ChannelConfig} from "../types/model.ts";
import {QuestionFilled} from "@element-plus/icons-vue";
import {
  requestJt1078ServerConfigApi,
  requestProxy9101CommandApi,
  requestProxy9102CommandApi,
  requestProxyJt808SessionsApi
} from "../api/proxy-api.ts";
import {Jt808ProtocolVersion, Jt808Session, TransportProtocolType} from "../model/jt808-models.ts";
import {
  Command9101CommandType,
  Jt1078DataType,
  Jt1078ServerConfig,
  Jt1078StreamType,
  MediaTypeToClose
} from "../model/jt1078-models.ts";
import {ElMessage} from "element-plus";

const pageState = reactive({
  commandDialog: {
    visible: false,
    activeTab: "command9101",
    autoCloseIfSuccess: true,
    command9101: {
      loading: false,
      commandResult: "",
    },
    command9102: {
      loading: false,
      commandResult: "",
    },
  },
  jt808SessionPanel: {
    visible: false,
  },
})

const closeCommandPanel = (panelToSwitch?: string) => {
  pageState.commandDialog.visible = false
  console.log(panelToSwitch)
  if (panelToSwitch) {
    pageState.commandDialog.activeTab = panelToSwitch
  }
}

const jt1078ServerConfig = ref<Jt1078ServerConfig>({} as Jt1078ServerConfig)
const jt808ProxySession = reactive<{
  data?: Jt808Session[],
  total?: number,
  selectedRow?: Jt808Session | undefined,
  formData: {
    terminalId: undefined,
    protocolVersion: Jt808ProtocolVersion,
    protocolType: TransportProtocolType,
    pageNumber: number,
    pageSize: number,
  }
}>({
  data: [],
  total: 0,
  selectedRow: undefined,
  formData: {
    terminalId: undefined,
    protocolVersion: '',
    protocolType: '',
    pageNumber: 1,
    pageSize: 10,
  }
})
const sessionPaginationCurrentChange = async (pageNumber: number) => {
  jt808ProxySession.formData.pageNumber = pageNumber
  await loadJt808SessionList()
}
const sessionPaginationSizeChange = async (pageSize: number) => {
  jt808ProxySession.formData.pageNumber = 1
  jt808ProxySession.formData.pageSize = pageSize
  await loadJt808SessionList()
}
const selectCurrentRowSession = (row: any) => {
  if (!editable.value) {
    return
  }
  jt808ProxySession.selectedRow = row
  formData.sim = row.terminalId
  console.log(row)
  pageState.jt808SessionPanel.visible = false
}

const tableRowClassName = ({row}: { row: Jt808Session }) => {
  if (row.id === jt808ProxySession.selectedRow?.id) {
    return 'current-selected-row';
  }
  return '';
};
const formData = reactive<{
  protocol: 'ws' | 'wss' | 'http' | 'https',
  host: string,
  port: number,
  sim: string | undefined,
  channelNumber: number,
  timeout: number,
  hasVideo: boolean,
  hasAudio: boolean,
  naluDecoderRingBufferSizeExponent: number,
  command9101: { dataType: Jt1078DataType, streamType: Jt1078StreamType, timeout: number },
  command9102: {
    commandType: Command9101CommandType,
    mediaTypeToClose: MediaTypeToClose,
    streamTypeToSwitch: Jt1078StreamType,
    timeout: number
  },
}>({
  protocol: 'ws',
  host: 'localhost',
  port: 9999,
  sim: undefined,
  channelNumber: 1,
  // 超过30秒没有音视频数据 断开连接
  timeout: 30,
  hasVideo: true,
  hasAudio: true,
  naluDecoderRingBufferSizeExponent: 18,
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
const naluDecoderRingBufferSize = computed(() => {
  return 2 ** formData.naluDecoderRingBufferSizeExponent
})
const onChannelChange = (ch: number) => {
  const option = channelOptions.find(it => it.channel === ch)
  if (option) {
    formData.hasVideo = option.hasVideo;
    formData.hasAudio = option.hasAudio;
  }
}
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
  const {hasVideo, hasAudio} = formData;
  const bufferSize = naluDecoderRingBufferSize.value;
  // dashboard-api/jt1078/v1/stream-data/http/flv/{sim}/{channel}
  // dashboard-api/jt1078/v1/stream-data/ws/flv/{sim}/{channel}
  return `${formData.protocol}://${formData.host}:${formData.port}/dashboard-api/jt1078/v1/stream-data/${type}/flv/${formData.sim}/${formData.channelNumber}?timeout=${formData.timeout}&hasVideo=${hasVideo}&hasAudio=${hasAudio}&naluDecoderRingBufferSize=${bufferSize}`
})
const playerStatus: Ref<FlvPlayerStatus> = ref(FlvPlayerStatus.OFFLINE)
const onPlayerStatusChange = (from: FlvPlayerStatus, to: FlvPlayerStatus, message?: string) => {
  console.log('onPlayerStatusChange', from, to, message)
  playerStatus.value = to
}

const editable = computed(() => playerStatus.value === FlvPlayerStatus.OFFLINE)

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
  pageState.commandDialog.command9101.loading = true
  const response = await requestProxy9101CommandApi(params).finally(() => {
    pageState.commandDialog.command9101.loading = false
  })
  pageState.commandDialog.command9101.commandResult = JSON.stringify(response, null, 2)

  if (response.result === 0) {
    if (pageState.commandDialog.autoCloseIfSuccess) {
      closeCommandPanel('command9102')
      ElMessage({
        message: '0x9101 指令下发成功',
        type: 'success',
        showClose: true,
      })
    }
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
  pageState.commandDialog.command9102.loading = true
  const result = await requestProxy9102CommandApi(params).finally(() => {
    pageState.commandDialog.command9102.loading = false
    resetPlayer()
  })

  if (result.result === 0) {
    if (pageState.commandDialog.autoCloseIfSuccess) {
      closeCommandPanel('command9101')
      ElMessage({
        message: '0x9102 指令下发成功',
        type: 'success',
        showClose: true,
      })
    }
  }
  pageState.commandDialog.command9102.commandResult = JSON.stringify(result, null, 2)

}

const loadJt808SessionList = async () => {
  const params = {
    terminalId: jt808ProxySession.formData.terminalId,
    pageNumber: jt808ProxySession.formData.pageNumber,
    protocolType: jt808ProxySession.formData.protocolType,
    protocolVersion: jt808ProxySession.formData.protocolVersion,
    pageSize: jt808ProxySession.formData.pageSize,
  };
  const {data, total} = await requestProxyJt808SessionsApi(params)
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
  formData.port = jt1078ServerConfig.value.jt1078ServerWebPort
  formData.host = window.location.hostname
  await loadJt808SessionList();
})
</script>

<template>
  <div class="demo-root">
    <el-card class="form-box">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center; height: 15px;">
          <div style="display: flex; justify-content: end; width: 100%; font-weight: bold;">H.264 -&gt; FLV</div>
          <div style="display: flex; justify-content: end; width: 100%;">
            <el-button type="success" @click="pageState.jt808SessionPanel.visible = true">选择设备
            </el-button>
            <el-button type="success" @click="pageState.commandDialog.visible = true">下发指令
            </el-button>
          </div>
        </div>
      </template>
      <div>
        <el-form label-width="auto" size="small" :disabled="!editable">
          <el-divider style="margin-top: 0;">JT/T 1078 服务器配置</el-divider>
          <el-form-item label="1078服务器IP(域名)">
            <template #label>
              <div style="display: flex;align-items: center;">
                <span>1078服务器IP(域名)</span>
                <el-tooltip content="JT/T 1078 码流会上传到这个地址(设备能访问即可，不一定要公网地址)"
                            placement="top">
                  <el-icon class="el-icon-info" style="margin-left: 5px;">
                    <QuestionFilled/>
                  </el-icon>
                </el-tooltip>
              </div>
            </template>
            <el-input v-model="jt1078ServerConfig.jt1078ServerHost"/>
          </el-form-item>
          <el-form-item label="1078服务器端口(TCP)">
            <el-input v-model="jt1078ServerConfig.jt1078ServerTcpPort"/>
          </el-form-item>
          <el-form-item label="1078服务器端口(UDP)">
            <el-input v-model="jt1078ServerConfig.jt1078ServerUdpPort">
              <template #suffix>
                <el-tag type="success">已支持UDP</el-tag>
              </template>
            </el-input>
          </el-form-item>
          <el-divider>码流订阅配置</el-divider>
          <el-alert v-if="!formData.sim" title="请先【选择设备】" type="error" effect="dark"
                    style="margin-bottom: 20px;"/>
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
            <div style="width: 100%; display: flex; justify-content: space-between; align-items: center;">
              <el-input v-model="formData.sim" placeholder="终端手机号：点击【选择设备】按钮"/>
            </div>
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
          ref="demoPlayerRef"
          class="player-box flv-player"
          style="border: 0;"
          :config="playerConfig"
          @on-player-status-change="onPlayerStatusChange"/>
    </div>
  </div>

  <el-dialog title="指令下发" v-model="pageState.commandDialog.visible">
    <el-tabs v-model="pageState.commandDialog.activeTab">
      <el-tab-pane label="0x9101" name="command9101">
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
          <el-form-item label="自动关闭">
            <el-checkbox v-model="pageState.commandDialog.autoCloseIfSuccess">如果指令下发成功，自动关闭对话框
            </el-checkbox>
          </el-form-item>
          <el-form-item label="客户端回复" v-if="pageState.commandDialog.command9101.commandResult">
            <el-input v-model="pageState.commandDialog.command9101.commandResult" type="textarea" autosize readonly
                      disabled/>
          </el-form-item>
          <el-form-item>
            <div style="display: flex; justify-content: center; align-items: center; width: 100%;">
              <el-button type="success" size="default" @click="sendCommand9101"
                         :loading="pageState.commandDialog.command9101.loading">发送指令
              </el-button>
              <el-button type="primary" size="default" @click="() => closeCommandPanel(undefined)">关闭</el-button>
            </div>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="0x9102" name="command9102">
        <el-form class="container" label-width="auto" size="small">
          <el-form-item label="终端手机号(SIM)">
            <el-input v-model="formData.sim" readonly disabled/>
          </el-form-item>
          <el-form-item label="通道号">
            <el-select v-model="formData.channelNumber" filterable placeholder="请选择通道">
              <el-option v-for="item in channelOptions" :key="item.channel" :label="item.title" :value="item.channel">
                {{ item.title }}
                <el-tag :type="!item.hasVideo ? 'danger': 'success'">{{ item.remark }}</el-tag>
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
          <el-form-item label="自动关闭">
            <el-checkbox v-model="pageState.commandDialog.autoCloseIfSuccess">如果指令下发成功，自动关闭对话框
            </el-checkbox>
          </el-form-item>
          <el-form-item label="客户端回复" v-if="pageState.commandDialog.command9102.commandResult">
            <el-input v-model="pageState.commandDialog.command9102.commandResult" type="textarea" autosize readonly
                      disabled/>
          </el-form-item>
          <el-form-item>
            <div style="display: flex; justify-content: center; align-items: center; width: 100%;">
              <el-button type="success" size="default" @click="sendCommand9102"
                         :loading="pageState.commandDialog.command9102.loading">发送指令
              </el-button>
              <el-button type="primary" size="default" @click="() => closeCommandPanel(undefined)">关闭</el-button>
            </div>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>

  <el-drawer
      v-model="pageState.jt808SessionPanel.visible"
      title="JT/T 808 终端列表"
      direction="rtl"
      size="calc(100vw - 100px)"
  >
    <div>
      <el-form inline size="small" style="width: 100%; display: flex; align-items: center; justify-content: center;">
        <el-form-item label="SIM">
          <el-input v-model="jt808ProxySession.formData.terminalId" clearable placeholder="终端手机号"/>
        </el-form-item>
        <el-form-item label="协议">
          <el-radio-group v-model="jt808ProxySession.formData.protocolType">
            <el-radio-button :value="'TCP'" label="TCP"></el-radio-button>
            <el-radio-button :value="'UDP'" label="UDP"></el-radio-button>
            <el-radio-button :value="''" label="不限"></el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="版本">
          <el-radio-group v-model="jt808ProxySession.formData.protocolVersion">
            <el-radio-button :value="'VERSION_2011'">2011</el-radio-button>
            <el-radio-button :value="'VERSION_2013'">2013</el-radio-button>
            <el-radio-button :value="'VERSION_2019'">2019</el-radio-button>
            <el-radio-button :value="''">不限</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadJt808SessionList">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="jt808ProxySession.data"
                :total="jt808ProxySession.total"
                :page-size="10"
                border
                :row-class-name="tableRowClassName"
                style="width: 100%;">
        <el-table-column label="ID" prop="id" fixed="left" show-overflow-tooltip width="510"/>
        <el-table-column label="协议" prop="protocolType" width="70">
          <template #default="scope">
            <el-tag v-if="scope.row.protocolType === 'TCP'" type="success">TCP</el-tag>
            <el-tag v-else-if="scope.row.protocolType === 'UDP'" type="success">UDP</el-tag>
            <el-tag v-else type="primary">{{ scope.row.protocolVersion }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="版本" prop="protocolVersion" width="80">
          <template #default="scope">
            <el-tag v-if="scope.row.protocolVersion === 'VERSION_2011'" type="info">2019</el-tag>
            <el-tag v-else-if="scope.row.protocolVersion === 'VERSION_2013'" type="success">2013</el-tag>
            <el-tag v-else-if="scope.row.protocolVersion === 'VERSION_2019'" type="primary">2019</el-tag>
            <el-tag v-else>{{ scope.row.protocolVersion }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="SIM" prop="terminalId" min-width="200"/>
        <el-table-column label="最近一次通信时间" prop="lastCommunicateTime" min-width="210"/>
        <el-table-column label="创建时间" prop="creationTime" min-width="210"/>
        <el-table-column label="操作" fixed="right" width="120" prop="ops">
          <template #default="scope">
            <el-button type="primary" size="small" @click="selectCurrentRowSession(scope.row)">选择设备</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="width: 100%; display: flex; align-items: center; justify-content: center; margin-top: 10px;">
        <el-pagination
            v-model:current-page="jt808ProxySession.formData.pageNumber"
            v-model:page-size="jt808ProxySession.formData.pageSize"
            :total="jt808ProxySession.total"
            :page-sizes="[10, 20, 50, 75, 100, 200, 300, 400]"
            :default-page-size="10"
            :default-current-page="1"
            background
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="sessionPaginationCurrentChange"
            @size-change="sessionPaginationSizeChange"
        />
      </div>
    </div>
  </el-drawer>
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

.container {
  box-sizing: border-box;
  border: 1px solid var(--el-border-color);
  border-radius: 10px;
  padding: 10px;
}

:deep(.el-table .current-selected-row) {
  --el-table-tr-bg-color: var(--el-color-primary-light-5);
}
</style>
