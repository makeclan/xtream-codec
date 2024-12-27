import { Button } from "@nextui-org/button";
import { Link } from "@nextui-org/link";
import {
  Navbar as NextUINavbar,
  NavbarContent,
  NavbarItem,
} from "@nextui-org/navbar";
import confetti from "canvas-confetti";

import { siteConfig } from "@/config/site";
import { ThemeSwitch } from "@/components/theme-switch";
import { GiteeIcon, FaGithubIcon, FaHeatIcon } from "@/components/icons";

export const Navbar = () => {
  const handleConfetti = () => {
    confetti({
      particleCount: 100,
      spread: 100,
      origin: { y: 0.6 },
    });
  };

  return (
    <NextUINavbar
      className="flex bg-transparent items-center gap-3 px-4"
      maxWidth="full"
      position="sticky"
    >
      <NavbarContent className="hidden sm:flex" justify="end">
        <NavbarItem className="hidden sm:flex gap-2">
          <Link isExternal href={siteConfig.links.github} title="GitHub">
            <FaGithubIcon className="text-default-500" size="xl" />
          </Link>
          <Link isExternal href={siteConfig.links.gitee} title="Gitee">
            <GiteeIcon className="text-default-500" />
          </Link>
          <ThemeSwitch />
        </NavbarItem>
        <NavbarItem className="hidden md:flex">
          <Button
            className="text-sm font-normal text-default-600 bg-default-100"
            startContent={<FaHeatIcon className="text-danger text-2xl" />}
            variant="flat"
            onPress={handleConfetti}
          >
            Sponsor
          </Button>
        </NavbarItem>
      </NavbarContent>

      <NavbarContent className="sm:hidden basis-1 pl-4" justify="end">
        <Link isExternal href={siteConfig.links.github}>
          <FaGithubIcon className="text-default-500" size="xl" />
        </Link>
        <Link isExternal href={siteConfig.links.gitee} title="Gitee">
          <GiteeIcon className="text-default-500" />
        </Link>
        <ThemeSwitch />
      </NavbarContent>
    </NextUINavbar>
  );
};
