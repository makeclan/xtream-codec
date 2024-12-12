import { Card, CardBody, CardHeader } from "@nextui-org/card";
import { Chip } from "@nextui-org/chip";
import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { useRouteLoaderData } from "react-router-dom";
import { Spacer } from "@nextui-org/spacer";
import { Popover, PopoverTrigger, PopoverContent } from "@nextui-org/popover";
import { getKeyValue } from "@nextui-org/table";
import { Button } from "@nextui-org/button";
import { Link } from "@nextui-org/link";
import { Tab, Tabs } from "@nextui-org/tabs";
import { Accordion, AccordionItem } from "@nextui-org/accordion";

import { CountNumber } from "./count-number.tsx";
import { CountTime } from "./count-time.tsx";
import { SpotlightCard } from "./spolight-card.tsx";

import { Metrics, ServerInfo } from "@/types";
import { FaServerIcon } from "@/components/icons.tsx";
import { DynamicThreadsCharts } from "@/components/dashboard/dynamic-threads-charts.tsx";
import { MsgMiniTable } from "@/components/dashboard/msg-mini-table.tsx";
export const CardBox = () => {
  const { config } = useRouteLoaderData("root") as { config: ServerInfo };
  const [data, setData] = useState<{ time: string; value: Metrics }>({
    time: "",
    value: {},
  });

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
          <CardBody className="overflow-visible p-4 min-h-48">
            <Tabs fullWidth aria-label="Tabs sizes" variant="light">
              <Tab key="version" title="版本">
                <p className="text-default-500 text-xl">
                  {config.dependencies?.xtreamCodec?.version}
                </p>
              </Tab>
              <Tab key="java" title="java">
                <Accordion variant="light">
                  <AccordionItem
                    aria-label="Accordion 1"
                    title={`version: ${config.java.version}`}
                  >
                    <pre>
                      {JSON.stringify(config.java, null, 2)
                        .replace(/["{},]/g, "")
                        .replace(/\n {2}\n/g, "\n")}
                    </pre>
                  </AccordionItem>
                </Accordion>
              </Tab>
              <Tab key="os" title="os">
                {Object.keys(config.os).map((key) => (
                  <p key={key}>
                    {key}: {getKeyValue(config.os, key)}
                  </p>
                ))}
              </Tab>
            </Tabs>
          </CardBody>
        </SpotlightCard>
        <SpotlightCard>
          <CardBody className="overflow-visible p-4 min-h-48">
            <p>服务启动时间</p>
            <p className="text-default-500 text-xl">
              {config.serverStartupTime}
            </p>
            <Spacer y={4} />
            <p>运行时间</p>
            <div className="text-default-500 text-xl">
              <CountTime start={new Date(config.serverStartupTime)} />
            </div>
          </CardBody>
        </SpotlightCard>
        <SpotlightCard>
          <CardBody className="overflow-visible p-4  min-h-48">
            <p>订阅者</p>
            <div className="flex justify-between">
              <p className="text-default-500 text-2xl">
                <CountNumber
                  end={getKeyValue(
                    data.value.eventPublisher?.subscriber,
                    "total",
                  )}
                />
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
          <SpotlightCard key={index} className="min-h-32">
            <CardHeader className="text-small">
              <b>会话数</b>
              <Spacer x={4} />
              <FaServerIcon />
              <Spacer x={1} />
              <Chip
                color={item.serverRole === "附件服务器" ? "warning" : "success"}
                size="sm"
              >
                {item.serverRole === "附件服务器" ? "附件" : "指令"}
              </Chip>
              <Spacer x={4} />
              <Chip
                color={item.protocolType === "TCP" ? "primary" : "secondary"}
                size="sm"
              >
                {item.protocolType}
              </Chip>
            </CardHeader>
            <CardBody className="overflow-visible p-4">
              <p className="text-default-500">
                当前:{" "}
                <CountNumber end={getKeyValue(data.value, item.key)?.current} />
              </p>
              <b>峰值: {getKeyValue(data.value, item.key)?.max}</b>
            </CardBody>
          </SpotlightCard>
        ))}
        {listRequest.map((item, index) => (
          <SpotlightCard key={index} className="min-h-32">
            <CardHeader className="text-small">
              <b>请求数</b>
              <Spacer x={4} />
              <FaServerIcon />
              <Spacer x={1} />
              <Chip
                color={item.serverRole === "附件服务器" ? "warning" : "success"}
                size="sm"
              >
                {item.serverRole === "附件服务器" ? "附件" : "指令"}
              </Chip>
              <Spacer x={4} />
              <Chip
                color={item.protocolType === "TCP" ? "primary" : "secondary"}
                size="sm"
              >
                {item.protocolType}
              </Chip>
            </CardHeader>
            <CardBody className="overflow-visible flex p-4">
              <div className="flex justify-between items-center">
                <p>总请求数:</p>
                <CountNumber end={getKeyValue(data.value, item.key)?.total} />
                <Spacer x={4} />
                {getKeyValue(data.value, item.key)?.total > 0 && (
                  <Popover placement="right">
                    <PopoverTrigger>
                      <Button color="primary" variant="light">
                        详情
                      </Button>
                    </PopoverTrigger>
                    <PopoverContent>
                      <MsgMiniTable
                        data={getKeyValue(data.value, item.key)?.details}
                      />
                    </PopoverContent>
                  </Popover>
                )}
              </div>
            </CardBody>
          </SpotlightCard>
        ))}
      </div>
      <Spacer y={4} />
      <div className="gap-2 grid grid-cols-1">
        <Card>
          <CardHeader>线程</CardHeader>
          <CardBody>
            <DynamicThreadsCharts
              data={{
                time: data.time,
                value: data.value.threads,
              }}
              series={["peak", "daemon", "live", "started"]}
            />
          </CardBody>
        </Card>
      </div>
    </>
  );
};
