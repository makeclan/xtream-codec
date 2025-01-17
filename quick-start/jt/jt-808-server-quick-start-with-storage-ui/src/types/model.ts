export interface DatabaseConfig {
    label: string;
    value: string;
    enabled: boolean;
}

export interface OssConfig {
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

export interface TerminalVo {
    id: string;
    terminalId: string;
    serverType: string;
    protocolVersion: string;
    protocolType: string;
    creationTime: string;
    lastCommunicateTime: string;
}

export interface Command8104Item {
    parameterId: number;
    parameterIdAsHexString: number;
    parameterLength: number;
    parameterValue: string;
    parameterType: string;
}

export interface Command8104Response {
    flowId: number;
    parameterCount: number;
    parameterItems: Array<Command8104Item>
}

export interface QuickstartServerConfig {
    database: Array<DatabaseConfig>;
    oss: Array<OssConfig>;
    server: {
        type: string,
        port: number,
        availableIpAddresses: Array<string>
    },
    jt808: {
        instructionServer: {
            tcpServer: { enabled: boolean, host: string, port: number },
            udpServer: { enabled: boolean, host: string, port: number },
        },
        attachmentServer: {
            tcpServer: { enabled: boolean, host: string, port: number },
            udpServer: { enabled: boolean, host: string, port: number },
        }
    },
}
