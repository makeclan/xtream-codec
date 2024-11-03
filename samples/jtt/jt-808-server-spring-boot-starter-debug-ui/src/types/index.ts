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
