import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;

public class PatientTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctor_office_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deletePatientsQuery = "DELETE FROM patients *;";
      String deleteDoctorsQuery = "DELETE FROM doctors *;";
      String deleteSpecialtiesQuery = "DELETE FROM specialties *;";
      con.createQuery(deletePatientsQuery).executeUpdate();
      con.createQuery(deleteDoctorsQuery).executeUpdate();
      con.createQuery(deleteSpecialtiesQuery).executeUpdate();
    }
  }

  @Test
  public void Patient_instantiatesCorrectly_true() {
    Patient myPatient = new Patient("Kim", "1987-10-10", 1);
    assertTrue(myPatient instanceof Patient);
  }

  @Test
  public void Patient_instantiatesWithName_String() {
    Patient myPatient = new Patient("Kim", "1987-10-10", 1);
    assertEquals("Kim", myPatient.getName());
  }

  @Test
  public void Patient_instantiatesWithBirthDate_1987() {
    Patient myPatient = new Patient("Kim", "1987-10-10", 1);
    assertEquals("1987-10-10", myPatient.getBirthdate());
  }

  @Test
  public void all_returnsAllInstancesOfPatient_true() {
    Patient firstPatient = new Patient("Kim", "1987-10-10", 1);
    firstPatient.save();
    Patient secondPatient = new Patient("Tim", "1980-07-07", 2);
    secondPatient.save();
    assertTrue(Patient.all().get(0).equals(firstPatient));
    assertTrue(Patient.all().get(1).equals(secondPatient));
  }

  @Test
  public void getId_tasksInstantiateWithAnId() {
    Patient myPatient = new Patient("Kim", "1987-10-10", 1);
    myPatient.save();
    assertTrue(myPatient.getId() > 0);
  }

  @Test
  public void find_returnsPatientWithSameId_secondPatient() {
    Patient firstPatient = new Patient("Kim", "1987-10-10", 1);
    firstPatient.save();
    Patient secondPatient = new Patient("Tim", "1980-07-07", 2);
    secondPatient.save();
    assertEquals(Patient.find(secondPatient.getId()), secondPatient);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Patient firstPatient = new Patient("Kim", "1987-10-10", 1);
    Patient secondPatient = new Patient("Kim", "1987-10-10", 1);
    assertTrue(firstPatient.equals(secondPatient));
  }

  @Test
  public void save_returnsTrueIfNamesAreTheSame() {
    Patient myPatient = new Patient("Kim", "1987-10-10", 1);
    myPatient.save();
    assertTrue(Patient.all().get(0).equals(myPatient));
  }

  @Test
  public void save_assignsIdToObject() {
    Patient myPatient = new Patient("Kim", "1987-10-10", 1);
    myPatient.save();
    Patient savedPatient = Patient.all().get(0);
    assertEquals(myPatient.getId(), savedPatient.getId());
  }

  @Test
  public void save_savesDoctorIdIntoDB_true() {
    Doctor myDoctor = new Doctor("Ferdinand", 1);
    myDoctor.save();
    Patient myPatient = new Patient("Kim", "1987-10-10", myDoctor.getId());
    myPatient.save();
    Patient savedPatient = Patient.find(myPatient.getId());
    assertEquals(savedPatient.getDoctorId(), myDoctor.getId());
  }

}
