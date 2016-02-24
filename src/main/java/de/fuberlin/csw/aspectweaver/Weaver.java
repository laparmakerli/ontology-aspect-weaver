package de.fuberlin.csw.aspectweaver;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitorEx;


import java.util.Collections;
import java.util.Set;

/**
 * Created by lars on 18.01.16.
 */
public class Weaver {



    public void startWeaving(final OWLOntologyManager manager, final IRI ontologyIRI){



        final OWLDataFactory df = manager.getOWLDataFactory();
        final OWLAnnotationProperty hasAspect =df.getOWLAnnotationProperty(IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect_owl#hasAspect"));


        try {


            //Create a walker
            OWLOntologyWalker walker =
                new OWLOntologyWalker(Collections.singleton(manager.getOntology(ontologyIRI)));


            //Define what's going to visited
            OWLOntologyWalkerVisitorEx<Object> visitor =
                    new OWLOntologyWalkerVisitorEx<Object>(walker) {

                        //visit object property assertions
                        @Override
                        public Object visit(OWLObjectPropertyAssertionAxiom axiom) {


                            // get <hasAspect> annotations
                            for (OWLAnnotation annotation :  axiom.getAnnotations(hasAspect) ){

                                System.out.println("######" + annotation.getValue());

                                Optional<IRI> optIRI = annotation.getValue().asIRI();


                                if (optIRI.isPresent()) {
                                    IRI aspectIRI = annotation.getValue().asIRI().get();
                                    System.out.println("---------------------"  + aspectIRI);

                                    OWLClass aspect = df.getOWLClass(aspectIRI);

                                    EntitySearcher.getInstances(aspect, manager.getOntology(ontologyIRI));

                                }
                            }

                            return null;
                    }
                };

        walker.walkStructure(visitor);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }



}