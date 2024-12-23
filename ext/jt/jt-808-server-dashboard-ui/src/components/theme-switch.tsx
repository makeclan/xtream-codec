import { Switch } from "@nextui-org/switch";
import { ThemeProps, useTheme } from "@nextui-org/use-theme";

import { FaMoonIcon, FaSunIcon } from "@/components/icons.tsx";

export const ThemeSwitch = () => {
  const { theme, setTheme } = useTheme();

  return (
    <Switch
      color="primary"
      isSelected={theme === ThemeProps.LIGHT}
      size="lg"
      thumbIcon={({ isSelected, className }) =>
        isSelected ? (
          <FaSunIcon className={className} />
        ) : (
          <FaMoonIcon className={className} />
        )
      }
      onChange={(e) => {
        setTheme(e.currentTarget.checked ? ThemeProps.LIGHT : ThemeProps.DARK);
      }}
    />
  );
};
