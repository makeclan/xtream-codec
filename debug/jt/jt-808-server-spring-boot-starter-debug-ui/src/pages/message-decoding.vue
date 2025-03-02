<script setup lang="ts">
import {onMounted, reactive, ref} from "vue";
import {requestCodecOptionsApi, requestDecodeMessageApi} from "../api/codec-api.ts";
import {ClassMetadata, DecodeResult} from "../types/model.ts";
import {codecMockData, toBinaryString, toHexString} from "../utils/codec-utils.ts";
import {getLocalStorage, setLocalStorage} from "../utils/storage.ts";

enum DecodeMode {
  SINGLE, MULTIPLE
}

interface PageState {
  decodeMode?: DecodeMode;
  query: {
    hexString: string;
    bodyClass: string;
  };
  classMetadataOptions: Array<ClassMetadata> | [];
  decodeResult: DecodeResult;
}

const pageState = reactive<PageState>(<PageState>{
  decodeMode: undefined,
  query: {
    hexString: "7e0200407c010000000001391234432900000000007b000000de01d907f2073d336c029a004e000014102119510901040000006f020200de0302029a0402014d2504000003093001211206010000000001130700000001000000310137642f000000dedf6f03420b0c0d0e0f04d201d907f2073d336c1410211951090001313233343536371410211951090101008e7e",
    bodyClass: "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0200Sample2",
    // hexString: "7e0704403f01000000000139123443290000000200001c000000010000000101d907f2073d336c029a00210059241029213903001c000000010000000101d907f3073d336d03090043005a241029213903f87e",
    // bodyClass: "io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.request.BuiltinMessage0704"
  },
  classMetadataOptions: [],
  decodeResult: {single: {}, multiple: {}}
})

