import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { Card, CardBody, CardHeader } from "@nextui-org/card";

import { Thread } from "@/types";
import { JsonPreview } from "@/components/json-preview.tsx";

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
              value: data.value.value,
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
    <div className="gap-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
      {threads.map((thread: Thread) => (
        <Card key={thread.name}>
          <CardHeader>{thread.name}</CardHeader>
          <CardBody>
            <p className="mb-2">time: {thread.time.slice(0, -4)}</p>
            <JsonPreview json={thread.value} page="threads" />
          </CardBody>
        </Card>
      ))}
    </div>
  );
};
