package com.axelor.gst.service;

import com.axelor.axelor.gst.db.Sequence;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;

public class SequenceServiceImpl implements SequenceService {
  @Override
public void setNextNumber(ActionRequest request, ActionResponse response) {
	Context context = request.getContext();
	Sequence sequence = context.asType(Sequence.class);
	String prefix = sequence.getPrefix();
	String suffix = sequence.getSuffix();
	int padding = sequence.getPadding();
	String str = "";
	 for(int i=0; i<padding; i++) {
		 str = str + "0";
	 }
	String nextNumber = prefix + str + suffix;
	response.setValue(nextNumber, nextNumber);
	
}
}
