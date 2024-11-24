import React, { useCallback } from "react";
import { Spacer } from "@nextui-org/spacer";
import { Tooltip } from "@nextui-org/tooltip";

import { DelIcon, EyeIcon } from "@/components/icons.tsx";
import { Session, SessionType } from "@/types";
export const useRenderCell = (
  _type: SessionType,
  handleMonitor: Function,
  handleDel: Function,
) => {
  const renderCell = useCallback((session: Session, columnKey: React.Key) => {
    const cellValue = session[columnKey as keyof Session];

    switch (columnKey) {
      case "operation":
        return (
          <div className="flex">
            <Tooltip content="Monitor">
              <button
                className="text-lg text-default-400 cursor-pointer active:opacity-50"
                onClick={() => handleMonitor(session)}
              >
                <EyeIcon />
              </button>
            </Tooltip>
            <Spacer x={4} />
            <Tooltip content="Delete">
              <button
                className="text-lg text-default-400 cursor-pointer active:opacity-50"
                onClick={() => handleDel(session)}
              >
                <DelIcon />
              </button>
            </Tooltip>
          </div>
        );
      default:
        return cellValue;
    }
  }, []);

  return { renderCell };
};
