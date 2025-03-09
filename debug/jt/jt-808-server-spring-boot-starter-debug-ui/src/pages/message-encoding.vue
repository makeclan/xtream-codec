<script setup lang="ts">
import {computed, onMounted, reactive, ref} from "vue";
import {requestCodecOptionsApi, requestEncodeMessageApi} from "../api/codec-api.ts";
import {ClassMetadata, EncodeResult} from "../types/model.ts";
import {codecMockData, toHexString} from "../utils/codec-utils.ts";
import {getLocalStorage, setLocalStorage} from "../utils/storage.ts";

enum EncodeMode {
  SINGLE, MULTIPLE
}

interface PageState {
  encodeMode?: EncodeMode;
  query: {
    terminalId: string,
    version: string,
    encryptionType: number;
    bodyClass: string;
    messageId: number;
    hasBodyData?: boolean;
    flowId: number;
    reversedBit15InHeader: number;
    maxPackageSize: number;
    bodyData: object;
  };
  temp: { bodyJsonString: string },
  classMetadataOptions: Array<ClassMetadata> | [];
  encodeResult: Array<EncodeResult> | [];
}

const pageState = reactive<PageState>(<PageState>{
  encodeMode: undefined,
  temp: {
    bodyJsonString: JSON.stringify({
      "alarmFlag": 111,
      "status": 22,
      "latitude": 31234567,
      "longitude": 121987654,
      "altitude": 123,
      "speed": 66,
      "direction": 3,
      "time": "2025-01-29T22:33:44Z",
      "extraItems": {
        "2": 100,
        "1": 12
      }
    }, null, 4)
  },
  query: {
    bodyClass: "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0200",
    terminalId: "00000000013912344329",
    version: "VERSION_2019",
    flowId: 0,
    encryptionType: 0,
    messageId: 512,
    maxPackageSize: 1024,
    reversedBit15InHeader: 0,
    bodyData: {}
  },
  classMetadataOptions: [],
  encodeResult: []
})
const multiPackageHexString = computed(() => {
  if (!pageState || !pageState.encodeResult || pageState.encodeResult.length <= 0) {
    return undefined
  }
  return pageState.encodeResult.map(it => it.escapedHexString).join('\n')
})
const onEncodeBtbClick = async () => {
  console.log(pageState.query)
  const input = pageState.temp.bodyJsonString
  if (!input) {
    return
  }
  pageState.query.bodyData = JSON.parse(input)
  const resp: Array<EncodeResult> = await requestEncodeMessageApi(pageState.query)

  pageState.encodeMode = resp.length > 1 ? EncodeMode.MULTIPLE : EncodeMode.SINGLE
  pageState.encodeResult = resp
  console.log(resp)
}
const bodyClassStorageKey = "xtream-codec-encoding-entity-class"
const onEntityClassChange = (value: keyof object) => {
  const data = codecMockData[value]
  if (data) {
    pageState.query.messageId = data.messageId
    pageState.query.hasBodyData = data.hasBodyData
    pageState.temp.bodyJsonString = JSON.stringify(data.bodyJson || {}, null, 4)
  } else {
    pageState.query.messageId = 0
    pageState.temp.bodyJsonString = "{}"
  }
  setLocalStorage(bodyClassStorageKey, pageState.query.bodyClass)
}
const filteredOptions = ref<ClassMetadata[]>(pageState.classMetadataOptions);
const filterEntityClass = (value: string) => {
  if (!value || value.trim() === "") {
    filteredOptions.value = pageState.classMetadataOptions
  } else {
    filteredOptions.value = pageState.classMetadataOptions.filter(it => it.targetClass.includes(value) || it.desc.includes(value))
  }
}
const defaultProps = {
  children: 'children',
  label: 'spanType',
}
const loadClassMetadata = async () => {
  const {defaultTerminalId, classMetadata} = await requestCodecOptionsApi()
  pageState.classMetadataOptions = classMetadata
  pageState.query.terminalId = defaultTerminalId
}
onMounted(async () => {
  await loadClassMetadata()
  const savedEntityClass = getLocalStorage<string>(bodyClassStorageKey)
  if (savedEntityClass) {
    pageState.query.bodyClass = savedEntityClass
    onEntityClassChange(savedEntityClass as keyof object)
  }
})
</script>

