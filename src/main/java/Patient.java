import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;
import java.util.Date;
// import java.sql.Date;

public class Patient {
  private String name;
  private String birthdate;
  private int id;
  private int doctorId;

  public Patient(String name, String birthdate, int doctorId) {
    this.name = name;
    this.birthdate = birthdate;
    this.doctorId = doctorId;
  }

  @Override
  public boolean equals(Object otherPatient) {
    if(!(otherPatient instanceof Patient)) {
      return false;
    } else {
      Patient newPatient = (Patient) otherPatient;
      return this.getName().equals(newPatient.getName()) &&
             this.getBirthdate().equals(newPatient.getBirthdate()) &&
             this.getId() == newPatient.getId() &&
             this.getDoctorId() == newPatient.getDoctorId();
    }
  }

  public static List<Patient> all() {
    String sql = "SELECT id, name, birthdate, doctorId FROM patients";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Patient.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patients (name, birthdate, doctorId) VALUES (:name, :birthdate, :doctorId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("birthdate", this.birthdate)
        .addParameter("doctorId", this.doctorId)
        .executeUpdate()
        .getKey();
    }
  }

  public String getName() {
    return name;
  }

  public String getBirthdate() {
    return birthdate;
  }

  public int getId() {
    return id;
  }

  public int getDoctorId() {
    return doctorId;
  }

  public static Patient find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients where id=:id";
      Patient patient = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Patient.class);
    return patient;
    }
  }
}
