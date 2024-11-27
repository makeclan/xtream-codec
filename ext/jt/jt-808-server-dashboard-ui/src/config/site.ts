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
    { name: "Dashboard", href: "/dashboard", icon: DashboardIcon },
    {
      name: "Attachment",
      href: "/attachment",
      icon: AttachmentIcon,
    },
    { name: "Instruction", href: "/instruction", icon: SessionIcon },
    { name: "Subscriber", href: "/subscriber", icon: SubIcon },
    { name: "Debug", href: "/debug", icon: CommandIcon },
    { name: "Configuration", href: "/configuration", icon: ConfigIcon },
  ],
  links: {
    github: "https://github.com/hylexus/jt-framework",
    sponsor: "https://github.com/hylexus/jt-framework",
  },
};
