import React, { useCallback } from "react";
import { Spacer } from "@nextui-org/spacer";
import { Tooltip } from "@nextui-org/tooltip";

import { DelIcon, EyeIcon } from "@/components/icons.tsx";
import { Session, SessionType } from "@/types";
export const useRenderCell = (type: SessionType, handleMonitor: Function) => {
  const renderCell = useCallback((session: Session, columnKey: React.Key) => {
    const cellValue = session[columnKey as keyof Session];
    const handleDel = (_session: Session) => {
      if (type === "instruction") {
        // TODO del instruction
      } else {
        // TODO del attachment
      }
    };

    switch (columnKey) {
      case "operation":
        return (
          <div className="flex">
            <Tooltip content="Monitor">
              <span
                className="text-lg text-default-400 cursor-pointer active:opacity-50"
                onClick={() => handleMonitor(session)}
              >
                <EyeIcon />
              </span>
            </Tooltip>
            <Spacer x={4} />
            <Tooltip content="Delete">
              <span
                className="text-lg text-default-400 cursor-pointer active:opacity-50"
                onClick={() => handleDel(session)}
              >
                <DelIcon />
              </span>
            </Tooltip>
          </div>
        );
      default:
        return cellValue;
    }
  }, []);

  return { renderCell };
};
