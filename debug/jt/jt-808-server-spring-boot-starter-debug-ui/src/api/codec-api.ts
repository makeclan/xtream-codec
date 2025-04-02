import {request} from "../utils/request.ts";
import {ClassMetadata, DecodeResult, EncodeResult} from "../types/model.ts";

export const requestCodecOptionsApi = async (): Promise<{
    defaultTerminalId: string;
    classMetadata: Array<ClassMetadata>
}> => {
    return await request({
        path: 'dashboard-api/jt808/v1/codec/codec-options',
        method: 'get',
    });
}
export const requestDecodeMessageApi = async (data: object): Promise<DecodeResult> => {
    return await request({
        path: 'dashboard-api/jt808/v1/codec/decode-with-entity',
        method: 'post',
        data
    });
}

export const requestEncodeMessageApi = async (data: object): Promise<Array<EncodeResult>> => {
    return await request({
        path: 'dashboard-api/jt808/v1/codec/encode-with-entity',
        method: 'post',
        data
    });
}

