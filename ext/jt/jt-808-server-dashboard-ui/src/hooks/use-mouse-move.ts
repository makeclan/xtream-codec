import { type Ref, useEffect, useState } from "react";

export const useMouseMove = (ref: Ref<HTMLDivElement>) => {
  const [position, setPosition] = useState({
    x: 0,
    y: 0,
  });

  useEffect(() => {
    function saveMousePosition(event: MouseEvent) {
      setPosition({
        x: event.offsetX,
        y: event.offsetY,
      });
    }
    function resetMousePosition() {
      setPosition({
        x: 0,
        y: 0,
      });
    }
    // @ts-ignore
    ref.current?.addEventListener("mousemove", saveMousePosition);
    // @ts-ignore
    ref?.current?.addEventListener("mouseleave", resetMousePosition);

    return () => {
      // @ts-ignore
      ref?.current?.removeEventListener("mousemove", saveMousePosition);
      // @ts-ignore
      ref?.current?.addEventListener("mouseleave", resetMousePosition);
    };
  });

  return position;
};
