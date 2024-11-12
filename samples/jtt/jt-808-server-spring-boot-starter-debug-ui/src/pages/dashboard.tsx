import { Code } from "@nextui-org/code";
import { useRouteLoaderData } from "react-router-dom";

import CardBox from "@/components/card-box.tsx";

export default function DashboardPage() {
  const config = useRouteLoaderData("root");

  return (
    <>
      <CardBox />
      <Code className=" w-full">
        <pre>{config ? JSON.stringify(config, null, 2) : null}</pre>
      </Code>
    </>
  );
}
