import {
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@nextui-org/table";
import { Spinner } from "@nextui-org/spinner";
import { Pagination } from "@nextui-org/pagination";
import React, { FC, useMemo } from "react";
import { Tooltip } from "@nextui-org/tooltip";

import { Dic } from "@/types";
import { usePageList } from "@/hooks/use-page-list.ts";

interface CellProps {
  item: Dic;
  columnKey: React.Key;
}

const RenderCell: FC<CellProps> = ({ item, columnKey }) => {
  const cellValue = item[columnKey as keyof Dic];

  switch (columnKey) {
    case "interestedEvents":
    case "metadata":
      return (
        <Tooltip content={<pre>{JSON.stringify(cellValue, null, 2)}</pre>}>
          <p className="line-clamp-1">{JSON.stringify(cellValue)}</p>
        </Tooltip>
      );
    case "createdAt":
      return cellValue.slice(0, -4);
    default:
      return cellValue;
  }
};

export const SubscribePage = () => {
  const path = "event-publisher/subscribers";
  const { setPage, page, pages, tableData, isLoading } = usePageList(path, 10);

  const loadingState =
    isLoading && tableData?.data?.length === 0 ? "loading" : "idle";

  const columns = [
    { key: "id", label: "ID", width: "20%" },
    { key: "interestedEvents", label: "订阅事件", width: "30%" },
    { key: "metadata", label: "元数据", width: "30%" },
    { key: "createdAt", label: "创建时间", width: "20%" },
  ];

  const bottomContent = useMemo(() => {
    return (
      pages > 0 && (
        <div className="flex w-full justify-center">
          <Pagination
            isCompact
            showControls
            showShadow
            color="secondary"
            page={page}
            total={pages}
            onChange={(page) => setPage(page)}
          />
        </div>
      )
    );
  }, [page, pages]);

  const topContent = useMemo(() => {
    // TODO 筛选
    return <p>总数： {tableData?.total}</p>;
  }, [tableData?.total]);

  return (
    <Table
      isHeaderSticky
      aria-label="Subscribe"
      bottomContent={bottomContent}
      bottomContentPlacement="outside"
      classNames={{
        wrapper: "max-h-[80vh]",
      }}
      topContent={topContent}
      topContentPlacement="outside"
    >
      <TableHeader columns={columns}>
        {(column) => (
          <TableColumn key={column.key} width={column.width as `${number}%`}>
            {column.label}
          </TableColumn>
        )}
      </TableHeader>
      <TableBody
        emptyContent={"暂无数据"}
        items={tableData?.data ?? []}
        loadingContent={<Spinner />}
        loadingState={loadingState}
      >
        {(item) => (
          <TableRow key={item?.id}>
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
