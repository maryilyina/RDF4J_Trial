package sesame.querying.configDB;

import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {

        String serverUrl = "http://localhost:8080/rdf4j-server";
        String repositoryName = "k500-in-mem";

        RemoteRepositoryManager remoteManager = new RemoteRepositoryManager(serverUrl);
        remoteManager.initialize();

        new QueryTesting(serverUrl, repositoryName).perform();

    }



}
