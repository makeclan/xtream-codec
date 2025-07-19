export type Jt808ProtocolVersion = 'VERSION_2011' | 'VERSION_2013' | 'VERSION_2019' | '' | undefined | string;
export type TransportProtocolType = 'TCP' | 'UDP' | '' | undefined | string;

export interface Jt808Session {
    id: string;
    protocolType: TransportProtocolType;
    protocolVersion: Jt808ProtocolVersion;
    terminalId: string;
    lastCommunicateTime: string;
    creationTime: string;
}
