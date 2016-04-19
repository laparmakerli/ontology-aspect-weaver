/**
 * Created by lars on 02.02.16.
 */


import de.fuberlin.csw.aspectweaver.ContextSlicesWeaver;
import de.fuberlin.csw.aspectweaver.Weaver;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;


public class TestWeaving {


    static String testfolder = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "ontologies" + File.separator;


    @Nonnull
    public static final IRI ASPECT_EXAMPLE_ONTO_IRI = IRI.create("http://csw.inf.fu-berlin.de/aood/context_slices_example");

    @Nonnull
    public static final File ASPECT_EXAMPLE_ONTO_FILE = new File(testfolder + "contextSlicesExample.owl");

    @Nonnull
    public static final IRI ADVISED_ASPECT_ONTO_IRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect_owl");

    @Nonnull
    public static final File ADVISED_ASPECT_ONTO_FILE = new File(testfolder + "aspectOWL.owl");

    @Nonnull
    public static final IRI CONTEXT_SLICES_IRI = IRI.create("http://example.org/ContextSlices");

    @Nonnull
    public static final File CONTEXT_SLICES_FILE = new File(testfolder + "contextSlices.owl");

    @Nonnull
    public static final IRI ASPECT_EXAMPLE_ONTO_IRI_WOVEN = IRI.create("http://csw.inf.fu-berlin.de/aood/context_slices_example_woven");

    @Nonnull
    public static final File ASPECT_EXAMPLE_ONTO_FILE_WOVEN = new File(testfolder + "context_slices_example_woven.owl");


    @Test
    public void testWaeaving(){

        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();



        ontologyManager.getIRIMappers().add(new SimpleIRIMapper(ADVISED_ASPECT_ONTO_IRI, IRI.create(ADVISED_ASPECT_ONTO_FILE)));
        ontologyManager.getIRIMappers().add(new SimpleIRIMapper(ASPECT_EXAMPLE_ONTO_IRI, IRI.create(ASPECT_EXAMPLE_ONTO_FILE)));
        ontologyManager.getIRIMappers().add(new SimpleIRIMapper(CONTEXT_SLICES_IRI, IRI.create(CONTEXT_SLICES_FILE)));



        try {


            OWLOntology ontology = ontologyManager.loadOntology(ASPECT_EXAMPLE_ONTO_IRI);

            Weaver contextSlicesWeaver = new ContextSlicesWeaver(ontology);
            Set<OWLAxiom> newAxiomSet = contextSlicesWeaver.startWeaving();


            ontologyManager.createOntology(newAxiomSet, ASPECT_EXAMPLE_ONTO_IRI_WOVEN);
            ontologyManager.getIRIMappers().add(new SimpleIRIMapper(ASPECT_EXAMPLE_ONTO_IRI_WOVEN, IRI.create(ASPECT_EXAMPLE_ONTO_FILE_WOVEN)));



            //ontologyManager.saveOntology(ontologyManager.getOntology(ASPECT_EXAMPLE_ONTO_IRI_WOVEN));

            ontologyManager.saveOntology(ontologyManager.getOntology(ASPECT_EXAMPLE_ONTO_IRI_WOVEN), new FileOutputStream(ASPECT_EXAMPLE_ONTO_FILE_WOVEN));





        } catch (Exception e){   // TODO
            e.printStackTrace();
        }





    }



}
