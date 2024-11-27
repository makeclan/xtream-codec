import { Card, CardBody, CardFooter, CardHeader } from "@nextui-org/card";
import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { Link } from "@nextui-org/link";
import { useRouteLoaderData } from "react-router-dom";
import { Spacer } from "@nextui-org/spacer";
import { Popover, PopoverTrigger, PopoverContent } from "@nextui-org/popover";
import { Button } from "@nextui-org/button";
import {
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@nextui-org/table";

import { Metrics, ServerInfo } from "@/types";

export default function CardBox() {
  const { config } = useRouteLoaderData("root") as { config: ServerInfo };
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
      <div className="gap-4 grid grid-cols-1 sm:grid-cols-3">
        <Card shadow="sm">
          <CardHeader>
            <p>Version</p>
          </CardHeader>
          <CardBody className="overflow-visible p-4">
            <p>{config.xtreamCodecVersion}</p>
          </CardBody>
        </Card>
        <Card shadow="sm">
          <CardHeader>
            <p>serverStartupTime</p>
          </CardHeader>
          <CardBody className="overflow-visible p-4">
            <p className="text-default-500">{config.serverStartupTime}</p>
          </CardBody>
        </Card>
        <Card shadow="sm">
          <CardHeader className="flex">
            <p>subscriber</p>
          </CardHeader>
          <CardBody className="overflow-visible p-4">
            <p className="text-default-500">
              {data.eventPublisher?.subscriber?.total}
            </p>
          </CardBody>
          <CardFooter className="text-small justify-between">
            <Link color="primary" href={"/subscriber"}>
              detail
            </Link>
          </CardFooter>
        </Card>
      </div>
      <Spacer y={4} />
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
            <CardHeader className="text-small justify-between">
              <b>{item}</b>
            </CardHeader>
            <CardBody className="overflow-visible p-4">
              <p className="text-default-500">
                current: {data?.[item]?.current}
              </p>
            </CardBody>
            <CardFooter className="text-small justify-between">
              <b>max: {data[item]?.max}</b>
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
            <CardHeader className="text-small justify-between">
              <b>{item}</b>
            </CardHeader>
            <CardBody className="overflow-visible p-4">
              {data[item]?.total > 0 && (
                <Popover placement="right">
                  <PopoverTrigger>
                    <Button>detail</Button>
                  </PopoverTrigger>
                  <PopoverContent>
                    <Table
                      aria-label="Detail"
                      classNames={{
                        base: "max-h-[520px] overflow-scroll",
                        table: "min-h-[100px]",
                      }}
                      shadow="none"
                    >
                      <TableHeader>
                        <TableColumn>messageId</TableColumn>
                        <TableColumn>desc</TableColumn>
                        <TableColumn>count</TableColumn>
                        <TableColumn>messageIdAsHexString</TableColumn>
                      </TableHeader>
                      <TableBody>
                        {data[item] &&
                          Object.keys(data[item].details).map((e, i) => (
                            <TableRow key={i}>
                              <TableCell>
                                {data[item].details[e].messageId}
                              </TableCell>
                              <TableCell>
                                {data[item].details[e].desc}
                              </TableCell>
                              <TableCell>
                                {data[item].details[e].count}
                              </TableCell>
                              <TableCell>
                                {data[item].details[e].messageIdAsHexString}
                              </TableCell>
                            </TableRow>
                          ))}
                      </TableBody>
                    </Table>
                  </PopoverContent>
                </Popover>
              )}
            </CardBody>
            <CardFooter className="text-small justify-between">
              <b>total: {data[item]?.total}</b>
            </CardFooter>
          </Card>
        ))}
      </div>
    </>
  );
}
