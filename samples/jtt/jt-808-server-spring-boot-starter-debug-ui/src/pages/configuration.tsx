import { useRouteLoaderData } from "react-router-dom";

import { Code } from "@nextui-org/code";
import { ServerInfo } from "@/types";

export default function ConfigurationPage() {
  const { config } = useRouteLoaderData("root") as { config: ServerInfo };

  return (
    <Code>
      <pre>{JSON.stringify(config.configuration, null, 2)}</pre>
    </Code>
  );
}
