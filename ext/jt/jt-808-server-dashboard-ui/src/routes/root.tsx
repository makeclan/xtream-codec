import { request } from "@/utils/request.ts";
import { Dependencies, JavaInfo, OsInfo, ServerInfo } from "@/types";

export async function loader() {
  const path = `server-info`;
  let data: ServerInfo = {
    dependencies: {} as Dependencies,
    serverStartupTime: "",
    jt808ServerConfig: {},
    java: {} as JavaInfo,
    os: {} as OsInfo,
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
