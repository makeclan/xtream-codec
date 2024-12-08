import {
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@nextui-org/table";
import { Spinner } from "@nextui-org/spinner";
import React, { FC } from "react";
import useSWR from "swr";
import { Tooltip } from "@nextui-org/tooltip";
import clsx from "clsx";

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
    { key: "handlerName", label: "处理器" },
    // { key: "messageId", label: "messageId" },
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
  const RenderCell: FC<CellProps> = ({ item, columnKey }) => {
    const cellValue = item[columnKey as keyof typeof item];

    switch (columnKey) {
      case "handlerName":
        return (
          <Tooltip color="primary" content={item.handler}>
            {cellValue}
          </Tooltip>
        );
      default:
        return cellValue;
    }
  };

  return (
    <Table aria-label="Example table with dynamic content">
      <TableHeader columns={columns}>
        {(column) => <TableColumn key={column.key}>{column.label}</TableColumn>}
      </TableHeader>
      <TableBody
        emptyContent={"暂无数据"}
        items={tableData}
        loadingContent={<Spinner />}
        loadingState={loadingState}
      >
        {(item) => (
          <TableRow
            key={item.key}
            className={clsx(item.nonBlocking ? "" : "bg-danger")}
          >
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
