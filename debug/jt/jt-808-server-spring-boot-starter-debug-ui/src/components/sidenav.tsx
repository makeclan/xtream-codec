import { Link } from "@nextui-org/link";

import { LogoIcon } from "@/components/icons";
import NavLinks from "@/components/nav-links.tsx";

export default function SideNav() {
  return (
    <div className="flex h-full flex-col px-3 py-4 md:px-2 bg-#060606">
      <Link
        className="mb-2 flex h-10 items-start justify-center rounded-md p-4 md:h-20"
        href="/"
      >
        <div className=" text-white">
          <LogoIcon />
        </div>
      </Link>
      <div className="flex grow flex-row justify-between space-x-2 md:flex-col md:space-x-0 md:space-y-2">
        <NavLinks />
        <div className="hidden h-auto w-full grow rounded-md md:block" />
      </div>
    </div>
  );
}
