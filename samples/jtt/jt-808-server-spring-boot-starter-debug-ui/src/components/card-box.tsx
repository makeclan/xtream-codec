import { Card, CardBody, CardFooter } from "@nextui-org/card";
import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { Code } from "@nextui-org/code";

import { Metrics } from "@/types";

export default function CardBox() {
  const [data, setData] = useState<Metrics>({});
  const listCount = [
    "tcpInstructionSession",
    "tcpAttachmentSession",
    "udpInstructionSession",
    "udpAttachmentSession",
  ];
  const listRequest = [
    "tcpInstructionRequest",
    "tcpAttachmentRequest",
    "udpInstructionRequest",
    "udpAttachmentRequest",
  ];

  useEffect(() => {
    const ctrl = new AbortController();

    fetchEventSource(`${import.meta.env.VITE_API_DASHBOARD_V1}metrics/basic`, {
      method: "GET",
      signal: ctrl.signal,
      onmessage: (event: EventSourceMessage) => {
        setData(JSON.parse(event.data));
      },
    }).then(() => {
      // TODO
    });

    return () => {
      ctrl.abort();
    };
  }, []);

  return (
    <>
      <Card className="m-4" shadow="sm">
        <CardBody className="overflow-visible p-4">
          <p className="text-default-500">
            subscriber: {data.eventPublisher?.subscriber?.total}
          </p>
        </CardBody>
        <CardFooter className="text-small justify-between">
          <b>subscriber</b>
        </CardFooter>
      </Card>
      <div className="gap-4 grid grid-cols-2 sm:grid-cols-4">
        {listCount.map((item, index) => (
          <Card
            key={index}
            isPressable
            shadow="sm"
            onPress={() => {
              // TODO
            }}
          >
            <CardBody className="overflow-visible p-4">
              <p className="text-default-500">max: {data[item]?.max}</p>
              <p className="text-default-500">
                current: {data?.[item]?.current}
              </p>
            </CardBody>
            <CardFooter className="text-small justify-between">
              <b>{item}</b>
            </CardFooter>
          </Card>
        ))}
        {listRequest.map((item, index) => (
          <Card
            key={index}
            isPressable
            shadow="sm"
            onPress={() => {
              // TODO
            }}
          >
            <CardBody className="overflow-visible p-4">
              <p className="text-default-500">total: {data[item]?.total}</p>
              <div className="text-default-500">
                detail:
                {data[item] &&
                  Object.keys(data[item].details).map((e, i) => (
                    <Code key={i}>
                      <pre>{JSON.stringify(e)}</pre>
                    </Code>
                  ))}
              </div>
            </CardBody>
            <CardFooter className="text-small justify-between">
              <b>{item}</b>
            </CardFooter>
          </Card>
        ))}
      </div>
    </>
  );
}
