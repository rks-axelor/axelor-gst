package com.axelor.gst.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.axelor.axelor.gst.db.Sequence;
import com.axelor.db.JpaSupport;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class PartyServiceImpl extends JpaSupport implements PartyService {
  @Override
public void setReference(ActionRequest request, ActionResponse response) {
	EntityManager em = getEntityManager();
	Query getNextNumber = em.createQuery("SELECT S FROM Sequence S WHERE S.model.name='Party'");
	@SuppressWarnings("unchecked")
	List<Sequence> list = (List<Sequence>)getNextNumber.getResultList();
	String nextNumber = list.get(0).getNextNumber();
	int padding = list.get(0).getPadding();
	String prefix = list.get(0).getPrefix();
	String suffix = list.get(0).getSuffix();
	int num = Integer.parseInt(nextNumber.substring(prefix.length(), nextNumber.length() - suffix.length()));
	num += 1;
	String newNextNumber = ""+num;
	int count = padding-newNextNumber.length();
	for(int i=0;i<count;i++) {
		
		newNextNumber = "0"+newNextNumber;
	}
	newNextNumber = prefix+newNextNumber+suffix;
	response.setValue("reference", nextNumber);
	em.getTransaction().begin();
	list.get(0).setNextNumber(newNextNumber);
	em.persist(list.get(0));
	em.getTransaction().commit();
}
}