<template>
  <div>
    <div>

      <el-alert type="success" title="提示" closable style="margin-bottom: 20px;">
        该功能会在后续版本中集成到 Dashboard 中
      </el-alert>

      <el-form size="small" label-width="auto" label-position="left">
        <el-form-item label="实体类">
          <el-select
              v-model="pageState.query.bodyClass"
              filterable
              @change="onEntityClassChange"
              :filter-method="filterEntityClass"
          >
            <template #label="scope">
              {{ scope.label }}
              <el-tag
                  style="margin-left: 10px;"
                  type="danger"
                  v-if="pageState.classMetadataOptions.find(it=>it.targetClass === scope.value)?.desc">
                {{ pageState.classMetadataOptions.find(it => it.targetClass === scope.value)?.desc }}
              </el-tag>
            </template>
            <el-option
                v-for="(item,idx) in filteredOptions"
                :key="idx"
                :value="item.targetClass"
                :label="item.targetClass">
              {{ item.targetClass }}
              <el-tag v-if="item.desc" type="danger">{{ item.desc }}</el-tag>
            </el-option>
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="消息ID">
              <el-input-number v-model="pageState.query.messageId" :controls="false" disabled style="min-width: 150px;">
                <template #suffix>
                  <el-tag type="danger">{{ toHexString(pageState.query.messageId) }}</el-tag>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="流水号">
              <el-input-number v-model="pageState.query.flowId"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="保留位(消息头bit15)">
              <el-radio-group v-model="pageState.query.reversedBit15InHeader">
                <el-radio-button :value="0" label="0"/>
                <el-radio-button :value="1" label="1"/>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="加密类型">
              <el-input-number v-model="pageState.query.encryptionType" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="单包最大字节数">
              <el-input-number v-model="pageState.query.maxPackageSize"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="版本">
              <el-radio-group v-model="pageState.query.version">
                <el-radio-button label="2019" value="VERSION_2019"/>
                <el-radio-button label="2013" value="VERSION_2013"/>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="终端手机号">
              <el-input v-model="pageState.query.terminalId"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="消息体">
          <el-input v-model="pageState.temp.bodyJsonString" type="textarea" :rows="5" autosize
                    :disabled="pageState.query.hasBodyData === false"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onEncodeBtbClick" style="width: 100%;" size="large">编码</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="pageState.encodeMode !== undefined">
      <div v-if="multiPackageHexString && pageState.encodeResult.length > 1" class="multi-package-hex-string">
        <el-alert type="success" :title="`当前报文编码为 ${pageState.encodeResult.length} 条分包报文`"
                  style="width: 100%;">
          <template #default>
            <el-input v-model="multiPackageHexString" type="textarea" autosize readonly/>
            分包详情如下：
          </template>
        </el-alert>
      </div>
      <el-card v-for="(item,index) in pageState.encodeResult" :key="index"
               style="border-radius: 10px;margin-bottom: 10px;">
        <template #header>
          #{{ index + 1 }}/{{ pageState.encodeResult.length }}
        </template>
        <div>
          <el-collapse :model-value="['1','2','3','4','5']">
            <el-collapse-item name="1">
              <template #title>
                转义前
              </template>
              <el-input v-model="item.rawHexString" type="textarea" readonly autosize/>
            </el-collapse-item>
            <el-collapse-item name="2">
              <template #title>
                转义后&nbsp;<el-tag
                  v-if="item.rawHexString === item.escapedHexString"
                  type="success">无转义字符
              </el-tag>
                <el-tag v-else type="danger">有转义字符</el-tag>
              </template>
              <el-input v-model="item.escapedHexString" type="textarea" readonly autosize/>
            </el-collapse-item>
            <el-collapse-item title="报文结构" name="3">
              <el-tree
                  style="width: 100%;overflow-x: scroll;"
                  :data="[item.details]"
                  node-key="id"
                  default-expand-all
                  :expand-on-click-node="false"
                  :props="defaultProps"
              >
                <template #default="{ data }">
        <span class="custom-tree-node">
          <el-tag type="info">{{ data.fieldName || data.spanType }}</el-tag>
          <span v-if="data.spanType === 'RootSpan'">
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
            <el-tag type="success">实体类: {{ data.entityClass }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'VirtualEntitySpan' ">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
            <el-tag type="success">实体类: {{ data.entityClass }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'VirtualFieldSpan' ">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
            <el-tag type="warning">原始值: {{ data.value }}</el-tag>
            <el-tag type="success">类型: {{ data.fieldType }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'NestedFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
            <el-tag type="success">类型: {{ data.fieldType }}</el-tag>
          </span>
          <span v-else-if="data.spanType == 'BasicFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
            <el-tag type="warning">原始值: {{ data.value }}</el-tag>
            <el-tag type="success">解码器: {{ data.fieldCodec }}</el-tag>
          </span>
          <span v-else-if="data.spanType == 'PrependLengthFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
            <el-tag type="warning">原始值: {{ data.value }}</el-tag>
            <el-tag type="success">解码器: {{ data.fieldCodec }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'CollectionFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
            <el-tag type="warning">类型: {{ data.fieldType }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'CollectionItemSpan'">
            <el-tag type="warning">offset: {{ data.offset }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'MapFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'MapEntrySpan'">
            <el-tag type="warning">offset: {{ data.offset }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'MapEntryItemSpan'">
            <el-tag type="primary">类型: {{ data.type }}</el-tag>
            <el-tag type="primary">编码结果(HEX): {{ data.hexString }}</el-tag>
            <el-tag type="warning">原始值: {{ data.value }}</el-tag>
            <el-tag type="success">解码器: {{ data.fieldCodec }}</el-tag>
          </span>
        </span>
                </template>
              </el-tree>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-card>

    </div>
  </div>
</template>

<style scoped lang="scss">
.custom-tree-node {
  .el-tag {
    &:not(:last-child) {
      margin-right: 10px;
    }
  }
}

.multi-package-hex-string {
  margin-bottom: 20px;

  ::v-deep(.el-alert__content) {
    width: 100%;
  }
}
</style>
