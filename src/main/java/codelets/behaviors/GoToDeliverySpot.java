/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *****************************************************************************/

package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
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

public class GoToDeliverySpot extends Codelet {

    private Memory leafletsMO;
    private Memory selfInfoMO;
    private Memory deliverySpotMO;
    private MemoryContainer legsMO;
    private final int creatureBasicSpeed;
    private final int reachDistance;

    public GoToDeliverySpot(int creatureBasicSpeed, int reachDistance) {
        this.creatureBasicSpeed = creatureBasicSpeed;
        this.reachDistance = reachDistance;
        this.name = "GoToDeliverySpot";
    }

    @Override
    public void accessMemoryObjects() {
        leafletsMO = (MemoryObject) this.getInput("LEAFLETS");
        selfInfoMO = (MemoryObject) this.getInput("INNER");
        deliverySpotMO = (MemoryObject) this.getInput("DELIVERY_SPOT");
        legsMO = (MemoryContainer) this.getOutput("LEGS");
    }

    @Override
    public void proc() {
        List<Leaflet> leaflets = (List<Leaflet>) leafletsMO.getI();
        WorldPoint deliverySpot = (WorldPoint) deliverySpotMO.getI();
        if (!LeafletUtils.hasCompletedLeaflet(leaflets) || deliverySpot == null) {
            activation = 0.0;
            // legsMO.setI("", activation, name);
            return;
        }
        
        Idea cis = (Idea) selfInfoMO.getI();
        double selfX = (double) cis.get("position.x").getValue();
        double selfY = (double) cis.get("position.y").getValue();

        Point2D pSelf = new Point();
        pSelf.setLocation(selfX, selfY);
        Point2D pDelivery = new Point();
        pDelivery.setLocation(deliverySpot.getX(), deliverySpot.getY());

        double distance = pSelf.distance(pDelivery);
        JSONObject message = new JSONObject();
        try {
            message.put("ACTION", "GOTO");
            message.put("X", (int) deliverySpot.getX());
            message.put("Y", (int) deliverySpot.getY());
            message.put("SPEED", distance > reachDistance ? creatureBasicSpeed : 0);
            activation = distance > reachDistance ? 1.2 : 0.8;
            legsMO.setI(message.toString(), activation, name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void calculateActivation() {
    }
}
