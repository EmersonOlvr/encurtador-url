package com.emerson.errorgroups;

import javax.validation.GroupSequence;

import com.emerson.errorgroups.urls.*;

@GroupSequence({LinkNotBlankGroup.class, NomePatternGroup.class, NomeSizeMaxGroup.class})
public interface ValidationSequence {

}
