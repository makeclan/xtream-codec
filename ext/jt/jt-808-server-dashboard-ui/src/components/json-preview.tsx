import { FC } from "react";
import { Code } from "@nextui-org/code";

export interface JSONPreviewProps {
  json: Object;
}
export const JsonPreview: FC<JSONPreviewProps> = ({ json }) => {
  return <Code>{JSON.stringify(json, null, 2)}</Code>;
};
