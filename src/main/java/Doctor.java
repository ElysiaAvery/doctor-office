import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Doctor {
  private String name;
  private int specialtyId;
  private int id;

  public Doctor(String name, int specialtyId) {
    this.name = name;
    this.specialtyId = specialtyId;
  }

  @Override
  public boolean equals(Object otherDoctor) {
    if (!(otherDoctor instanceof Doctor)) {
      return false;
    } else {
      Doctor newDoctor = (Doctor) otherDoctor;
      return this.getName().equals(newDoctor.getName()) &&
             this.getSpecialtyId() == newDoctor.getSpecialtyId() &&
             this.getId() == newDoctor.getId();
    }
  }


  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO doctors (name, specialtyId) VALUES (:name, :specialtyId)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("specialtyId", this.specialtyId)
      .executeUpdate()
      .getKey();
    }
  }

  public String getName() {
    return name;
  }

  public int getSpecialtyId() {
    return specialtyId;
  }

  public static List<Doctor> all() {
    String sql = "SELECT id, name, specialtyId FROM doctors";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Doctor.class);
    }
  }

  public int getId() {
    return id;
  }

  public List<Patient> getPatients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients where doctorId=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Patient.class);
    }
  }

  public static Doctor find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors where id=:id";
      Doctor doctor = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Doctor.class);
    return doctor;
    }
  }

  public static List<Doctor> arrange(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors ORDER BY :name DESC;";
      List<Doctor> doctor = con.createQuery(sql)
      .addParameter("name", name)
      .executeAndFetch(Doctor.class);
    return doctor;
    }
  }
}
