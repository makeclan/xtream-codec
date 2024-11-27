import {
  getKeyValue,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@nextui-org/table";
import { Spinner } from "@nextui-org/spinner";
import { Pagination } from "@nextui-org/pagination";

import { usePageList } from "@/hooks/use-page-list.ts";

export default function SubscribePage() {
  const path = `event-publisher/subscribers`;
  const { setPage, page, pages, tableData, isLoading } = usePageList(path);

  const loadingState =
    isLoading && tableData?.data?.length === 0 ? "loading" : "idle";

  const columns = [
    { key: "id", label: "id" },
    { key: "interestedEvents", label: "interestedEvents" },
    { key: "createdAt", label: "createdAt" },
    { key: "metadata", label: "metadata" },
  ];

  return (
    <>
      <Table
        aria-label="Subscribe"
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
                  <pre>
                    {JSON.stringify(getKeyValue(item, columnKey), null, 2)}
                  </pre>
                </TableCell>
              )}
            </TableRow>
          )}
        </TableBody>
      </Table>
    </>
  );
}
