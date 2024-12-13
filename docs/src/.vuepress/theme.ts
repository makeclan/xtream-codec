import {hopeTheme} from "vuepress-theme-hope";
import {zhNavbar} from "./navbar";
import {zhSidebar} from "./sidebar";
import { path } from "vuepress/utils"


export default hopeTheme({
    hostname: "https://mister-hope.github.io",
    author: {
        name: "xtream-codec",
        url: "https://github.com/hylexus",
    },

    // https://theme-hope.vuejs.press/zh/guide/interface/icon.html
    iconAssets: "fontawesome",

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
    markdown: {
        codetabs: true,
        component: true,
        figure: true,
        imgLazyload: true,
        imgSize: true,
        tabs: true,
        include: {
            resolvePath: (file) => {
                if (file.startsWith("@src"))
                    return file.replace("@src", path.resolve(__dirname, ".."));

                return file;
            },
        },
        align: true,
        attrs: true,
        demo: true,
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
        vPre: true,
        slimsearch: {
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
        }
    },
    plugins: {
        blog: true,
        components: {
            components: ["Badge", "VPCard"],
        },
    },
});
