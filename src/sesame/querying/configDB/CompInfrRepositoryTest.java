package sesame.querying.configDB;

public class CompInfrRepositoryTest extends IRepositoryQueryTest{

    public CompInfrRepositoryTest(String serverUrl, String repositoryName) {
        super(serverUrl, repositoryName);
    }

    @Override
    public void perform() {

        //all statements
        String queryAllStatements = "SELECT ?s ?p ?o WHERE {?s ?p ?o }";
        //executeTupleQuery(queryAllStatements, "all statements, icci", false, 2);

        //all connections via ports
        String queryAllConnections = "PREFIX infr: <http://www.semanticweb.org/m-ilyina/ontologies/computer_infrastructure/>\n" +
                "SELECT ?obj1 ?port1 ?obj2 ?port2\n" +
                "WHERE {  \n" +
                "     ?link rdf:type infr:PhysicalLink.\n" +
                "     ?link infr:connects ?port1.\n" +
                "     ?link infr:connects ?port2.\n" +
                "FILTER (?port1 != ?port2)\n" +
                "     ?obj1 infr:hasPort ?port1.\n" +
                "     ?obj2 infr:hasPort ?port2\n" +
                "FILTER (xsd:string(?obj1) > xsd:string(?obj2))\n" +
                "}\n" +
                "ORDER BY ?obj1";
        executeTupleQuery(queryAllConnections, "all connections via ports, icci", false, 5);

        repository.shutDown();

    }
}
