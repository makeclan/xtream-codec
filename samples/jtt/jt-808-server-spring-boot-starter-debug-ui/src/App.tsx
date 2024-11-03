import { Route, Routes } from "react-router-dom";

import IndexPage from "@/pages/index";
import DebugPage from "@/pages/debug.tsx";

function App() {
  return (
    <Routes>
      <Route element={<IndexPage />} path="/" />
      <Route element={<DebugPage />} path="/debug" />
    </Routes>
  );
}

export default App;
