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

import { Thread } from "@/types";
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LineChart,
  CanvasRenderer,
]);

export const DynamicThreadsCharts = ({ data }: { data: Thread }) => {
  const DEFAULT_OPTION = {
    tooltip: {
      trigger: "axis",
      borderWidth: 0,
      backgroundColor: "rgba(50,50,50,0.5)",
      textStyle: {
        color: "#FFF",
      },
      axisPointer: {
        type: "line",
        lineStyle: {
          color: "#008acd",
        },
        crossStyle: {
          color: "#008acd",
        },
        shadowStyle: {
          color: "rgba(200,200,200,0.2)",
        },
      },
    },
    legend: {},
    dataset: {
      source: [["time"], ["peak"], ["daemon"], ["live"], ["started"]],
    },
    xAxis: { type: "category" },
    yAxis: { gridIndex: 0 },
    series: [
      {
        type: "line",
        seriesLayoutBy: "row",
        emphasis: { focus: "series" },
      },
      {
        type: "line",
        seriesLayoutBy: "row",
        emphasis: { focus: "series" },
      },
      {
        type: "line",
        seriesLayoutBy: "row",
        emphasis: { focus: "series" },
      },
      {
        type: "line",
        seriesLayoutBy: "row",
        emphasis: { focus: "series" },
      },
    ],
  };

  const [option, setOption] = useState(DEFAULT_OPTION);

  useEffect(() => {
    if (!data.value) return;
    const newOption = cloneDeep(option); // immutable

    const time = new Date(data.time);

    newOption.dataset.source[0].push(`${time.toTimeString().slice(0, 9)}`);
    newOption.dataset.source[1].push(String(data.value.peak));
    newOption.dataset.source[2].push(String(data.value.daemon));
    newOption.dataset.source[3].push(String(data.value.live));
    newOption.dataset.source[4].push(String(data.value.started));
    setOption(newOption);
  }, [data]);

  return <ReactECharts option={option} style={{ height: 400 }} />;
};
