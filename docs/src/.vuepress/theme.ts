import {hopeTheme} from "vuepress-theme-hope";
import {zhNavbar} from "./navbar";
import {zhSidebar} from "./sidebar";
import { getDirname, path } from "vuepress/utils"


export default hopeTheme({
    hostname: "https://mister-hope.github.io",
    author: {
        name: "xtream-codec",
        url: "https://github.com/hylexus",
    },

    // https://theme-hope.vuejs.press/zh/guide/interface/icon.html
    iconAssets: "iconfont",

    logo: "/logo.svg",

    repo: "https://github.com/hylexus/xtream-codec",

    docsDir: "src",

    blog: {
        medias: {
            Gitee: "https://gitee.com/hylexus/xtream-codec",
            GitHub: "https://github.com/hylexus/xtream-codec",
        },
    },
    navbarLayout: {
        start: ["Brand"],
        center: ["Links"],
        end: ["RepoGitee", "Repo", 'SocialLink', "Outlook", "Search"],
    },
    locales: {
        /**
         * Chinese locale config
         */
        "/": {
            // navbar
            navbar: zhNavbar,

            // sidebar
            sidebar: zhSidebar,

            // footer: "默认页脚",

            displayFooter: true,

            blog: {
                description: "......",
                intro: "/core/index.html",
            },

            // page meta
            metaLocales: {
                editLink: "在 GitHub 上编辑此页",
            },
        },
    },

    // enable it to preview all changes in time
    hotReload: true,

    plugins: {
        blog: true,
        searchPro: {
            // 索引全部内容
            indexContent: true,
            hotKeys: [{key: 'k', ctrl: true}],
            // 为分类和标签添加索引
            customFields: [
                {
                    getter: (page) => page.frontmatter.category,
                    formatter: "分类：$content",
                },
                {
                    getter: (page) => page.frontmatter.tag,
                    formatter: "标签：$content",
                },
            ],
        },
        // install @waline/client before enabling it
        // WARNING: This is a test server for demo only.
        // You should create and use your own comment service in production.
        // comment: {
        //   provider: "Waline",
        //   serverURL: "https://waline-comment.vuejs.press",
        // },

        components: {
            components: ["Badge", "VPCard"],
        },

        // all features are enabled for demo, only preserve features you need here
        mdEnhance: {
            include: {
                resolvePath: (file) => {
                    if (file.startsWith("@src"))
                        return file.replace("@src", path.resolve(__dirname, ".."));

                    return file;
                },
            },
            align: true,
            attrs: true,
            codetabs: true,
            component: true,
            demo: true,
            figure: true,
            imgLazyload: true,
            imgSize: true,
            mark: true,
            stylize: [
                {
                    matcher: "Recommended",
                    replacer: ({tag}) => {
                        if (tag === "em")
                            return {
                                tag: "Badge",
                                attrs: {type: "tip"},
                                content: "Recommended",
                            };
                    },
                },
            ],
            sub: true,
            sup: true,
            tabs: true,
            vPre: true,

            // install chart.js before enabling it
            // chart: true,

            // insert component easily

            // install echarts before enabling it
            // echarts: true,

            // install flowchart.ts before enabling it
            // flowchart: true,

            // gfm requires mathjax-full to provide tex support
            // gfm: true,

            // install katex before enabling it
            // katex: true,

            // install mathjax-full before enabling it
            // mathjax: true,

            // install mermaid before enabling it
            // mermaid: true,

            // playground: {
            //   presets: ["ts", "vue"],
            // },

            // install reveal.js before enabling it
            // revealJs: {
            //   plugins: ["highlight", "math", "search", "notes", "zoom"],
            // },

            // install @vue/repl before enabling it
            // vuePlayground: true,

            // install sandpack-vue3 before enabling it
            // sandpack: true,
        },

        // install @vuepress/plugin-pwa and uncomment these if you want a PWA
        // pwa: {
        //   favicon: "/favicon.ico",
        //   cacheHTML: true,
        //   cachePic: true,
        //   appendBase: true,
        //   apple: {
        //     icon: "/assets/icon/apple-icon-152.png",
        //     statusBarColor: "black",
        //   },
        //   msTile: {
        //     image: "/assets/icon/ms-icon-144.png",
        //     color: "#ffffff",
        //   },
        //   manifest: {
        //     icons: [
        //       {
        //         src: "/assets/icon/chrome-mask-512.png",
        //         sizes: "512x512",
        //         purpose: "maskable",
        //         type: "image/png",
        //       },
        //       {
        //         src: "/assets/icon/chrome-mask-192.png",
        //         sizes: "192x192",
        //         purpose: "maskable",
        //         type: "image/png",
        //       },
        //       {
        //         src: "/assets/icon/chrome-512.png",
        //         sizes: "512x512",
        //         type: "image/png",
        //       },
        //       {
        //         src: "/assets/icon/chrome-192.png",
        //         sizes: "192x192",
        //         type: "image/png",
        //       },
        //     ],
        //     shortcuts: [
        //       {
        //         name: "Demo",
        //         short_name: "Demo",
        //         url: "/demo/",
        //         icons: [
        //           {
        //             src: "/assets/icon/guide-maskable.png",
        //             sizes: "192x192",
        //             purpose: "maskable",
        //             type: "image/png",
        //           },
        //         ],
        //       },
        //     ],
        //   },
        // },
    },
});
