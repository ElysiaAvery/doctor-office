import java.util.Arrays;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DoctorTest {

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
    public void doctor_instantiatesCorrectly_true() {
      Doctor testDoctor = new Doctor("Ferdinand", 1);
      assertTrue(testDoctor instanceof Doctor);
    }

    @Test
    public void getName_doctorInstantiatesWithName_Ferdinand() {
      Doctor testDoctor = new Doctor("Ferdinand", 1);
      assertEquals("Ferdinand", testDoctor.getName());
    }

    @Test
    public void all_returnsAllInstancesOfDoctor_true() {
      Doctor firstDoctor = new Doctor("Ferdinand", 1);
      firstDoctor.save();
      Doctor secondDoctor = new Doctor("George", 2);
      secondDoctor.save();
      assertTrue(Doctor.all().get(0).equals(firstDoctor));
      assertTrue(Doctor.all().get(1).equals(secondDoctor));
    }

    @Test
    public void getId_doctorsInstantiateWithAnId_1() {
      Doctor testDoctor = new Doctor("Ferdinand", 1);
      testDoctor.save();
      assertTrue(testDoctor.getId() > 0);
    }

    @Test
    public void find_returnsDoctorWithSameId_secondDoctor() {
      Doctor firstDoctor = new Doctor("Ferdinand", 1);
      firstDoctor.save();
      Doctor secondDoctor = new Doctor("George", 2);
      secondDoctor.save();
      assertEquals(Doctor.find(secondDoctor.getId()), secondDoctor);
    }

    @Test
    public void getPatients_initiallyReturnsEmptyList_ArrayList() {
      Doctor testDoctor = new Doctor("Ferdinand", 1);
      assertEquals(0, testDoctor.getPatients().size());
    }

    @Test
    public void equals_returnsTrueIfNamesAreTheSame() {
      Doctor firstDoctor = new Doctor("Ferdinand", 1);
      Doctor secondDoctor = new Doctor("Ferdinand", 1);
      assertTrue(firstDoctor.equals(secondDoctor));
    }

    @Test
    public void save_savesIntoDatabase_true() {
      Doctor myDoctor = new Doctor("Ferdinand", 1);
      myDoctor.save();
      assertTrue(Doctor.all().get(0).equals(myDoctor));
    }

    @Test
    public void save_assignsIdToObject() {
      Doctor myDoctor = new Doctor("Ferdinand", 1);
      myDoctor.save();
      Doctor savedDoctor = Doctor.all().get(0);
      assertEquals(myDoctor.getId(), savedDoctor.getId());
    }

    @Test
    public void getPatients_retrievesAllPatientsFromDatabase_patientsList() {
      Doctor myDoctor = new Doctor("Ferdinand", 1);
      myDoctor.save();
      Patient firstPatient = new Patient ("Bob", "1989-11-11", myDoctor.getId());
      firstPatient.save();
      Patient secondPatient = new Patient("Michael", "1980-10-10", myDoctor.getId());
      secondPatient.save();
      Patient[] patients = new Patient[] { firstPatient, secondPatient };
      assertTrue(myDoctor.getPatients().containsAll(Arrays.asList(patients)));
    }
}
