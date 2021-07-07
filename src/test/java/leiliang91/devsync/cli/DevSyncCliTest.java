package leiliang91.devsync.cli;

import org.junit.jupiter.api.Test;

import static leiliang91.devsync.cli.DevSyncCli.DURATION;
import static leiliang91.devsync.cli.DevSyncCli.INTERVAL;
import static leiliang91.devsync.cli.DevSyncCli.REMOTE_IP;
import static leiliang91.devsync.cli.DevSyncCli.REMOTE_PATH;
import static leiliang91.devsync.cli.DevSyncCli.REMOTE_USER;
import static leiliang91.devsync.cli.DevSyncCli.SOURCE_PATH;

public class DevSyncCliTest {
    @Test
    public void test() {
        DevSyncCli.main(new String[] {
                "-" + SOURCE_PATH, "sourcePath",
                "-" + REMOTE_PATH, "remotePath",
                "-" + REMOTE_USER, "remoteUser",
                "-" + REMOTE_IP, "remoteIp",
                "-" + INTERVAL, "1",
                "-" + DURATION, "3"
        });
    }
}
