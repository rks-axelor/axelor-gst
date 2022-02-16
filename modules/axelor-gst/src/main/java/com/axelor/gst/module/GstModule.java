package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.PartyManagementRepository;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceLineServiceImpl;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImpl;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImpl;

public class GstModule extends AxelorModule {
  
	@Override
	protected void configure() {
		bind(SequenceService.class).to(SequenceServiceImpl.class);
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
		bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
	    bind(PartyRepository.class).to(PartyManagementRepository.class);
		super.configure();
	}
}
