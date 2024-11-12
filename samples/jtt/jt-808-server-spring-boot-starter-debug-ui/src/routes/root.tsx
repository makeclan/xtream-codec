import { request } from "@/utils/request.ts";

export async function loader() {
  const path = `${import.meta.env.VITE_API_DASHBOARD_V1}config`;
  let data = null;

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
