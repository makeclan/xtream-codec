import { useMemo, useState } from "react";
import useSWR from "swr";

import { Session } from "@/types";
import { request } from "@/utils/request.ts";

export const usePageList = (path: string) => {
  const [page, setPage] = useState(1);
  const rowsPerPage = 10;

  const { data, isLoading, mutate } = useSWR<{
    total: number;
    data: Session[];
  }>(
    `${path}${page}${rowsPerPage}`,
    () =>
      request({
        path,
        method: "GET",
        params: {
          page,
          size: rowsPerPage,
        },
      }),
    {
      keepPreviousData: true,
    },
  );

  const pages = useMemo(() => {
    return data?.total ? Math.ceil(data.total / rowsPerPage) : 0;
  }, [data?.total, rowsPerPage]);

  return { page, pages, tableData: data, isLoading, setPage, mutate };
};
