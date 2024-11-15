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
import { Button } from "@nextui-org/button";
import { Modal, ModalBody, ModalContent, ModalHeader } from "@nextui-org/modal";
import { ScrollShadow } from "@nextui-org/scroll-shadow";
import { Select, SelectItem } from "@nextui-org/select";
import { Input } from "@nextui-org/input";
import { Spacer } from "@nextui-org/spacer";
import { useEffect, useRef, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";

import { usePageList } from "@/hooks/use-page-list.ts";
import { useRenderCell } from "@/hooks/use-render-cell";
import { EventType, Session, Event, SessionType } from "@/types";
import Message from "@/components/message.tsx";
import { subtitle } from "@/components/primitives.ts";

export default function SessionTable(props: { type: SessionType }) {
  const path = `${import.meta.env.VITE_API_DASHBOARD_V1}session/${props.type}-session/list`;
  const { setPage, page, pages, data, isLoading } = usePageList(path);
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [selectedRow, setSelectedRow] = useState<Session | null>(null);
  const [linkData, setLinkData] = useState<Event[]>([]);
  const [max, setMax] = useState("100");
  const handleMonitor = (item: Session) => {
    setSelectedRow(item);
    setIsOpen(true);
  };
  const { renderCell } = useRenderCell(props.type, handleMonitor);

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
          const data: any = JSON.parse(event.data);

          data.type = event.event;
          setLinkData((pre) => {
            if (pre.length < Number(max)) {
              return pre.concat([data]);
            } else {
              return pre.concat([data]);
            }
          });
        },
      },
    ).then(() => {
      // TODO
    });

    return () => {
      ctrl.abort();
    };
  }, [isOpen]);

  useEffect(() => {
    scrollToIndex(linkData.length - 1);
  }, [linkData, isOpen]);

  const listRef = useRef<HTMLDivElement>(null);
  const scrollToIndex = (index: number) => {
    const listNode = listRef.current;
    const imgNode = listNode?.querySelectorAll(".message-card")[index];

    imgNode?.scrollIntoView({
      behavior: "smooth",
      block: "nearest",
      inline: "center",
    });
  };
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

  const onClose = () => {
    setIsOpen(false);
  };
  const eventList = [];

  for (const key in EventType) {
    if (!["-1", "ALL"].includes(key) && !isNaN(Number(key))) {
      eventList.push({ key, name: key });
    }
  }

  const onFilter = () => {
    // TODO filter
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
              {(columnKey) => (
                <TableCell>{renderCell(item, columnKey)}</TableCell>
              )}
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
            <ModalHeader className="flex justify-between gap-1">
              <div className={subtitle()}>
                terminalId: {selectedRow?.terminalId}
              </div>
            </ModalHeader>
            <ModalBody>
              <div className=" flex items-center">
                <Input
                  className="w-1/6"
                  label="Max Length"
                  size="sm"
                  type="number"
                  value={max}
                  onValueChange={setMax}
                />
                <Spacer x={4} />
                <Select
                  className="w-1/3"
                  label="Event Type"
                  selectionMode="multiple"
                  size="sm"
                >
                  {eventList.map((item) => (
                    <SelectItem key={item.key}>{item.name}</SelectItem>
                  ))}
                </Select>
                <Spacer x={4} />
                <Button color="primary" size="sm" onPress={onFilter}>
                  filter
                </Button>
              </div>
              <ScrollShadow ref={listRef} hideScrollBar>
                {linkData?.map((item: any, index: number) => (
                  <Message key={index} className="message-card" item={item} />
                ))}
              </ScrollShadow>
            </ModalBody>
          </>
        </ModalContent>
      </Modal>
    </>
  );
}
