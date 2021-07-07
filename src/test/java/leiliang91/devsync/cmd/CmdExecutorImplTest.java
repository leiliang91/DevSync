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

import com.google.inject.Guice;
import com.google.inject.Injector;
import leiliang91.devsync.TestHelper;
import leiliang91.devsync.guice.GuiceModuleForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class CmdExecutorImplTest {
    private static Injector injector;

    @BeforeAll
    public static void winCheck() {
        assumeFalse(TestHelper.isWindows());
    }

    @BeforeAll
    public static void setUp() {
        injector = Guice.createInjector(GuiceModuleForTest.builder().build());
    }

    @Test
    public void execute_success() {
        CmdExecutor cmdExecutor = injector.getInstance(CmdExecutorImpl.class);
        CmdExecutorResult result = cmdExecutor.execute("pwd");
        assertEquals(0, result.getExitCode());
        assertNull(result.getException());
        assertNotNull(result.getOutput());
        assertNotNull(result.getError());
    }

    @Test
    public void execute_fail() {
        CmdExecutor cmdExecutor = injector.getInstance(CmdExecutorImpl.class);
        CmdExecutorResult result = cmdExecutor.execute("unrecognized cmd");
        assertEquals(0, result.getExitCode());
        assertNotNull(result.getException());
        assertNull(result.getOutput());
        assertNull(result.getError());
    }
}
