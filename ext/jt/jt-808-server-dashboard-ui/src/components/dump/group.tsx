import { Accordion, AccordionItem } from "@heroui/accordion";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { useEffect, useMemo, useState } from "react";
import { Axis } from "@visx/axis";
import { timeFormat } from "@visx/vendor/d3-time-format";
import { coerceNumber, scaleUtc } from "@visx/scale";
import { Chip } from "@heroui/chip";
import { ParentSize } from "@visx/responsive";
import { NumberValue } from "@visx/vendor/d3-scale";
import { semanticColors } from "@heroui/theme";
import { useTheme } from "@heroui/use-theme";

import { Dump, Group } from "./types.ts";
const types = {
  NEW: { color: "#7b9ce1" },
  RUNNABLE: { color: "#bd6d6c" },
  BLOCKED: { color: "#75d874" },
  WAITING: { color: "#e0bc78" },
  TIMED_WAITING: { color: "#dc77dc" },
  TERMINATED: { color: "#72b362" },
};
const format = timeFormat("%H:%m:%S");
const maxPixelsPerSeconds = 15;

export const DumpGroup = () => {
  const [groups, setGroups] = useState<Group[]>([]);
  const [timeValues, setTimeValues] = useState<NumberValue[]>([]);
  const { theme } = useTheme();
  const strokeColor = useMemo(() => {
    // @ts-ignore
    return semanticColors[theme].foreground.DEFAULT;
  }, [theme]);
  const calcTimeValues = (time: string) => {
    const dateTime = +new Date(time.slice(0, -4));
    let _tempTimes = [...timeValues];

    if (dateTime) {
      if (timeValues.includes(dateTime)) {
        return;
      }
      _tempTimes.push(+dateTime);
      setTimeValues(() => _tempTimes);
    }
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
            const data: Dump = JSON.parse(event.data);

            calcTimeValues(data.time);
            setGroups((prevState) => {
              const groupIndex = prevState.findIndex(
                (group) => group.name === data.value.group,
              );

              if (groupIndex > -1) {
                const tempGroup = prevState[groupIndex];
                const threadIndex = tempGroup.threads.findIndex(
                  (thread) =>
                    thread.threadName === data.value.dumpInfo.threadName,
                );

                if (threadIndex > -1) {
                  const tempThread = tempGroup.threads[threadIndex];

                  if (tempThread.dumps.length > 20) {
                    tempThread.dumps.shift();
                  }
                  if (!tempThread.dumps.find((e) => e.time === data.time)) {
                    tempThread.dumps.push({
                      time: data.time,
                      threadState: data.value.dumpInfo.threadState,
                    });
                  }
                  tempGroup.threads[threadIndex] = tempThread;
                } else {
                  tempGroup.threads.push({
                    threadName: data.value.dumpInfo.threadName,
                    stackTrace: data.value.dumpInfo.stackTrace,
                    threadId: data.value.dumpInfo.threadId,
                    dumps: [
                      {
                        time: data.time,
                        threadState: data.value.dumpInfo.threadState,
                      },
                    ],
                  });
                }

                return prevState.toSpliced(groupIndex, 1, tempGroup);
              } else {
                return prevState.concat([
                  {
                    name: data.value.group,
                    threads: [
                      {
                        threadName: data.value.dumpInfo.threadName,
                        stackTrace: data.value.dumpInfo.stackTrace,
                        threadId: data.value.dumpInfo.threadId,
                        dumps: [
                          {
                            time: data.time,
                            threadState: data.value.dumpInfo.threadState,
                          },
                        ],
                      },
                    ],
                  },
                ]);
              }
            });
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
  const getMinMax = (vals: (number | { valueOf(): number })[]) => {
    const numericVals = vals.map(coerceNumber);

    return [Math.min(...numericVals), Math.max(...numericVals)];
  };

  return (
    <div className="w-full">
      <div className="w-full ml-32 h-10">
        <ParentSize>
          {({ width, height }) => {
            const [start, end] = getMinMax(timeValues);
            const totalSeconds = Math.floor(width / maxPixelsPerSeconds);
            const axisWidth = width - 128 - 40 + 10;

            return (
              <svg height={height} width={axisWidth}>
                <Axis
                  hideAxisLine
                  left={40}
                  scale={scaleUtc({
                    domain: [
                      start,
                      Math.max(start + (totalSeconds + 1) * 1000, end),
                    ],
                    range: [0, axisWidth],
                    nice: true,
                  })}
                  tickFormat={(v: NumberValue) => format(v as Date)}
                  tickLabelProps={{
                    fill: strokeColor,
                    fontSize: 12,
                    textAnchor: "middle",
                  }}
                  tickStroke={strokeColor}
                />
              </svg>
            );
          }}
        </ParentSize>
      </div>
      <Accordion isCompact selectionMode="multiple">
        {groups.map((group) => (
          <AccordionItem
            key={group.name}
            aria-label={group.name}
            title={
              <div className="flex justify-between">
                <h2>{group.name}</h2> <Chip>{group.threads.length}</Chip>
              </div>
            }
          >
            {group.threads.map((thread) => (
              <Accordion
                key={thread.threadName}
                hideIndicator
                isCompact
                showDivider={false}
              >
                <AccordionItem
                  title={
                    <div className="flex">
                      <div className="w-40 line-clamp-1">
                        {thread.threadName}
                      </div>
                      <div className="flex-1">
                        <ParentSize>
                          {({ width, height }) => {
                            const reactWidth = width - 128;

                            return (
                              <svg height={height} width={reactWidth}>
                                {thread.dumps.map((e, i) => (
                                  <rect
                                    key={i}
                                    className="transition-width duration-150"
                                    fill={types[e.threadState].color}
                                    height={20}
                                    width={20}
                                    x={i * 20}
                                    y={10}
                                  />
                                ))}
                              </svg>
                            );
                          }}
                        </ParentSize>
                      </div>
                    </div>
                  }
                >
                  <ul>
                    <li>
                      <span>ID: </span>
                      <span>{thread.threadId}</span>
                    </li>
                    <li>
                      <span>StackTrance: </span>
                      <span>
                        <pre>{JSON.stringify(thread.stackTrace, null, 2)}</pre>
                      </span>
                    </li>
                  </ul>
                </AccordionItem>
              </Accordion>
            ))}
          </AccordionItem>
        ))}
      </Accordion>
    </div>
  );
};
