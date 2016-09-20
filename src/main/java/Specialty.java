import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Specialty {
  private String name;
  private int id;

  public Specialty(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object otherSpecialty) {
    if (!(otherSpecialty instanceof Specialty)) {
      return false;
    } else {
      Specialty newSpecialty = (Specialty) otherSpecialty;
      return this.getName().equals(newSpecialty.getName()) &&
             this.getId() == newSpecialty.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO specialties (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .executeUpdate()
      .getKey();
    }
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public static List<Specialty> all() {
    String sql = "SELECT id, name FROM specialties";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Specialty.class);
    }
  }

  public List<Doctor> getDoctors() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors where specialtyId=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Doctor.class);
    }
  }

  public static Specialty find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM specialties where id=:id";
      Specialty specialty = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Specialty.class);
    return specialty;
    }
  }
}
