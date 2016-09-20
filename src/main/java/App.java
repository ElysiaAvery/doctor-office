import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import java.util.ArrayList;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("specialties", Specialty.all());
      model.put("doctors", Doctor.all());
      model.put("patients", Patient.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("doctors", Doctor.all());
      model.put("template", "templates/doctors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String name = request.queryParams("name");
      Specialty specialty = Specialty.find(Integer.parseInt(request.queryParams("specialtyId")));
      Doctor newDoctor = new Doctor(name, specialty.getId());
      newDoctor.save();
      model.put("template", "templates/doctors.vtl");
      return new ModelAndView(model, layout);
    },new VelocityTemplateEngine());

    get("/doctors/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.params(":id")));
      Patient patient = Patient.find(Integer.parseInt(request.params(":id")));
      Specialty specialty = Specialty.find(Integer.parseInt(request.params(":id")));
      model.put("patient", patient);
      model.put("doctor", doctor);
      model.put("specialty", specialty);
      model.put("template", "templates/doctor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patients", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("patients", Patient.all());
      model.put("template", "templates/patients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patients", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.queryParams("doctorId")));
      String name = request.queryParams("patient-name");
      String birthdate = request.queryParams("birthdate");
      Patient newPatient = new Patient(name, birthdate, doctor.getId());
      newPatient.save();
      model.put("patient", newPatient);
      model.put("doctor", doctor);
      model.put("template", "templates/patients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patients/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      Patient patient = Patient.find(Integer.parseInt(request.params(":id")));
      model.put("patient", patient);
      model.put("template", "templates/patient.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/specialties", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("specialties", Specialty.all());
      model.put("template", "templates/specialties.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/specialties", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String name = request.queryParams("specialty");
      Specialty newSpecialty = new Specialty(name);
      newSpecialty.save();
      model.put("specialty", newSpecialty);
      model.put("template", "templates/specialties.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/specialties/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      Specialty specialty = Specialty.find(Integer.parseInt(request.params(":id")));
      Doctor doctor = Doctor.find(Integer.parseInt(request.params(":id")));
      model.put("specialty", specialty);
      model.put("doctor", doctor);
      model.put("template", "templates/specialty.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
