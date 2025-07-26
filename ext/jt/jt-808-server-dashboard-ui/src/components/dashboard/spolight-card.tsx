import { ReactNode, useRef } from "react";
import { Card } from "@heroui/card";

import { useMouseMove } from "@/hooks/use-mouse-move.ts";
export const SpotlightCard = ({
  children,
  ...props
}: {
  children: ReactNode;
  className?: string;
}) => {
  const ref = useRef<HTMLDivElement | null>(null);
  const { x, y } = useMouseMove(ref);

  return (
    <Card
      ref={ref}
      className="border-default-100 border-1"
      shadow="sm"
      style={{
        background:
          x > 0 || y > 0
            ? `radial-gradient(450px at ${x}px ${y}px, rgba(120, 40, 200, 0.4), transparent 80%)`
            : "",
      }}
      {...props}
    >
      {children}
    </Card>
  );
};
