import { useState, useEffect } from "react";

export const CountTime = ({ start }: { start: Date }) => {
  const [timeLeft, setTimeLeft] = useState("");

  // 使用useEffect来处理副作用，比如倒计时逻辑
  useEffect(() => {
    let timer: number | undefined;

    // 初始化倒计时
    const startCountdown = () => {
      const now = new Date();
      // @ts-ignore
      const distance = now - start;

      const days = Math.floor(distance / (1000 * 60 * 60 * 24));
      const hours = Math.floor(
        (distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60),
      );
      const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
      const seconds = Math.floor((distance % (1000 * 60)) / 1000);

      // 更新倒计时时间
      setTimeLeft(
        `${days > 0 ? days + "d " : ""}${hours > 0 ? hours + "h " : ""}${minutes}m ${seconds}s`,
      );

      // 递归调用以更新时间
      // @ts-ignore
      timer = setTimeout(startCountdown, 1000);
    };

    startCountdown();

    return () => {
      clearTimeout(timer);
    };
  }, [start]);

  return <p>{timeLeft}</p>;
};
