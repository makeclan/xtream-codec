import { useEffect, useState } from "react";
import {
  EventSourceMessage,
  fetchEventSource,
} from "@microsoft/fetch-event-source";
import { Card, CardBody, CardHeader } from "@nextui-org/card";
import { Code } from "@nextui-org/code";

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
    <div className="gap-4 grid grid-cols-2 sm:grid-cols-3">
      {threads.map((thread: Thread) => (
        <Card key={thread.name}>
          <CardHeader>{thread.name}</CardHeader>
          <CardBody>
            <p>time: {thread.time}</p>
            {/*TODO start 数据展示UI重新设计*/}
            <Code>
              <pre>
                {JSON.stringify(thread.value, null, 2)
                  .replace(/["{},]/g, "")
                  .replace(/\n {2}\n/g, "\n")}
              </pre>
            </Code>
            {/*end 数据展示UI重新设计*/}
          </CardBody>
        </Card>
      ))}
    </div>
  );
};
