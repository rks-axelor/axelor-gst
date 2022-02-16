package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.axelor.gst.db.Invoice;
import com.axelor.axelor.gst.db.InvoiceLine;

public interface InvoiceService {
	public BigDecimal[] setInvoiceDeafult(Invoice invoice);
    public List<InvoiceLine> createInvoice(List<Long> strIds, Invoice invoice);
}
