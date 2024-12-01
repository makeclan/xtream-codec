import { Outlet } from "react-router-dom";

import { Navbar } from "@/components/navbar";
import { SideNav } from "@/components/sidenav.tsx";
import { Provider } from "@/provider.tsx";

export const DashboardLayout = () => {
  return (
    <Provider>
      <div className=" flex max-w-8xl mx-auto h-screen flex-col md:flex-row md:overflow-hidden">
        <div className=" w-full flex-none md:w-64">
          <SideNav />
        </div>
        <div className=" flex-grow md:overflow-y-auto">
          <Navbar />
          <main className="container mx-auto max-w-7xl px-6 py-8 flex-grow">
            <Outlet />
          </main>
        </div>
      </div>
    </Provider>
  );
};
