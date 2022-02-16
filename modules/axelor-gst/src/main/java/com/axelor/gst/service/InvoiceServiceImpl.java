package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;
import com.axelor.axelor.gst.db.Invoice;
import com.axelor.axelor.gst.db.InvoiceLine;
import com.axelor.axelor.gst.db.Product;
import com.axelor.axelor.gst.db.repo.ProductRepository;
import com.axelor.db.JpaSupport;
import com.axelor.inject.Beans;

public class InvoiceServiceImpl extends JpaSupport implements InvoiceService {
 

	
  @Override
	public BigDecimal[] setInvoiceDeafult(Invoice invoice) {
		BigDecimal[] gstGrossArray = new BigDecimal[5];
		List<InvoiceLine> invoiceList = invoice.getInvioceItemsList();
		if(invoiceList == null || invoiceList.size()==0) {
			return gstGrossArray;
		}
		if(invoice.getParty() == null|| invoice.getParty().getAddressList() == null || invoice.getParty().getAddressList().size()==0) return gstGrossArray;
		if(invoice.getCompany() == null || invoice.getCompany().getAddress() == null ) return gstGrossArray;
		BigDecimal igst = BigDecimal.ZERO;
		BigDecimal cgst = BigDecimal.ZERO;
		BigDecimal sgst = BigDecimal.ZERO;
		BigDecimal netAmount = BigDecimal.ZERO;
		BigDecimal grossAmount = BigDecimal.ZERO;
		for(InvoiceLine invoiceline : invoiceList) {
			igst = igst.add(invoiceline.getIgst());
			cgst = cgst.add(invoiceline.getCgst());
			sgst = sgst.add(invoiceline.getSgst());
			netAmount = netAmount.add(invoiceline.getNetAmount());
			grossAmount = grossAmount.add(invoiceline.getGrossAmount());
		}
		gstGrossArray[0] = igst;
		gstGrossArray[1] = sgst;
		gstGrossArray[2] = cgst;
		gstGrossArray[3] = grossAmount;
		gstGrossArray[4] = netAmount;
		
		return gstGrossArray;
	}
  @Override
	public List<InvoiceLine> createInvoice(List<Long> strIds, Invoice invoice) {
		
	    System.err.println(strIds);
	    
		List<Product> ProductList = Beans.get(ProductRepository.class).all().filter("self.id in :productIds").bind("productIds", strIds).fetch();
	    List<InvoiceLine> InvoiceItemList = invoice.getInvioceItemsList();
	    for(Product product:ProductList) {
	    	InvoiceLine iline = new InvoiceLine();
	    	iline.setProduct(product);
	    	iline.setPrice(product.getSalePrice());
	    	iline.setItem(product.getCode()+product.getName());
	    	iline.setHsbn(product.getHsbn());
	    	iline.setQty(5);
	    	InvoiceItemList.add(iline);
	    }
		return InvoiceItemList; 
	}

	}

