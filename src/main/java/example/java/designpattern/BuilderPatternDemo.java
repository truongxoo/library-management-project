package example.java.designpattern;

enum RAMType {
    RAM8G, RAM16G;
}

enum CPUType {
    COREI5, COREI7, COREI9;
}

enum GPUType {
    ARC, IRIS;
}

interface PCBuilder {

    PCBuilder RAMType(RAMType RAMType);

    PCBuilder CPUType(CPUType CPUType);

    PCBuilder GPUType(GPUType GPUType);

    PC build();

}

class GamingPCBuilder implements PCBuilder {

    private RAMType RAMType;
    private CPUType CPUType;
    private GPUType GPUType;

    @Override
    public PCBuilder RAMType(example.java.designpattern.RAMType RAMType) {
        this.RAMType = RAMType;
        return this;
    }

    @Override
    public PCBuilder CPUType(example.java.designpattern.CPUType CPUType) {
        this.CPUType = CPUType;
        return this;
    }

    @Override
    public PCBuilder GPUType(example.java.designpattern.GPUType GPUType) {
        this.GPUType = GPUType;
        return this;
    }

    @Override
    public PC build() {
        return new PC(RAMType, CPUType, GPUType);
    }

}

class PC {
    private RAMType RAMType;
    private CPUType CPUType;
    private GPUType GPUType;

    public RAMType getRAMType() {
        return RAMType;
    }

    public CPUType getCPUType() {
        return CPUType;
    }

    public GPUType getGPUType() {
        return GPUType;
    }

    public PC(example.java.designpattern.RAMType rAMType, example.java.designpattern.CPUType cPUType,
            example.java.designpattern.GPUType gPUType) {
        super();
        RAMType = rAMType;
        CPUType = cPUType;
        GPUType = gPUType;
    }

    @Override
    public String toString() {
        return "PC [RAMType=" + RAMType + ", CPUType=" + CPUType + ", GPUType=" + GPUType + "]";
    }

}

public class BuilderPatternDemo {
    public static void main(String[] args) {
        PC pc = new GamingPCBuilder().RAMType(RAMType.RAM16G).CPUType(CPUType.COREI7).GPUType(GPUType.IRIS).build();
        System.out.println(pc);
    }
}
