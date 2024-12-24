import { FC } from "react";
import {
  JsonView,
  collapseAllNested,
  darkStyles,
  defaultStyles,
} from "react-json-view-lite";
import "react-json-view-lite/dist/index.css";
import "@/styles/jsonpreview.css";
import { ThemeProps, useTheme } from "@nextui-org/use-theme";

export interface JSONPreviewProps {
  json: Object;
}
export const JsonPreview: FC<JSONPreviewProps> = ({ json }) => {
  const { theme } = useTheme();

  // 动态切换theme暂不生效
  return (
    <JsonView
      clickToExpandNode
      data={json}
      shouldExpandNode={collapseAllNested}
      style={
        theme === ThemeProps.DARK
          ? { ...darkStyles, container: "jv-container" }
          : { ...defaultStyles, container: "jv-container" }
      }
    />
  );
};
