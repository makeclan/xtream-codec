import { useRouteLoaderData } from "react-router-dom";

import { JsonPreview } from "@/components/json-preview.tsx";
import { ServerInfo } from "@/types";

export const ConfigurationPage = () => {
  const { config } = useRouteLoaderData("root") as { config: ServerInfo };

  return (
    <div>
      <JsonPreview json={config.configuration} />
    </div>
  );
};
