package com.x8ing.thsensor.thserver.web.services.info.bean;

public class MemoryInfo {

    private long totalMemoryKb;

    private long maxMemoryKb;

    private long freeMemoryKb;

    private long availableProcessors;

    public MemoryInfo(long totalMemoryKb, long maxMemoryKb, long freeMemoryKb) {
        this.totalMemoryKb = totalMemoryKb;
        this.maxMemoryKb = maxMemoryKb;
        this.freeMemoryKb = freeMemoryKb;
    }

    public MemoryInfo() {
    }

    public static MemoryInfo getCurrent() {
        MemoryInfo memoryInfo = new MemoryInfo();
        final int kB = 1024;

        memoryInfo.setFreeMemoryKb(Runtime.getRuntime().freeMemory() / kB);
        memoryInfo.setTotalMemoryKb(Runtime.getRuntime().totalMemory() / kB);
        memoryInfo.setMaxMemoryKb(Runtime.getRuntime().maxMemory() / kB);
        memoryInfo.setAvailableProcessors(Runtime.getRuntime().availableProcessors());


        return memoryInfo;
    }

    public long getTotalMemoryKb() {
        return totalMemoryKb;
    }

    public void setTotalMemoryKb(long totalMemoryKb) {
        this.totalMemoryKb = totalMemoryKb;
    }

    public long getMaxMemoryKb() {
        return maxMemoryKb;
    }

    public void setMaxMemoryKb(long maxMemoryKb) {
        this.maxMemoryKb = maxMemoryKb;
    }

    public long getFreeMemoryKb() {
        return freeMemoryKb;
    }

    public void setFreeMemoryKb(long freeMemoryKb) {
        this.freeMemoryKb = freeMemoryKb;
    }

    public long getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(long availableProcessors) {
        this.availableProcessors = availableProcessors;
    }
}
