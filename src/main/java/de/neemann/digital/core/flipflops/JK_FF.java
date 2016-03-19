package de.neemann.digital.core.flipflops;

import de.neemann.digital.core.BitsException;
import de.neemann.digital.core.Node;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.ObservableValue;
import de.neemann.digital.core.part.Part;
import de.neemann.digital.core.part.PartAttributes;
import de.neemann.digital.core.part.PartTypeDescription;

/**
 * @author hneemann
 */
public class JK_FF extends Node implements Part {

    public static final PartTypeDescription DESCRIPTION = new PartTypeDescription(JK_FF.class, "J", "C", "K");
    private ObservableValue jVal;
    private ObservableValue kVal;
    private ObservableValue clockVal;
    private ObservableValue q;
    private ObservableValue qn;
    private boolean lastClock;
    private boolean out;

    public JK_FF(PartAttributes attributes) {
        this.q = new ObservableValue("Q", 1);
        this.qn = new ObservableValue("~Q", 1);
    }

    @Override
    public void readInputs() throws NodeException {
        boolean clock = clockVal.getBool();
        if (clock && !lastClock) {
            boolean j = jVal.getBool();
            boolean k = kVal.getBool();

            if (j && k) out = !out;
            else if (j) out = true;
            else if (k) out = false;
        }
        lastClock = clock;
    }

    @Override
    public void writeOutputs() throws NodeException {
        q.setBool(out);
        qn.setBool(!out);
    }

    @Override
    public void setInputs(ObservableValue... inputs) throws BitsException {
        jVal = inputs[0];
        jVal.addObserver(this);
        clockVal = inputs[1];
        clockVal.addObserver(this);
        kVal = inputs[2];
        kVal.addObserver(this);

        if (jVal.getBits() != 1)
            throw new BitsException("wrongBitCount", jVal);

        if (kVal.getBits() != 1)
            throw new BitsException("wrongBitCount", kVal);

        if (clockVal.getBits() != 1)
            throw new BitsException("carryIsABit", clockVal);
    }

    @Override
    public ObservableValue[] getOutputs() {
        return new ObservableValue[]{q, qn};
    }

}
