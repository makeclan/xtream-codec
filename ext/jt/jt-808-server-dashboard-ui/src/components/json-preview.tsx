import "jsoneditor/dist/jsoneditor.css";
import "@/styles/json-preview.css";
import JSONEditor, { JSONEditorOptions } from "jsoneditor";
import { FC, useEffect, useRef } from "react";

export interface JSONPreviewProps {
  json: Object;
}
export const JsonPreview: FC<JSONPreviewProps> = ({ json }) => {
  const options: JSONEditorOptions = {
    mode: "view",
    mainMenuBar: false,
    navigationBar: false,
  };
  const ref = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    // @ts-ignore
    const jsonEditor = new JSONEditor(ref.current, options);

    jsonEditor.set(json);

    return () => {
      jsonEditor.destroy();
    };
  }, [json]);

  return <div ref={ref} className="json-preview" />;
};
