package me.zacharilius;

import static me.zacharilius.service.OfyService.ofy;
import static org.junit.Assert.*;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import me.zacharilius.domain.Profile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Tests for Conference POJO.
 */
public class ConferenceTest {

    private static final long ID = 123456L;

    private static final String NAME = "GCP Live";

    private static final String DESCRIPTION = "New announcements for Google Cloud Platform";

    private static final String ORGANIZER_USER_ID = "123456789";

    private static final String CITY = "San Francisco";

    private static final int MONTH = 3;

    private static final int CAP = 500;

    private Date startDate;

    private Date endDate;

    private List<String> topics;


    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        startDate = dateFormat.parse("03/25/2014");
        endDate = dateFormat.parse("03/26/2014");
        topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }



   

 
}
