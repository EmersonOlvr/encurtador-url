package com.emerson.errorgroups;

import javax.validation.GroupSequence;

import com.emerson.errorgroups.url.*;

@GroupSequence({UrlOriginalNotBlankGroup.class, UrlEncurtadaPatternGroup.class, UrlEncurtadaSizeMaxGroup.class})
public interface ValidationSequence {

}
