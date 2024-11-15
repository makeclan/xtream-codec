import clsx from "clsx";
import { Avatar } from "@nextui-org/avatar";
import { Spacer } from "@nextui-org/spacer";
import { Card, CardBody } from "@nextui-org/card";

import { EventType, Event } from "@/types";

export default function Message({
  item,
  className,
}: {
  item: Event;
  className?: string;
}) {
  return (
    <div
      className={clsx(
        className,
        "flex m-4",
        EventType.AFTER_REQUEST_RECEIVED === Number(item.type)
          ? "flex-row-reverse"
          : "",
      )}
    >
      <Avatar
        className="flex-shrink-0"
        name={
          EventType.AFTER_REQUEST_RECEIVED === Number(item.type) ? "S" : "C"
        }
      />
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