const onDecodeBtbClick = async () => {
  console.log(pageState.query)
  const input = pageState.query.hexString
  if (!input) {
    return
  }
  const lines = input.split("\n")
  const resp: DecodeResult = await requestDecodeMessageApi({
        hexString: lines,
        // hexString: ["7e0200601001000000000139123443290000000300010000006f0000001601dc9a07074562465a7e",
        //   "7e020060100100000000013912344329000100030002007b0042000325012922334402020064017e",
        //   "7e02006006010000000001391234432900020003000301040000000c1a7e"],
        bodyClass: pageState.query.bodyClass
      }
  )
  pageState.decodeMode = resp.single ? DecodeMode.SINGLE : DecodeMode.MULTIPLE
  pageState.decodeResult = resp
  console.log(resp)
  if (resp.single) {
    pageState.decodeMode = DecodeMode.SINGLE
    treeData.value = [pageState.decodeResult.single.details]
  } else {
    pageState.decodeMode = DecodeMode.MULTIPLE
    treeData.value = [pageState.decodeResult.multiple.details]
  }
}
const bodyClassStorageKey = "xtream-codec-decoding-entity-class"
const onEntityClassChange = (value: keyof object) => {
  const data = codecMockData[value]
  if (data) {
    pageState.query.hexString = data.hexString;
    setLocalStorage(bodyClassStorageKey, pageState.query)
  } else {
    pageState.query.hexString = ""
  }
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
const treeData = ref<any>([])
const loadClassMetadata = async () => {
  const {classMetadata} = await requestCodecOptionsApi()
  pageState.classMetadataOptions = classMetadata
}
onMounted(async () => {
  await loadClassMetadata()
  const savedEntityClass = getLocalStorage<{ bodyClass: string, hexString: string }>(bodyClassStorageKey)
  if (savedEntityClass) {
    pageState.query = savedEntityClass
  }
})
</script>

<template>
  <div>
    <div>
      <el-form size="small" label-width="auto">
        <el-form-item label="实体类">
          <el-select
              v-model="pageState.query.bodyClass"
              filterable
              @change="onEntityClassChange"
              :filter-method="filterEntityClass">
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
        <el-form-item label="十六进制报文">
          <el-input v-model="pageState.query.hexString" type="textarea" :rows="3"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onDecodeBtbClick" style="width: 100%;" size="large">解码</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="pageState.decodeMode !== undefined">
      <el-collapse :model-value="['1','2','3','4','5']">
        <template v-if="pageState.decodeMode === DecodeMode.SINGLE">
          <el-collapse-item name="1">
            <template #title>
              转义前
            </template>
            <el-input v-model="pageState.decodeResult.single.rawHexString" type="textarea" readonly
                      autosize></el-input>
          </el-collapse-item>
          <el-collapse-item name="2">
            <template #title>
              转义后&nbsp;<el-tag
                v-if="pageState.decodeResult.single.rawHexString === pageState.decodeResult.single.escapedHexString"
                type="success">无转义字符
            </el-tag>
              <el-tag v-else type="danger">有转义字符</el-tag>
            </template>
            <el-input v-model="pageState.decodeResult.single.escapedHexString" type="textarea" readonly autosize/>
          </el-collapse-item>
        </template>
        <template v-if="pageState.decodeMode === DecodeMode.MULTIPLE">
          <el-collapse-item title="子包信息" name="5">
            <el-descriptions
                v-for="(item,index) in pageState.decodeResult.multiple.subPackageMetadata"
                :key="index"
                :column="7"
                direction="vertical"
                border
                style="margin-top: 20px;">
              <el-descriptions-item label="子包序号">
                <el-tag type="primary">
                  {{ item.header.subPackageProps.currentPackageNo }}/{{
                    item.header.subPackageProps.totalSubPackageCount
                  }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="消息ID">{{ toHexString(item.header.messageId) }}</el-descriptions-item>
              <el-descriptions-item label="流水号">{{ item.header.flowId }}</el-descriptions-item>
              <el-descriptions-item label="终端手机号">{{ item.header.terminalId }}</el-descriptions-item>
              <el-descriptions-item label="版本">{{ item.header.protocolVersion }}</el-descriptions-item>
              <el-descriptions-item label="消息体长度">{{
                  item.header.bodyProps.messageBodyLength
                }}
              </el-descriptions-item>
              <el-descriptions-item label="数据加密方式">
                {{ toBinaryString(item.header.bodyProps.encryptionType, 3) }}
              </el-descriptions-item>
              <el-descriptions-item label="消息体数据">
                <el-input v-model="item.bodyHexString" readonly autosize/>
              </el-descriptions-item>
            </el-descriptions>
          </el-collapse-item>
          <el-collapse-item title="合并后报文" name="4">
            <el-input v-model="pageState.decodeResult.multiple.mergedHexString" type="textarea" readonly autosize/>
          </el-collapse-item>
        </template>
        <el-collapse-item title="报文结构" name="3">
          <el-tree
              style="width: 100%;overflow-x: scroll;"
              :data="treeData"
              node-key="id"
              default-expand-all
              :expand-on-click-node="false"
              :props="defaultProps"
          >
            <template #default="{ data }">
        <span class="custom-tree-node">
          <el-tag type="info">{{ data.fieldName || data.spanType }}</el-tag>
          <span v-if="data.spanType === 'RootSpan'">
            <el-tag type="primary">十六进制: {{ data.hexString }}</el-tag>
            <el-tag type="success">实体类: {{ data.entityClass }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'NestedFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">十六进制: {{ data.hexString }}</el-tag>
            <el-tag type="success">类型: {{ data.fieldType }}</el-tag>
          </span>
          <span v-else-if="data.spanType == 'BasicFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">十六进制: {{ data.hexString }}</el-tag>
            <el-tag type="warning">解码结果: {{ data.value }}</el-tag>
            <el-tag type="success">解码器: {{ data.fieldCodec }}</el-tag>
          </span>
          <span v-else-if="data.spanType == 'PrependLengthFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">十六进制: {{ data.hexString }}</el-tag>
            <el-tag type="warning">解码结果: {{ data.value }}</el-tag>
            <el-tag type="success">解码器: {{ data.fieldCodec }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'CollectionFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">十六进制: {{ data.hexString }}</el-tag>
            <el-tag type="warning">类型: {{ data.fieldType }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'CollectionItemSpan'">
            <el-tag type="warning">offset: {{ data.offset }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'MapFieldSpan'">
            <el-tag type="danger" v-if="data.fieldDesc">{{ data.fieldDesc }}</el-tag>
            <el-tag type="primary">十六进制: {{ data.hexString }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'MapEntrySpan'">
            <el-tag type="warning">offset: {{ data.offset }}</el-tag>
            <el-tag type="primary">十六进制: {{ data.hexString }}</el-tag>
          </span>
          <span v-else-if="data.spanType === 'MapEntryItemSpan'">
            <el-tag type="primary">类型: {{ data.type }}</el-tag>
            <el-tag type="primary">十六进制: {{ data.hexString }}</el-tag>
            <el-tag type="warning">解码结果: {{ data.value }}</el-tag>
            <el-tag type="success">解码器: {{ data.fieldCodec }}</el-tag>
          </span>
        </span>
            </template>
          </el-tree>
        </el-collapse-item>
      </el-collapse>
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
</style>
