export interface Dump {
  time: string;
  value: {
    group: string;
    dumpInfo: {
      threadName: string;
      threadId: number;
      blockedTime: number;
      blockedCount: number;
      waitedTime: number;
      waitedCount: number;
      lockName: string;
      lockOwnerId: number;
      lockOwnerName: string;
      daemon: boolean;
      inNative: boolean;
      suspended: boolean;
      threadState:
        | "NEW"
        | "RUNNABLE"
        | "BLOCKED"
        | "WAITING"
        | "TIMED_WAITING"
        | "TERMINATED";
      priority: number;
      stackTrace: any;
      lockedMonitors: any[];
      lockedSynchronizers: any[];
      lockInfo: any;
    };
  };
}

export interface Group {
  name: string;
  threads: [
    {
      threadName: string;
      threadId: number;
      stackTrace: any;
      dumps: {
        time: string;
        threadState:
          | "NEW"
          | "RUNNABLE"
          | "BLOCKED"
          | "WAITING"
          | "TIMED_WAITING"
          | "TERMINATED";
      }[];
    },
  ];
}
