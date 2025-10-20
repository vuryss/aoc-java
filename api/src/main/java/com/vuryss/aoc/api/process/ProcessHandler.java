package com.vuryss.aoc.api.process;

import com.zaxxer.nuprocess.NuAbstractProcessHandler;
import com.zaxxer.nuprocess.NuProcess;
import io.quarkus.logging.Log;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public class ProcessHandler extends NuAbstractProcessHandler {
    private NuProcess process;
    private final CompletableFuture<ProcessResult> solutionResultFuture;
    private final StringBuilder stdout = new StringBuilder();
    private final StringBuilder stderr = new StringBuilder();

    public ProcessHandler(CompletableFuture<ProcessResult> solutionResultFuture) {
        super();
        this.solutionResultFuture = solutionResultFuture;
    }

    @Override
    public void onStart(NuProcess nuProcess) {
        this.process = nuProcess;
    }

    @Override
    public void onStdout(ByteBuffer buffer, boolean closed) {
        if (buffer == null) return;

        var bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        stdout.append(new String(bytes, StandardCharsets.UTF_8));
    }

    @Override
    public void onStderr(ByteBuffer buffer, boolean closed) {
        if (buffer == null) return;

        var bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        stderr.append(new String(bytes, StandardCharsets.UTF_8));
    }

    @Override
    public void onExit(int statusCode) {
        if (statusCode == Integer.MIN_VALUE) {
            // Process failed to start
            solutionResultFuture.completeExceptionally(
                new ProcessExecutionException("Failed to start process")
            );
            return;
        }

        if (statusCode == Integer.MAX_VALUE) {
            // Process was killed or unexpectedly terminated
            solutionResultFuture.completeExceptionally(
                new ProcessExecutionException("Process was killed or unexpectedly terminated")
            );
            return;
        }

        solutionResultFuture.complete(
            new ProcessResult(stdout.toString(), stderr.toString(), statusCode)
        );
    }

    public void killProcess() {
        if (process == null) {
            return;
        }

        ProcessHandle.of(process.getPID()).ifPresent(parent -> {
            parent
                .descendants()
                .sorted(Comparator.comparingLong(ProcessHandle::pid).reversed())
                .forEach(processHandle -> {
                    Log.info("Killing child process with PID: " + processHandle.pid());
                    processHandle.destroyForcibly();
                });

            Log.info("Killing parent process with PID: " + parent.pid());
            parent.destroyForcibly();
        });
    }
}
