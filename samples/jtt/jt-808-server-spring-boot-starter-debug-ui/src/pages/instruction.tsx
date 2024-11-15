import SessionTable from "@/components/session-table.tsx";

export default function InstructionPage() {
  return (
    <section className="flex flex-col items-center justify-center gap-4 py-8 md:py-10">
      <SessionTable type="instruction" />
    </section>
  );
}
