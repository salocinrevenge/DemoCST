/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *****************************************************************************/

package codelets.sensors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.List;
import ws3dproxy.model.Bag;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;

public class LeafletBagSensor extends Codelet {

    private final Creature c;
    private Memory leafletsMO;
    private Memory bagMO;

    public LeafletBagSensor(Creature nc) {
        c = nc;
        this.name = "LeafletBagSensor";
    }

    @Override
    public void accessMemoryObjects() {
        leafletsMO = (MemoryObject) this.getOutput("LEAFLETS");
        bagMO = (MemoryObject) this.getOutput("BAG");
    }

    @Override
    public void proc() {
        try {
            c.updateBag();
            Bag bag = c.getBag();
            List<Leaflet> leaflets = new ArrayList<Leaflet>(c.getLeaflets());

            bagMO.setI(bag);
            leafletsMO.setI(leaflets);
            System.out.println("LeafletBagSensor: leaflets=" + leaflets.size()
                    + " bagCrystals=" + (bag != null ? bag.getTotalNumberCrystals() : 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void calculateActivation() {
    }
}
