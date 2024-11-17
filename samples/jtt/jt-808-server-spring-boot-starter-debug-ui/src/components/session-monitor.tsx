import type { SharedSelection } from "@nextui-org/system";

import { Modal, ModalBody, ModalContent, ModalHeader } from "@nextui-org/modal";
import { Input } from "@nextui-org/input";
import { Spacer } from "@nextui-org/spacer";
import {
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
} from "@nextui-org/dropdown";
import { Button } from "@nextui-org/button";
import { ScrollShadow } from "@nextui-org/scroll-shadow";
import {
  Dispatch,
  SetStateAction,
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";

import Message from "@/components/message.tsx";
import { ChevronDownIcon } from "@/components/icons.tsx";
import { subtitle } from "@/components/primitives.ts";
import { Event, EventType, Session } from "@/types";

export default function SessionMonitor({
  row,
  isOpen,
  setIsOpen,
}: {
  row: Session | null;
  isOpen: boolean;
  setIsOpen: Dispatch<SetStateAction<boolean>>;
}) {
  const [max, setMax] = useState("100");
  const [selected, setSelected] = useState<SharedSelection>("all");
  const [linkData, setLinkData] = useState<Event[]>([]);

  useEffect(() => {
    if (!isOpen || !row?.terminalId) {
      return;
    }
    const ctrl = new AbortController();

    fetchEventSource(
      `${import.meta.env.VITE_API_DASHBOARD_V1}event/link-data?terminalId=${row.terminalId}`,
      {
        method: "GET",
        signal: ctrl.signal,
        onmessage: (event: EventSourceMessage) => {
          const data: any = JSON.parse(event.data);

          data.type = event.event;
          setLinkData((pre) => {
            if (pre.length <= Number(max)) {
              return pre.concat([data]);
            } else {
              return pre
                .concat([data])
                .filter((_e, i) => pre.length - i < Number(max));
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
    setLinkData((pre) => {
      if (pre.length <= Number(max)) {
        return pre;
      } else {
        return pre.filter((_e, i) => i < Number(max));
      }
    });
  }, [max]);
  const listRef = useRef<HTMLDivElement>(null);
  const scrollToIndex = (index: number) => {
    const listNode = listRef.current;
    const imgNode = listNode?.querySelectorAll(".message-card")[index];

    imgNode?.scrollIntoView({
      behavior: "smooth",
      block: "start",
      inline: "center",
    });
  };
  const onClose = () => {
    setIsOpen(false);
  };
  const eventList = [];

  for (const key in EventType) {
    if (!["-1", "ALL"].includes(key) && !isNaN(Number(key))) {
      eventList.push({ key, name: key });
    }
  }
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
            <div className={subtitle()}>terminalId: {row?.terminalId}</div>
          </ModalHeader>
          <ModalBody>
            <div className=" flex items-center">
              <Input
                className="w-1/6"
                label="Max"
                labelPlacement="outside-left"
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
              {filteredLinkData?.map((item: any, index: number) => (
                <Message key={index} className="message-card" item={item} />
              ))}
            </ScrollShadow>
          </ModalBody>
        </>
      </ModalContent>
    </Modal>
  );
}
