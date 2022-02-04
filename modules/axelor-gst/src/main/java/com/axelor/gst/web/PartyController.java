package com.axelor.gst.web;

import com.axelor.gst.service.PartyService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class PartyController {
	@Inject
	PartyService service;
   public void setReference(ActionRequest request, ActionResponse response) {
	   service.setReference(request, response);
   }
}
