// export type SiteConfig = typeof siteConfig;

import {
  GithubIcon,
  HeartFilledIcon,
  MoonFilledIcon,
  SearchIcon,
  SunFilledIcon
} from "@/components/icons.tsx";

export const siteConfig = {
  name: "Xtream",
  description: "Xtream",
  sidenav: [
    { name: "Dashboard", href: "/dashboard", icon: GithubIcon },
    {
      name: "Attachment",
      href: "/attachment",
      icon: HeartFilledIcon,
    },
    { name: "Instruction", href: "/instruction", icon: MoonFilledIcon },
    { name: "Subscriber", href: "/subscriber", icon: SearchIcon },
    { name: "Debug", href: "/debug", icon: SunFilledIcon },
    { name: "Configuration", href: "/configuration", icon: MoonFilledIcon },
  ],
  links: {
    github: "https://github.com/hylexus/jt-framework",
    sponsor: "https://github.com/hylexus/jt-framework",
  },
};
