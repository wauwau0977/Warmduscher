package com.x8ing.thsensor.thserver.device.service.impl;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.x8ing.thsensor.thserver.Profiles;
import com.x8ing.thsensor.thserver.db.entity.HeatPumpEntity;
import com.x8ing.thsensor.thserver.device.service.HeatingDataReadService;
import com.x8ing.thsensor.thserver.utils.mutex.GlobalSynced;
import com.x8ing.thsensor.thserver.utils.mutex.Hooks;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Component
@Profile("!" + Profiles.SENSOR_MOCK)
public class HeatingModbusReadService implements HeatingDataReadService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // SlaveID is between 1-255 and should be unique
    // https://www.modbustools.com/mbslave-user-manual.html#_slave_id
    private static final AtomicInteger slaveIdGlobal = new AtomicInteger(new Random().nextInt(128));

    // 169.254.92.80
    @Value("${thserver.heatPumpIP}")
    private String heatPumpIP;

    private GlobalSynced<ModbusMaster> modbusMasterSynced = null;

    private int slaveId;

    private final Hooks<ModbusMaster> modBusMasterHooks = new Hooks<>() {
        @Override
        public void before(ModbusMaster modbusMaster) throws Throwable {
            // since 1.2.8
            if (!modbusMaster.isConnected()) {
                modbusMaster.connect();
            }
        }

        @Override
        public void after(ModbusMaster modbusMaster) throws Throwable {
            modbusMaster.disconnect();
        }
    };

    @Override
    @PostConstruct
    public void init() throws Exception {
        log.info("Init " + this.getClass().getSimpleName());

        TcpParameters tcpParameters = new TcpParameters();

        //tcp parameters have already set by default as in example
        tcpParameters.setHost(InetAddress.getByName(heatPumpIP));
        tcpParameters.setKeepAlive(true);
        tcpParameters.setPort(Modbus.TCP_PORT);

        //if you would like to set connection parameters separately,
        // you should use another method: createModbusMasterTCP(String host, int port, boolean keepAlive);
        ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
        Modbus.setAutoIncrementTransactionId(true);

        slaveId = slaveIdGlobal.incrementAndGet();

        modbusMasterSynced = new GlobalSynced<>(m, modBusMasterHooks);
    }

    @Override
    public HeatPumpEntity getData() throws Throwable {

        log.debug("Start read data from ModBus");
        long t0 = System.currentTimeMillis();

        HeatPumpEntity ret = new HeatPumpEntity();

        modbusMasterSynced.requestOperation((modbusMaster) -> {

            // read base registers. certain what they do
            ret.setCompressorHours(modbusMaster.readInputRegisters(slaveId, 41, 1)[0]);

            // read temperature values (certain ones)
            ret.setHeatingOut(readInputRegister(modbusMaster, 10, true, 10));
            ret.setHeatingIn(readInputRegister(modbusMaster, 11, true, 10));
            ret.setSoleIn(readInputRegister(modbusMaster, 12, true, 10));
            ret.setSoleOut(readInputRegister(modbusMaster, 13, true, 10));
            ret.setBoilerTemp(readInputRegister(modbusMaster, 150, true, 10));

            // read additional input registers, not yet fully clear what they do
            ret.setIreg50CircTemp(readInputRegister(modbusMaster, 50, true, 10));  // gots data, uncertain what
            ret.setIreg90TempCirc2(readInputRegister(modbusMaster, 90, true, 10)); // seems to be constant 9999
            ret.setIreg152Boiler2(readInputRegister(modbusMaster, 152, true, 1)); // Boiler Elektro-Einsatz Stunden
            ret.setIreg170TempPsp(readInputRegister(modbusMaster, 170, true, 10)); // gots data, uncertain what
            ret.setIreg300TempOutdoor(readInputRegister(modbusMaster, 300, true, 10)); // outdoor temp

            // read additional discrete inputs, not yet fully clear what they do
            ret.setDi1Error(modbusMaster.readDiscreteInputs(slaveId, 1, 1)[0]);
            ret.setDi10Compressor1(modbusMaster.readDiscreteInputs(slaveId, 10, 1)[0]); // used in regular operation
            ret.setDi11Compressor2(modbusMaster.readDiscreteInputs(slaveId, 11, 1)[0]);
            ret.setDi12Valve4(modbusMaster.readDiscreteInputs(slaveId, 12, 1)[0]);
            ret.setDi13(modbusMaster.readDiscreteInputs(slaveId, 13, 1)[0]);
            ret.setDi14PumpDirect(modbusMaster.readDiscreteInputs(slaveId, 14, 1)[0]); // used in regular operation
            ret.setDi15PumpBoiler(modbusMaster.readDiscreteInputs(slaveId, 15, 1)[0]); // used in regular operation
            ret.setDi16We(modbusMaster.readDiscreteInputs(slaveId, 16, 1)[0]);
            ret.setDi17BoilerEl(modbusMaster.readDiscreteInputs(slaveId, 17, 1)[0]);
            ret.setDi18PoolPump(modbusMaster.readDiscreteInputs(slaveId, 18, 1)[0]);
            ret.setDi19HeatPumpOn(modbusMaster.readDiscreteInputs(slaveId, 19, 1)[0]);
            ret.setDi20Error(modbusMaster.readDiscreteInputs(slaveId, 20, 1)[0]);
            ret.setDi21PumpPrimary(modbusMaster.readDiscreteInputs(slaveId, 21, 1)[0]); // used in regular operation
            ret.setDi22PumpLoad(modbusMaster.readDiscreteInputs(slaveId, 22, 1)[0]); // used in regular operation
            ret.setDi30Compressor1Ready(modbusMaster.readDiscreteInputs(slaveId, 30, 1)[0]);
            ret.setDi31Compressor2Ready(modbusMaster.readDiscreteInputs(slaveId, 31, 1)[0]);
            ret.setDi70PumpHK1(modbusMaster.readDiscreteInputs(slaveId, 70, 1)[0]); // used in regular operation
            ret.setDi71HKM1ixOpen(modbusMaster.readDiscreteInputs(slaveId, 71, 1)[0]); // used in regular operation
            ret.setDi72HKM1ixClose(modbusMaster.readDiscreteInputs(slaveId, 72, 1)[0]); // used in regular operation
            ret.setDi90PumpHK2(modbusMaster.readDiscreteInputs(slaveId, 90, 1)[0]);
            ret.setDi91HKM2ixOpen(modbusMaster.readDiscreteInputs(slaveId, 91, 1)[0]);
            ret.setDi92HKM2ixClose(modbusMaster.readDiscreteInputs(slaveId, 92, 1)[0]);
            ret.setDi150(modbusMaster.readDiscreteInputs(slaveId, 150, 1)[0]);
            ret.setDi151(modbusMaster.readDiscreteInputs(slaveId, 151, 1)[0]);
            ret.setDi152(modbusMaster.readDiscreteInputs(slaveId, 152, 1)[0]);
            ret.setDi153(modbusMaster.readDiscreteInputs(slaveId, 153, 1)[0]);
            ret.setDi154(modbusMaster.readDiscreteInputs(slaveId, 154, 1)[0]);

        });

        long dt = System.currentTimeMillis() - t0;
        log.info("Completed reading modbus data in dt=" + dt + ", " + ret);

        return ret;

    }

    /**
     * Pretty inefficient. Should read out multiple registers, but, was lazy. It seems to work ok too.
     */
    @Override
    public List<String> scanAllRegisters(int maxRegister) {
        long t0 = System.currentTimeMillis();
        List<String> res = new ArrayList<>();
        final String SEP = "================================================================================";


        modbusMasterSynced.requestOperation(modbusMaster -> {

            res.add("Scan start time: " + new Date());
            res.add("maxRegister: " + maxRegister);

            // read input registers
            res.add(SEP);
            res.add("InputRegister");
            for (int inputRegister = 0; inputRegister <= maxRegister; inputRegister++) {
                int[] values = modbusMaster.readInputRegisters(slaveId, inputRegister, 1);
                String val = Arrays.toString(values);
                if (!StringUtils.equals("[0]", val)) {
                    res.add(inputRegister + ": " + val);
                }
            }

            //
            res.add(SEP);
            res.add("Holding Register");
            for (int inputRegister = 0; inputRegister < maxRegister; inputRegister++) {
                int[] values = modbusMaster.readHoldingRegisters(slaveId, inputRegister, 1);
                String val = Arrays.toString(values);
                if (!StringUtils.equals("[0]", val)) {
                    res.add(inputRegister + ": " + val);
                }
            }

            res.add(SEP);
            res.add("Discrete Input");
            for (int inputRegister = 0; inputRegister < maxRegister; inputRegister++) {
                boolean[] values = modbusMaster.readDiscreteInputs(slaveId, inputRegister, 1);
                String val = Arrays.toString(values);
                if (!StringUtils.equals("[false, false, false, false, false, false, false, false]", val)) {
                    res.add(inputRegister + ": " + val);
                }
            }

        });

        long dt = System.currentTimeMillis() - t0;

        res.add(SEP);
        res.add("Scanning time " + dt + "ms");


        return res;

    }

    protected double readInputRegister(ModbusMaster modbusMaster, int address, boolean signed, int scale) throws Exception {

        int vInt = modbusMaster.readInputRegisters(slaveId, address, 1)[0];

        double vDouble = vInt;
        if (signed) {
            vDouble = getSignedNumber(vInt);
        }
        return vDouble / scale;
    }

    /**
     * https://minimalmodbus.readthedocs.io/en/stable/modbusdetails.html#:~:text=Negative%20numbers%20(INT16%20%3D%20short),%2D32768%20to%20%2D1).
     * <p>
     * Negative numbers (INT16 = short)
     * Some manufacturers allow negative values for some registers. Instead of an allowed integer range 0-65535,
     * a range -32768 to 32767 is allowed. This is implemented as any received value in the upper range (32768-65535)
     * is interpreted as negative value (in the range -32768 to -1).
     * <p>
     * This is two’s complement and is described at https://en.wikipedia.org/wiki/Two%27s_complement.
     * Help functions to calculate the two’s complement value (and back) are provided in MinimalModbus.
     */
    protected static int getSignedNumber(int v) {
        if (v >= 32768) {
            // negative number
            return v - 65535 - 1;
        } else {
            // normal number
            return v;
        }
    }
}
