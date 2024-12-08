import { Outlet } from "react-router-dom";

import { Navbar } from "@/components/navbar";
import { Sidebar } from "@/components/sidebar.tsx";
import { Provider } from "@/provider.tsx";
import { AwesomeBg } from "@/components/awesome-bg.tsx";

export const DashboardLayout = () => {
  return (
    <Provider>
      <AwesomeBg />
      <div className=" flex max-w-8xl mx-auto h-screen flex-col md:flex-row md:overflow-hidden">
        <Sidebar />
        <div className=" flex-grow md:overflow-y-auto">
          <Navbar />
          <main className="container mx-auto px-6 py-8">
            <Outlet />
          </main>
        </div>
      </div>
    </Provider>
  );
};
