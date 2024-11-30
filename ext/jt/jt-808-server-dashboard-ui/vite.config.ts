import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tsconfigPaths from "vite-tsconfig-paths";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  base: process.env.VITE_BASE_PATH || "/dashboard-ui/",
  server: {
    proxy: {
      "/dashboard-api": {
        target: "http://localhost:8888",
        changeOrigin: true,
        rewrite: (path) =>
          path.replace(/^\/dashboard-api/, "/dashboard-api/v1"),
      },
    },
  },
});
