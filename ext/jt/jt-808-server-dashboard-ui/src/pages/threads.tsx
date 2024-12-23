import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { Card, CardBody, CardHeader } from "@nextui-org/card";

import { Thread } from "@/types";

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

          setThreads((pre) => {
            const tempThread = {
              time: data.time,
              name: data.value.name,
              value: {
                "active.task": data.value.value.active.tasks,
                "completed.count": data.value.value.completed.count,
                "completed.max": data.value.value.completed.max,
                "pending.active": data.value.value.pending.active,
                "submitted.direct": data.value.value.submitted.direct,
              },
            };
            const index = pre.findIndex((e) => e.name === data.value.name);

            if (index !== -1) {
              return pre.toSpliced(index, 1, tempThread);
            } else {
              return pre.concat([tempThread]);
            }
          });
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
    <div className="gap-4 grid grid-cols-1 sm:grid-cols-2">
      {threads.map((thread: Thread) => (
        <Card key={thread.name}>
          <CardHeader>{thread.name}</CardHeader>
          <CardBody>这里是线程卡片</CardBody>
        </Card>
      ))}
    </div>
  );
};
