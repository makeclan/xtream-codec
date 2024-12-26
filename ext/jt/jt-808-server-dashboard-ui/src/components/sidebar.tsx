import { Link } from "@nextui-org/link";
import { useState } from "react";
import { Listbox, ListboxItem } from "@nextui-org/listbox";
import { ReactNode } from "react";
import clsx from "clsx";

import { FaChevronDownIcon, LogoIcon } from "@/components/icons";
import { siteConfig } from "@/config/site.ts";

const ListboxWrapper = ({
  isOpen,
  children,
}: {
  isOpen: boolean;
  children: ReactNode;
}) => (
  <div
    className={clsx(
      "h-full transition-width px-2 py-2 flex flex-col items-center",
      isOpen ? "w-64" : "w-16",
    )}
  >
    {children}
  </div>
);

const TopContent = () => {
  return (
    <Link
      className="flex items-center px-3 justify-center gap-0"
      href={siteConfig.links.github}
    >
      <LogoIcon />
    </Link>
  );
};

export const Sidebar = () => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <ListboxWrapper isOpen={isOpen}>
      <Listbox
        bottomContent={
          <FaChevronDownIcon
            className="fa-rotate-270"
            onClick={() => setIsOpen(!isOpen)}
          />
        }
        disabledKeys={["/debug"]}
        items={siteConfig.sidenav}
        topContent={<TopContent />}
      >
        {(link) => {
          const LinkIcon = link.icon;
          const iconClasses =
            "text-xl text-default-500 pointer-events-none flex-shrink-0 xw-6 mx-1";

          return (
            <ListboxItem
              key={link.href}
              className="py-2 text-default-500 selected:text-foreground active:bg-default-100 hover:transition-colors"
              color={link.href === "/debug" ? "danger" : "default"}
              href={link.href}
              startContent={<LinkIcon className={iconClasses} />}
            >
              {link.name}
            </ListboxItem>
          );
        }}
      </Listbox>
    </ListboxWrapper>
  );
};
