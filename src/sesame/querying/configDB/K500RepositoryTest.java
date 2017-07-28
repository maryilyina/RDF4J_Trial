package sesame.querying.configDB;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Mary on 21.07.2017.
 */
public class K500RepositoryTest extends IRepositoryQueryTest {

    //private final Logger logger = LoggerFactory.getLogger(QueryK500Repository.class);

    public K500RepositoryTest(String serverUrl, String repositoryName) {
        super(serverUrl, repositoryName);
    }

    public void perform() {

        //all statements
        String queryAllStatements = "SELECT ?s ?p ?o WHERE {?s ?p ?o }";
        executeTupleQuery(queryAllStatements, "all statements", false, 0);

        //all device types
        IRI devtype = repository.getValueFactory().createIRI("<http://www.semanticweb.org/mary/ontologies/2017/0/control-system-ontology/CtrlSystDevType>");
        String queryAllDeviceTypes = "SELECT ?s WHERE {?s rdf:type " + devtype + " }";
        executeTupleQuery(queryAllDeviceTypes, "all device types", false, 0);


        //all channels of devtypes
        String queryAllDevTypeChannels = "PREFIX cs: <http://www.semanticweb.org/mary/ontologies/2017/0/control-system-ontology/> " +
                "SELECT  ?devtype ?channel (STR(?_number) AS ?number)  (STR(?_datatype) AS ?datatype)  (STR(?_direction) AS ?direction) " +
                "WHERE { ?devtype rdf:type + cs:CtrlSystDevType. " +
                "?devtype cs:hasChannel ?channel." +
                "?channel cs:hasNumber ?_number .   " +
                "?channel cs:hasDataType ?_datatype." +
                "?channel cs:hasDirectionType ?_direction." +
                "} ORDER BY ?number";

        executeTupleQuery(queryAllDevTypeChannels, "all channels of devtypes", true, 5);

        repository.shutDown();

    }

}
