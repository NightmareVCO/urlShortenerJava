package Util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.List;

public class DbManagement<T> {

  private Class<T> entityClass;
  private MongoClient mongoDb = MongoDbConnection.getMongoClient();

  protected Datastore getConnectionMorphia(){
    return Morphia.createDatastore(mongoDb, MongoDbConnection.getDataBaseName());
  }

  public DbManagement(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected Datastore getConexionMorphia(){
    return Morphia.createDatastore(mongoDb,MongoDbConnection.getDataBaseName());
  }

  public void createDb(T entity){
    Datastore datastore = getConexionMorphia();
    try {
      datastore.save(entity);
    }catch (Exception  e){
      e.printStackTrace();
    }
  }
  public Query<T> findDb(){
    Datastore datastore = getConexionMorphia();
    Query<T> query = datastore.find(entityClass);
    return query;
  }
  public T findDbByID(String id){
    Datastore datastore = getConexionMorphia();
    Query<T> query = datastore.find(entityClass).filter(Filters.eq("_id", new ObjectId(id)));
    return  query.first();
  }

  public List<T> findAllDb() {
    return findDb().iterator().toList();
  }

  public void deleteDbById(String id){
    Datastore datastore = getConexionMorphia();
    datastore.find(entityClass).filter(Filters.eq("_id", new ObjectId(id))).delete();

  }

  public void updateDb(T entity){
    Datastore datastore = getConexionMorphia();
    try {
      datastore.save(entity);
    }catch (Exception  e){
      e.printStackTrace();
    }
  }
}

class MongoDbConnection {

  private static MongoClient mongoClient;
  private static String dataBaseName;

  public static MongoClient getMongoClient() {
    if(mongoClient == null){
      ProcessBuilder processBuilder = new ProcessBuilder();
      String URL_MONGODB = processBuilder.environment().get(StaticFiles.URL_MONGO.getValue());
      dataBaseName = processBuilder.environment().get(StaticFiles.DB_NAME.getValue());
      mongoClient = MongoClients.create(URL_MONGODB);
    }
    return mongoClient;
  }

  public static String getDataBaseName(){
    return dataBaseName;
  }
}