import {
  getKeyValue,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@heroui/table";
import { FC } from "react";
export const MsgMiniTable: FC<{ data: any }> = ({ data }) => {
  const tableData = Object.keys(data).map((key) => {
    const cellValue = getKeyValue(data, key);

    return {
      ...cellValue,
    };
  });

  return (
    <Table
      aria-label="Detail"
      classNames={{
        base: "max-h-[520px] overflow-scroll",
        table: "min-h-[100px]",
      }}
      shadow="none"
    >
      <TableHeader>
        <TableColumn key="messageIdAsHexString">消息ID</TableColumn>
        <TableColumn key="desc">消息描述</TableColumn>
        <TableColumn key="count">总数</TableColumn>
      </TableHeader>
      <TableBody items={tableData}>
        {(item) => (
          <TableRow key={item}>
            {(columnKey) => (
              <TableCell>{getKeyValue(item, columnKey)}</TableCell>
            )}
          </TableRow>
        )}
      </TableBody>
    </Table>
  );
};
