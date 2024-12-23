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
import { FC, useEffect, useState } from "react";
import { cloneDeep } from "lodash-es";

import { Thread } from "@/types";
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LineChart,
  CanvasRenderer,
]);
interface chartProps {
  data: Thread;
  maxLength?: number;
  series: string[];
}
export const DynamicThreadsCharts: FC<chartProps> = ({
  data,
  maxLength = 1000,
  series,
}) => {
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
      source: [["time"], ...series.map((e) => [e])],
    },
    xAxis: { type: "category" },
    yAxis: { gridIndex: 0 },
    series: series.map(() => ({
      type: "line",
      seriesLayoutBy: "row",
      emphasis: { focus: "series" },
    })),
  };

  const [option, setOption] = useState(DEFAULT_OPTION);

  useEffect(() => {
    if (!data.value) return;
    const newOption = cloneDeep(option); // immutable

    const time = new Date(data.time);

    if (newOption.dataset.source[0].length > maxLength) {
      newOption.dataset.source[0].shift();
      for (let i = 0; i < series.length; i++) {
        newOption.dataset.source[i + 1].shift();
      }
    }
    newOption.dataset.source[0].push(`${time.toTimeString().slice(0, 9)}`);
    series.forEach((e, i) => {
      newOption.dataset.source[i + 1].push(String(data.value[e]));
    });
    setOption(newOption);
  }, [data]);

  return <ReactECharts option={option} style={{ height: 400 }} />;
};

*/
