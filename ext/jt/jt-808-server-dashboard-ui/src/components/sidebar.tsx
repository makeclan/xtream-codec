import { Link } from "@nextui-org/link";
import { useState } from "react";
import { Listbox, ListboxItem } from "@nextui-org/listbox";
import clsx from "clsx";
import { Tooltip } from "@nextui-org/tooltip";
import { Button } from "@nextui-org/button";

import { FaChevronRightIcon, LogoIcon } from "@/components/icons";
import { siteConfig } from "@/config/site.ts";

const TopContent = () => {
  return (
    <Link
      className="flex items-center px-3 justify-center gap-0 mb-8"
      href={siteConfig.links.github}
    >
      <LogoIcon className="flex h-8 w-8 items-center justify-center rounded-full" />
    </Link>
  );
};

export const Sidebar = () => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="relative">
      <Listbox
        aria-label="sideBar"
        bottomContent={<div className="mt-28 flex-1" />}
        className={clsx(
          "relative flex h-full flex-col !border-r-small border-divider p-6 transition-width items-center",
          isOpen ? "w-56" : "w-16 px-2 py-6",
        )}
        disabledKeys={["/debug", "/threads", "/dump"]}
        topContent={<TopContent />}
      >
        {siteConfig.sidenav.map((link) => {
          const LinkIcon = link.icon;
          const iconClasses =
            "pointer-events-none flex-shrink-0 xw-6 mx-1 fa-fw text-xl";

          return (
            <ListboxItem
              key={link.href}
              className="py-2 min-h-11 group text-default-500 active:bg-default-100 hover:transition-colors relative selected:bg-default-100 selected:text-foreground"
              color={link.href === "/debug" ? "danger" : "default"}
              href={link.href}
            >
              {!isOpen ? (
                <Tooltip content={link.name} placement="right">
                  <div className="truncate flex items-center justify-between gap-2">
                    <LinkIcon className={iconClasses} />
                    <span className="flex-1 truncate font-medium text-base" />
                  </div>
                </Tooltip>
              ) : (
                <div className="truncate flex items-center justify-between gap-2">
                  <LinkIcon className={iconClasses} />
                  <span className="flex-1 truncate font-medium text-base">
                    {link.name}
                  </span>
                </div>
              )}
            </ListboxItem>
          );
        })}
      </Listbox>
      <Button
        isIconOnly
        className="absolute top-[30px] right-[-10px] text-white shadow-lg text-small min-w-5 w-5 h-5"
        radius="full"
        size="sm"
        onPress={() => setIsOpen(!isOpen)}
      >
        <FaChevronRightIcon
          className={clsx("fa-sm", isOpen ? "fa-rotate-180" : "")}
        />
      </Button>
    </div>
  );
};
