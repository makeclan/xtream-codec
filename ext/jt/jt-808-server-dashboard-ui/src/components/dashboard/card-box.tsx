import { CardBody, CardHeader } from "@nextui-org/card";
import { Chip } from "@nextui-org/chip";
import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { useRouteLoaderData } from "react-router-dom";
import { Spacer } from "@nextui-org/spacer";
import { Popover, PopoverTrigger, PopoverContent } from "@nextui-org/popover";
import {
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@nextui-org/table";
import { Button } from "@nextui-org/button";
import { Badge } from "@nextui-org/badge";
import { Link } from "@nextui-org/link";

import { CountNumber } from "./count-number.tsx";
import { CountTime } from "./count-time.tsx";
import { SpotlightCard } from "./spolight-card.tsx";

import { Metrics, ServerInfo } from "@/types";
import { ServerIcon } from "@/components/icons.tsx";

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
              {config.dependencies?.xtreamCodec?.version}
            </p>
          </CardBody>
        </SpotlightCard>
        <SpotlightCard>
          <CardBody className="overflow-visible p-4">
            <p>服务启动时间</p>
            <p className="text-default-500 text-xl">
              {config.serverStartupTime}
            </p>
            <Spacer y={4} />
            <p>运行时间</p>
            <p className="text-default-500 text-xl">
              <CountTime start={new Date(config.serverStartupTime)} />
            </p>
          </CardBody>
        </SpotlightCard>
        <SpotlightCard>
          <CardHeader className="flex">
            <p>订阅者</p>
          </CardHeader>
          <CardBody className="overflow-visible p-4">
            <div className="flex justify-between">
              <p className="text-default-500 text-2xl">
                <CountNumber end={data.eventPublisher?.subscriber?.total} />
              </p>
              <Button
                as={Link}
                color="primary"
                href={"/subscriber"}
                variant="light"
              >
                详情
              </Button>
            </div>
          </CardBody>
        </SpotlightCard>
      </div>
      <Spacer y={4} />
      <div className="gap-4 grid grid-cols-2 sm:grid-cols-4">
        {listCount.map((item, index) => (
          <SpotlightCard key={index}>
            <CardHeader className="text-small">
              <b>会话数</b>
              <Spacer x={4} />
              <Badge
                color={item.serverRole === "附件服务器" ? "warning" : "success"}
                content={item.serverRole === "附件服务器" ? "附件" : "指令"}
                placement="bottom-right"
                shape="circle"
                size="sm"
              >
                <ServerIcon size={30} />
              </Badge>
              <Spacer x={4} />
              <Chip
                color={item.protocolType === "TCP" ? "primary" : "secondary"}
              >
                {item.protocolType}
              </Chip>
            </CardHeader>
            <CardBody className="overflow-visible p-4">
              <p className="text-default-500">
                当前: <CountNumber end={data?.[item.key]?.current} />
              </p>
              <b>峰值: {data[item.key]?.max}</b>
            </CardBody>
          </SpotlightCard>
        ))}
        {listRequest.map((item, index) => (
          <SpotlightCard key={index}>
            <CardHeader className="text-small">
              <b>请求数</b>
              <Spacer x={4} />
              <Badge
                color={item.serverRole === "附件服务器" ? "warning" : "success"}
                content={item.serverRole === "附件服务器" ? "附件" : "指令"}
                placement="bottom-right"
                shape="circle"
                size="sm"
              >
                <ServerIcon size={30} />
              </Badge>
              <Spacer x={4} />
              <Chip
                color={item.protocolType === "TCP" ? "primary" : "secondary"}
              >
                {item.protocolType}
              </Chip>
            </CardHeader>
            <CardBody className="overflow-visible flex p-4">
              <div className="flex justify-between items-center">
                <p>总请求数:</p>
                <CountNumber end={data[item.key]?.total} />
                <Spacer x={4} />
                {data[item.key]?.total > 0 && (
                  <Popover placement="right">
                    <PopoverTrigger>
                      <Button color="primary" variant="light">
                        详情
                      </Button>
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
                                  {
                                    data[item.key].details[e]
                                      .messageIdAsHexString
                                  }
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
              </div>
            </CardBody>
          </SpotlightCard>
        ))}
      </div>
    </>
  );
};
