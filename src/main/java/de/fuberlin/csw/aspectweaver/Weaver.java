package de.fuberlin.csw.aspectweaver;

import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * Created by lars on 24.02.16.
 */
public interface Weaver {


    public abstract Set<OWLAxiom> startWeaving();

    public abstract Set<OWLAxiom> modelObjectPropertyWithAspect(OWLObjectPropertyAssertionAxiom objectProperty, IRI aspectIRI, OWLAnnotationProperty aspectAnnotationProperty);



}
