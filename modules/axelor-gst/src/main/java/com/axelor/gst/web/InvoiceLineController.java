package com.axelor.gst.web;

import java.math.BigDecimal;

import com.axelor.axelor.gst.db.Address;
import com.axelor.axelor.gst.db.Invoice;
import com.axelor.axelor.gst.db.InvoiceLine;
import com.axelor.axelor.gst.db.Product;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import com.google.inject.Inject;

public class InvoiceLineController {
   @Inject
   InvoiceLineService service;
   public void setInvoiceLine(ActionRequest request, ActionResponse response) {
	   try {
	   Context context = request.getContext();
		InvoiceLine invoiceLine = context.asType(InvoiceLine.class);
		Invoice invoice = invoiceLine.getInvoice();
		
		if(invoice==null) {
				invoice = context.getParent().asType(Invoice.class);
			}
		Product product = invoiceLine.getProduct();
		if(product==null) return;
		if(invoice == null) {
			System.err.println("invoice is null");
			return;
		}
		BigDecimal[] gstArray = new BigDecimal[5];
		for(int i=0;i<5;i++) {
			gstArray[i] = BigDecimal.TEN;
		}
		if(invoice.getCompany() == null || invoice.getCompany().getAddress() == null) response.setError(" Company or Company AddressShould Not be Empty ");
		//else if(invoice.getInvoiceAddress() == null) response.setError("Invoice Address Should Not be Empty");
		else {
			Address companyAddress = invoice.getCompany().getAddress();
			Address invoiceAddrs = invoice.getInvoiceAddress();
			gstArray = service.setInvoiceLine(invoiceLine, companyAddress, invoiceAddrs,product);
		}
		
		response.setValue("netAmount", gstArray[0]);
		response.setValue("grossAmount", gstArray[1]);
		response.setValue("igst", gstArray[2]);
		response.setValue("sgst", gstArray[3]);
		response.setValue("cgst", gstArray[4]);
	   }
		catch(Exception e){
			response.setError(e.getMessage());
		}
	}
   public void setItem(ActionRequest request, ActionResponse response) {
	   
	   try {
	   InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
	   Product product = invoiceline.getProduct();
	   if(product == null) return;
	  String item = product.getCode() + product.getName();
	  response.setValue("item", item);
	  response.setValue("gstRate", product.getGstRate());
	  response.setValue("hsbn", product.getHsbn());
	  response.setValue("price", product.getSalePrice());
	  response.setValue("qty", 5);
	   }
		catch(Exception e){
			response.setError(e.getMessage());
		}
   }
}
