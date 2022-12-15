package com.fif.app.Bfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fif.app.Ahttpmethod.Person;

@Controller
public class FileController {
    public static List<Person> personList = new ArrayList<Person>();
    static String path = "/Users/fif/Desktop/ODOC/SteveleeStudy/study-workspace/FIFStudy/src/main/java/com/fif/app/Bfile/person.txt";

    @RequestMapping(value = "fileHandling", method = RequestMethod.GET)
    public String main() {
        System.out.println("파일 컨트롤러 main()");
        return "Bfile/main";
    }

    @RequestMapping(value = "savePerson", method = RequestMethod.POST)
    public List<Person> savePerson(@RequestBody Person person) throws Exception {
        System.out.println("savePerson()");

        System.out.println("id: " + person.getId());
        System.out.println("name: " + person.getName());
        System.out.println("age: " + person.getAge());

        FileController fc = new FileController();
        fc.writeFile(path, person.getId(), person.getName(), person.getAge());
        List<Person> rtnPerson = fc.readFile(path, person);

        return rtnPerson;
    }

    @RequestMapping(value = "showPersonList", method = RequestMethod.GET)
    public String showPersonList(Person person, Model model) throws Exception {
        System.out.println("showPersonList()");

        FileController fc = new FileController();
        List<Person> list = fc.savePerson(person);
        model.addAttribute("personList", list);

        return "Bfile/personList";
    }

    public void writeFile(String path, String id, String name, String age) throws Exception {

        File file = new File(path);
        FileWriter fw = null;

        try {
            fw = new FileWriter(file, false);
            fw.write(id + "," + name + "," + age);
            fw.flush();
            System.out.println("***File Write END***");
        } catch (IOException e) {
            if (fw != null) {
                try {
                    fw.close();
                } catch (Exception ex) {
                    throw ex;
                }
            }
            e.printStackTrace();
        }
    } // end writeFile()

    public List<Person> readFile(String path, Person person) throws IOException {
        List<Person> rtn = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line = null;

            while ((line = br.readLine()) != null) {
                System.out.println("Read File...");
                System.out.println(line);

                String[] personInfo;
                personInfo = line.split(",");

                String id = personInfo[0];
                String name = personInfo[1];
                String age = personInfo[2];

                person.setId(id);
                person.setName(name);
                person.setAge(age);

                personList.add(person);
            }

            rtn = personList;
        } catch (Exception e) {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ex) {
                }
            }
        }
        return rtn;
    }
}
