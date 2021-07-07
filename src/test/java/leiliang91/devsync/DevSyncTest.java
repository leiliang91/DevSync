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

import com.google.inject.Guice;
import com.google.inject.Injector;
import leiliang91.devsync.cmd.CmdExecutorResult;
import leiliang91.devsync.guice.GuiceModuleForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DevSyncTest {
    private static Injector injector;

    @BeforeAll
    public static void setUp() {
        injector = Guice.createInjector(GuiceModuleForTest.builder().build());
    }

    @Test
    public void sync_success() {
        DevSync devSync = injector.getInstance(DevSync.class);
        CmdExecutorResult cmdExecutorResult = devSync.sync(DevSyncRequest.builder()
                .sourcePath("source-path")
                .remotePath("target-path").build());
        assertEquals(0, cmdExecutorResult.getExitCode());
        assertNull(cmdExecutorResult.getException());
        assertNotNull(cmdExecutorResult.getOutput());
        assertNotNull(cmdExecutorResult.getError());
        assertTrue(cmdExecutorResult.isSuccess());
    }

    @Test
    public void sync_fail() {
        DevSync devSync = injector.getInstance(DevSync.class);
        CmdExecutorResult cmdExecutorResult = devSync.sync(DevSyncRequest.builder()
                .sourcePath("bad-source-path")
                .remotePath("target-path").build());
        assertFalse(cmdExecutorResult.isSuccess());
    }

    @Test
    public void sync_fail2() {
        DevSync devSync = injector.getInstance(DevSync.class);
        CmdExecutorResult cmdExecutorResult = devSync.sync(DevSyncRequest.builder()
                .sourcePath("bad-source-path2")
                .remotePath("target-path").build());
        assertFalse(cmdExecutorResult.isSuccess());
    }
}
