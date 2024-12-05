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

import { request } from "@/utils/request.ts";

export const MappingsPage = () => {
  const { data, isLoading } = useSWR<{
    dispatcherXtreamHandler: any[];
  }>(
    "mappings",
    () =>
      request({
        path: "mappings",
        method: "GET",
      }),
    {},
  );
  const tableData = (data?.dispatcherXtreamHandler ?? [])
    .map((e) => ({
      handler: e.handler.match(/[^.]+$/)?.[0] ?? "",
      detail: {
        ...e,
      },
    }))
    .reduce((acc, cur) => {
      if (!acc.find((e) => e.handler === cur.handler)) {
        acc.push(cur);
      }

      return acc;
    }, []);
  const columns = [
    { key: "handler", label: "handler" },
    { key: "detail", label: "detail" },
  ];

  interface CellProps {
    item: any;
    columnKey: React.Key;
  }
  const RenderCell: FC<CellProps> = ({ item, columnKey }) => {
    const cellValue = item[columnKey as keyof typeof item];

    switch (columnKey) {
      case "detail":
        return Object.keys(cellValue).map((item) => (
          <div key={item}>
            <div>{cellValue[item as keyof typeof cellValue]}</div>
          </div>
        ));
      default:
        return cellValue;
    }
  };

  return (
    <div>
      <Table aria-label="Example table with dynamic content">
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
            <TableRow key={item?.handler}>
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
