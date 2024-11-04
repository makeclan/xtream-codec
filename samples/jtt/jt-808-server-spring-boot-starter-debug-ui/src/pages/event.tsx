import { fetchEventSource } from "@microsoft/fetch-event-source";
import { useEffect, useState } from "react";
import {Code} from '@nextui-org/code';
import DefaultLayout from "@/layouts/default";

export default function EventPage() {
  const [data, setData] = useState<Event[]>([]);

  useEffect(() => {
    const ctrl = new AbortController();

    fetchEventSource(
      `${import.meta.env.VITE_API_BASE_URL}api/v1/message/event/sse`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        signal: ctrl.signal,
        onmessage: function (event) {
          console.log("Received message:", event.data);
          setData((pre) => pre.concat(JSON.parse(event.data)));
        },
      },
    );

    return () => {
      ctrl.abort();
    };
  }, []);

  return (
    <DefaultLayout>
      <p>LIST</p>
      {data.map((e, index) => (
        <Code key={index} color="default">
          {Object.keys(e).map((key) => key + ":" + e[key])}
        </Code>
      ))}
    </DefaultLayout>
  );
}
