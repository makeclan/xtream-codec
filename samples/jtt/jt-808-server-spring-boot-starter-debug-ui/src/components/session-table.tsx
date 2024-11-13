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
import { Button } from "@nextui-org/button";
import { Modal, ModalBody, ModalContent, ModalHeader } from "@nextui-org/modal";
import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { Card, CardBody } from "@nextui-org/card";
import { Avatar } from "@nextui-org/avatar";
import { Spacer } from "@nextui-org/spacer";
import clsx from "clsx";

import { usePageList } from "@/hooks/use-page-list.ts";
import { EventType, Session } from "@/types";

export default function SessionTable(props: { path: string }) {
  const { setPage, page, pages, data, isLoading } = usePageList(props.path);
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [selectedRow, setSelectedRow] = useState<Session | null>(null);
  const [linkData, setLinkData] = useState<any>([]);

  useEffect(() => {
    if (!isOpen) {
      return;
    }
    const ctrl = new AbortController();

    fetchEventSource(
      `${import.meta.env.VITE_API_DASHBOARD_V1}event/link-data?terminalId=${selectedRow?.terminalId}`,
      {
        method: "GET",
        signal: ctrl.signal,
        onmessage: (event: EventSourceMessage) => {
          console.log("Received event", event);
          const data: any = JSON.parse(event.data);

          data.type = event.event;
          setLinkData((pre) => pre.concat([data]));
        },
      },
    ).then(() => {
      // TODO
    });

    return () => {
      ctrl.abort();
    };
  }, [isOpen]);
  const loadingState =
    isLoading && data?.data?.length === 0 ? "loading" : "idle";

  const columns = [
    { key: "terminalId", label: "terminalId" },
    { key: "serverType", label: "serverType" },
    { key: "protocolVersion", label: "protocolVersion" },
    { key: "protocolType", label: "protocolType" },
    { key: "creationTime", label: "creationTime" },
    { key: "lastCommunicateTime", label: "lastCommunicateTime" },
    { key: "operation", label: "operation" },
  ];
  const handleMonitor = (item: Session) => {
    setSelectedRow(item);
    setIsOpen(true);
  };
  const onClose = () => {
    setIsOpen(false);
    // closeConnection();
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
          items={data?.data ?? []}
          loadingContent={<Spinner />}
          loadingState={loadingState}
        >
          {(item) => (
            <TableRow key={item?.id}>
              {(columnKey) =>
                columnKey === "operation" ? (
                  <TableCell>
                    <Button size="sm" onClick={() => handleMonitor(item)}>
                      Monitor
                    </Button>
                  </TableCell>
                ) : (
                  <TableCell>{getKeyValue(item, columnKey)}</TableCell>
                )
              }
            </TableRow>
          )}
        </TableBody>
      </Table>
      <Modal
        backdrop="blur"
        isOpen={isOpen}
        scrollBehavior="inside"
        size="4xl"
        onClose={onClose}
      >
        <ModalContent>
          <>
            <ModalHeader className="flex flex-col gap-1">
              {selectedRow?.terminalId}
            </ModalHeader>
            <ModalBody>
              {linkData.map((item: any, index: number) => (
                <div
                  key={index}
                  className={clsx(
                    "flex items-center",
                    EventType.AFTER_REQUEST_RECEIVED === item.type
                      ? "flex-row-reverse"
                      : "",
                  )}
                >
                  <Avatar
                    className="flex-shrink-0"
                    name={
                      EventType.AFTER_REQUEST_RECEIVED === item.type ? "S" : "C"
                    }
                  />
                  <Spacer x={2} />
                  <Card className="flex-grow-0">
                    <CardBody>
                      {Object.keys(item).map((e, i) => (
                        <p key={i}>{`${e}: ${item[e]}`}</p>
                      ))}
                    </CardBody>
                  </Card>
                </div>
              ))}
            </ModalBody>
          </>
        </ModalContent>
      </Modal>
    </>
  );
}
