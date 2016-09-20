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
      model.put("doctors", Doctor.all());
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
      Map<String, Object> model = HashMap<>();
      String name = request.queryParams("name");
      String specialty = request.queryParams("specialty");
      Doctor newDoctor = new Doctor(name, specialty);
      newDoctor.save();
      model.put("template", "templates/doctors.vtl");
      return new ModelAndView(model, layout);
    },new VelocityTemplateEngine());

    get("/doctors/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.params(":id")));
      model.put("doctor", doctor);
      model.put("template", "templates/doctor.vtl");
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
      model.put("doctor", doctor);
      model.put("template", "templates/")
    }, new VelocityTemplateEngine());
  }
}
