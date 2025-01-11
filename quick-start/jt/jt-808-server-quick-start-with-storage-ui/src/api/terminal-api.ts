import {Command8104Response, TerminalVo} from "../types/model.ts";
import {request} from "../utils/request.ts";

export const requestTerminalList = async (query: object): Promise<{ data: TerminalVo[], total: number }> => {
    return await request({
        path: 'dashboard-api/v1/session/instruction-sessions',
        method: 'GET',
        params: query
    });
}
export const requestSendCommand8104 = async (data: object): Promise<Command8104Response> => {
    return await request({
        path: 'api/jt-808-quick-start-with-storage/v1/command/8104',
        method: 'POST',
        data
    });
}
