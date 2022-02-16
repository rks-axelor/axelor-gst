package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.axelor.gst.db.Address;
import com.axelor.axelor.gst.db.InvoiceLine;
import com.axelor.axelor.gst.db.Product;


public interface InvoiceLineService {
	public BigDecimal[] setInvoiceLine(InvoiceLine invoiceLine,Address companyAddress,Address invoiceAddrs,Product product);
	

}
