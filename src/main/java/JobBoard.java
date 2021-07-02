import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class JobBoard {

  public static void main(String[] args) throws IOException {

    ClassLoader classLoader = JobBoard.class.getClassLoader();
    File file = new File(classLoader.getResource("positions.json").getFile());

    List<HashMap<String, String>> positions;
    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    positions = mapper.readValue(file, ArrayList.class);

    System.out.println(mapper.writeValueAsString(positions));

    Map<String, Integer> numJobsLocation = new HashMap<>();
    Map<String, Integer> numJobsCompany = new HashMap<>();

    String location;
    int numJobs;
    for(HashMap<String, String> position : positions) {
      location = position.get("location");
      if(numJobsLocation.containsKey(location)) {
        numJobs = numJobsLocation.get(location) + 1;
        numJobsLocation.replace(location, numJobs);
      } else {
        numJobs = 1;
        numJobsLocation.put(location, numJobs);
      }
    }

    String company;
    for(HashMap<String, String> position : positions) {
       company = position.get("company");
       if(numJobsCompany.containsKey(company)) {
         numJobs = numJobsCompany.get(company) + 1;
         numJobsCompany.replace(company, numJobs);
       } else {
          numJobs = 1;
          numJobsCompany.put(company, numJobs);
       }
    }

    Set<String> locations = numJobsLocation.keySet();
    System.out.println("\n-------\nNumber of Jobs by Location:");
    for(String jobLocation : locations) {
      System.out.println(jobLocation + ": " + numJobsLocation.get(jobLocation));
    }

    Set<String> companies = numJobsCompany.keySet();
    System.out.println("\n-------\nNumber of Jobs by Company:");
    for(String jobCompany : companies) {
      System.out.println(jobCompany + ": " + numJobsCompany.get(jobCompany));
    }

    Scanner inputScanner = new Scanner(System.in);
    System.out.println("Would you like to submit a job? y/n:");
    String submitJob = inputScanner.nextLine();
    if(submitJob.equals("y")) {
      System.out.println("Enter the following information for the new job:");
      System.out.println("Type:");
      String type = inputScanner.nextLine();
      System.out.println("URL:");
      String url = inputScanner.nextLine();
      System.out.println("Title:");
      String title = inputScanner.nextLine();
      System.out.println("Description:");
      String description = inputScanner.nextLine();
      Map<String, String> newJob = new HashMap<>();
      newJob.put("type", type);
      newJob.put("url", url);
      newJob.put("title", title);
      newJob.put("description", description);

      ObjectWriter objectWriter = mapper.writer(new DefaultPrettyPrinter());
      objectWriter.writeValue(new File("job.json"), newJob);
    }
  }
}
