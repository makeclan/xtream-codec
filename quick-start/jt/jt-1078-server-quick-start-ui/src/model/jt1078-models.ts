export interface Jt1078ServerConfig {
    jt1078ServerHost: string;
    jt1078ServerTcpPort: number;
    jt1078ServerUdpPort: number;
    jt1078ServerWebPort: number;
}

export enum Jt1078DataType {
    // 音视频
    DATA_TYPE_AUDIO_VIDEO = 0,
    // 视频
    DATA_TYPE_VIDEO = 1,
    // 双向对讲
    DATA_TYPE_INTERACTIVE = 2,
    // 监听
    DATA_TYPE_LISTEN = 3,
    // 中心广播
    DATA_TYPE_CENTER_BROADCAST = 4,
    // 透传
    DATA_TYPE_TRANSPARENT = 5,
}

export enum Jt1078StreamType {
    // 主码流
    STREAM_TYPE_MAIN = 0,
    // 子码流
    STREAM_TYPE_SUB = 1,
}

export enum Command9101CommandType {
    // 关闭音视频传输指令
    TYPE_0 = 0,
    // 切换码流(增加暂停和继续)
    TYPE_1 = 1,
    // 暂停该通道所有流的发送
    TYPE_2 = 2,
    // 恢复暂停前流的发送，与暂停前的流类型一致
    TYPE_3 = 3,
    // 关闭双向对讲
    TYPE_4 = 4,
}

export enum MediaTypeToClose {
    // 关闭该通道有关的音视频数据
    TYPE_0 = 0,
    // 只关闭该通道有关的音频，保留该通道有关的视频
    TYPE_1 = 1,
    // 只关闭该通道有关的视频，保留该通道有关的音频
    TYPE_2 = 2,
}

export interface TerminalCommonReply {
    serverFlowId: number;
    serverMessageId: number;
    result: number;
}
