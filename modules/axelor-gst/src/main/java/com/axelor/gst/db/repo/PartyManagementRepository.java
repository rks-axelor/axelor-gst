package com.axelor.gst.db.repo;

import javax.persistence.PersistenceException;

import com.axelor.axelor.gst.db.Party;
import com.axelor.axelor.gst.db.Sequence;
import com.axelor.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.service.SequenceService;
import com.google.inject.Inject;

public class PartyManagementRepository extends PartyRepository {
	@Inject
	SequenceService service;

	@Override
	public Party save(Party entity) {
		
		if (entity.getReference() == null) {
			Sequence sequence = service.saveNextNumber("Party");
			if (sequence == null) {
				throw new PersistenceException("No Sequence Available");
			} 
			String nextNumber = sequence.getNextNumber();
			entity.setReference(nextNumber);
			
		}
		return super.save(entity);
	}
}