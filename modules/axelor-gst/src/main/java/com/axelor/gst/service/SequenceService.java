package com.axelor.gst.service;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public interface SequenceService {
    public void setNextNumber(ActionRequest request, ActionResponse response);
}
