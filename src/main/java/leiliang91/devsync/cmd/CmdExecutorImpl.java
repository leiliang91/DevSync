/*
    Copyright 2021 leiliang91

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package leiliang91.devsync.cmd;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Slf4j
public class CmdExecutorImpl implements CmdExecutor {
    private static final Runtime runtime = Runtime.getRuntime();

    @Override
    public CmdExecutorResult execute(String... cmd) {
        log.info("Running cmd: {}", String.join(" ", cmd));

        CmdExecutorResult.CmdExecutorResultBuilder resultBuilder = CmdExecutorResult.builder();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Process process = runtime.exec(cmd);

            InputStreamReader outputStream = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
            InputStreamReader errorStream = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8);
            Future<String> future = executorService.submit(new ReadStreamTask(outputStream));

            String error = new ReadStreamTask(errorStream).call();
            String output = future.get();

            int exitCode = process.waitFor();
            resultBuilder.exitCode(exitCode).output(output).error(error);
            process.destroy();
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error("Encountered exception during cmd execution", e);
            resultBuilder.exception(e);
        }

        CmdExecutorResult cmdExecutorResult = resultBuilder.build();
        log.info("CmdExecutorResult is: {}", cmdExecutorResult.toString());

        return cmdExecutorResult;
    }

    private static class ReadStreamTask implements Callable<String> {
        private final InputStreamReader inputStreamReader;

        public ReadStreamTask(InputStreamReader inputStreamReader) {
            this.inputStreamReader = inputStreamReader;
        }

        @Override
        public String call() throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        }
    }
}
