import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { Card, CardBody } from "@nextui-org/card";
import { cloneDeep } from "lodash-es";

import { Thread } from "@/types";
import { DynamicThreadsCharts } from "@/components/dashboard/dynamic-threads-charts.tsx";

export const ThreadsPage = () => {
  const [threads, setThreads] = useState<Thread[]>([]);

  useEffect(() => {
    const ctrl = new AbortController();

    fetchEventSource(
      `${import.meta.env.VITE_API_DASHBOARD_V1}metrics/schedulers`,
      {
        method: "GET",
        signal: ctrl.signal,
        onmessage: (event: EventSourceMessage) => {
          const data = JSON.parse(event.data);
          const tmp_threads = cloneDeep(threads);
          const index = threads.findIndex((e) => e.name === data.value.name);

          if (index !== -1) {
            tmp_threads[index] = {
              time: data.time,
              name: data.value.name,
              value: {
                peak: data.value.active.tasks,
                daemon: data.value.completed.count,
                live: data.value.pending.active,
                started: data.value.submitted.direct,
              },
            };
          } else {
            tmp_threads.push({
              time: data.time,
              name: data.value.name,
              value: {
                peak: data.value.active.tasks,
                daemon: data.value.completed.count,
                live: data.value.pending.active,
                started: data.value.submitted.direct,
              },
            });
          }
          setThreads(tmp_threads);
        },
      },
    ).then(() => {
      // TODO
    });

    return () => {
      ctrl.abort();
    };
  }, []);

  return (
    <div className="gap-4 grid grid-cols-1 sm:grid-cols-3">
      {threads.map((thread: Thread) => (
        <Card key={thread.name}>
          <CardBody>
            <DynamicThreadsCharts data={thread} />
          </CardBody>
        </Card>
      ))}
    </div>
  );
};
