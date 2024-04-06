package services;

import Util.DbManagement;
import entities.Statistic;
import entities.User;

import java.util.Date;
import java.util.List;

public class StatisticService extends DbManagement<Statistic> {

    public StatisticService() {
      super(Statistic.class);
    }

    public void create(String browser, String operatingSystem, String ipDirection) {
      Statistic statistic = new Statistic(browser, operatingSystem, ipDirection);
      createDb(statistic);
    }

    public void update(Statistic statistic) {
      updateDb(statistic);
    }

    public void delete(String id) {
      deleteDbById(id);
    }

    public Statistic find(String id) {
      return findDbByID(id);
    }

    public List<Statistic> findAll() {
      return findAllDb();
    }
}
