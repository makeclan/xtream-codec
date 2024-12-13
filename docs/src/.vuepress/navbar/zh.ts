import {navbar} from "vuepress-theme-hope";

export const zhNavbar = navbar([
    // "/",
    {
        text: "指南",
        icon: "info",
        link: '/guide/'
    },
    {
        text: "扩展",
        icon: "info",
        link: '/ext/jt/jt808/'
    },
    {
        text: "FAQ",
        icon: "question",
        link: '/frequently-asked-questions/'
    },
    {
        text: "发版记录",
        icon: "info",
        link: '/release-notes/latest.md'
    },
    {
        text: '致谢',
        icon: 'share',
        children: [
            {text: 'Reactor Netty', link: 'https://projectreactor.io', icon: 'leaf'},
            {text: 'Netty', link: 'https://github.com/netty/netty', icon: 'leaf'},
            {text: 'Spring', icon: 'leaf', link: 'https://github.com/spring-projects'},
            {
                text: 'Vue', children: [
                    {text: 'VuePress', icon: 'vue', link: 'https://www.vuepress.cn/'},
                    {text: 'vuepress-theme-hope', icon: 'vue', link: 'https://theme-hope.vuejs.press/zh/'},
                ],
            },
        ]
    },
    {
        text: '关于我们',
        icon: 'copyright',
        link: '/about.md'
    },
]);
