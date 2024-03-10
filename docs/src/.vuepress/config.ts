import {defineUserConfig} from "vuepress";
import theme from "./theme.js";
import {path} from "vuepress/utils"

export default defineUserConfig({
    base: "/xtream-codec/",
    port: 8090,
    locales: {
        "/": {
            lang: "zh-CN",
            title: "xtream-codec",
            description: "xtream-codec 文档",
        },
    },

    theme,

    // Enable it with pwa
    // shouldPrefetch: false,
    markdown: {
        importCode: {
            handleImportPath: (str) =>
                str.replace(
                    /^@src/,
                    path.resolve(__dirname, '../code-snippet/')
                ),
        },
    },
});
