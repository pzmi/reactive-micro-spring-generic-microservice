package io.github.pzmi.genericmicroservice;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class Application {

    public static void main(String[] args) throws InterruptedException {

        RouterFunction router = route(GET("/{name}"), Application::greet)
                .andRoute(GET("/"), Application::hello);

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(router);

        HttpServer
                .create("localhost", 8080)
                .newHandler(new ReactorHttpHandlerAdapter(httpHandler))
                .block();

        Thread.currentThread().join();
    }

    private static Mono<ServerResponse> hello(ServerRequest request) {
        EmitterProcessor emitterProcessor = EmitterProcessor.create();
        emitterProcessor.sink();
        return ServerResponse.ok().body(Mono.just("Hello"), String.class);
    }

    private static Mono<ServerResponse> greet(ServerRequest request) {
        String name = request.pathVariable("name");
        return ServerResponse.ok().body(Mono.just("Hello " + name), String.class);
    }
}
