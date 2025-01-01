export interface DatabaseConfig {
    label: string;
    value: string;
    enabled: boolean;
}

export interface AlarmInfoVo {
    id: string;
    terminalId: string;
    alarmNo: string;
    alarmTime: string;
    alarmSequence: number;
    attachmentCount: number;
    clientId: string;
    fileName: string;
    fileType: number;
    fileSize: number;
    filePath: string;
    createdAt: string;
    previewUrl: string;
}

export interface TraceLogVo {
    requestId: string;
    traceId: string;
    terminalId: string;
    netType: string;
    messageId: number;
    messageDesc: string;
    version: string;
    flowId: number;
    messageBodyLength: number;
    messageBodyProperty: number;
    receivedAt: string;
    sentAt?: string;
    currentPackageNo: number;
    totalPackage: number;
    requestHex: string;
    requestHexEscaped: string;
    responseHex?: string;
    subpackage: boolean;
}
