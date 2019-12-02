public class CustomObject {
    private String machineOS;
    private long count;

    public CustomObject(String machineOS, long count) {
        this.machineOS = machineOS;
        this.count = count;
    }

    public String getMachineOS() {
        return machineOS;
    }

    public void setMachineOS(String machineOS) {
        this.machineOS = machineOS;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
