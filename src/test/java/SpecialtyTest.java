import java.util.Arrays;
import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SpecialtyTest {

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
      Specialty testSpecialty = new Specialty("Cardiology");
      assertTrue(testSpecialty instanceof Specialty);
    }

    @Test
    public void getName_specialtyInstantiatesWithName_Cardiology() {
      Specialty testSpecialty = new Specialty("Cardiology");
      assertEquals("Cardiology", testSpecialty.getName());
    }

    @Test
    public void all_returnsAllInstancesOfSpecialty_true() {
      Specialty firstSpecialty = new Specialty("Cardiology");
      firstSpecialty.save();
      Specialty secondSpecialty = new Specialty("Oncology");
      secondSpecialty.save();
      assertTrue(Specialty.all().get(0).equals(firstSpecialty));
      assertTrue(Specialty.all().get(1).equals(secondSpecialty));
    }

    @Test
    public void getId_doctorsInstantiateWithAnId_1() {
      Specialty testSpecialty = new Specialty("Cardiology");
      testSpecialty.save();
      assertTrue(testSpecialty.getId() > 0);
    }

    @Test
    public void find_returnsSpecialtyWithSameId_secondSpecialty() {
      Specialty firstSpecialty = new Specialty("Cardiology");
      firstSpecialty.save();
      Specialty secondSpecialty = new Specialty("Oncology");
      secondSpecialty.save();
      assertEquals(Specialty.find(secondSpecialty.getId()), secondSpecialty);
    }

    @Test
    public void getDoctors_initiallyReturnsEmptyList_ArrayList() {
      Specialty testSpecialty = new Specialty("Cardiology");
      assertEquals(0, testSpecialty.getDoctors().size());
    }

    @Test
    public void equals_returnsTrueIfNamesAreTheSame() {
      Specialty firstSpecialty = new Specialty("Cardiology");
      Specialty secondSpecialty = new Specialty("Cardiology");
      assertTrue(firstSpecialty.equals(secondSpecialty));
    }

    @Test
    public void save_savesIntoDatabase_true() {
      Specialty mySpecialty = new Specialty("Cardiology");
      mySpecialty.save();
      assertTrue(Specialty.all().get(0).equals(mySpecialty));
    }

    @Test
    public void save_assignsIdToObject() {
      Specialty mySpecialty = new Specialty("Cardiology");
      mySpecialty.save();
      Specialty savedSpecialty = Specialty.all().get(0);
      assertEquals(mySpecialty.getId(), savedSpecialty.getId());
    }

    @Test
    public void getDoctors_retrievesAllDoctorsFromDatabase_doctorsList() {
      Specialty mySpecialty = new Specialty("Cardiology");
      mySpecialty.save();
      Doctor firstDoctor = new Doctor ("Ferdinand", mySpecialty.getId());
      firstDoctor.save();
      Doctor secondDoctor = new Doctor("Miguel", mySpecialty.getId());
      secondDoctor.save();
      Doctor[] doctors = new Doctor[] { firstDoctor, secondDoctor };
      assertTrue(mySpecialty.getDoctors().containsAll(Arrays.asList(doctors)));
    }
}
