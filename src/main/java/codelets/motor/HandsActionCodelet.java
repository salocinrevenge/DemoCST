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

package codelets.motor;


import org.json.JSONException;
import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Random;
import java.util.logging.Logger;
import ws3dproxy.model.Creature;

/**
 *  Hands Action Codelet monitors working storage for instructions and acts on the World accordingly.
 *  
 * @author klaus
 *
 */


public class HandsActionCodelet extends Codelet{

	private Memory handsMO;
	private String previousHandsAction="";
        private Creature c;
        private Random r = new Random();
        static Logger log = Logger.getLogger(HandsActionCodelet.class.getCanonicalName());

	public HandsActionCodelet(Creature nc) {
                c = nc;
                this.name = "HandsActionCodelet";
	}
	
        @Override
	public void accessMemoryObjects() {
		handsMO=(MemoryObject)this.getInput("HANDS"); // Obtem o memory object de entrada "HANDS"
	}
	public void proc() {
		System.out.println("Sanityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
            
                String command = (String) handsMO.getI(); // Lê o comando atual do memory object "HANDS"
		System.out.println("Received command: "+command);

		if(!command.equals("") && (!command.equals(previousHandsAction))){
			JSONObject jsonAction;
			try {
				jsonAction = new JSONObject(command);
				System.out.println("Processing hands action: "+command);
				if(jsonAction.has("ACTION") && jsonAction.has("OBJECT")){
					String action=jsonAction.getString("ACTION");
					String objectName=jsonAction.getString("OBJECT");
					System.out.println("Action: "+action+" Object: "+objectName);
					if(action.equals("PICKUP")){
												System.out.println("Trying to pick up the object: "+objectName);
												// Tenta 5 vezes
												for(int i=0; i<5; i++){
                                                try {
													c.putInSack(objectName);
													c.updateState();
													c.updateBag();
													break; // Se conseguir, sai do loop
													} catch (Exception e) {
														
													} 
												}
						log.info("Sending Put In Sack command to agent:****** "+objectName+"**********");							
						
						
						//							}
					}
					if(action.equals("EATIT")){
                                                try {
                                                 c.eatIt(objectName);
												 System.out.println("I sent the comand eatit to Eating the object: "+objectName);
                                                } catch (Exception e) {
                                                    
                                                }
						log.info("Sending Eat command to agent:****** "+objectName+"**********");							
					}
					if(action.equals("BURY")){
                                                try {
                                                 c.hideIt(objectName);
                                                } catch (Exception e) {
                                                    
                                                }
						log.info("Sending Bury command to agent:****** "+objectName+"**********");							
					}
				}
                                if(jsonAction.has("ACTION") && jsonAction.has("LEAFLET_ID")){
					String action=jsonAction.getString("ACTION");
					String leafletId=jsonAction.getString("LEAFLET_ID");
					if(action.equals("DELIVER")){
						System.out.println("Trying to deliver the leaflet: "+leafletId);
                                                try {
                                                 c.deliverLeaflet(leafletId);
                                                 c.genLeaflet();
                                                 c.updateState();
                                                 c.updateBag();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
												System.out.println("Delivering leaflet: "+leafletId);
						log.info("Sending Deliver Leaflet command to agent:****** "+leafletId+"**********");							
					}
				}
//                                else if (jsonAction.has("ACTION")) {
//                                    int x=0,y=0;
//                                    String action=jsonAction.getString("ACTION");
//                                    if(action.equals("FORAGE")){
//                                                try {
//                                                      x = r.nextInt(600);
//                                                      y = r.nextInt(800);
//                                                 c.moveto(1, x,y );
//                                                } catch (Exception e) {
//                                                    
//                                                }
//						System.out.println("Sending Forage command to agent:****** ("+x+","+y+") **********");							
//					}
//                                }
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
//		System.out.println("OK_hands");
		previousHandsAction = (String) handsMO.getI();
	}//end proc

    @Override
    public void calculateActivation() {
        
    }


}
