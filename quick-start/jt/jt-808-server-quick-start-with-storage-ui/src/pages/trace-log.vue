<script setup lang="ts">
import {onMounted, reactive} from 'vue'
import {DatabaseConfig, TraceLogVo} from "../types/model.ts";
import {requestServerConfig, requestTraceLog} from "../api/demo-query-api.ts";
import {jt808VersionTagType, toHexString} from "../utils/common-utils.ts";

interface PageState {
  data: TraceLogVo[],
  total: number,
  databaseOptions: DatabaseConfig[],
  tableDataLoading: boolean,
  query: {
    page: number,
    pageSize: number,
    st?: string
  }
}

const pageState = reactive<PageState>({
  data: [],
  total: 0,
  databaseOptions: [],
  query: {page: 1, pageSize: 10, st: undefined},
  tableDataLoading: false,
} as PageState)
const loadData = async () => {
  pageState.tableDataLoading = true
  const {data, total} = await requestTraceLog(pageState.query);
  pageState.tableDataLoading = false
  pageState.data = data;
  pageState.total = total;
}

onMounted(async () => {
  const {database} = await requestServerConfig()
  pageState.databaseOptions = database
  if (database.length > 0) {
    pageState.query.st = database.find(item => item.enabled)?.value
    if (pageState.query.st) {
      await loadData()
    }
  }
})

</script>

<template>
  <el-card class="my-card">
    <el-form inline>
      <el-form-item label="数据源" style="margin-bottom: 0;">
        <el-radio-group v-model="pageState.query.st"
                        @change="async ()=>await loadData()">
          <el-radio-button v-for="(item,index) in pageState.databaseOptions"
                           :key="index"
                           :label="item.label"
                           :value="item.value"
                           :disabled="!item.enabled"/>
        </el-radio-group>
      </el-form-item>
      <el-form-item style="margin-bottom: 0;">
        <el-button type="primary" @click="loadData" :disabled="!pageState.query.st">查询</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card class="my-card">
    <template #header>请求日志</template>
    <div>
      <el-table :data="pageState.data" v-loading="pageState.tableDataLoading" border style="width: 100%">
        <el-table-column prop="requestId" label="requestId" fixed width="290" show-overflow-tooltip/>
        <el-table-column prop="traceId" label="traceId" fixed width="290" show-overflow-tooltip/>
        <el-table-column prop="terminalId" label="终端手机号" fixed width="200"/>
        <el-table-column prop="netType" label="网络" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.netType === 'TCP' ? 'success' : 'danger'">
              {{ scope.row.netType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="messageId" label="消息ID" width="200" show-overflow-tooltip>
          <template #default="scope">
            {{ toHexString(scope.row.messageId, 4) }} ({{ scope.row.messageDesc }})
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="80">
          <template #default="scope">
            <el-tag :type="jt808VersionTagType(scope.row.version)">
              {{ scope.row.version }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="subpackage" label="分包" width="60">
          <template #default="scope">
            <el-tag :type="scope.row.subpackage ? 'danger' : 'success'">
              {{ scope.row.subpackage ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="flowId" label="流水号" width="80"/>
        <el-table-column prop="currentPackageNo" label="包序号" width="80">
          <template #default="scope">
            {{ scope.row.currentPackageNo }} / {{ scope.row.totalPackage }}
          </template>
        </el-table-column>
        <el-table-column prop="receivedAt" label="接收时间" width="195"/>
        <el-table-column prop="sentAt" label="响应时间" width="195"/>
        <el-table-column prop="requestHex" label="请求报文(未转义)" width="300" show-overflow-tooltip/>
        <el-table-column prop="requestHexEscaped" label="请求报文(转义)" width="300" show-overflow-tooltip/>
        <el-table-column prop="responseHex" label="响应报文" width="300" show-overflow-tooltip/>
      </el-table>
      <el-card>
        <el-pagination
            :total="pageState.total"
            :current-page="pageState.query.page"
            :page-size="pageState.query.pageSize"
            :page-sizes="[10,20,30,50,100]"
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
</template>

<style scoped lang="scss">
.my-card {

  &:not(:first-child) {
    margin-top: 20px;
  }

  border-radius: 10px;
}
</style>

