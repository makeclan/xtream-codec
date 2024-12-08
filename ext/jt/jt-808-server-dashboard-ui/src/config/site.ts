// export type SiteConfig = typeof siteConfig;

import {
  AttachmentIcon,
  SessionIcon,
  CommandIcon,
  SubIcon,
  DashboardIcon,
  ConfigIcon,
} from "@/components/icons.tsx";

export const siteConfig = {
  name: "Xtream",
  description: "Xtream",
  sidenav: [
    { name: "仪表盘", href: "/dashboard", icon: DashboardIcon },
    { name: "808服务", href: "/instruction", icon: SessionIcon },
    {
      name: "附件服务",
      href: "/attachment",
      icon: AttachmentIcon,
    },
    { name: "订阅", href: "/subscriber", icon: SubIcon },
    { name: "配置", href: "/configuration", icon: ConfigIcon },
    { name: "映射", href: "/mappings", icon: ConfigIcon },
    { name: "线程转储", href: "/dump", icon: ConfigIcon },
    { name: "线程监控", href: "/threads", icon: ConfigIcon },
    { name: "调试", href: "/debug", icon: CommandIcon },
  ],
  links: {
    gitee: "https://gitee.com/hylexus/xtream-codec",
    github: "https://github.com/hylexus/xtream-codec",
    sponsor: "https://github.com/hylexus/jt-framework",
  },
};
