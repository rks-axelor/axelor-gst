package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.axelor.gst.db.Address;

import com.axelor.axelor.gst.db.InvoiceLine;
import com.axelor.axelor.gst.db.Product;




public class InvoiceLineServiceImpl implements InvoiceLineService {
	@Override
	public BigDecimal[] setInvoiceLine(InvoiceLine invoiceLine,Address companyAddress,Address invoiceAddrs,Product product) {
		BigDecimal qty = BigDecimal.valueOf(invoiceLine.getQty());     
		BigDecimal price = invoiceLine.getPrice();
		BigDecimal netAmount = price.multiply(qty);
		BigDecimal gstRate = product.getGstRate();
		BigDecimal igst = BigDecimal.ZERO;
		BigDecimal sgst = BigDecimal.ZERO;
		BigDecimal cgst = BigDecimal.ZERO;
		BigDecimal grossAmount = BigDecimal.ZERO;
		sgst = netAmount.multiply(gstRate).divide(new BigDecimal("100"));
		cgst = netAmount.multiply(gstRate).divide(new BigDecimal("100"));
		if(companyAddress != null && companyAddress.getState().equals(invoiceAddrs.getState())) {
			
			sgst = sgst.divide(new BigDecimal("2"));
			cgst = cgst.divide(new BigDecimal("2"));
		}
		else {
			igst = netAmount.multiply(gstRate).divide(new BigDecimal("100"));
		}
		grossAmount = netAmount.add(igst).add(sgst).add(cgst);
		BigDecimal[] gstArray = new BigDecimal[5];
		gstArray[0] = netAmount;
		gstArray[1] = grossAmount;
		gstArray[2] = igst;
		gstArray[3] = sgst;
		gstArray[4] = cgst;
       return gstArray;
	}
}
