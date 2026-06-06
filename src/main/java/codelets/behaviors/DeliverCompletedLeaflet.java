/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *****************************************************************************/

package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import support.LeafletUtils;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.WorldPoint;

public class DeliverCompletedLeaflet extends Codelet {

    private Memory leafletsMO;
    private Memory innerSenseMO;
    private Memory deliverySpotMO;
    private Memory handsMO;
    private final int reachDistance;

    public DeliverCompletedLeaflet(int reachDistance) {
        this.reachDistance = reachDistance;
        this.name = "DeliverCompletedLeaflet";
    }

    @Override
    public void accessMemoryObjects() {
        leafletsMO = (MemoryObject) this.getInput("LEAFLETS");
        innerSenseMO = (MemoryObject) this.getInput("INNER");
        deliverySpotMO = (MemoryObject) this.getInput("DELIVERY_SPOT");
        handsMO = (MemoryObject) this.getOutput("HANDS");
    }

    @Override
    public void proc() {
        List<Leaflet> leaflets = (List<Leaflet>) leafletsMO.getI();
        Leaflet completed = LeafletUtils.getCompletedLeaflet(leaflets);
        WorldPoint deliverySpot = (WorldPoint) deliverySpotMO.getI();
        if (completed == null || deliverySpot == null) {
            // handsMO.setI("");
            activation = 0.0;
            return;
        }

        Idea cis = (Idea) innerSenseMO.getI();
        double selfX = (double) cis.get("position.x").getValue();
        double selfY = (double) cis.get("position.y").getValue();
        Point2D pSelf = new Point();
        pSelf.setLocation(selfX, selfY);
        Point2D pDelivery = new Point();
        pDelivery.setLocation(deliverySpot.getX(), deliverySpot.getY());

        if (pSelf.distance(pDelivery) <= reachDistance) {
            JSONObject message = new JSONObject();
            try {
                message.put("ACTION", "DELIVER");
                message.put("LEAFLET_ID", completed.getID().toString());
                handsMO.setI(message.toString());
                System.out.println("I sent the comand deliver to deliver the leaflet: " + message.toString());
                activation = 1.0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // handsMO.setI("");
            activation = 0.0;
        }
    }

    @Override
    public void calculateActivation() {
    }
}
