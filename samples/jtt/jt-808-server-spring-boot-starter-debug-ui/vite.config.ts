import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tsconfigPaths from "vite-tsconfig-paths";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  server: {
    proxy: {
      "/dashboard-api": {
        target: "http://localhost:9999",
        changeOrigin: true,
        rewrite: (path) =>
          path.replace(/^\/dashboard-api/, "/dashboard-api/v1"),
      },
    },
  },
});
