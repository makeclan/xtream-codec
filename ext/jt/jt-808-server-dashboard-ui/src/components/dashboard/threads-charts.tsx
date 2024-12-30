import { ParentSize } from "@visx/responsive";
import {
  AnimatedGrid,
  AnimatedAxis,
  AnimatedLineSeries,
  XYChart,
} from "@visx/xychart";
import { lightTheme, darkTheme, XYChartTheme, Tooltip } from "@visx/xychart";
import { FC, useEffect, useState } from "react";
import { ThemeProps, useTheme } from "@nextui-org/use-theme";
import { curveLinear } from "@visx/curve";

interface Threads {
  date: string;
  started: number;
  peak: number;
  live: number;
  daemon: number;
}

export type XYChartProps = {
  width: number;
  height: number;
  data: Threads[];
};

interface chartProps {
  data: Threads;
  maxLength?: number;
}
const getDate = (d: Threads) => d.date;

export const LineCharts = ({ width, height, data }: XYChartProps) => {
  const [theme, setTheme] = useState<XYChartTheme>(darkTheme);
  const WebTheme = useTheme();

  useEffect(() => {
    if (WebTheme.theme === ThemeProps.LIGHT) {
      setTheme(lightTheme);
    }
  }, [WebTheme]);

  const config = {
    x: { type: "band", paddingInner: 0.3 } as const,
    y: { type: "linear" } as const,
  };

  return (
    <XYChart
      captureEvents={true}
      height={height}
      theme={theme}
      width={width}
      xScale={config.x}
      yScale={config.y}
    >
      <AnimatedGrid
        key="grid-min" // force animate on update
        animationTrajectory="min"
        columns={false}
        numTicks={4}
        rows={true}
      />
      <>
        <AnimatedLineSeries
          curve={curveLinear}
          data={data}
          dataKey="started"
          xAccessor={getDate}
          yAccessor={(d: Threads) => d.started}
        />
        <AnimatedLineSeries
          curve={curveLinear}
          data={data}
          dataKey="live"
          xAccessor={getDate}
          yAccessor={(d: Threads) => d.live}
        />
        <AnimatedLineSeries
          curve={curveLinear}
          data={data}
          dataKey="daemon"
          xAccessor={getDate}
          yAccessor={(d: Threads) => d.daemon}
        />
        <AnimatedLineSeries
          curve={curveLinear}
          data={data}
          dataKey="peak"
          xAccessor={getDate}
          yAccessor={(d: Threads) => d.peak}
        />
      </>
      <AnimatedAxis
        key="time-axis-min-false"
        animationTrajectory="min"
        numTicks={4}
        orientation="bottom"
      />
      <AnimatedAxis
        key="temp-axis-min-false"
        animationTrajectory="min"
        numTicks={4}
        orientation="left"
      />
      <Tooltip<Threads>
        renderTooltip={({ tooltipData, colorScale }) => (
          <>
            {tooltipData?.nearestDatum?.datum.date || "No date"}
            <br />
            <br />
            {Object.keys(tooltipData?.datumByKey ?? {})
              .filter((type) => type)
              .map((type) => {
                const count =
                  tooltipData?.nearestDatum?.datum[type as keyof Threads];

                return (
                  <div key={type}>
                    <em
                      style={{
                        color: colorScale?.(type),
                        textDecoration:
                          tooltipData?.nearestDatum?.key === type
                            ? "underline"
                            : undefined,
                      }}
                    >
                      {type}
                    </em>{" "}
                    {count == null || Number.isNaN(count) ? "–" : `${count}`}
                  </div>
                );
              })}
          </>
        )}
        showDatumGlyph={true}
        showHorizontalCrosshair={true}
        showSeriesGlyphs={true}
        showVerticalCrosshair={true}
        snapTooltipToDatumX={true}
        snapTooltipToDatumY={true}
      />
    </XYChart>
  );
};

export const ThreadsCharts: FC<chartProps> = ({ data, maxLength = 500 }) => {
  const [chartData, setChartData] = useState<Threads[]>([]);

  useEffect(() => {
    if (!data) return;
    setChartData((prevState) => {
      return prevState.toSpliced(
        -1,
        prevState.length > maxLength ? 1 : 0,
        data,
      );
    });
  }, [data]);

  return (
    <ParentSize>
      {({ width, height }) => (
        <LineCharts data={chartData} height={height} width={width} />
      )}
    </ParentSize>
  );
};
