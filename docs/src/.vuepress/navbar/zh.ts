import {navbar} from "vuepress-theme-hope";

export const zhNavbar = navbar([
    // "/",
    {
        text: "指南",
        icon: "creative",
        link: '/core/'
    },
    {
        text: "FAQ",
        icon: "question",
        link: '/frequently-asked-questions/'
    },
    {
        text: "发版记录",
        icon: "branch",
        link: '/release-notes/latest.md'
    },
    {
        text: '致谢',
        icon: 'share',
        children: [
            {text: 'Netty', link: 'https://github.com/netty/netty', icon: 'leaf'},
            {text: 'Spring', icon: 'leaf', link: 'https://github.com/spring-projects'},
        ]
    },
    {
        text: '关于我们',
        icon: 'copyright',
        link: '/about.md'
    },
]);
