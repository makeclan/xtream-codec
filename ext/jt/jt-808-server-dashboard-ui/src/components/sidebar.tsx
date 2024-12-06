import { Link } from "@nextui-org/link";

import { LogoIcon } from "@/components/icons";
import { siteConfig } from "@/config/site.ts";
export const Sidebar = () => {
  return (
    <div className="flex h-full flex-col p-6">
      <Link
        className="mb-2 flex h-10  items-center justify-center md:h-20"
        href={siteConfig.links.github}
      >
        <LogoIcon />
        <p>{siteConfig.name}</p>
      </Link>
      <div className="flex grow flex-row justify-between space-x-2 md:flex-col md:space-x-0 md:space-y-2">
        {siteConfig.sidenav.map((link) => {
          const LinkIcon = link.icon;

          return (
            <Link
              key={link.name}
              isBlock
              className="py-2 md:px-10 text-default-500 selected:text-foreground active:bg-default-100 hover:transition-colors"
              href={link.href}
            >
              <LinkIcon className="xw-6 mx-1" />
              <p className="hidden md:block px-2">{link.name}</p>
            </Link>
          );
        })}
        <div className="hidden h-auto w-full grow rounded-md md:block" />
      </div>
    </div>
  );
};
