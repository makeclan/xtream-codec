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

import { Metrics } from "@/types";
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LineChart,
  CanvasRenderer,
]);

export const DynamicThreadsCharts = ({
  data,
}: {
  data: { time: string; value: Metrics };
}) => {
  const DEFAULT_OPTION = {
    tooltip: {
      trigger: "axis",
    },
    toolbox: {
      show: true,
      feature: {
        restore: {},
        saveAsImage: {},
      },
    },
    grid: {
      top: 60,
      left: 30,
      right: 60,
      bottom: 30,
    },
    dataZoom: {
      show: false,
      start: 0,
      end: 100,
    },
    xAxis: {
      type: "time",
      splitLine: {
        show: false,
      },
      data: [],
    },
    yAxis: {
      type: "value",
    },
    series: [
      {
        name: "peak",
        type: "line",
        stack: "thread",
        data: [],
      },
      {
        name: "daemon",
        type: "line",
        stack: "thread",
        data: [],
      },
      {
        name: "live",
        type: "line",
        stack: "thread",
        data: [],
      },
      {
        name: "started",
        type: "line",
        stack: "thread",
        data: [],
      },
    ],
  };

  const [option, setOption] = useState(DEFAULT_OPTION);

  useEffect(() => {
    if (!data.value.threads) return;
    const newOption = cloneDeep(option); // immutable

    const data_peak: any[] = newOption.series[0].data;
    const data_daemon: any[] = newOption.series[1].data;
    const data_live: any[] = newOption.series[2].data;
    const data_started: any[] = newOption.series[3].data;

    if (data_peak.length > 100) {
      data_peak.shift();
      data_daemon.shift();
      data_live.shift();
      data_started.shift();
      newOption.xAxis.data.shift();
    }
    // @ts-ignore
    newOption.xAxis.data.push(data.time);
    data_peak.push({
      name: data.time,
      value: data.value.threads.peak,
    });
    data_daemon.push({
      name: data.time,
      value: data.value.threads.daemon,
    });
    data_live.push({
      name: data.time,
      value: data.value.threads.live,
    });
    data_started.push({
      name: data.time,
      value: data.value.threads.started,
    });
    setOption(newOption);
  }, [data]);

  return <ReactECharts option={option} style={{ height: 400 }} />;
};
