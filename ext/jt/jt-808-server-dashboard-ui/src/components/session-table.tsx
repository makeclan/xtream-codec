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
import React, { FC, useState } from "react";
import { Tooltip } from "@nextui-org/tooltip";
import { Spacer } from "@nextui-org/spacer";

import { usePageList } from "@/hooks/use-page-list.ts";
import { Session, SessionType } from "@/types";
import { request } from "@/utils/request.ts";
import { SessionMonitor } from "@/components/session-monitor.tsx";
import { DelIcon, EyeIcon } from "@/components/icons.tsx";

interface CellProps {
  handleMonitor: Function;
  handleDel: Function;
  session: Session;
  columnKey: React.Key;
}
const SessionCell: FC<CellProps> = ({
  handleMonitor,
  handleDel,
  session,
  columnKey,
}) => {
  const cellValue = session[columnKey as keyof Session];

  switch (columnKey) {
    case "operation":
      return (
        <div className="flex">
          <Tooltip content="链路监控">
            <button
              className="text-lg text-default-400 cursor-pointer active:opacity-50"
              onClick={() => handleMonitor(session)}
            >
              <EyeIcon />
            </button>
          </Tooltip>
          <Spacer x={4} />
          <Tooltip content="删除会话">
            <button
              className="text-lg text-default-400 cursor-pointer active:opacity-50"
              onClick={() => handleDel(session)}
            >
              <DelIcon />
            </button>
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
  const path = `session/${type}-sessions`;
  const { setPage, page, pages, tableData, isLoading, mutate } =
    usePageList(path);
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [selectedRow, setSelectedRow] = useState<Session | null>(null);

  const loadingState =
    isLoading && tableData?.data?.length === 0 ? "loading" : "idle";

  const columns = [
    { key: "terminalId", label: "终端手机号" },
    { key: "serverType", label: "服务类型" },
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
        path: `session/instruction-session/${session.id}`,
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

  return (
    <>
      <Table
        aria-label="Example table with dynamic content"
        bottomContent={
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
        }
      >
        <TableHeader columns={columns}>
          {(column) => (
            <TableColumn key={column.key}>{column.label}</TableColumn>
          )}
        </TableHeader>
        <TableBody
          emptyContent={"No rows to display."}
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
