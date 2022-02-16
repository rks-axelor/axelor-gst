package com.axelor.gst.service;

import com.axelor.axelor.gst.db.Sequence;

public interface SequenceService {
    public String setNextNumber(Sequence sequence);

	public Sequence saveNextNumber(String model);
}
