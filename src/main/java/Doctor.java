import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Doctor {
  private String name;
  private String specialty;
  private int id;

  public Doctor(String name, String specialty) {
    this.name = name;
    this.specialty = specialty;
  }

  @Override
  public boolean equals(Object otherDoctor) {
    if (!(otherDoctor instanceof Doctor)) {
      return false;
    } else {
      Doctor newDoctor = (Doctor) otherDoctor;
      return this.getName().equals(newDoctor.getName()) &&
             this.getSpecialty().equals(newDoctor.getSpecialty()) &&
             this.getId() == newDoctor.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO doctors (name, specialty) VALUES (:name, :specialty)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("specialty", this.specialty)
      .executeUpdate()
      .getKey();
    }
  }

  public String getName() {
    return name;
  }

  public String getSpecialty() {
    return specialty;
  }

  public static List<Doctor> all() {
    String sql = "SELECT id, name, specialty FROM doctors";
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
}
