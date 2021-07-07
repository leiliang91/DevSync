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

import java.util.ArrayList;
import java.util.List;

public class RsyncConverter {
    public static String[] convert(DevSyncRequest request) {
        List<String> command = new ArrayList<>();
        command.add("rsync");
        command.add("--human-readable");
        command.add("-e");
        command.add("ssh -q -o StrictHostKeyChecking=no");
        command.add("--archive");
        command.add("--hard-links");
        command.add(request.getSourcePath());
        String remote = "";
        if(request.getRemoteUser() != null && request.getRemoteIp() != null) {
            remote += request.getRemoteUser() + "@" + request.getRemoteIp() + ":";
        }
        remote += request.getRemotePath();
        command.add(remote);
        return command.toArray(new String[0]);
    }
}
