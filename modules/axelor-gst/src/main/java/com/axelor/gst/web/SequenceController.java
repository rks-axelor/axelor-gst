package com.axelor.gst.web;

import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class SequenceController {
	@Inject
	SequenceService service;
    public void setNextNumber(ActionRequest request, ActionResponse response) {
    	service.setNextNumber(request, response);
    }
}
