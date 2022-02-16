package com.axelor.gst.web;


import com.axelor.axelor.gst.db.Sequence;

import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import com.google.inject.Inject;


public class SequenceController {
	@Inject
	SequenceService service;
    public void setNextNumber(ActionRequest request, ActionResponse response) {
    	try {
	    	Context context = request.getContext();
			Sequence sequence = context.asType(Sequence.class);
			String nextNumber = service.setNextNumber(sequence);
			response.setValue("nextNumber", nextNumber);
    	}
    	catch(Exception e){
			response.setError(e.getStackTrace().toString());
		}
    }
	 
	 
}
