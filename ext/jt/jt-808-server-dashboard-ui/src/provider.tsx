import { HeroUIProvider } from "@heroui/system";
import { ReactNode } from "react";
import { useNavigate, useHref } from "react-router-dom";

export function Provider({ children }: { children: ReactNode }) {
  const navigate = useNavigate();

  return (
    <HeroUIProvider navigate={navigate} useHref={useHref}>
      {children}
    </HeroUIProvider>
  );
}
