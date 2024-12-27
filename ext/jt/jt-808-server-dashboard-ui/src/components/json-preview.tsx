import type { TreeDataNode } from "antd";

import { Tree, ConfigProvider } from "antd";
import { FC } from "react";
import clsx from "clsx";

import {
  FaChevronDownIcon,
  FaCircleCheckIcon,
  FaCubeIcon,
  FaHashtagIcon,
  FaQuoteRightIcon,
} from "@/components/icons.tsx";
import { Dic } from "@/types";
const annotation: Dic = {
  requestLogger: "请求日志",
};
const isObject = (val: any) => {
  return typeof val === "object" && val !== null;
};
const valueColor = (item: string | number | boolean) => {
  switch (typeof item) {
    case "number":
      return "text-primary";
    case "boolean":
      return "text-success";
    case "string":
      return "text-secondary";
  }
};
const valueIcon = (item: string | number | boolean) => {
  switch (typeof item) {
    case "number":
      return <FaHashtagIcon className="text-small" />;
    case "boolean":
      return <FaCircleCheckIcon className="text-small" />;
    case "string":
      return <FaQuoteRightIcon className="text-small" />;
  }
};
const generateData = (json: Object, _preKey: string, page?: string) => {
  const tree: TreeDataNode[] = [];

  Object.keys(json).forEach((key, index) => {
    const item: any = json[key as keyof Object];
    const curKey = _preKey + "-" + index;

    if (isObject(item)) {
      tree.push({
        key: curKey,
        icon: <FaCubeIcon className="text-small" />,
        title: (
          <div
            className={clsx(
              "inline-flex gap-x-12",
              page === "threads" ? "justify-between" : "justify-normal",
            )}
          >
            <span>{key}</span>
            {annotation[key] && (
              <span className="text-small text-default-300">
                {annotation[key]}
              </span>
            )}
          </div>
        ),
        children: generateData(item, curKey, page),
      });
    } else {
      tree.push({
        key: curKey,
        icon: valueIcon(item),
        title: (
          <div
            className={clsx(
              "inline-flex gap-x-12 w-[80%] flex-grow",
              page === "threads" ? "justify-between" : "justify-normal",
            )}
          >
            <span>{key}</span>
            <span className={valueColor(item)}>{String(item)}</span>
            {annotation[key] && <span>{annotation[key]}</span>}
          </div>
        ),
      });
    }
  });

  return tree;
};

export interface JSONPreviewProps {
  json: Object;
  page?: string;
}
export const JsonPreview: FC<JSONPreviewProps> = ({ json, page }) => {
  const treeData: TreeDataNode[] = generateData(json, "0", page);

  return (
    <ConfigProvider
      theme={{
        token: {
          // @ts-ignore
          fontSize: "var(--xc-font-size-medium)",
          colorBgContainer: "var(--xc-background)",
          titleHeight: "var(--xc-line-height-large)",
          colorText: "var(--xc-content1)",
        },
      }}
    >
      <Tree
        blockNode
        showIcon
        defaultExpandedKeys={["0-0", "0-1", "0-2", "0-3"]}
        selectable={false}
        showLine={page !== "threads"}
        switcherIcon={(props) =>
          props.expanded ? (
            <FaChevronDownIcon />
          ) : (
            <FaChevronDownIcon className="fa-rotate-270" />
          )
        }
        treeData={treeData}
      />
    </ConfigProvider>
  );
};
