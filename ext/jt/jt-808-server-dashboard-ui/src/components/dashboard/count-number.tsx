import { useCountUp } from "react-countup";
import { useEffect, useRef } from "react";

export const CountNumber = ({ end }: { end: number }) => {
  const countUpRef = useRef(null);
  const { update } = useCountUp({
    ref: countUpRef,
    start: 0,
    end,
  });

  useEffect(() => {
    update(end);
  }, [end]);

  return <span ref={countUpRef} />;
};
