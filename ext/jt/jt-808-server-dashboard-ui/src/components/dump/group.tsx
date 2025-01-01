import { Accordion, AccordionItem } from "@nextui-org/accordion";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { useEffect, useState } from "react";
// @ts-ignore
import { Axis, AxisTop } from "@visx/axis";
import { timeFormat } from "@visx/vendor/d3-time-format";
import { coerceNumber, scaleUtc } from "@visx/scale";

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
const types = {
  NEW: { color: "#7b9ce1" },
  RUNNABLE: { color: "#bd6d6c" },
  BLOCKED: { color: "#75d874" },
  WAITING: { color: "#e0bc78" },
  TIMED_WAITING: { color: "#dc77dc" },
  TERMINATED: { color: "#72b362" },
};

interface Group {
  name: string;
  threads: [
    {
      threadName: string;
      dumps: {
        time: string;
        threadState:
          | "NEW"
          | "RUNNABLE"
          | "BLOCKED"
          | "WAITING"
          | "TIMED_WAITING"
          | "TERMINATED";
      }[];
    },
  ];
}
export const DumpGroup = () => {
  const [groups, setGroups] = useState<Group[]>([]);
  const [timeValues, setTimeValues] = useState<Date[]>([]);

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

            // @ts-ignore
            if (!timeValues.find((e) => e === data.time)) {
              setTimeValues((prevState) => {
                if (prevState.length > 100) {
                  prevState.shift();
                }

                return prevState.concat([new Date(data.time)]);
              });
            }
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

                  if (tempThread.dumps.length > 100) {
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
      <svg className="w-full ml-40">
        <Axis
          label="time"
          scale={scaleUtc({
            domain: getMinMax(timeValues),
            range: [0, 800],
          })}
          // @ts-ignore
          tickFormat={(v: Date) => timeFormat("%H:%m:%S")(v)}
        />
      </svg>
      <Accordion selectionMode="multiple">
        {groups &&
          groups.map((group) => (
            <AccordionItem
              key={group.name}
              aria-label={group.name}
              subtitle="Press to expand"
              title={group.name}
            >
              {group.threads.map((thread) => (
                <div key={thread.threadName} className="flex">
                  <div className="w-40 line-clamp-1">{thread.threadName}</div>
                  <div className="w-full">
                    {thread.dumps.map((e) => (
                      <span
                        key={thread.threadName + e.time}
                        className="w-3.5 h-2 inline-block"
                        style={{
                          background: types[e.threadState].color,
                        }}
                      />
                    ))}
                  </div>
                </div>
              ))}
            </AccordionItem>
          ))}
      </Accordion>
    </div>
  );
};
