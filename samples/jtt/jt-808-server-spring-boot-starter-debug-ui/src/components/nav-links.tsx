import { Link } from "@nextui-org/link";
import { useLocation } from "react-router-dom";

import { siteConfig } from "@/config/site";

export default function NavLinks() {
  const location = useLocation();

  return (
    <>
      {siteConfig.sidenav.map((link) => {
        const LinkIcon = link.icon;

        return (
          <Link
            key={link.name}
            className="p-2"
            color={link.href === location.pathname ? "primary" : "foreground"}
            href={link.href}
          >
            <LinkIcon className="w-6" />
            <p className="hidden md:block">{link.name}</p>
          </Link>
        );
      })}
    </>
  );
}
