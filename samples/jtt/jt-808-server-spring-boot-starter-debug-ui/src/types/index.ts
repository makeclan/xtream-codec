import { SVGProps } from "react";

export type IconSvgProps = SVGProps<SVGSVGElement> & {
  size?: number;
};

export type Session = {
  id: string;
  terminalId: string;
  serverType: string;
  protocolVersion: string;
  protocolType: string;
  creationTime: string;
  lastCommunicateTime: string;
};

export type Event = {
  requestId: string;
  traceId: string;
  version: string;
  isSubPackage: string;
  messageId: string;
  rawHexString: string;
  escapedHexString: string;
};

export enum EventType {
  RECEIVE_PACKAGE = -100, // 请求
  MERGE_PACKAGE = -101, // "合并请求"
  SEND_PACKAGE = -102, // "响应"
  COMMAND = -103, // "指令下发"
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
