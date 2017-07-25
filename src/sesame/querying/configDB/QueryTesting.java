package sesame.querying.configDB;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Mary on 21.07.2017.
 */
public class QueryTesting {

    private static RepositoryManager remoteManager;
    private static Repository repository;
    private final Logger logger = LoggerFactory.getLogger(QueryTesting.class);

    public QueryTesting(String serverUrl, String repositoryName) {
        remoteManager = RepositoryProvider.getRepositoryManager(serverUrl);
        repository = remoteManager.getRepository(repositoryName);
    }

    public void perform() {

        //all statements
        String queryAllStatements = "SELECT ?s ?p ?o WHERE {?s ?p ?o }";
        executeTupleQuery(queryAllStatements, false);

        //all device types
        IRI devtype = repository.getValueFactory().createIRI("<http://www.semanticweb.org/mary/ontologies/2017/0/control-system-ontology/CtrlSystDevType>");
        String queryAllDeviceTypes = "SELECT ?s WHERE {?s rdf:type " + devtype + " }";
        executeTupleQuery(queryAllDeviceTypes, true);


        //all channels of devtypes
        String queryAllDevTypeChannels = "PREFIX cs: <http://www.semanticweb.org/mary/ontologies/2017/0/control-system-ontology/> " +
                "SELECT  ?devtype ?channel (STR(?_number) AS ?number)  (STR(?_datatype) AS ?datatype)  (STR(?_direction) AS ?direction) " +
                "WHERE { ?devtype rdf:type + cs:CtrlSystDevType. " +
                "?devtype cs:hasChannel ?channel." +
                "?channel cs:hasNumber ?_number .   " +
                "?channel cs:hasDataType ?_datatype." +
                "?channel cs:hasDirectionType ?_direction." +
                "} ORDER BY ?number";

        executeTupleQuery(queryAllDevTypeChannels, true);

        repository.shutDown();

    }

    private void executeTupleQuery(String query, boolean shouldPrint) {
        try (RepositoryConnection connection = repository.getConnection()){
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
            logger.info("Performing query {}", query);

            long start = System.currentTimeMillis();
            TupleQueryResult result = tupleQuery.evaluate();
            long finish = System.currentTimeMillis();

            if (result != null && shouldPrint) {
                List<String> bindingNames = result.getBindingNames();
                while (result.hasNext()) {
                    BindingSet set = result.next();
                    for (String name : bindingNames) {
                        System.out.print(set.getValue(name));
                        System.out.print(" ");
                    }

                    System.out.println();
                }
            }

            logger.info("Time spent: {} ms", finish - start);

        }
    }
}
