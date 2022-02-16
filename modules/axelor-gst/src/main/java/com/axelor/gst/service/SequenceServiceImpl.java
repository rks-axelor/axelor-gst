package com.axelor.gst.service;

import com.axelor.axelor.gst.db.Sequence;
import com.axelor.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;


public class SequenceServiceImpl implements SequenceService {
  @Override
public String setNextNumber(Sequence sequence) {
	String prefix = sequence.getPrefix();
	String suffix = sequence.getSuffix();
	int padding = sequence.getPadding();
	String str = "";
	 for(int i=0; i<padding; i++) {
		 str = str + "0";
	 }
	String nextNumber = prefix + str + suffix;
	return nextNumber;
	
}
  @Transactional
  public Sequence saveNextNumber(String model) {
	  Sequence sequence = Beans.get(SequenceRepository.class).all().filter("self.model.name=?", model)
				.fetchOne();
		if (sequence == null) {
			return sequence;
		} 
	 
	 String nextNumber = sequence.getNextNumber();
	 int padding = sequence.getPadding();
	 String suffix = sequence.getSuffix();
	 String prefix = sequence.getPrefix();
	 int numn = Integer.parseInt(nextNumber.substring(prefix.length(), nextNumber.length() - suffix.length()));
	  numn += 1; 
	  String newNextNumber = ""+numn;
     int count = padding-newNextNumber.length(); 
     for(int i=0;i<count;i++) {
       newNextNumber = "0"+newNextNumber;
    } 
     newNextNumber = prefix+newNextNumber+suffix;
     sequence.setNextNumber(newNextNumber);

     return Beans.get(SequenceRepository.class).save(sequence);
     
     
  }
}

