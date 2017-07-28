package sesame.querying.configDB;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public abstract class IRepositoryQueryTest {

    protected static RepositoryManager remoteManager;
    protected static Repository repository;

    public IRepositoryQueryTest(String serverUrl, String repositoryName){
        remoteManager = RepositoryProvider.getRepositoryManager(serverUrl);
        repository = remoteManager.getRepository(repositoryName);
    }

    public abstract void perform();


    protected void executeTupleQuery(String query, String queryName, boolean shouldPrint, int repeatTimes) {

        if (repeatTimes != 0) {

            StringBuilder queryTimes = new StringBuilder();


            while (repeatTimes > 0) {

                try (RepositoryConnection connection = repository.getConnection()) {
                    TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
                    //logger.info("Performing query {}", query);
                    System.out.println("Performing query " + queryName);


                    long start = System.currentTimeMillis();
                    TupleQueryResult result = tupleQuery.evaluate();
                    long finish = System.currentTimeMillis();


                    if (result != null && shouldPrint && repeatTimes == 1) {
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

                    repeatTimes--;
                    queryTimes.append(finish - start).append("\n");

                    System.out.println();
                    System.out.print("Spent ");
                    System.out.print(finish - start);
                    System.out.print(" ms\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            try {
                printResults(queryName, queryTimes.toString());
            } catch (IOException e) {
                System.out.println(queryTimes.toString());
                e.printStackTrace();
            }
        }
    }

    private void printResults(String queryName, String queryResults) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(queryName + ".txt", true));
        printWriter.print(queryResults);
        printWriter.close();
    }
}
