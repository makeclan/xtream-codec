import { Card, CardBody } from "@nextui-org/card";

import { DumpCharts } from "@/components/dashboard/dump-charts.tsx";

export const DumpPage = () => {
  return (
    <Card>
      <CardBody>
        <DumpCharts />
      </CardBody>
    </Card>
  );
};
