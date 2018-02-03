package org.iproduct.demos.spring.streamingdemos.domain;

import java.io.Serializable;

public class ProcessInfo implements Serializable {
    private static final long serialVersionUID = -6705829915457870975L;

    private long pid;
    private String command;

    public ProcessInfo() {
    }

    public ProcessInfo(long pid, String command) {
        this.pid = pid;
        this.command = command;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((command == null) ? 0 : command.hashCode());
        result = prime * result + (int) (pid ^ (pid >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProcessInfo other = (ProcessInfo) obj;
        if (command == null) {
            if (other.command != null)
                return false;
        } else if (!command.equals(other.command))
            return false;
        if (pid != other.pid)
            return false;
        return true;
    }

    public String toString() {
        return "ProcessInfo[" + pid + ", " + command + "]";
    }

}
