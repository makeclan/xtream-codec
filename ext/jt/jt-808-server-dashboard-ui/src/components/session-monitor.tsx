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
  FC,
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
import { Card, CardBody } from "@nextui-org/card";
import clsx from "clsx";
import { Avatar } from "@nextui-org/avatar";

import { ChevronDownIcon } from "@/components/icons.tsx";
import { subtitle } from "@/components/primitives.ts";
import { Event, EventType, Session } from "@/types";
const SESSION_MAX_LENGTH = "100";

interface MessageProps {
  item: Event;
  className?: string;
}
const Message: FC<MessageProps> = ({ item, className }) => {
  const rowDisplay = useMemo(() => {
    return [
      EventType.AFTER_REQUEST_RECEIVED,
      EventType.AFTER_SUB_REQUEST_MERGED,
    ].includes(Number(item.type))
      ? {
          name: "C",
          flexRow: "",
        }
      : {
          name: "S",
          flexRow: "flex-row-reverse",
        };
  }, [item]);

  return [
    EventType.AFTER_SESSION_CREATED,
    EventType.BEFORE_SESSION_CLOSED,
  ].includes(Number(item.type)) ? (
    <div className="flex m-4 justify-center">
      <Card className="flex-grow-0 max-w-2xl">
        <CardBody>
          <p className="text-primary line-clamp-2 text-sm">{`Session${EventType.BEFORE_SESSION_CLOSED ? " closed" : " opened"} at: ${item.eventTime} remoteAddress: ${item.remoteAddress} reason: ${item.reason}`}</p>
        </CardBody>
      </Card>
    </div>
  ) : (
    <div className={clsx(className, "flex m-4", rowDisplay.flexRow)}>
      <Avatar className="flex-shrink-0" name={rowDisplay.name} />
      <Spacer x={2} />
      <Card className="flex-grow-0 max-w-2xl">
        <CardBody>
          {Object.keys(item)
            .filter((k) =>
              ["messageId", "rawHexString", "eventTime"].includes(k),
            )
            .map((e, i) => (
              <p key={i}>{`${e}: ${item[e as keyof Event]}`}</p>
            ))}
        </CardBody>
      </Card>
    </div>
  );
};

export interface SessionMonitorProps {
  row: Session | null;
  isOpen: boolean;
  setIsOpen: Dispatch<SetStateAction<boolean>>;
}
export const SessionMonitor: FC<SessionMonitorProps> = ({
  row,
  isOpen,
  setIsOpen,
}) => {
  const [max, setMax] = useState(SESSION_MAX_LENGTH);
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
  const listBottomRef = useRef<HTMLDivElement>(null);
  const scrollToIndex = () => {
    const listNode = listBottomRef.current;

    listNode?.scrollIntoView({
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
    scrollToIndex();
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
            <ScrollShadow hideScrollBar>
              {filteredLinkData?.map((item: any, index: number) => (
                <Message key={index} className="message-card" item={item} />
              ))}
              <div ref={listBottomRef} />
            </ScrollShadow>
          </ModalBody>
        </>
      </ModalContent>
    </Modal>
  );
};
