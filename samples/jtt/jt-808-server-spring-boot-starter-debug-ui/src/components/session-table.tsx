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
import { Button } from "@nextui-org/button";

import { usePageList } from "@/hooks/use-page-list.ts";
import { Session } from "@/types";

export default function SessionTable(props: { path: string }) {
  const { setPage, page, pages, data, isLoading } = usePageList(props.path);
  const loadingState =
    isLoading && data?.data?.length === 0 ? "loading" : "idle";

  const columns = [
    { key: "terminalId", label: "terminalId" },
    { key: "serverType", label: "serverType" },
    { key: "protocolVersion", label: "protocolVersion" },
    { key: "protocolType", label: "protocolType" },
    { key: "creationTime", label: "creationTime" },
    { key: "lastCommunicateTime", label: "lastCommunicateTime" },
  ];
  const handleMonitor = (_item: Session) => {
    // TODO
  };

  return (
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
        {(column) => <TableColumn key={column.key}>{column.label}</TableColumn>}
      </TableHeader>
      <TableBody
        emptyContent={"No rows to display."}
        items={data?.data ?? []}
        loadingContent={<Spinner />}
        loadingState={loadingState}
      >
        {(item) => (
          <TableRow key={item?.id}>
            {(columnKey) =>
              columnKey === "operation" ? (
                <Button onClick={() => handleMonitor(item)}>Monitor</Button>
              ) : (
                <TableCell>{getKeyValue(item, columnKey)}</TableCell>
              )
            }
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
}
