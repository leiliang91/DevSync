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
package leiliang91.devsync;

import com.google.inject.Inject;
import leiliang91.devsync.cmd.CmdExecutor;
import leiliang91.devsync.cmd.CmdExecutorResult;
import leiliang91.devsync.rsync.RsyncConverter;

public class DevSync {
    private final CmdExecutor cmdExecutor;

    @Inject
    public DevSync(CmdExecutor cmdExecutor) {
        this.cmdExecutor = cmdExecutor;
    }

    public CmdExecutorResult sync(DevSyncRequest request) {
        String[] command = RsyncConverter.convert(request);
        return cmdExecutor.execute(command);
    }
}
