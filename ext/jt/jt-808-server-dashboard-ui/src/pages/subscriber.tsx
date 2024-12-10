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

export const SubscribePage = () => {
  const path = "event-publisher/subscribers";
  const { setPage, page, pages, tableData, isLoading } = usePageList(path);

  const loadingState =
    isLoading && tableData?.data?.length === 0 ? "loading" : "idle";

  const columns = [
    { key: "id", label: "ID" },
    { key: "interestedEvents", label: "订阅事件" },
    { key: "createdAt", label: "创建时间" },
    { key: "metadata", label: "元数据" },
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
          emptyContent={"暂无数据"}
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
};
