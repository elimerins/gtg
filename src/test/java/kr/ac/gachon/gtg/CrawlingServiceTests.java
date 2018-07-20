package kr.ac.gachon.gtg;

import kr.ac.gachon.gtg.persistence.CourseRepository;
import kr.ac.gachon.gtg.persistence.GeneralEducationRepository;
import kr.ac.gachon.gtg.persistence.MajorRepository;
import kr.ac.gachon.gtg.service.CrawlingService;
import kr.ac.gachon.gtg.service.CrawlingServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlingServiceTests {

    @Autowired
    CourseRepository corRepo;

    @Autowired
    GeneralEducationRepository geRepo;

    @Autowired
    MajorRepository mjRepo;

    @Test
    public void insertGeneralEduCodesTest() {
        CrawlingService crawling = new CrawlingServiceImpl(corRepo, geRepo, mjRepo);
        crawling.insertGeneralEducationCodes();
    }

    @Test
    public void insertMajorCodesTest() {
        CrawlingService crawling = new CrawlingServiceImpl(corRepo, geRepo, mjRepo);
        crawling.insertMajorCodes();
    }

    @Test
    public void insertMajorCoursesTest() {
        CrawlingService crawling = new CrawlingServiceImpl(corRepo, geRepo, mjRepo);
        ((CrawlingServiceImpl) crawling).insertMajorCourses(2018, CrawlingService.FALL_SEMESTER);
    }

    @Test
    public void insertGeneralEduCoursesTest() {
        CrawlingService crawling = new CrawlingServiceImpl(corRepo, geRepo, mjRepo);
        ((CrawlingServiceImpl) crawling).insertGeneralEduCourses(2018, CrawlingService.FALL_SEMESTER);
    }

    @Test
    public void insertCoursesTest() {
        CrawlingService crawling = new CrawlingServiceImpl(corRepo, geRepo, mjRepo);
        crawling.insertCourses(2018, CrawlingService.FALL_SEMESTER);
    }
}
