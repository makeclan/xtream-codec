<script setup lang="ts">
import {onMounted, reactive} from 'vue'
import {Command8104Response, TerminalVo} from "../types/model.ts";
import {requestSendCommand8104, requestTerminalList} from "../api/terminal-api.ts";

interface PageState {
  data: TerminalVo[],
  total: number,
  tableDataLoading: boolean,
  query: {
    page: number,
    pageSize: number,
    terminalId?: string,
    protocolVersion?: string,
    protocolType?: string,
  }
}

const pageState = reactive<PageState>({
  data: [],
  total: 0,
  tableDataLoading: false,
  query: {
    page: 1,
    pageSize: 5,
    terminalId: undefined,
    protocolVersion: "",
    protocolType: ""
  },
} as PageState)
const command8104State = reactive<{
  loading: boolean,
  data?: Command8104Response,
  timeout: number
}>({loading: false, data: undefined, timeout: 30})
const loadData = async () => {
  pageState.tableDataLoading = true
  const {data, total} = await requestTerminalList(pageState.query);
  pageState.tableDataLoading = false
  pageState.data = data;
  pageState.total = total;
}
const sendCommand8104 = async (sessionId: string) => {
  command8104State.loading = true
  command8104State.data = await requestSendCommand8104({sessionId, timeout: command8104State.timeout});
  command8104State.loading = false
}
onMounted(async () => {
  await loadData()
})

</script>

<template>
  <el-card class="my-card">
    <el-form inline style="display: flex;align-items: center;justify-items: center;">
      <el-form-item label="终端手机号" style="margin-bottom: 0;">
        <el-input v-model="pageState.query.terminalId" placeholder="终端手机号" clearable/>
      </el-form-item>
      <el-form-item label="协议版本" style="margin-bottom: 0;">
        <el-radio-group
            v-model="pageState.query.protocolVersion"
            @change="async ()=>await loadData()">
          <el-radio-button value="">不限</el-radio-button>
          <el-radio-button value="VERSION_2019">2019</el-radio-button>
          <el-radio-button value="VERSION_2013">2013</el-radio-button>
          <el-radio-button value="VERSION_2011">2011</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="网络类型" style="margin-bottom: 0;">
        <el-radio-group
            v-model="pageState.query.protocolType"
            @change="async ()=>await loadData()">
          <el-radio-button value="">不限</el-radio-button>
          <el-radio-button value="TCP">TCP</el-radio-button>
          <el-radio-button value="UDP">UDP</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item style="margin-bottom: 0;">
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-card class="my-card">
    <template #header>在线终端列表</template>
    <div>
      <el-table
          :data="pageState.data"
          v-loading="pageState.tableDataLoading"
          border
          style="width: 100%;margin-top: 10px;">
        <el-table-column prop="id" label="ID" fixed min-width="500" show-overflow-tooltip/>
        <el-table-column prop="terminalId" label="终端手机号" min-width="200"/>
        <el-table-column prop="protocolVersion" label="协议版本" min-width="130">
          <template #default="scope">
            <el-tag :type="scope.row.protocolVersion === 'VERSION_2019' ? 'success' : 'info'">
              {{ scope.row.protocolVersion }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="protocolType" label="网络类型" fixed min-width="90">
          <template #default="scope">
            <el-tag :type="scope.row.protocolType === 'TCP' ? 'success' : 'info'">
              {{ scope.row.protocolType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creationTime" label="会话创建时间" width="200"/>
        <el-table-column prop="lastCommunicateTime" label="最后一次通信时间" width="200"/>
        <el-table-column label="操作" fixed="right" width="130">
          <template #default="scope">
            <el-button type="primary" size="small" @click="sendCommand8104(scope.row.id)">查询终端参数</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-card>
        <el-pagination
            :total="pageState.total"
            :current-page="pageState.query.page"
            :default-page-size="3"
            :page-size="pageState.query.pageSize"
            :page-sizes="[5,10,20,30,50,100]"
            :background="true"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="async (val:number) => {
          pageState.query.pageSize = val;
          await loadData();
        }"
            @current-change="async (val:number)=> {
          pageState.query.page = val;
          await loadData();
        }"
        />
      </el-card>
    </div>
  </el-card>
  <el-card class="my-card">
    <template #header>
      <div style="display: flex;align-items: center;">
        <h5 style="margin: 0 100px 0 0;">
          终端参数查询
        </h5>
        <el-form-item label="查询超时时间(秒)" style="margin-bottom: 0;">
          <el-input-number v-model="command8104State.timeout" :min="1" :max="600"/>
        </el-form-item>

      </div>
    </template>
    <el-table
        :data="command8104State.data?.parameterItems" v-loading="command8104State.loading" border
        style="width: 100%">
      <el-table-column prop="parameterIdAsHexString" label="参数ID" width="100"/>
      <el-table-column prop="parameterLength" label="参数长度" width="100"/>
      <el-table-column prop="parameterType" label="参数类型" width="120"/>
      <el-table-column prop="parameterValue" label="参数值"/>
    </el-table>
  </el-card>
</template>

<style scoped lang="scss">
.my-card {

  &:not(:first-child) {
    margin-top: 20px;
  }

  border-radius: 10px;
}
</style>

