package de.fuberlin.csw.aspectweaver;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lars on 24.02.16.
 */
public class NaryRelAspectModeler implements Weaver {


    OWLOntology sourceOntology;
    OWLOntologyManager ontologyManager;
    OWLDataFactory dataFactory;


    public NaryRelAspectModeler(OWLOntology sourceOntology){
        this.sourceOntology = sourceOntology;
        this.ontologyManager = sourceOntology.getOWLOntologyManager();
        this.dataFactory = ontologyManager.getOWLDataFactory();
    }

    public Set<OWLAxiom> startWeaving(){
        return null;
    }

    public Set<OWLAxiom> modelObjectPropertyWithAspect(OWLObjectPropertyAssertionAxiom objectProperty, IRI aspectIRI, OWLAnnotationProperty aspectAnnotationProperty){


        // prepare Ontology
        OWLClass aspect = dataFactory.getOWLClass(aspectIRI);

        Set<OWLAxiom> resultAxiomSet = new HashSet<OWLAxiom>();

        /*  -----------------Model left side of the property-----------------*/

        // old subject remains
        OWLIndividual newSubject = objectProperty.getSubject();

        // new target is the old object property as individual
        OWLIndividual newTarget = dataFactory.getOWLNamedIndividual(objectProperty.getProperty().asOWLObjectProperty().getIRI());

        // new object property from old aspect annotation property
        OWLObjectProperty newProperty = dataFactory.getOWLObjectProperty(aspectAnnotationProperty.getIRI());

        // create new Axiom
        resultAxiomSet.add(dataFactory.getOWLObjectPropertyAssertionAxiom(newProperty, newSubject, newTarget));



        /*  -----------------Model right side of the property (possibly results in multiple axioms)-----------------*/

        // new subject is the old object property as individual
        newSubject = dataFactory.getOWLNamedIndividual(objectProperty.getProperty().asOWLObjectProperty().getIRI());

        // new object property
        newProperty = dataFactory.getOWLObjectProperty(IRI.create(   (aspectAnnotationProperty.getIRI()).toString() + "_value" ));

        // new target is the old target
        for (OWLIndividual possibleWorld : EntitySearcher.getInstances(aspect, sourceOntology)) {
            newTarget = possibleWorld;
            resultAxiomSet.add(dataFactory.getOWLObjectPropertyAssertionAxiom(newProperty, newSubject, newTarget));
        }

        return resultAxiomSet;
    }






}
