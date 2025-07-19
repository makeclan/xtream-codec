import {request} from "../utils/request.ts";
import {Jt1078ServerConfig, TerminalCommonReply} from "../model/jt1078-models.ts";

export const requestProxyJt808SessionsApi = async (params: object): Promise<any> => {
    return await request({
        path: 'dashboard-api/jt1078/808-dashboard-proxy/v1/session/instruction-sessions',
        method: 'get',
        params
    });
}
export const requestProxy9101CommandApi = async (data: any): Promise<TerminalCommonReply> => {
    return await request({
        path: 'dashboard-api/jt1078/808-dashboard-proxy/v1/command/9101',
        method: 'post',
        data,
    });
}
export const requestProxy9102CommandApi = async (data: any): Promise<TerminalCommonReply> => {
    return await request({
        path: 'dashboard-api/jt1078/808-dashboard-proxy/v1/command/9102',
        method: 'post',
        data,
    });
}

export const requestJt1078ServerConfigApi = async (): Promise<Jt1078ServerConfig> => {
    return await request({
            path: '/jt-1078-server-quick-start/api/v1/server-config',
            method: 'get',
        }
    )
}
