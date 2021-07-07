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
package leiliang91.devsync.guice;

import leiliang91.devsync.cmd.CmdExecutor;
import leiliang91.devsync.cmd.CmdExecutorResult;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CmdExecutorForTest implements CmdExecutor {

    @Override
    public CmdExecutorResult execute(String... cmd) {
        log.info("Running cmd: {}", String.join(" ", cmd));

        CmdExecutorResult cmdExecutorResult;

        if (cmd[6].equals("bad-source-path")) {
            cmdExecutorResult = CmdExecutorResult.builder()
                    .exitCode(1)
                    .error("some error")
                    .output("")
                    .exception(null).build();
        } else if (cmd[6].equals("bad-source-path2")){
            cmdExecutorResult = CmdExecutorResult.builder()
                    .exitCode(0)
                    .error(null)
                    .output(null)
                    .exception(new Exception()).build();
        } else {
            cmdExecutorResult = CmdExecutorResult.builder()
                    .exitCode(0)
                    .error("")
                    .output("some output")
                    .exception(null).build();
        }

        log.info("Mocked CmdExecutorResult is: {}", cmdExecutorResult.toString());
        return cmdExecutorResult;
    }
}
