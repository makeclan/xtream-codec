import { Route, Routes } from "react-router-dom";

import IndexPage from "@/pages/index";
import DebugPage from "@/pages/debug.tsx";
import EventPage from "@/pages/event.tsx";

function App() {
  return (
    <Routes>
      <Route element={<IndexPage />} path="/" />
      <Route element={<DebugPage />} path="/debug" />
      <Route element={<EventPage />} path="/event" />
    </Routes>
  );
}

export default App;
