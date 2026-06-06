/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 *****************************************************************************/

package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import support.LeafletUtils;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;

/** 
 * 
 * @author klaus
 * 
 * 
 */

public class Forage extends Codelet {
    
        private Memory knownAMO;
        private Memory knownJMO;
        private Memory leafletsMO;
        private MemoryObject selfInfoMO;
        private List<Thing> knownA;
        private List<Thing> knownJ;
        private MemoryContainer legsMO;


	/**
	 * Default constructor
	 */
	public Forage(){
            this.name = "Forage";
	}

	@Override
	@SuppressWarnings("unchecked")
	public void proc() {
			Idea cis = (Idea) selfInfoMO.getI();
            knownA = (List<Thing>) knownAMO.getI();
            knownJ = (List<Thing>) knownJMO.getI();
            List<Leaflet> leaflets = (List<Leaflet>) leafletsMO.getI();

			String stateFuel = (String) cis.get("stateFuel").getValue();
			boolean forageFood = "food".equals(stateFuel) && knownA.isEmpty();
			boolean forageJewel = "jewel".equals(stateFuel)
                                && !LeafletUtils.hasNeededJewel(knownJ, leaflets)
                                && LeafletUtils.hasOpenLeaflet(leaflets)
                                && !LeafletUtils.hasCompletedLeaflet(leaflets);

			System.out.println("Forage: stateFuel=" + stateFuel + " knownA=" + knownA.size() + " knownJ=" + knownJ.size());

			double activation = 0.0;
			JSONObject message = new JSONObject();
			try {
				message.put("ACTION", "FORAGE");
				if (forageFood || forageJewel) {
					activation = 1.0;
				}
				legsMO.setI(message.toString(), activation, name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void accessMemoryObjects() {
            knownAMO = (Memory)this.getInput("KNOWN_APPLES");
            knownJMO = (Memory)this.getInput("KNOWN_JEWELS");
            leafletsMO = (Memory)this.getInput("LEAFLETS");
            selfInfoMO=(MemoryObject)this.getInput("INNER");

            legsMO = (MemoryContainer)this.getOutput("LEGS");
	}
        
        @Override
        public void calculateActivation() {
            
        }


}
