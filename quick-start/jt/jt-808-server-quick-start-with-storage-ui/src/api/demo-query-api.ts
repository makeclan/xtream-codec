import {request} from "../utils/request.ts";
import {AlarmInfoVo, QuickstartServerConfig, TraceLogVo} from "../types/model.ts";

export const requestServerConfig = async (): Promise<QuickstartServerConfig> => {
    return await request({path: 'api/jt-808-quick-start-with-storage/v1/data-query/server-config', method: 'GET'});
}

export const requestAlarmInfo = async (st: object): Promise<{ data: AlarmInfoVo[], total: number }> => {
    return await request({
        path: 'api/jt-808-quick-start-with-storage/v1/data-query/attachment-info',
        method: 'GET',
        params: st
    });
}

export const requestTraceLog = async (st: object): Promise<{ data: TraceLogVo[], total: number }> => {
    return await request({
        path: 'api/jt-808-quick-start-with-storage/v1/data-query/trace-log',
        method: 'GET',
        params: st
    });
}
