package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;

import com.axelor.axelor.gst.db.Address;
import com.axelor.axelor.gst.db.Contact;
import com.axelor.axelor.gst.db.Invoice;
import com.axelor.axelor.gst.db.InvoiceLine;
import com.axelor.axelor.gst.db.Party;
import com.axelor.axelor.gst.db.Product;
import com.axelor.axelor.gst.db.Sequence;
import com.axelor.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import com.google.inject.Inject;

public class InvoiceController {
	@Inject
	InvoiceService service;
	@Inject
	InvoiceLineService lineservice;
	@Inject
	SequenceService seqservice;

	public void setPartydependentValues(ActionRequest request, ActionResponse response) {
		try {
			Invoice invoice = request.getContext().asType(Invoice.class);
			if (invoice.getParty() == null) {
				response.setValue("partyContact", null);
				response.setValue("invoiceAddress", null);
			} else {
				Party party = invoice.getParty();
				List<Contact> contactlist = party.getContactList();
				List<Address> addresslist = party.getAddressList();
				for (Contact c : contactlist) {
					if (c.getType().equals("primary")) {
						response.setValue("partyContact", c);
						break;
					}

				}
				for (Address a : addresslist) {
					if (a.getType().equals("default") || a.getType().equals("invoice")) {
						response.setValue("invoiceAddress", a);
						break;
					}
				}
			}
			if (invoice.getIsInvoiceAddressAsShipping() == false) {
				Party party = invoice.getParty();
				List<Address> addresslist = party.getAddressList();
				for (Address a : addresslist) {
					if (a.getType().equals("default") || a.getType().equals("shipping")) {
						response.setValue("shippingAddress", a);
						break;
					}
				}
			}
		} catch (Exception e) {
			response.setError(e.getMessage());
		}
	}

	public void setReference(ActionRequest request, ActionResponse response) {
		try {
			Invoice invoice = request.getContext().asType(Invoice.class);
			if (invoice.getReference() == null) {
				Sequence sequence = seqservice.saveNextNumber("Invoice");
				if (sequence == null) {
					throw new PersistenceException("No Sequence Available");
				} 
				String nextNumber = sequence.getNextNumber();
				response.setValue("reference", nextNumber);
			}
		} catch (Exception e) {
			response.setError(e.getMessage());
		}

	}

	public void setInvoiceDefault(ActionRequest request, ActionResponse response) {
		try {
			Context context = request.getContext();
			Invoice invoice = context.asType(Invoice.class);
			BigDecimal[] grossGstArray = service.setInvoiceDeafult(invoice);
			response.setValue("igst", grossGstArray[0]);
			response.setValue("sgst", grossGstArray[1]);
			response.setValue("cgst", grossGstArray[2]);
			response.setValue("grossAmount", grossGstArray[3]);
			response.setValue("netAmount", grossGstArray[4]);
		} catch (Exception e) {
			response.setError(e.getMessage());
		}
	}

	public void recalculateInvoice(ActionRequest request, ActionResponse response) {

		try {

			Context context = request.getContext();
			Invoice invoice = context.asType(Invoice.class);

			if (invoice.getCompany() != null && invoice.getCompany().getAddress() != null) {
				if (invoice.getParty() != null && invoice.getParty().getAddressList() != null
						&& invoice.getParty().getAddressList().size() != 0) {
					if (invoice.getInvioceItemsList() != null && invoice.getInvioceItemsList().size() != 0) {

						Address companyAddress = invoice.getCompany().getAddress();
						Address invoiceAddrs = invoice.getInvoiceAddress();
						BigDecimal[] gstArray = new BigDecimal[5];
						for (int i = 0; i < 5; i++) {
							gstArray[i] = BigDecimal.TEN;
						}
						for (InvoiceLine invoiceLine : invoice.getInvioceItemsList()) {
							Product product = invoiceLine.getProduct();
							gstArray = lineservice.setInvoiceLine(invoiceLine, companyAddress, invoiceAddrs, product);
							invoiceLine.setNetAmount(gstArray[0]);
							invoiceLine.setGrossAmount(gstArray[1]);
							invoiceLine.setIgst(gstArray[2]);
							invoiceLine.setSgst(gstArray[3]);
							invoiceLine.setCgst(gstArray[4]);
						}
					}
				}
			}
		} catch (Exception e) {
			response.setError(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	public void createInvoice(ActionRequest request, ActionResponse response) {
		try {
			Context context = request.getContext();
			if (context.containsKey("createWithSelectedProducts")) {
				List<Long> strIds = (List<Long>) context.get("createWithSelectedProducts");
				Invoice invoice = context.asType(Invoice.class);
				List<InvoiceLine> InvoiceItemList = service.createInvoice(strIds, invoice);
				if (InvoiceItemList != null)
					response.setValue("invioceItemsList", InvoiceItemList);
			}
		} catch (Exception e) {
			response.setError(e.getMessage());
		}
	}
	
	public void generateRpc(ActionRequest request, ActionResponse response) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<String> eventType = new ArrayList<>(Arrays.asList(
		                "draft",
		                "validate",
		                "cancelled",
		                "paid"));
		Long vinvoice = Beans.get(InvoiceRepository.class).all()
				.filter("self.status = ?","validate").count();
		Long pinvoice = Beans.get(InvoiceRepository.class).all()
				.filter("self.status = ?","paid").count();
		Long dinvoice = Beans.get(InvoiceRepository.class).all()
				.filter("self.status = ?","draft").count();
		Long cinvoice = Beans.get(InvoiceRepository.class).all()
				.filter("self.status = ?","cancelled").count();
		Map<String, Object> vdataMap = new HashMap<>();
		Map<String, Object> cdataMap = new HashMap<>();
		Map<String, Object> pdataMap = new HashMap<>();
		Map<String, Object> ddataMap = new HashMap<>();
		vdataMap.put("count_status", vinvoice);
		vdataMap.put("status_name", StringUtils.capitalize(eventType.get(1)));
		ddataMap.put("count_status", dinvoice);
		ddataMap.put("status_name", StringUtils.capitalize(eventType.get(0)));
		cdataMap.put("count_status", cinvoice);
		cdataMap.put("status_name", StringUtils.capitalize(eventType.get(2)));
		pdataMap.put("count_status", pinvoice);
		pdataMap.put("status_name", StringUtils.capitalize(eventType.get(3)));
		
		dataList.add(vdataMap);
		dataList.add(pdataMap);
		dataList.add(cdataMap);
		dataList.add(ddataMap);
		response.setData(dataList);
		
	}

}
