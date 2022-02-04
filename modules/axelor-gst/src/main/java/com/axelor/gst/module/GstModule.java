package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.PartyService;
import com.axelor.gst.service.PartyServiceImpl;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImpl;

public class GstModule extends AxelorModule {
  
	@Override
	protected void configure() {
		bind(SequenceService.class).to(SequenceServiceImpl.class);
		bind(PartyService.class).to(PartyServiceImpl.class);
		super.configure();
	}
}
