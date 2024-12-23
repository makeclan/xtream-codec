import { useState } from "react";
import { extent, max } from "@visx/vendor/d3-array";
import * as allCurves from "@visx/curve";
import { Group } from "@visx/group";
import { LinePath } from "@visx/shape";
import { scaleTime, scaleLinear } from "@visx/scale";
import {
  MarkerArrow,
  MarkerCross,
  MarkerX,
  MarkerCircle,
  MarkerLine,
} from "@visx/marker";
import generateDateValue, {
  DateValue,
} from "@visx/mock-data/lib/generators/genDateValue";
import { ParentSize } from "@visx/responsive";

type CurveType = keyof typeof allCurves;

const curveTypes = Object.keys(allCurves);
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

const LineCharts = ({ width, height, showControls = true }: CurveProps) => {
  const [curveType, setCurveType] = useState<CurveType>("curveNatural");
  const [showPoints, setShowPoints] = useState<boolean>(true);
  const svgHeight = showControls ? height - 40 : height;
  const lineHeight = svgHeight / lineCount;

  // update scale output ranges
  xScale.range([0, width - 50]);
  yScale.range([lineHeight - 2, 0]);

  return (
    <div className="visx-curves-demo">
      {showControls && (
        <>
          <label>
            Curve type &nbsp;
            <select
              value={curveType}
              onChange={(e) => setCurveType(e.target.value as CurveType)}
            >
              {curveTypes.map((curve) => (
                <option key={curve} value={curve}>
                  {curve}
                </option>
              ))}
            </select>
          </label>
          &nbsp;
          <label>
            Show points&nbsp;
            <input
              checked={showPoints}
              type="checkbox"
              onChange={() => setShowPoints(!showPoints)}
            />
          </label>
          <br />
        </>
      )}
      <svg height={svgHeight} width={width}>
        <MarkerX
          id="marker-x"
          markerUnits="userSpaceOnUse"
          size={22}
          stroke="#333"
          strokeWidth={4}
        />
        <MarkerCross
          id="marker-cross"
          markerUnits="userSpaceOnUse"
          size={22}
          stroke="#333"
          strokeOpacity={0.6}
          strokeWidth={4}
        />
        <MarkerCircle fill="#333" id="marker-circle" refX={2} size={2} />
        <MarkerArrow
          id="marker-arrow-odd"
          size={8}
          stroke="#333"
          strokeWidth={1}
        />
        <MarkerLine fill="#333" id="marker-line" size={16} strokeWidth={1} />
        <MarkerArrow fill="#333" id="marker-arrow" refX={2} size={6} />
        <rect fill="#efefef" height={svgHeight} rx={14} ry={14} width={width} />
        {width > 8 &&
          series.map((lineData, i) => {
            const even = i % 2 === 0;
            let markerStart = even ? "url(#marker-cross)" : "url(#marker-x)";

            if (i === 1) markerStart = "url(#marker-line)";
            const markerEnd = even
              ? "url(#marker-arrow)"
              : "url(#marker-arrow-odd)";

            return (
              <Group key={`lines-${i}`} left={13} top={i * lineHeight}>
                {showPoints &&
                  lineData.map((d, j) => (
                    <circle
                      key={i + j}
                      cx={xScale(getX(d))}
                      cy={yScale(getY(d))}
                      fill="transparent"
                      r={3}
                      stroke="rgba(33,33,33,0.5)"
                    />
                  ))}
                <LinePath<DateValue>
                  curve={allCurves[curveType]}
                  data={lineData}
                  markerEnd={markerEnd}
                  markerMid="url(#marker-circle)"
                  markerStart={markerStart}
                  shapeRendering="geometricPrecision"
                  stroke="#333"
                  strokeOpacity={even ? 0.6 : 1}
                  strokeWidth={even ? 2 : 1}
                  x={(d) => xScale(getX(d)) ?? 0}
                  y={(d) => yScale(getY(d)) ?? 0}
                />
              </Group>
            );
          })}
      </svg>
    </div>
  );
};

export const DynamicThreadsCharts = () => {
  return (
    <ParentSize>
      {({ width, height }) => <LineCharts height={height} width={width} />}
    </ParentSize>
  );
};
