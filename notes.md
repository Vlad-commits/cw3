
./spark-submit --conf "spark.memory.fraction=0.5" --conf "spark.memory.storageFraction=0.5" --conf "spark.driver.extraJavaOptions=-XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/hoodie-heapdump.hprof" --conf "spark.executor.extraJavaOptions=-XX:+PrintFlagsFinal -XX:+PrintReferenceGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintAdaptiveSizePolicy -XX:+UnlockDiagnosticVMOptions -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/hoodie-heapdump.hprof" --packages org.apache.hudi:hudi-spark-bundle_2.11:0.7.0,org.apache.spark:spark-avro_2.11:2.4.5 --executor-memory 6G --num-executors 4 --total-executor-cores 12  --master=spark://master:7077 /opt/javapp/hudi-1.0-SNAPSHOT-all.jar
4 workers

21/06/08 07:59:21 INFO HudiEtlBenchmarks: etl finished for chunk size 1000000, time taken = 248709
21/06/08 07:59:21 INFO HudiEtlBenchmarks: etl finished for chunk size 100000, time taken = 66233
21/06/08 07:59:21 INFO HudiEtlBenchmarks: etl finished for chunk size 10000, time taken = 35065
21/06/08 07:59:21 INFO HudiEtlBenchmarks: etl finished for chunk size 1000, time taken = 34350

21/06/08 08:10:40 INFO HudiEtlBenchmarks: etl finished for chunk size 1000000, time taken = 249241
21/06/08 08:10:40 INFO HudiEtlBenchmarks: etl finished for chunk size 100000, time taken = 66702
21/06/08 08:10:40 INFO HudiEtlBenchmarks: etl finished for chunk size 10000, time taken = 41216
21/06/08 08:10:40 INFO HudiEtlBenchmarks: etl finished for chunk size 1000, time taken = 34374

21/06/08 08:19:20 INFO HudiEtlBenchmarks: etl finished for chunk size 1000000, time taken = 254815
21/06/08 08:19:20 INFO HudiEtlBenchmarks: etl finished for chunk size 100000, time taken = 67883
21/06/08 08:19:20 INFO HudiEtlBenchmarks: etl finished for chunk size 10000, time taken = 41060
21/06/08 08:19:20 INFO HudiEtlBenchmarks: etl finished for chunk size 1000, time taken = 33709

21/06/08 08:29:10 INFO HudiEtlBenchmarks: etl finished for chunk size 1000000, time taken = 253546
21/06/08 08:29:10 INFO HudiEtlBenchmarks: etl finished for chunk size 100000, time taken = 64023
21/06/08 08:29:10 INFO HudiEtlBenchmarks: etl finished for chunk size 10000, time taken = 41094
21/06/08 08:29:10 INFO HudiEtlBenchmarks: etl finished for chunk size 1000, time taken = 35601

21/06/08 16:27:22 INFO HudiEtlBenchmarks: etl finished for chunk size 1000000, time taken = 249874
21/06/08 16:27:22 INFO HudiEtlBenchmarks: etl finished for chunk size 100000, time taken = 64988
21/06/08 16:27:22 INFO HudiEtlBenchmarks: etl finished for chunk size 10000, time taken = 42051
21/06/08 16:27:22 INFO HudiEtlBenchmarks: etl finished for chunk size 1000, time taken = 36368

21/06/08 16:35:19 INFO HudiEtlBenchmarks: etl finished for chunk size 1000000, time taken = 237043
21/06/08 16:35:19 INFO HudiEtlBenchmarks: etl finished for chunk size 100000, time taken = 69024
21/06/08 16:35:19 INFO HudiEtlBenchmarks: etl finished for chunk size 10000, time taken = 44754
21/06/08 16:35:19 INFO HudiEtlBenchmarks: etl finished for chunk size 1000, time taken = 43863


2 workers

./spark-submit --conf "spark.memory.fraction=0.5" --conf "spark.memory.storageFraction=0.5" --conf "spark.driver.extraJavaOptions=-XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/hoodie-heapdump.hprof" --conf "spark.executor.extraJavaOptions=-XX:+PrintFlagsFinal -XX:+PrintReferenceGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintAdaptiveSizePolicy -XX:+UnlockDiagnosticVMOptions -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/hoodie-heapdump.hprof" --packages org.apache.hudi:hudi-spark-bundle_2.11:0.7.0,org.apache.spark:spark-avro_2.11:2.4.5 --executor-memory 6G --num-executors 2 --total-executor-cores 6  --master=spark://master:7077 /opt/javapp/hudi-1.0-SNAPSHOT-all.jar
INIT
21/06/09 18:06:10 INFO HudiEtlBenchmarks: etl finished for chunk size 1000000, time taken = 312460
21/06/09 18:06:10 INFO HudiEtlBenchmarks: etl finished for chunk size 100000, time taken = 90042
21/06/09 18:06:10 INFO HudiEtlBenchmarks: etl finished for chunk size 10000, time taken = 66889
21/06/09 18:06:10 INFO HudiEtlBenchmarks: etl finished for chunk size 1000, time taken = 54806
REAL
21/06/09 18:31:03 INFO HudiEtlBenchmarks: etl finished for chunk size 1000000, time taken = 392860
21/06/09 18:31:03 INFO HudiEtlBenchmarks: etl finished for chunk size 100000, time taken = 104635
21/06/09 18:31:03 INFO HudiEtlBenchmarks: etl finished for chunk size 10000, time taken = 50325
21/06/09 18:31:03 INFO HudiEtlBenchmarks: etl finished for chunk size 1000, time taken = 47107







