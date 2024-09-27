package zabbum.oelremake;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class Confirm {
    protected boolean confirmStatus = false;

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
