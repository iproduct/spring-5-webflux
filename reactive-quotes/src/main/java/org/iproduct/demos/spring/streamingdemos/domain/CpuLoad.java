package org.iproduct.demos.spring.streamingdemos.domain;

import java.io.Serializable;

public class CpuLoad implements Serializable {
    private static final long serialVersionUID = -6705829915457870975L;

    private long instant;
    private long pid;
    private int load;
    private boolean changed;

    public CpuLoad() {
    }

    public CpuLoad(long instant, long pid, int load, boolean changed) {
        this.instant = instant;
        this.pid = pid;
        this.load = load;
        this.changed = changed;
    }

    public long getInstant() {
        return instant;
    }

    public void setInstant(long timestamp) {
        this.instant = instant;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CpuLoad{");
        sb.append("timestamp=").append(instant);
        sb.append(", pid=").append(pid);
        sb.append(", load=").append(load);
        sb.append(", changed=").append(changed);
        sb.append('}');
        return sb.toString();
    }
}
