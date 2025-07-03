import {
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@heroui/table";
import { Spinner } from "@heroui/spinner";
import { Pagination } from "@heroui/pagination";
import React, { FC, useMemo, useState } from "react";
import { Tooltip } from "@heroui/tooltip";
import { Chip } from "@heroui/chip";

import { SessionMonitor } from "./monitor.tsx";

import { usePageList } from "@/hooks/use-page-list.ts";
import { Session, SessionType } from "@/types";
import { request } from "@/utils/request.ts";
import { FaEyeIcon, FaTrashIcon } from "@/components/icons.tsx";

interface CellProps {
  handleMonitor: Function;
  handleDel: Function;
  session: Session;
  columnKey: React.Key;
}
const ServerMap = {
  INSTRUCTION_SERVER: "808 服务",
  ATTACHMENT_SERVER: "附件服务",
};

const SessionCell: FC<CellProps> = ({
  handleMonitor,
  handleDel,
  session,
  columnKey,
}) => {
  const cellValue = session[columnKey as keyof Session];

  switch (columnKey) {
    case "serverType":
      return ServerMap[cellValue as keyof typeof ServerMap];
    case "protocolVersion":
      return cellValue.replace("VERSION_", "");
    case "protocolType":
      return (
        <Chip color="primary" size="sm">
          {cellValue}
        </Chip>
      );
    case "operation":
      return (
        <div className="relative flex items-center gap-2">
          <Tooltip content="链路监控">
            <FaEyeIcon
              className="text-lg text-default-400 cursor-pointer active:opacity-50"
              onClick={() => handleMonitor(session)}
            />
          </Tooltip>
          <Tooltip content="删除会话">
            <FaTrashIcon
              className="text-lg text-danger cursor-pointer active:opacity-50"
              onClick={() => handleDel(session)}
            />
          </Tooltip>
        </div>
      );
    default:
      return cellValue;
  }
};

export interface SessionTableProps {
  type: SessionType;
}
export const SessionTable: FC<SessionTableProps> = ({ type }) => {
  const { setPage, page, pages, tableData, isLoading, mutate } = usePageList(
    `session/${type}-sessions`,
  );
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [selectedRow, setSelectedRow] = useState<Session | null>(null);

  const loadingState =
    isLoading && tableData?.data?.length === 0 ? "loading" : "idle";

  const columns = [
    { key: "id", label: "会话ID" },
    { key: "terminalId", label: "终端手机号" },
    // { key: "serverType", label: "服务类型" },
    { key: "protocolVersion", label: "808协议版本" },
    { key: "protocolType", label: "协议" },
    { key: "creationTime", label: "创建时间" },
    { key: "lastCommunicateTime", label: "最近一次通信时间" },
    { key: "operation", label: "操作" },
  ];

  const handleMonitor = (item: Session) => {
    setSelectedRow(item);
    setIsOpen(true);
  };
  const handleDel = async (session: Session) => {
    try {
      const res: any = await request({
        path: `session/${type}-session/${session.id}`,
        method: "DELETE",
      });

      if (res.closed) {
        await mutate();
      }
    } catch (_e) {
      // TODO
      console.error(_e);
    }
  };
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
    <>
      <Table
        aria-label="Example table with dynamic content"
        bottomContent={bottomContent}
        bottomContentPlacement="outside"
        topContent={topContent}
        topContentPlacement="outside"
      >
        <TableHeader columns={columns}>
          {(column) => (
            <TableColumn
              key={column.key}
              align={
                [
                  "serverType",
                  "protocolVersion",
                  "protocolType",
                  "operation",
                ].includes(column.key)
                  ? "center"
                  : "start"
              }
            >
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
                  <SessionCell
                    columnKey={columnKey}
                    handleDel={handleDel}
                    handleMonitor={handleMonitor}
                    session={item}
                  />
                </TableCell>
              )}
            </TableRow>
          )}
        </TableBody>
      </Table>
      <SessionMonitor isOpen={isOpen} row={selectedRow} setIsOpen={setIsOpen} />
    </>
  );
};
