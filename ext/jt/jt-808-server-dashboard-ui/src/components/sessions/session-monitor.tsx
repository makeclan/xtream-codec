import type { SharedSelection } from "@nextui-org/system";

import {
  Drawer,
  DrawerBody,
  DrawerContent,
  DrawerHeader,
} from "@nextui-org/drawer";
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

import {
  FaChevronDownIcon,
  FaDesktopIcon,
  FaRobotIcon,
  FaServerIcon,
} from "@/components/icons.tsx";
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
          avatar: <FaServerIcon className="text-green-500 fa-fade" />,
          bg: " bg-content3",
        }
      : {
          name: "S",
          avatar: <FaDesktopIcon className="text-blue-500 fa-beat" />,
          bg: " bg-content2",
        };
  }, [item]);

  return [
    EventType.AFTER_SESSION_CREATED,
    EventType.BEFORE_SESSION_CLOSED,
  ].includes(Number(item.type)) ? (
    <div className="flex gap-3">
      <div className="w-12">
        <Avatar icon={<FaRobotIcon className="text-primary" />} />
      </div>
      <Card className="w-full">
        <CardBody className="text-center">
          <p className="text-primary line-clamp-2 text-sm">{`Session${EventType.BEFORE_SESSION_CLOSED ? " closed" : " opened"} at: ${item.eventTime} remoteAddress: ${item.remoteAddress} reason: ${item.reason}`}</p>
        </CardBody>
        <div className="w-12" />
      </Card>
    </div>
  ) : (
    <div className={clsx(className, "flex gap-3")}>
      <div className="w-12">
        {rowDisplay.name === "C" && <Avatar icon={rowDisplay.avatar} />}
      </div>
      <Card className={clsx("flex-grow-0 w-full", rowDisplay.bg)}>
        <CardBody>
          {Object.keys(item)
            .filter((k) => ["messageId", "hexString", "eventTime"].includes(k))
            .map((e, i) => (
              <p key={i}>{`${e}: ${item[e as keyof Event]}`}</p>
            ))}
        </CardBody>
      </Card>
      <div className="w-12">
        {rowDisplay.name === "S" && <Avatar icon={rowDisplay.avatar} />}
      </div>
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
    <Drawer
      backdrop="blur"
      isOpen={isOpen}
      scrollBehavior="inside"
      size="5xl"
      onClose={onClose}
    >
      <DrawerContent>
        <>
          <DrawerHeader className="flex justify-between gap-1">
            <div className={subtitle()}>terminalId: {row?.terminalId}</div>
          </DrawerHeader>
          <DrawerBody>
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
                    endContent={<FaChevronDownIcon className="text-small" />}
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
            <ScrollShadow hideScrollBar className="flex flex-col gap-4 px-1">
              {filteredLinkData?.map((item: any, index: number) => (
                <Message key={index} className="message-card" item={item} />
              ))}
              <div ref={listBottomRef} />
            </ScrollShadow>
          </DrawerBody>
        </>
      </DrawerContent>
    </Drawer>
  );
};
