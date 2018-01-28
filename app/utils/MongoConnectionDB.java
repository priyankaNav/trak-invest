package utils;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import play.Logger;

public class MongoConnectionDB {
	 private static final Logger.ALogger logger = Logger.of(MongoConnectionDB.class);

	    private static Morphia morphia;
	    public static Datastore datastore;

		public static Datastore getDS() {
        try {
            logger.info("Initialising Mongo Connection..");
            if(morphia == null) {
            	String DATABASE_NAME = "trak_invest";
                morphia = new Morphia();
                String serverUri = "mongodb://priyanka:priyanka@ds139665.mlab.com:39665/trak_invest";
                String localUri = "mongodb://localhost:27017/";
                MongoClient mongoClient = new MongoClient(new MongoClientURI(serverUri));
                datastore = morphia.createDatastore(mongoClient, DATABASE_NAME);
                Logger.info("\n");
                Logger.info(datastore.toString());
                Logger.info("\n");
            }else{
                logger.debug("Skipping Init as Morphia is already instantiated");
            }
        }catch (Exception e){
            Logger.error("DB Init Error",e);
            throw new RuntimeException("DB start up error");
        }
		return datastore;
   }
}