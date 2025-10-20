package com.vuryss.aoc.api.process;

import com.zaxxer.nuprocess.NuProcessBuilder;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class ProcessExecutor {
    public CompletableFuture<ProcessResult> execute(List<String> command, Duration timeout) {
        var future = new CompletableFuture<ProcessResult>();
        var handler = new ProcessHandler(future);
        var npb = new NuProcessBuilder(command);

        npb.setProcessListener(handler);
        npb.start();

        return future
            .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
            .whenComplete((result, exception) -> {
                if (exception instanceof TimeoutException) {
                    Log.info("Process execution completed with a timeout, killing process tree...");
                    handler.killProcess();
                }
            })
            .exceptionallyCompose(
                exception -> CompletableFuture.failedFuture(new ProcessExecutionException(exception.getMessage()))
            );
    }
}
