import { createBrowserRouter, RouterProvider } from "react-router-dom";

import DebugPage from "@/pages/debug.tsx";
import AttachmentPage from "@/pages/attachment.tsx";
import InstructionPage from "@/pages/instruction.tsx";
import DashboardPage from "@/pages/dashboard.tsx";
import DashboardLayout from "@/layouts/dashboard.tsx";
import { loader as rootLoader } from "@/routes/root.tsx";
const router = createBrowserRouter([
  {
    path: "/",
    id: "root",
    element: <DashboardLayout />,
    loader: rootLoader,
    children: [
      {
        index: true,
        element: <DashboardPage />,
      },
      {
        path: "dashboard",
        element: <DashboardPage />,
      },
      {
        path: "instruction",
        element: <InstructionPage />,
      },
      {
        path: "attachment",
        element: <AttachmentPage />,
      },
      {
        path: "debug",
        element: <DebugPage />,
      },
    ],
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
