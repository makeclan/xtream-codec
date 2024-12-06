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
  const tableData = (data?.dispatcherXtreamHandler ?? [])
    .map((e) => ({
      handlerName: e.handler.match(/[^.]+$/)?.[0] ?? "",
      handlerField: e.handler.match(/^(.*)\./)?.[0] ?? "",
      ...e,
    }))
    .reduce((acc, cur) => {
      if (!acc.find((e) => e.handler === cur.handler)) {
        acc.push(cur);
      }

      return acc;
    }, []);
  const columns = [
    // { key: "messageId", label: "messageId" },
    { key: "messageIdAsHexString", label: "消息ID" },
    { key: "messageIdDesc", label: "消息描述" },
    { key: "version", label: "协议版本" },
    { key: "scheduler", label: "调度器" },
    { key: "handlerName", label: "处理器" },
    // { key: "handlerField", label: "handlerField" },
    { key: "handlerDesc", label: "备注" },
  ];

  interface CellProps {
    item: any;
    columnKey: React.Key;
  }
  const RenderCell: FC<CellProps> = ({ item, columnKey }) => {
    const cellValue = item[columnKey as keyof typeof item];

    switch (columnKey) {
      case "handlerField":
        return <Tooltip content={cellValue}>{cellValue.slice(0, 10)}</Tooltip>;
      default:
        return cellValue;
    }
  };

  return (
    <div>
      <Table
        aria-label="Example table with dynamic content"
        defaultSelectedKeys={[""]}
      >
        <TableHeader columns={columns}>
          {(column) => (
            <TableColumn key={column.key}>{column.label}</TableColumn>
          )}
        </TableHeader>
        <TableBody
          emptyContent={"暂无数据"}
          items={tableData}
          loadingContent={<Spinner />}
          loadingState={isLoading}
        >
          {(item) => (
            <TableRow
              key={item?.handler}
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
    </div>
  );
};
