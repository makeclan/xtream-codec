import { Outlet } from "react-router-dom";

import { Navbar } from "@/components/navbar";
import { Sidebar } from "@/components/sidebar.tsx";
import { Provider } from "@/provider.tsx";
import { AwesomeBg } from "@/components/awesome-bg.tsx";

export const DashboardLayout = () => {
  return (
    <Provider>
      <AwesomeBg />
      <div className=" flex h-screen max-w-8xl mx-auto">
        <Sidebar />
        <div className=" w-full flex-1 flex-col p-4">
          <Navbar />
          <main className="m-4 h-[90%] w-full overflow-visible relative overflow-x-auto overflow-y-auto">
            <Outlet />
          </main>
        </div>
      </div>
    </Provider>
  );
};
