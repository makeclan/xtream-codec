import { BarStackHorizontal } from "@visx/shape";
import { SeriesPoint } from "@visx/shape/lib/types";
import { Group } from "@visx/group";
import { AxisBottom, AxisLeft } from "@visx/axis";
import { scaleBand, scaleOrdinal } from "@visx/scale";
import { timeParse, timeFormat } from "@visx/vendor/d3-time-format";
import { withTooltip, Tooltip, defaultStyles } from "@visx/tooltip";
import { WithTooltipProvidedProps } from "@visx/tooltip/lib/enhancers/withTooltip";
import { ParentSize } from "@visx/responsive";
import { LegendOrdinal } from "@visx/legend";
const dataGroup = [
  {
    time: "2024-12-30 10:24:09",
    "x8a-udp-nio-8": { group: "others", threadState: "WAITING" },
    "x9a-udp-nio-9": { group: "others", threadState: "RUNNABLE" },
    "reactor-http-nio-8": {
      group: "reactor_loop_resources_attachment_udp",
      threadState: "RUNNABLE",
    },
    "parallel-8": {
      group: "reactor_loop_resources_attachment_udp",
      threadState: "WAITING",
    },
  },
  {
    time: "2024-12-30 10:24:14",
    "x8a-udp-nio-8": { group: "others", threadState: "WAITING" },
    "x9a-udp-nio-9": { group: "others", threadState: "RUNNABLE" },
    "reactor-http-nio-8": {
      group: "reactor_loop_resources_attachment_udp",
      threadState: "RUNNABLE",
    },
    "parallel-8": {
      group: "reactor_loop_resources_attachment_udp",
      threadState: "WAITING",
    },
  },
];

type TooltipData = {
  bar: SeriesPoint<typeof dataGroup>;
  key: string;
  index: number;
  height: number;
  width: number;
  x: number;
  y: number;
  color: string;
};

export type BarStackHorizontalProps = {
  width: number;
  height: number;
  margin?: { top: number; right: number; bottom: number; left: number };
  events?: boolean;
};

export const purple3 = "#a44afe";
export const background = "#eaedff";
const defaultMargin = { top: 40, left: 50, right: 40, bottom: 100 };
const tooltipStyles = {
  ...defaultStyles,
  minWidth: 60,
  backgroundColor: "rgba(0,0,0,0.9)",
  color: "white",
};

const keys = ["WAITING", "RUN"];
const parseDate = timeParse("%Y-%m-%d %H:%m:%s");
const format = timeFormat("%H:%m:%s");
const formatDate = (date: string) => format(parseDate(date) as Date);
// accessors
const getDate = (d: any) => d.time;

const threadScale = scaleBand<string>({
  domain: Object.keys(dataGroup[0]).filter((e) => e !== "time"),
  padding: 0.2,
});
const dateScale = scaleBand<string>({
  domain: dataGroup.map(getDate),
  padding: 0.2,
});
const colorScale = scaleOrdinal<string, string>({
  domain: keys,
  range: ["green", "red", purple3],
});

let tooltipTimeout: number;

export const DumpCharts = withTooltip<BarStackHorizontalProps, TooltipData>(
  ({
    width,
    height,
    events = false,
    margin = defaultMargin,
    tooltipOpen,
    tooltipLeft,
    tooltipTop,
    tooltipData,
    hideTooltip,
    showTooltip,
  }: BarStackHorizontalProps & WithTooltipProvidedProps<TooltipData>) => {
    // bounds
    const xMax = width - margin.left - margin.right;
    const yMax = height - margin.top - margin.bottom;

    dateScale.rangeRound([0, xMax]);

    return width < 10 ? null : (
      <div>
        <svg height={height} width={width}>
          <rect fill={background} height={height} rx={14} width={width} />
          <Group left={margin.left} top={margin.top}>
            <BarStackHorizontal<any, string>
              color={colorScale}
              data={dataGroup}
              height={yMax}
              keys={keys}
              xScale={dateScale}
              y={(d) => d}
              yScale={threadScale}
            >
              {(barStacks) => {
                return barStacks.map((barStack) =>
                  barStack.bars.map((bar) => (
                    <rect
                      key={`barstack-horizontal-${barStack.index}-${bar.index}`}
                      fill={bar.color}
                      height={bar.height}
                      width={bar.width}
                      x={bar.x}
                      y={bar.y}
                      onClick={() => {
                        if (events) alert(`clicked: ${JSON.stringify(bar)}`);
                      }}
                      onMouseLeave={() => {
                        tooltipTimeout = window.setTimeout(() => {
                          hideTooltip();
                        }, 300);
                      }}
                      onMouseMove={() => {
                        if (tooltipTimeout) clearTimeout(tooltipTimeout);
                        const top = bar.y + margin.top;
                        const left = bar.x + bar.width + margin.left;

                        showTooltip({
                          tooltipData: bar,
                          tooltipTop: top,
                          tooltipLeft: left,
                        });
                      }}
                    />
                  )),
                );
              }}
            </BarStackHorizontal>
            <AxisLeft
              hideAxisLine
              hideTicks
              scale={threadScale}
              stroke={purple3}
              tickFormat={(thread: any) => thread.name}
              tickLabelProps={{
                fill: purple3,
                fontSize: 11,
                textAnchor: "end",
                dy: "0.33em",
              }}
              tickStroke={purple3}
            />
            <AxisBottom
              scale={dateScale}
              stroke={purple3}
              tickLabelProps={{
                fill: purple3,
                fontSize: 11,
                textAnchor: "middle",
              }}
              tickStroke={purple3}
              top={yMax}
            />
          </Group>
        </svg>
        <div
          style={{
            position: "absolute",
            top: margin.top / 2 - 10,
            width: "100%",
            display: "flex",
            justifyContent: "center",
            fontSize: "14px",
          }}
        >
          <LegendOrdinal
            direction="row"
            labelMargin="0 15px 0 0"
            scale={colorScale}
          />
        </div>
        {tooltipOpen && tooltipData && (
          <Tooltip left={tooltipLeft} style={tooltipStyles} top={tooltipTop}>
            <div style={{ color: colorScale(tooltipData.key) }}>
              <strong>{tooltipData.key}</strong>
            </div>
            <div>{tooltipData.bar.data[tooltipData.key]}</div>
            <div>
              <small>{formatDate(getDate(tooltipData.bar.data))}</small>
            </div>
          </Tooltip>
        )}
      </div>
    );
  },
);

export const DumpGroup = () => {
  return (
    <ParentSize>
      {({ width, height }) => <DumpCharts height={height} width={width} />}
    </ParentSize>
  );
};
