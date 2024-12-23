import React, { useCallback, useRef } from "react";
import { extent, max } from "@visx/vendor/d3-array";
import { Group } from "@visx/group";
import { LinePath } from "@visx/shape";
import { scaleTime, scaleLinear } from "@visx/scale";
import { MarkerCircle } from "@visx/marker";
import { Tooltip, withTooltip } from "@visx/tooltip";
import generateDateValue, {
  DateValue,
} from "@visx/mock-data/lib/generators/genDateValue";
import { ParentSize } from "@visx/responsive";
import { curveLinear } from "@visx/curve";
import { localPoint } from "@visx/event";
import { WithTooltipProvidedProps } from "@visx/tooltip/lib/enhancers/withTooltip";

const lineCount = 5;
const series = new Array(lineCount).fill(null).map((_, i) =>
  // vary each series value deterministically
  generateDateValue(25, /* seed= */ i / 72).sort(
    (a: DateValue, b: DateValue) => a.date.getTime() - b.date.getTime(),
  ),
);
const allData = series.reduce((rec, d) => rec.concat(d), []);

// data accessors
const getX = (d: DateValue) => d.date;
const getY = (d: DateValue) => d.value;

// scales
const xScale = scaleTime<number>({
  domain: extent(allData, getX) as [Date, Date],
});
const yScale = scaleLinear<number>({
  domain: [0, max(allData, getY) as number],
});

export type CurveProps = {
  width: number;
  height: number;
  showControls?: boolean;
};
let tooltipTimeout: number;

const LineCharts = withTooltip<CurveProps, WithTooltipProvidedProps<any>>(
  ({
    width,
    height,
    showTooltip,
    hideTooltip,
    tooltipData,
    tooltipTop = 0,
    tooltipLeft = 0,
  }) => {
    const svgRef = useRef<SVGSVGElement>(null);
    const svgHeight = height;
    const lineHeight = svgHeight / lineCount;

    // update scale output ranges
    xScale.range([0, width - 50]);
    yScale.range([lineHeight - 2, 0]);
    // event handlers
    const handleMouseMove = useCallback(
      (event: React.MouseEvent | React.TouchEvent) => {
        if (tooltipTimeout) clearTimeout(tooltipTimeout);
        if (!svgRef.current) return;

        // find the nearest polygon to the current mouse position
        const point = localPoint(svgRef.current, event);

        if (!point) return;
        showTooltip({
          tooltipLeft: point.x,
          tooltipTop: point.y,
          tooltipData: "x",
        });
      },
      [xScale, yScale, showTooltip],
    );

    const handleMouseLeave = useCallback(() => {
      tooltipTimeout = window.setTimeout(() => {
        hideTooltip();
      }, 300);
    }, [hideTooltip]);

    return (
      <div className="visx-curves-demo">
        <svg ref={svgRef} height={svgHeight} width={width}>
          <MarkerCircle fill="#333" id="marker-circle" refX={2} size={2} />
          <rect
            fill="transparent"
            height={svgHeight}
            rx={14}
            ry={14}
            width={width}
          />
          {width > 8 &&
            series.map((lineData, i) => {
              return (
                <Group key={`lines-${i}`} left={13} top={i * lineHeight}>
                  <LinePath<DateValue>
                    curve={curveLinear}
                    data={lineData}
                    markerEnd="url(#marker-circle)"
                    markerMid="url(#marker-circle)"
                    markerStart="url(#marker-circle)"
                    shapeRendering="geometricPrecision"
                    stroke="#333"
                    strokeOpacity={1}
                    strokeWidth={1}
                    x={(d) => xScale(getX(d)) ?? 0}
                    y={(d) => yScale(getY(d)) ?? 0}
                    onMouseLeave={handleMouseLeave}
                    onMouseMove={handleMouseMove}
                  />
                </Group>
              );
            })}
        </svg>
        <Tooltip left={tooltipLeft + 10} top={tooltipTop + 10}>
          <div>
            <strong>y:</strong> {tooltipData}
          </div>
        </Tooltip>
      </div>
    );
  },
);

export const DynamicThreadsCharts = () => {
  return (
    <ParentSize>
      {({ width, height }) => <LineCharts height={height} width={width} />}
    </ParentSize>
  );
};
