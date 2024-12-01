import { Card, CardBody, CardFooter, CardHeader } from "@nextui-org/card";
import { Chip } from "@nextui-org/chip";
import { ReactNode, useEffect, useRef, useState } from "react";
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
import { useMouseMove } from "@/hooks/use-mouse-move.ts";
const SpotlightCard = ({ children }: { children: ReactNode }) => {
  const ref = useRef<HTMLDivElement | null>(null);
  const { x, y } = useMouseMove(ref);

  return (
    <Card
      ref={ref}
      shadow="sm"
      style={{
        background:
          x > 0 || y > 0
            ? `radial-gradient(450px at ${x}px ${y}px, rgba(120, 40, 200, 0.5), transparent 80%)`
            : "",
      }}
    >
      {children}
    </Card>
  );
};

export const CardBox = () => {
  const { config } = useRouteLoaderData("root") as { config: ServerInfo };
  const [data, setData] = useState<Metrics>({});

  const listCount = [
    {
      key: "tcpInstructionSession",
      name: "808服务会话数",
      protocolType: "TCP",
      serverRole: "指令服务器",
    },
    {
      key: "tcpAttachmentSession",
      name: "附件服务会话数",
      protocolType: "TCP",
      serverRole: "附件服务器",
    },
    {
      key: "udpInstructionSession",
      name: "808服务会话数",
      protocolType: "UDP",
      serverRole: "指令服务器",
    },
    {
      key: "udpAttachmentSession",
      name: "附件服务会话数",
      protocolType: "UDP",
      serverRole: "附件服务器",
    },
  ];
  const listRequest = [
    {
      key: "tcpInstructionRequest",
      name: "808服务请求数",
      protocolType: "TCP",
      serverRole: "指令服务器",
    },
    {
      key: "tcpAttachmentRequest",
      name: "附件服务请求数",
      protocolType: "TCP",
      serverRole: "附件服务器",
    },
    {
      key: "udpInstructionRequest",
      name: "808服务请求数",
      protocolType: "UDP",
      serverRole: "指令服务器",
    },
    {
      key: "udpAttachmentRequest",
      name: "附件服务请求数",
      protocolType: "UDP",
      serverRole: "附件服务器",
    },
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
        <SpotlightCard>
          <CardHeader>
            <p>版本</p>
          </CardHeader>
          <CardBody className="overflow-visible p-4">
            <p className="text-default-500 text-2xl">
              {config.xtreamCodecVersion}
            </p>
          </CardBody>
        </SpotlightCard>
        <SpotlightCard>
          <CardHeader>
            <p>服务启动时间</p>
          </CardHeader>
          <CardBody className="overflow-visible p-4">
            <p className="text-default-500 text-2xl">
              {config.serverStartupTime}
            </p>
          </CardBody>
        </SpotlightCard>
        <SpotlightCard>
          <CardHeader className="flex">
            <p>订阅者</p>
          </CardHeader>
          <CardBody className="overflow-visible p-4">
            <p className="text-default-500 text-2xl">
              {data.eventPublisher?.subscriber?.total}
            </p>
          </CardBody>
          <CardFooter className="text-small justify-between">
            <Link color="primary" href={"/subscriber"}>
              详情
            </Link>
          </CardFooter>
        </SpotlightCard>
      </div>
      <Spacer y={4} />
      <div className="gap-4 grid grid-cols-2 sm:grid-cols-4">
        {listCount.map((item, index) => (
          <SpotlightCard key={index}>
            <CardHeader className="text-small justify-between">
              <b>会话数</b>
              <Chip
                color={item.serverRole === "附件服务器" ? "warning" : "success"}
              >
                {item.serverRole}
              </Chip>
              <Chip
                color={item.protocolType === "TCP" ? "primary" : "secondary"}
              >
                {item.protocolType}
              </Chip>
            </CardHeader>
            <CardBody className="overflow-visible p-4">
              <p className="text-default-500">
                当前: {data?.[item.key]?.current}
              </p>
            </CardBody>
            <CardFooter className="text-small justify-between">
              <b>峰值: {data[item.key]?.max}</b>
            </CardFooter>
          </SpotlightCard>
        ))}
        {listRequest.map((item, index) => (
          <SpotlightCard key={index}>
            <CardHeader className="text-small justify-between">
              <b>请求数</b>
              <Chip
                color={item.serverRole === "附件服务器" ? "warning" : "success"}
              >
                {item.serverRole}
              </Chip>
              <Chip
                color={item.protocolType === "TCP" ? "primary" : "secondary"}
              >
                {item.protocolType}
              </Chip>
            </CardHeader>
            <CardBody className="overflow-visible p-4">
              {data[item.key]?.total > 0 && (
                <Popover placement="right">
                  <PopoverTrigger>
                    <Button>详情</Button>
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
                        <TableColumn>消息ID</TableColumn>
                        <TableColumn>消息描述</TableColumn>
                        <TableColumn>总数</TableColumn>
                      </TableHeader>
                      <TableBody>
                        {data[item.key] &&
                          Object.keys(data[item.key].details).map((e, i) => (
                            <TableRow key={i}>
                              <TableCell>
                                {data[item.key].details[e].messageIdAsHexString}
                              </TableCell>
                              <TableCell>
                                {data[item.key].details[e].desc}
                              </TableCell>
                              <TableCell>
                                {data[item.key].details[e].count}
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
              <b>总请求数: {data[item.key]?.total}</b>
            </CardFooter>
          </SpotlightCard>
        ))}
      </div>
    </>
  );
};
