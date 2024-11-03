import {
  Table,
  TableHeader,
  TableBody,
  TableColumn,
  TableRow,
  TableCell,
  getKeyValue,
} from "@nextui-org/table";
import { Spinner } from "@nextui-org/spinner";
import { Pagination } from "@nextui-org/pagination";
import { useState, useMemo } from "react";
import useSWR from "swr";

import DefaultLayout from "@/layouts/default";
import { request } from "@/utils/request";
import { Session } from "@/types";

export default function DebugPage() {
  const [page, setPage] = useState(1);
  const rowsPerPage = 10;
  const path = "api/v1/instruction-session/list";
  const { data, isLoading } = useSWR<{ total: number; data: Session[] }>(
    `${path}${page}`,
    () =>
      request({
        path,
        method: "GET",
        data: {
          page,
          size: rowsPerPage,
        },
      }),
    {
      keepPreviousData: true,
    },
  );
  const loadingState =
    isLoading && data?.data?.length === 0 ? "loading" : "idle";

  const columns = [
    // { key: "terminalId", label: "terminalId" },
    { key: "serverType", label: "serverType" },
    { key: "protocolVersion", label: "protocolVersion" },
    { key: "protocolType", label: "protocolType" },
    { key: "creationTime", label: "creationTime" },
    { key: "lastCommunicateTime", label: "lastCommunicateTime" },
  ];
  const pages = useMemo(() => {
    return data?.total ? Math.ceil(data.total / rowsPerPage) : 0;
  }, [data?.total, rowsPerPage]);

  return (
    <DefaultLayout>
      <section className="flex flex-col items-center justify-center gap-4 py-8 md:py-10">
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
            items={data?.data ?? []}
            loadingContent={<Spinner />}
            loadingState={loadingState}
          >
            {(item) => (
              <TableRow key={item?.id}>
                {(columnKey) => (
                  <TableCell>{getKeyValue(item, columnKey)}</TableCell>
                )}
              </TableRow>
            )}
          </TableBody>
        </Table>
      </section>
    </DefaultLayout>
  );
}
