package sesame.querying.configDB;

import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;

public class Main {

    public static void main(String[] args) {

        String serverUrl = "http://localhost:8080/rdf4j-server";

        RemoteRepositoryManager remoteManager = new RemoteRepositoryManager(serverUrl);
        remoteManager.initialize();

        new CompInfrRepositoryTest(serverUrl, "ic-comp-infr").perform();
        //new K500RepositoryTest(serverUrl, "k500-conf").perform();

    }



}
