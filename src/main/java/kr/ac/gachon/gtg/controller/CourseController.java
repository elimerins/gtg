package kr.ac.gachon.gtg.controller;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.CourseId;
import kr.ac.gachon.gtg.service.CourseService;
import kr.ac.gachon.gtg.service.GeneralEducationService;
import kr.ac.gachon.gtg.service.MajorService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/course/")
@Log
public class CourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    MajorService majorService;

    @Autowired
    GeneralEducationService generalEducationService;

    private String pageTitle;
    private String breadcrumb;

    @RequestMapping("/test")
    public void test(HttpServletRequest request, Model model) {
        log.info("METHOD: " + request.getMethod());
    }

    @GetMapping("/{year}/{semester}/{code}")
    public ResponseEntity<Course> getCourseInfo(
            @PathVariable("year") Integer year,
            @PathVariable("semester") Integer semester,
            @PathVariable("code") String code) {
        CourseId id = new CourseId();
        id.setYear(year);
        id.setSemester(semester);
        id.setCode(code);

        Course course = courseService.findCourseById(id);

        if (course.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(course, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public void search(HttpServletRequest request,
                       @RequestParam Map<String, String> body,
                       Model model) {
        /*
         * common code for search
         */
        pageTitle = "강의 조회";
        breadcrumb = "Search";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);
        // For major select form
        model.addAttribute("majorList", majorService.findAll());
        // For general education select form
        model.addAttribute("generalList", generalEducationService.findAll());
        /*
         * end common code for search
         */

        if (request.getMethod().equals("POST")) {
            // Parsing request body
            log.info("Request Body: " + body.toString());
            body.forEach(model::addAttribute);

            // search courses using year, semester, grade, major, general, title
            Integer year = Integer.valueOf(body.get("year"));
            Integer semester = Integer.valueOf(body.get("semester"));
            String grade = body.get("grade");
            String major = body.get("major");
            String general = body.get("general");
            String title = body.get("title");

            if (title == null || title.isEmpty()) {
                // fetch & pass the course list
                if (grade.equals("0")) {
                    String code = major.length() > 0 ? major : general;

                    model.addAttribute("list",
                            courseService.findCourses(year, semester, code));
                } else {
                    model.addAttribute("list",
                            courseService.findCourses(year, semester, grade, major));
                }
            } else {
                // search courses using title
                if (title.length() > 2) {
                    model.addAttribute("list",
                            courseService.findCoursesByTitle(year, semester, title));
                } else {
                    model.addAttribute("list", null);
                }
            }

            // For syllabus link
            String baseUrl = "http://203.249.126.126:9090";
            model.addAttribute("baseUrl", baseUrl);
        }
    }


    @GetMapping("/create")
    public void create(Model model) {
        pageTitle = "시간표 짜기";
        breadcrumb = "Create";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);
        // For major select form
        model.addAttribute("majorList", majorService.findAll());
    }
}
