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
package leiliang91.devsync.rsync;


import leiliang91.devsync.DevSyncRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class RsyncConverterTest {
    @Test
    public void convert() {
        DevSyncRequest devSyncRequest = DevSyncRequest.builder()
                .sourcePath("sourcePath")
                .remoteUser("remoteUser")
                .remoteIp("remoteIp")
                .remotePath("remotePath").build();
        String[] command = RsyncConverter.convert(devSyncRequest);

        String[] expected = new String[] {
                "rsync",
                "--human-readable",
                "-e",
                "ssh -q -o StrictHostKeyChecking=no",
                "--archive",
                "--hard-links",
                "sourcePath",
                "remoteUser@remoteIp:remotePath"
        };
        assertArrayEquals(expected, command);
    }

}
