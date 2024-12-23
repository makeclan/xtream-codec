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
        target: "http://localhost:8888",
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
            [
              "@nextui-org/system",
              "@nextui-org/theme",
              "@nextui-org/accordion",
              "@nextui-org/badge",
              "@nextui-org/button",
              "@nextui-org/card",
              "@nextui-org/switch",
              "@nextui-org/kbd",
              "@nextui-org/scroll-shadow",
              "@nextui-org/tooltip",
              "@nextui-org/modal",
              "framer-motion",
            ].some((e) => id.includes(e))
          ) {
            return "vendors1";
          } else if (
            ["@nextui-org/", "@fortawesome/", "@visx/"].some(
              (e) =>
                id.includes(e) ||
                [
                  "canvas-confetti",
                  "react-countup",
                  "swr",
                  "axios",
                  "framer-motion",
                ].includes(id),
            )
          ) {
            return "vendors2";
          }
        },
      },
    },
  },
});
