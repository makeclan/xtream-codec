import {request} from "../utils/request.ts";
import {AlarmInfoVo, DatabaseConfig, TraceLogVo} from "../types/model.ts";

export const requestServerConfig = async (): Promise<{ database: DatabaseConfig[] }> => {
    return await request({path: 'v1/data-query/server-config', method: 'GET'});
}

export const requestAlarmInfo = async (st: object): Promise<{ data: AlarmInfoVo[], total: number }> => {
    return await request({
        path: 'v1/data-query/attachment-info',
        method: 'GET',
        params: st
    });
}

export const requestTraceLog = async (st: object): Promise<{ data: TraceLogVo[], total: number }> => {
    return await request({
        path: 'v1/data-query/trace-log',
        method: 'GET',
        params: st
    });
}
