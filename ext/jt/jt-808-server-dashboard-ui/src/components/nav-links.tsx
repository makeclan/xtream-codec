import { Link } from "@nextui-org/link";
import { useLocation } from "react-router-dom";
import clsx from "clsx";

import { siteConfig } from "@/config/site";

export const NavLinks = () => {
  const location = useLocation();

  return (
    <>
      {siteConfig.sidenav.map((link) => {
        const LinkIcon = link.icon;

        return (
          <Link
            key={link.name}
            className={clsx(
              "py-2 md:px-10",
              link.href === location.pathname ? "md:bg-content1" : "",
            )}
            color="foreground"
            href={link.href}
          >
            <LinkIcon
              className={clsx(
                "xw-6 mx-1",
                link.href === location.pathname
                  ? "text-primary"
                  : "text-foreground",
              )}
            />
            <p className="hidden md:block px-2">{link.name}</p>
          </Link>
        );
      })}
    </>
  );
};
