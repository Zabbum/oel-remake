package zabbum.oelremake;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Confirm {
    private boolean confirmStatus = false;

    public synchronized boolean isConfirmed() {
        return confirmStatus;
    }

    public synchronized void confirm() {
        this.confirmStatus = true;
        notifyAll();
    }

    public synchronized void waitForConfirm() throws InterruptedException {
        while (!isConfirmed()) {
            wait();
        }
    }
}
