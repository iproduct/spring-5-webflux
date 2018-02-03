package org.iproduct.demos.spring.streamingdemos.handlers;

import org.iproduct.demos.spring.streamingdemos.domain.CpuLoad;
import org.iproduct.demos.spring.streamingdemos.domain.ProcessInfo;
import org.iproduct.demos.spring.streamingdemos.services.Profiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static org.springframework.http.MediaType.*;

@Component
//@Slf4j
public class ReactiveProfilerHandler {

    @Autowired
    private Profiler profiler;

//    @Autowired
//    private QuotesGenerator generator;


    public Mono<ServerResponse> streamCpu(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_STREAM_JSON)
                .body(profiler.getProfilerStream(Duration.ofMillis(500)), CpuLoad.class);
    }

    public Mono<ServerResponse> streamCpuSSE(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(profiler.getProfilerStream(Duration.ofMillis(1000)), CpuLoad.class);
    }

    public Mono<ServerResponse> getProcesses(ServerRequest request) {
        ParameterizedTypeReference<List<ProcessInfo>> typeRef = new ParameterizedTypeReference<List<ProcessInfo>>() {};
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body( Mono.just(profiler.getJavaProcesses()), typeRef);
    }

}