./spark-submit --conf "spark.memory.fraction=0.5" --conf "spark.memory.storageFraction=0.5" --conf "spark.driver.extraJavaOptions=-XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/hoodie-heapdump.hprof" --conf "spark.executor.extraJavaOptions=-XX:+PrintFlagsFinal -XX:+PrintReferenceGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintAdaptiveSizePolicy -XX:+UnlockDiagnosticVMOptions -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/hoodie-heapdump.hprof" --packages io.delta:delta-core_2.11:0.6.1 --executor-memory 6G --num-executors 4 --total-executor-cores 12  --master=spark://master:7077 /opt/javapp/delta-1.0-SNAPSHOT-all.jar

21/06/08 16:48:38 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000000, time taken = 99236
21/06/08 16:48:38 INFO DeltaEtlBenchmarking: etl finished for chunk size 100000, time taken = 33782
21/06/08 16:48:38 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000, time taken = 25424
21/06/08 16:48:38 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000, time taken = 21022

21/06/08 16:52:52 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000000, time taken = 99018
21/06/08 16:52:52 INFO DeltaEtlBenchmarking: etl finished for chunk size 100000, time taken = 37203
21/06/08 16:52:52 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000, time taken = 26232
21/06/08 16:52:52 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000, time taken = 22672


21/06/08 16:58:17 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000000, time taken = 101387
21/06/08 16:58:17 INFO DeltaEtlBenchmarking: etl finished for chunk size 100000, time taken = 34057
21/06/08 16:58:17 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000, time taken = 23279
21/06/08 16:58:17 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000, time taken = 22083

+1
21/06/08 17:59:31 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000000, time taken = 529370
21/06/08 17:59:31 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000000, time taken = 517831
21/06/08 17:59:31 INFO DeltaEtlBenchmarking: etl finished for chunk size 100000, time taken = 408151
21/06/08 17:59:31 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000, time taken = 62369
21/06/08 17:59:31 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000, time taken = 129485

21/06/08 18:48:01 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000000, time taken = 1147003
21/06/08 18:48:01 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000000, time taken = 736916
21/06/08 18:48:01 INFO DeltaEtlBenchmarking: etl finished for chunk size 100000, time taken = 532565
21/06/08 18:48:01 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000, time taken = 81439
21/06/08 18:48:01 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000, time taken = 313720


2 
./spark-submit --conf "spark.memory.fraction=0.5" --conf "spark.memory.storageFraction=0.5" --conf "spark.driver.extraJavaOptions=-XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/hoodie-heapdump.hprof" --conf "spark.executor.extraJavaOptions=-XX:+PrintFlagsFinal -XX:+PrintReferenceGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintAdaptiveSizePolicy -XX:+UnlockDiagnosticVMOptions -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/hoodie-heapdump.hprof" --packages io.delta:delta-core_2.11:0.6.1 --executor-memory 6G --num-executors 2 --total-executor-cores 6  --master=spark://master:7077 /opt/javapp/delta-1.0-SNAPSHOT-all.jar
INIT
21/06/09 18:42:23 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000000, time taken = 316694
21/06/09 18:42:23 INFO DeltaEtlBenchmarking: etl finished for chunk size 100000, time taken = 77605
21/06/09 18:42:23 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000, time taken = 35107
21/06/09 18:42:23 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000, time taken = 28516

21/06/09 18:51:17 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000000, time taken = 329112
21/06/09 18:51:17 INFO DeltaEtlBenchmarking: etl finished for chunk size 100000, time taken = 90836
21/06/09 18:51:17 INFO DeltaEtlBenchmarking: etl finished for chunk size 10000, time taken = 27039
21/06/09 18:51:17 INFO DeltaEtlBenchmarking: etl finished for chunk size 1000, time taken = 30535



version: "3"
services:
  node:
    image: USER/Your-Pre-Built-Image
    environment:
      - VIRTUAL_HOST=localhost
    volumes:
      - logs:/app/out/
    command: ["npm","start"]
    cap_drop:
      - NET_ADMIN
      - SYS_ADMIN
    deploy:
      resources:
        limits:
          cpus: '0.001'
          memory: 50M
        reservations:
          cpus: '0.0001'
          memory: 20M
          
          
        HoodieWriteConfig.newBuilder()
            .withBulkInsertParallelism(8)
            .withParallelism(8,8)
            .withBulkInsertSortMode(BulkInsertSortMode.NONE.toString())
            .withCompactionConfig(HoodieCompactionConfig.newBuilder()
                .withAutoClean(false)
                .build())
            .withIndexConfig(HoodieIndexConfig.newBuilder()
                .bloomIndexBucketizedChecking(false)
                .build())
            .build().getProps()            