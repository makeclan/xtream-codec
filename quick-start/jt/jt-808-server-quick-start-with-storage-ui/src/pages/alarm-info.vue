<script setup lang="ts">
import {onMounted, reactive} from 'vue'
import {AlarmInfoVo, DatabaseConfig} from "../types/model.ts";
import {requestAlarmInfo, requestServerConfig} from "../api/demo-query-api.ts";
import {fileTypeDescriptor, formatFileSize} from "../utils/common-utils.ts";

interface PageState {
  data: AlarmInfoVo[],
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
  const {data, total} = await requestAlarmInfo(pageState.query);
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
  <el-card>
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
  <el-table :data="pageState.data" v-loading="pageState.tableDataLoading" border style="width: 100%">
    <el-table-column prop="id" label="ID" fixed width="320" show-overflow-tooltip/>
    <el-table-column prop="terminalId" label="终端手机号" fixed width="200"/>
    <el-table-column prop="alarmNo" label="报警编号(服务端生成的唯一编号)" fixed width="280" show-overflow-tooltip/>
    <el-table-column prop="alarmTime" label="报警时间" width="200"/>
    <el-table-column prop="attachmentCount" label="附件数" width="150"/>
    <el-table-column prop="fileName" label="文件名" width="410">
      <template #default="scope">
        <el-link type="primary" :href="scope.row.previewUrl" target="_blank">{{ scope.row.fileName }}</el-link>
      </template>
    </el-table-column>
    <el-table-column prop="fileType" label="文件类型" width="90">
      <template #default="scope">
        <el-tag :type="fileTypeDescriptor(scope.row.fileType)?.type">
          {{ fileTypeDescriptor(scope.row.fileType)?.text }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="fileSize" label="文件大小" width="180" show-overflow-tooltip>
      <template #default="scope">
        {{ scope.row.fileSize }}({{ formatFileSize(scope.row.fileSize) }})
      </template>
    </el-table-column>
    <el-table-column prop="filePath" label="文件路径" min-width="200" show-overflow-tooltip/>
    <el-table-column prop="createdAt" label="入库时间" min-width="200" show-overflow-tooltip/>
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
</template>

<style scoped lang="scss">
</style>

