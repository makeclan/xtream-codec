/**

import * as echarts from "echarts/core";
import { LineChart } from "echarts/charts";
import {
  GridComponent,
  TooltipComponent,
  TitleComponent,
} from "echarts/components";
import { CanvasRenderer } from "echarts/renderers";
import ReactECharts from "echarts-for-react";
import { useEffect, useState } from "react";
import { cloneDeep } from "lodash-es";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { getKeyValue } from "@nextui-org/table";

echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LineChart,
  CanvasRenderer,
]);
// interface chartProps {}
export const _deprecated_dumpCharts = () => {
  const renderItem = (params: any, api: any) => {
    const categoryIndex = api.value(0);
    const start = api.coord([api.value(1), categoryIndex]);
    const end = api.coord([api.value(1) + 3000, categoryIndex]);
    const height = api.size([0, 1])[1] * 0.6;
    const rectShape = echarts.graphic.clipRectByRect(
      {
        x: start[0],
        y: start[1] - height / 2,
        width: end[0] - start[0],
        height: height,
      },
      {
        x: params.coordSys.x,
        y: params.coordSys.y,
        width: params.coordSys.width,
        height: params.coordSys.height,
      },
    );

    return (
      rectShape && {
        type: "rect",
        transition: ["shape"],
        shape: rectShape,
        style: api.style(),
      }
    );
  };
  let startTime = Date.now();
  const defaultOption = {
    tooltip: {
      formatter: (params: any) => {
        return params.marker + params.name + ": " + params.value[0];
      },
    },
    dataZoom: [
      {
        type: "slider",
        filterMode: "weakFilter",
        showDataShadow: false,
        top: 890,
        labelFormatter: "",
      },
      {
        type: "inside",
        filterMode: "weakFilter",
      },
    ],
    grid: {
      height: 800,
    },
    xAxis: {
      min: startTime,
      scale: true,
      axisLabel: {
        formatter: (val: number) => new Date(val).toTimeString().slice(0, 9),
      },
    },
    yAxis: {
      data: [],
    },
    series: [
      {
        type: "custom",
        renderItem,
        itemStyle: {
          opacity: 0.8,
        },
        encode: {
          x: [1],
          y: 0,
        },
        data: [],
      },
    ],
  };
  const types = {
    NEW: { color: "#7b9ce1" },
    RUNNABLE: { color: "#bd6d6c" },
    BLOCKED: { color: "#75d874" },
    WAITING: { color: "#e0bc78" },
    TIMED_WAITING: { color: "#dc77dc" },
    TERMINATED: { color: "#72b362" },
  };
  const [option, setOption] = useState(defaultOption);

  interface Dump {
    time: string;
    value: {
      group: string;
      dumpInfo: {
        threadName: string;
        threadId: number;
        blockedTime: number;
        blockedCount: number;
        waitedTime: number;
        waitedCount: number;
        lockName: string;
        lockOwnerId: number;
        lockOwnerName: string;
        daemon: boolean;
        inNative: boolean;
        suspended: boolean;
        threadState:
          | "NEW"
          | "RUNNABLE"
          | "BLOCKED"
          | "WAITING"
          | "TIMED_WAITING"
          | "TERMINATED";
        priority: number;
        stackTrace: any;
        lockedMonitors: any[];
        lockedSynchronizers: any[];
        lockInfo: any;
      };
    };
  }
  const categories: string[] = [];
  const renewOptions = (remoteData: Dump) => {
    const newOption = cloneDeep(defaultOption); // immutable
    const data: any[] = newOption.series[0].data;
    const dumpInfo = remoteData.value.dumpInfo;
    const found = categories.findIndex((e) => e === dumpInfo.threadName);

    found === -1 && categories.push(dumpInfo.threadName);

    categories.forEach((_category, index) => {
      // if (newOption.series[0].data.length > 100 * categories.length) {
      //   data.shift();
      // }
      data.push({
        name: dumpInfo.threadState,
        value: [index, +new Date(remoteData.time)],
        itemStyle: {
          color: getKeyValue(types, dumpInfo.threadState).color,
        },
      });
    });
    // @ts-ignore
    newOption.yAxis.data = categories;
    // @ts-ignore
    newOption.series[0].data = data;
    setOption(newOption);
  };

  useEffect(() => {
    const ctrl = new AbortController();

    fetchEventSource(
      `${import.meta.env.VITE_API_DASHBOARD_V1}metrics/thread-dump`,
      {
        method: "GET",
        signal: ctrl.signal,
        onmessage: (event: EventSourceMessage) => {
          if (event.event === "dumpInfo") {
            const data = JSON.parse(event.data);

            renewOptions(data);
          }
        },
      },
    ).then(() => {
      // TODO
    });

    return () => {
      ctrl.abort();
    };
  }, []);

  return <ReactECharts option={option} style={{ height: 900 }} />;
};

*/
