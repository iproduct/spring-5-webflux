package org.iproduct.demos.spring.streamingdemos.services;

import org.iproduct.demos.spring.streamingdemos.domain.CpuLoad;
import org.iproduct.demos.spring.streamingdemos.domain.ProcessInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Profiler {

    private List<ProcessInfo> oldProcesses;

    private Logger logger = LoggerFactory.getLogger(Profiler.class);

    public Profiler() {
        logger.info("!!!!!  Profiler bean created successfully.");
    }

//	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
//		logger.info("Profiler successfully initialized.");
//    }
//
//    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
//		logger.info("Profiler destroyed.");
//    }

    public Stream<ProcessHandle> getJavaProcessesStream() {
        return ProcessHandle.allProcesses()
                .filter(ph -> ph.info().toString().indexOf("java") >= 0);
    }

    public List<ProcessInfo> getJavaProcesses() {
        List<ProcessInfo> allProcesses = getJavaProcessesStream()
                .filter(ph -> ph.info().command().isPresent())
                .map(ph -> new ProcessInfo(ph.pid(), ph.info().command().get()))
                .collect(Collectors.toList());
        return allProcesses;
    }

    public Stream<Double> getJavaCPULoad() {
        Stream<Double> processTimes = getJavaProcessesStream().map(ph -> {
            // System.out.println(info);
            Optional<Duration> time = ph.info().totalCpuDuration();
            return (time.isPresent()) ? time.get().get(ChronoUnit.NANOS) : 0d;
        }).map(time -> time / 1000000d); // time in millis

        return processTimes;
    }

    public boolean areProcessesChanged() {
        List<ProcessInfo> newProcesses = getJavaProcesses();
        boolean result = !newProcesses.equals(oldProcesses);
        oldProcesses = newProcesses;
        return result;
    }

    public Flux<CpuLoad> getProfilerStream(Duration period) {
        return Flux.interval(period)
            .flatMap(index ->
                Flux.fromStream(getJavaProcessesStream()).map(ph -> {
                    // System.out.println(info);
                    Optional<Duration> time = ph.info().totalCpuDuration();
                    int cpuTime = (time.isPresent()) ? (int) time.get().get(ChronoUnit.NANOS)/100000 : 0;
                    return new CpuLoad(System.currentTimeMillis(), ph.pid(), cpuTime, areProcessesChanged());
                })
            ).share()
            .log();
    }

    public static void main(String[] args) throws InterruptedException {
//		Profiler profiler = new Profiler();
//		Flowable.interval(1000, TimeUnit.MILLISECONDS).map(t -> profiler.getJavaCPULoad())
//				.subscribe(System.out::println);
//		Thread.sleep(100000);

    }

}
