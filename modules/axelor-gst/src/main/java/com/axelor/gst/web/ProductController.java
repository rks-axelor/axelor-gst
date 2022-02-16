package com.axelor.gst.web;

import java.util.List;

import com.axelor.axelor.gst.db.Invoice;
import com.axelor.axelor.gst.db.Product;
import com.axelor.axelor.gst.db.ProductCategory;

import com.axelor.i18n.I18n;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.meta.schema.actions.ActionView.ActionViewBuilder;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;


public class ProductController {

	public void setgstrate(ActionRequest request, ActionResponse response) {
		try {
		Context context = request.getContext();
		Product product = context.asType(Product.class);
		ProductCategory productcategory = product.getCategory();
		response.setValue("gstRate", productcategory.getGstRate());
		}
		catch(Exception e){
			response.setError(e.getStackTrace().toString());
		}
	}
	@SuppressWarnings("unchecked")
	public void createInvoicewithproduct(ActionRequest request, ActionResponse response) {
		try {
			if (request.getContext().get("_ids") == null) {
				response.setError("There is No record selected ");
				return;
			}
			List<Long> productIds = (List<Long>)request.getContext().get("_ids");

			ActionViewBuilder actionView = 
					 ActionView.define(I18n.get("Invoice"))
			            .model(Invoice.class.getName())
			            .add("form", "invoice-form");
			
			actionView .context("createWithSelectedProducts", productIds);
			response.setView(actionView.map());
		}
		catch(Exception e){
			response.setError(e.getStackTrace().toString());
		}
	}
	
	 
	}



