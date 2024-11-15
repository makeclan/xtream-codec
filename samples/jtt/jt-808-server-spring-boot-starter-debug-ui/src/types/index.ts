import { SVGProps } from "react";

export type IconSvgProps = SVGProps<SVGSVGElement> & {
  size?: number;
};

export interface Session {
  id: string;
  terminalId: string;
  serverType: string;
  protocolVersion: string;
  protocolType: string;
  creationTime: string;
  lastCommunicateTime: string;
}

// export interface Event {
//   requestId: string;
//   traceId: string;
//   version: string;
//   isSubPackage: string;
//   messageId: string;
//   rawHexString: string;
//   escapedHexString: string;
//   type: EventType;
// }

export enum EventType {
  ALL = -1, // 所有事件
  AFTER_SESSION_CREATED = -99, // Session创建
  BEFORE_SESSION_CLOSED = -98, // Session关闭
  AFTER_REQUEST_RECEIVED = -100, // 收到请求
  BEFORE_RESPONSE_SEND = -102, // 发送响应
  BEFORE_COMMAND_SEND = -103, // 指令下发
}

// interface SessionCount {
//   max: number;
//   current: number;
// }
// interface SessionRequest {
//   total: number;
//   details: any;
// }
export interface Metrics {
  [key: string]: any;
}
export interface Event {
  [key: string]: any;
}

export interface ServerInfo {
  // xtream-codec 版本
  xtreamCodecVersion: string;
  // 服务启动时间
  serverStartupTime: string;
  // 服务配置(application.yaml#jt808-server.*)
  configuration: {};
}
