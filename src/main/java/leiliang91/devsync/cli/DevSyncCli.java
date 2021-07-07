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
package leiliang91.devsync.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import leiliang91.devsync.DevSync;
import leiliang91.devsync.DevSyncRequest;
import leiliang91.devsync.guice.GuiceModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class DevSyncCli {
    public static final String SOURCE_PATH = "sourcePath";
    public static final String REMOTE_USER = "remoteUser";
    public static final String REMOTE_IP = "remoteIp";
    public static final String REMOTE_PATH = "remotePath";
    public static final String INTERVAL = "interval";
    public static final String DURATION = "duration";

    private static final int DEFAULT_INTERVAL = 30;
    private static final int INFINITE_DURATION = -1;

    private static final CommandLineParser parser = new DefaultParser();
    private static final List<Option> optionList = Arrays.asList(
        new Option(SOURCE_PATH, true, "source path"),
        new Option(REMOTE_USER, true, "remote user"),
        new Option(REMOTE_IP, true, "remote IP"),
        new Option(REMOTE_PATH, true, "remote path")
    );

    private static final List<Option> optionalOptionList = Arrays.asList(
            new Option(INTERVAL, true, "interval in seconds"),
            new Option(DURATION, true, "duration in seconds")
    );

    public static void main(String[] args) {
        
        log.info("DevSync starts with args: {}", String.join(" ", args));
        Options options = new Options();
        for (Option option : optionList) {
            options.addOption(option);
        }
        for (Option option : optionalOptionList) {
            options.addOption(option);
        }

        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            log.error("Encountered exception when parsing command line args", e);
            System.exit(-1);
        }

        for (Option option : optionList) {
            if (!commandLine.hasOption(option.getOpt())) {
                log.error("Option {} is required", option.getOpt());
                System.exit(-1);
            }
        }

        int interval = DEFAULT_INTERVAL;
        String intervalStr = commandLine.getOptionValue(INTERVAL);
        if (intervalStr != null) {
            int value = Integer.parseInt(intervalStr);
            if (value > 0) {
                interval = value;
            }
        }
        log.info("DevSync interval is {} seconds", interval);

        int duration = INFINITE_DURATION;
        String durationStr = commandLine.getOptionValue(DURATION);
        if (durationStr != null) {
            int value = Integer.parseInt(durationStr);
            if(value > 0) {
                duration = value;
            }
        }
        log.info("DevSync duration is {} minutes", duration == INFINITE_DURATION? "infinity" : duration);

        Injector injector = Guice.createInjector(new GuiceModule());
        DevSync devSync = injector.getInstance(DevSync.class);

        DevSyncRequest devSyncRequest = DevSyncRequest.builder()
                .sourcePath("sourcePath")
                .remoteUser("remoteUser")
                .remoteIp("remoteIp")
                .remotePath("remotePath").build();

        long start = System.currentTimeMillis();
        long current = start;
        int runNumber = 1;
        long durationInMillis = duration * 1000;
        while(current - start < durationInMillis || duration == INFINITE_DURATION) {
            log.info("Run number: {}", runNumber++);
            devSync.sync(devSyncRequest);
            log.info("Wait for {} seconds before next run", interval);
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                log.error("Interrupted during sleep", e);
                System.exit(1);
            }
            current = System.currentTimeMillis();
        }
        log.info("DevSync stopped after {} seconds", duration);
    }
}
