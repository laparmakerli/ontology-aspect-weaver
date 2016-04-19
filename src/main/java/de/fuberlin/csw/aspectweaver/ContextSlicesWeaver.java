package de.fuberlin.csw.aspectweaver;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitorEx;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lars on 01.03.16.
 */
public class ContextSlicesWeaver implements Weaver {

    OWLOntology sourceOntology;
    OWLOntologyManager ontologyManager;
    OWLDataFactory dataFactory;


    /* predefined IRI's from context slices ontology */
    public static final IRI CONTEXT = IRI.create("http://example.org/ContextSlices#Context");
    public static final IRI CONTEXTUAL_PROJECTION = IRI.create("http://example.org/ContextSlices#ContextualProjection");
    public static final IRI HAS_CONTEXT = IRI.create("http://example.org/ContextSlices#hasContext");
    public static final IRI PROJECTION_OF = IRI.create("http://example.org/ContextSlices#projectionOf");


    public ContextSlicesWeaver(OWLOntology sourceOntology){
        this.sourceOntology = sourceOntology;
        this.ontologyManager = sourceOntology.getOWLOntologyManager();
        this.dataFactory = ontologyManager.getOWLDataFactory();
    }


    public Set<OWLAxiom> startWeaving(){

        final Set<OWLAxiom> newOntologyAxiomSet = new HashSet<OWLAxiom>();
        newOntologyAxiomSet.addAll(sourceOntology.getAxioms(Imports.INCLUDED));

        final OWLAnnotationProperty hasAspect = dataFactory.getOWLAnnotationProperty(IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect_owl#hasAspect"));

        try {

            //Create a walker
            OWLOntologyWalker walker =
                    new OWLOntologyWalker(Collections.singleton(sourceOntology));


            //Define what's going to visited
            OWLOntologyWalkerVisitorEx<Object> visitor =
                    new OWLOntologyWalkerVisitorEx<Object>(walker) {

                        //visit object property assertions
                        @Override
                        public Object visit(OWLObjectPropertyAssertionAxiom objectPropertyAssertionAxiom) {


                            // collect axiom annotations
                            Set<OWLAnnotationProperty> aspectAnnotationProperties = new HashSet<OWLAnnotationProperty>();
                            aspectAnnotationProperties.addAll(EntitySearcher.getSubProperties(hasAspect, sourceOntology));
                            aspectAnnotationProperties.add(hasAspect);


                            // get <hasAspect> annotations
                            for (OWLAnnotationProperty aspectAnnotationProperty : aspectAnnotationProperties) {
                                for (OWLAnnotation annotation : objectPropertyAssertionAxiom.getAnnotations(aspectAnnotationProperty)) {

                                    Optional<IRI> optIRI = annotation.getValue().asIRI();


                                    if (optIRI.isPresent()) {
                                        IRI aspectIRI = annotation.getValue().asIRI().get();
                                        System.out.println("---------------------" + aspectIRI);


                                        for (OWLClassExpression cl : EntitySearcher.getSubClasses(dataFactory.getOWLClass(CONTEXT), sourceOntology)){
                                            System.out.println(cl);
                                        }

                                        if ( EntitySearcher.getSubClasses(dataFactory.getOWLClass(CONTEXT), sourceOntology)
                                                .contains(dataFactory.getOWLClass(aspectIRI))){

                                            newOntologyAxiomSet.remove(objectPropertyAssertionAxiom);
                                            newOntologyAxiomSet.addAll(
                                                    modelObjectPropertyWithAspect(objectPropertyAssertionAxiom, aspectIRI, aspectAnnotationProperty));

                                        }
                                    }
                                }

                            }

                            return null;
                        }
                    };

            walker.walkStructure(visitor);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newOntologyAxiomSet;

    }


    public Set<OWLAxiom> modelObjectPropertyWithAspect(OWLObjectPropertyAssertionAxiom objectProperty, IRI aspectIRI, OWLAnnotationProperty aspectAnnotationProperty){

        // prepare Ontology
        OWLClass aspect = dataFactory.getOWLClass(aspectIRI);

        Set<OWLAxiom> resultAxiomSet = new HashSet<OWLAxiom>();


        /* TODO: make sure cs ontology is imported by the source-ontology
           TODO: (may also use some different narkers for context-aspects
           TODO: and add the cs ontology afterwards to the target ontology since
           TODO: only the Context class of the cs ontology is needed before) */


        /*create projections for subject and object*/

        OWLIndividual subjectProjection = null;

        /*subject projection*/
        if(objectProperty.getSubject().isNamed()){
            subjectProjection = dataFactory.getOWLNamedIndividual
                    (IRI.create(objectProperty.getSubject().asOWLNamedIndividual().getIRI().toString() + "@" + aspectIRI.getShortForm()) );
        } else {
            /*  TODO  */
        }

        resultAxiomSet.add(dataFactory.getOWLObjectPropertyAssertionAxiom(dataFactory.getOWLObjectProperty(PROJECTION_OF), subjectProjection, objectProperty.getSubject()));


        OWLIndividual objectProjection = null;

        /*object projection*/
        if(objectProperty.getObject().isNamed()){
            objectProjection = dataFactory.getOWLNamedIndividual
                    (IRI.create(objectProperty.getObject().asOWLNamedIndividual().getIRI().toString() + "@" + aspectIRI.getShortForm()) );
        } else {
            /*  TODO  */
        }

        resultAxiomSet.add(dataFactory.getOWLObjectPropertyAssertionAxiom(dataFactory.getOWLObjectProperty(PROJECTION_OF), objectProjection, objectProperty.getObject()));


        /*make projections instances of cs:ContextualProjection*/
        resultAxiomSet.add(dataFactory.getOWLClassAssertionAxiom(dataFactory.getOWLClass(CONTEXTUAL_PROJECTION), objectProjection));
        resultAxiomSet.add(dataFactory.getOWLClassAssertionAxiom(dataFactory.getOWLClass(CONTEXTUAL_PROJECTION), subjectProjection));



        /*add hasConhttps://www.google.de/webhp?ie=UTF-8&sa=Search&channel=fe&client=browser-ubuntu&hl=en&gws_rd=cr,ssl&ei=gPfWVpHxGoKAacHusdAFtextProperties to projections*/

        for (OWLIndividual instance : EntitySearcher.getInstances(dataFactory.getOWLClass(aspectIRI), sourceOntology)){
            resultAxiomSet.add(dataFactory.getOWLObjectPropertyAssertionAxiom(dataFactory.getOWLObjectProperty(HAS_CONTEXT), subjectProjection, instance));
            resultAxiomSet.add(dataFactory.getOWLObjectPropertyAssertionAxiom(dataFactory.getOWLObjectProperty(HAS_CONTEXT), objectProjection, instance));
        }


        /*add original object property between projections*/
        resultAxiomSet.add(dataFactory.getOWLObjectPropertyAssertionAxiom(objectProperty.getProperty(), subjectProjection, objectProjection));

        return resultAxiomSet;
    }


}
