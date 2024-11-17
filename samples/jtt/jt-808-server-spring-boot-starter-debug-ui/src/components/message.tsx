import clsx from "clsx";
import { Avatar } from "@nextui-org/avatar";
import { Spacer } from "@nextui-org/spacer";
import { Card, CardBody } from "@nextui-org/card";
import { useMemo } from "react";

import { EventType, Event } from "@/types";

export default function Message({
  item,
  className,
}: {
  item: Event;
  className?: string;
}) {
  const rowDisplay = useMemo(() => {
    return [
      EventType.AFTER_REQUEST_RECEIVED,

      EventType.AFTER_SUB_REQUEST_MERGED,
    ].includes(Number(item.type))
      ? {
          name: "C",
          flexRow: "flex-row-reverse",
        }
      : {
          name: "S",
          flexRow: "",
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
          {Object.keys(item).map((e, i) => (
            <p key={i}>{`${e}: ${item[e as keyof Event]}`}</p>
          ))}
        </CardBody>
      </Card>
    </div>
  );
}
