import { request } from "@/utils/request.ts";
import { ServerInfo } from "@/types";

export async function loader() {
  const path = `${import.meta.env.VITE_API_DASHBOARD_V1}config`;
  let data = {
    xtreamCodecVersion: "Unknown",
    serverStartupTime: "",
    configuration: {},
  } as ServerInfo;

  try {
    data = await request({
      path,
      method: "GET",
    });
  } catch (error) {
    // TODO
    console.log(error);
  }

  return { config: data.configuration };
}
