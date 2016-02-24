/**
 * Created by lars on 02.02.16.
 */

import de.fuberlin.csw.aspectweaver.Weaver;
import junit.framework.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import javax.annotation.Nonnull;
import java.io.File;


public class TestWeaving {


    static String testfolder = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "ontologies" + File.separator;


    @Nonnull
    public static final IRI ASPECT_EXAMPLE_ONTO_IRI = IRI.create("http://csw.inf.fu-berlin.de/aood/example");

    @Nonnull
    public static final File ASPECT_EXAMPLE_ONTO_FILE = new File(testfolder + "AspectsExample.owl");

    @Nonnull
    public static final IRI ADVISED_ASPECT_ONTO_IRI = IRI.create("http://www.corporate-semantic-web.de/ontologies/aspect_owl");

    @Nonnull
    public static final File ADVISED_ASPECT_ONTO_FILE = new File(testfolder + "AspectsExample.owl");



    @Test
    public void testWaeaving(){

        OWLOntologyManager om = OWLManager.createOWLOntologyManager();

        om.getIRIMappers().add(new SimpleIRIMapper(ADVISED_ASPECT_ONTO_IRI, IRI.create(ADVISED_ASPECT_ONTO_FILE)));
        om.getIRIMappers().add(new SimpleIRIMapper(ASPECT_EXAMPLE_ONTO_IRI, IRI.create(ASPECT_EXAMPLE_ONTO_FILE)));


        try {


            OWLOntology ont = om.loadOntology(ASPECT_EXAMPLE_ONTO_IRI);

            Weaver weaver = new Weaver();

            weaver.startWeaving(om, ASPECT_EXAMPLE_ONTO_IRI);



        } catch (OWLOntologyCreationException e){
            e.printStackTrace();
        }





    }



}
