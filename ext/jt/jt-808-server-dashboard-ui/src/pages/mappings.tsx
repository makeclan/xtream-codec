import {
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@heroui/table";
import { Spinner } from "@heroui/spinner";
import React, { FC } from "react";
import useSWR from "swr";
import { Tooltip } from "@heroui/tooltip";
import { Chip } from "@heroui/chip";

import { request } from "@/utils/request.ts";

export const MappingsPage = () => {
  const { data, isLoading } = useSWR<{
    dispatcherXtreamHandler: any[];
  }>(
    "mappings",
    () =>
      request({
        path: "actuator/mappings",
        method: "GET",
      }),
    {},
  );
  const columns = [
    { key: "status", label: "状态" },
    { key: "handlerName", label: "处理器" },
    { key: "messageIdAsHexString", label: "消息ID" },
    { key: "messageIdDesc", label: "消息描述" },
    { key: "version", label: "协议版本" },
    { key: "scheduler", label: "调度器" },
    { key: "handlerDesc", label: "备注" },
  ];
  const tableData = (data?.dispatcherXtreamHandler ?? []).map((e, i) => ({
    handlerName: e.handler.match(/[^.]+$/)?.[0] ?? "",
    key: i,
    ...e,
  }));
  const loadingState =
    isLoading && tableData?.length === 0 ? "loading" : "idle";

  interface CellProps {
    item: any;
    columnKey: React.Key;
  }
  const calcStatus = (item: any) => {
    const { virtualThread, nonBlocking, rejectBlockingTask } = item;
    let color: "success" | "warning" | "danger";
    let label: string;

    if (virtualThread) {
      color = "success";
      label = "虚拟线程";
    } else {
      if (nonBlocking) {
        if (rejectBlockingTask) {
          color = "success";
          label = "非阻塞";
        } else {
          color = "warning";
          label = "非阻塞(可执行同步任务)";
        }
      } else {
        if (rejectBlockingTask) {
          color = "danger";
          label = "请检查调度器配置";
        } else {
          color = "success";
          label = "阻塞";
        }
      }
    }

    return {
      color,
      label,
    };
  };
  const RenderCell: FC<CellProps> = ({ item, columnKey }) => {
    const cellValue = item[columnKey as keyof typeof item];

    switch (columnKey) {
      case "handlerName":
        return <Tooltip content={item.handler}>{cellValue}</Tooltip>;
      case "status":
        return (
          <Chip color={calcStatus(item).color} size="sm" variant="dot">
            {calcStatus(item).label}
          </Chip>
        );
      default:
        return cellValue;
    }
  };

  return (
    <Table aria-label="table">
      <TableHeader columns={columns}>
        {(column) => (
          <TableColumn
            key={column.key}
            align={["nonBlocking"].includes(column.key) ? "center" : "start"}
          >
            {column.label}
          </TableColumn>
        )}
      </TableHeader>
      <TableBody
        emptyContent={"暂无数据"}
        items={tableData}
        loadingContent={<Spinner />}
        loadingState={loadingState}
      >
        {(item) => (
          <TableRow key={item.key}>
            {(columnKey) => (
              <TableCell>
                <RenderCell columnKey={columnKey} item={item} />
              </TableCell>
            )}
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
};
