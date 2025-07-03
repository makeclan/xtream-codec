import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tsconfigPaths from "vite-tsconfig-paths";
import { visualizer } from "rollup-plugin-visualizer";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), tsconfigPaths(), visualizer()],
  base: process.env.VITE_BASE_PATH || "/dashboard-ui/",
  server: {
    proxy: {
      "/dashboard-api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) =>
          path.replace(/^\/dashboard-api/, "/dashboard-api/v1"),
      },
    },
  },
  build: {
    chunkSizeWarningLimit: 600,
    rollupOptions: {
      output: {
        manualChunks: (id) => {
          if (
            ["@heroui/accordion", "@heroui/avatar"].some((e) => id.includes(e))
          ) {
            return "vendors1";
          } else if (id.includes("@visx/")) {
            return "vendors2";
          }
        },
      },
    },
  },
});
