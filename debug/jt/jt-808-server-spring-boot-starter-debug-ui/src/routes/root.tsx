import { request } from "@/utils/request.ts";
import { ServerInfo } from "@/types";

export async function loader() {
  const path = `config`;
  let data: ServerInfo = {
    xtreamCodecVersion: "Unknown",
    serverStartupTime: "",
    configuration: {},
  };

  try {
    data = await request({
      path,
      method: "GET",
    });
  } catch (error) {
    // TODO
    console.log(error);
  }

  return { config: data };
}
