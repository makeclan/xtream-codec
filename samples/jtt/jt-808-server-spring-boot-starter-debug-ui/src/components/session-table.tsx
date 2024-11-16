import type { SharedSelection } from "@nextui-org/system";

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
import { Modal, ModalBody, ModalContent, ModalHeader } from "@nextui-org/modal";
import { ScrollShadow } from "@nextui-org/scroll-shadow";
import { Input } from "@nextui-org/input";
import { useEffect, useMemo, useRef, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import {
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
} from "@nextui-org/dropdown";
import { Button } from "@nextui-org/button";
import { Spacer } from "@nextui-org/spacer";

import { usePageList } from "@/hooks/use-page-list.ts";
import { useRenderCell } from "@/hooks/use-render-cell";
import { EventType, Session, Event, SessionType } from "@/types";
import Message from "@/components/message.tsx";
import { subtitle } from "@/components/primitives.ts";
import { request } from "@/utils/request.ts";
import { ChevronDownIcon } from "@/components/icons.tsx";

export default function SessionTable(props: { type: SessionType }) {
  const path = `session/${props.type}-session/list`;
  const { setPage, page, pages, tableData, isLoading } = usePageList(path);
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [selectedRow, setSelectedRow] = useState<Session | null>(null);
  const [linkData, setLinkData] = useState<Event[]>([]);
  const [max, setMax] = useState("100");
  const [selected, setSelected] = useState<SharedSelection>("all");

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
    isLoading && tableData?.data?.length === 0 ? "loading" : "idle";

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
        const idx = tableData?.data.findIndex((e) => e.id === session.id);

        if (idx && idx > -1) {
          tableData?.data.splice(idx, 1);
        }
      }
    } catch (_e) {
      // TODO
      console.error(_e);
    }
  };
  const { renderCell } = useRenderCell(props.type, handleMonitor, handleDel);

  const filteredLinkData = useMemo(() => {
    if (
      selected === "all" ||
      Array.from(selected).length === eventList.length
    ) {
      return linkData;
    }

    return linkData.filter((e) => Array.from(selected).includes(e.type)) || [];
  }, [linkData, selected]);

  useEffect(() => {
    scrollToIndex(filteredLinkData.length - 1);
  }, [filteredLinkData, isOpen]);

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
                  label="Max"
                  labelPlacement="outside-left"
                  size="sm"
                  type="number"
                  value={max}
                  onValueChange={setMax}
                />
                <Spacer x={4} />
                <Dropdown>
                  <DropdownTrigger className="hidden sm:flex">
                    <Button
                      endContent={<ChevronDownIcon className="text-small" />}
                      variant="flat"
                    >
                      Event
                    </Button>
                  </DropdownTrigger>
                  <DropdownMenu
                    disallowEmptySelection
                    aria-label="Event"
                    closeOnSelect={false}
                    selectedKeys={selected}
                    selectionMode="multiple"
                    onSelectionChange={setSelected}
                  >
                    {eventList.map((event) => (
                      <DropdownItem key={event.key} className="capitalize">
                        {event.name}
                      </DropdownItem>
                    ))}
                  </DropdownMenu>
                </Dropdown>
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
