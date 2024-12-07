import { ReactNode, useRef } from "react";
import { Card } from "@nextui-org/card";

import { useMouseMove } from "@/hooks/use-mouse-move.ts";

export const SpotlightCard = ({ children }: { children: ReactNode }) => {
  const ref = useRef<HTMLDivElement | null>(null);
  const { x, y } = useMouseMove(ref);

  return (
    <Card
      ref={ref}
      shadow="sm"
      style={{
        background:
          x > 0 || y > 0
            ? `radial-gradient(450px at ${x}px ${y}px, rgba(120, 40, 200, 0.4), transparent 80%)`
            : "",
      }}
    >
      {children}
    </Card>
  );
};
