package io.crnk.example.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.crnk.core.engine.transaction.TransactionRunner;
import io.crnk.example.springboot.domain.model.*;
import io.crnk.example.springboot.domain.repository.ProjectRepository;
import io.crnk.example.springboot.domain.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Configuration
public class TestDataLoader {
	private Long schedulingPeriodId;
	private final Long CAMPAIGN_ID = 0L;
	private final Long CAMPAIGN_DEID = 10L;

	@Autowired
	private EntityManager em;

	@Autowired
	private TransactionRunner transactionRunner;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskRepository taskRepository;

	@PostConstruct
	public void setup() {
		schedulingPeriodId = 0L;
		List<String> interests = new ArrayList<>();
		interests.add("coding");
		interests.add("art");

		Project project121 = projectRepository.save(new Project(121L, "Great Project"));
		Project project122 = projectRepository.save(new Project(122L, "Crnk Project"));
		Project project123 = projectRepository.save(new Project(123L, "Some Project"));
		projectRepository.save(new Project(124L, "JSON API Project"));

		Task task = new Task(1L, "Create tasks");
		task.setProject(project121);
		taskRepository.save(task);
		task = new Task(2L, "Make coffee");
		task.setProject(project122);
		taskRepository.save(task);
		task = new Task(3L, "Do things");
		task.setProject(project123);
		taskRepository.save(task);

		transactionRunner.doInTransaction(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				SchedulingPeriodEntity sp1 = createSchedulingPeriodEntity(getNewID(), CAMPAIGN_DEID);
				SchedulingPeriodEntity sp2 = createSchedulingPeriodEntity(getNewID(), CAMPAIGN_DEID);
				SchedulingPeriodEntity sp3 = createSchedulingPeriodEntity(getNewID(), CAMPAIGN_DEID);

				em.persist(sp1);
				em.persist(sp2);
				em.persist(sp3);

				CampaignEntity campaign = createCampaignEntity(CAMPAIGN_ID, CAMPAIGN_DEID);
				campaign.addSchedulingPeriod(sp1);
				campaign.addSchedulingPeriod(sp2);
				campaign.addSchedulingPeriod(sp3);

				em.persist(campaign);

				em.flush();
				return null;
			}
		});
	}

	@PostConstruct
	public void configureJackson() {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	private CampaignEntity createCampaignEntity(long id, Long deid) {
		final String CAMPAIGN_NAME = "Campaign: " + id;
		final String CAMPAIGN_DESCRIPTION = "This is campaign Number: " + id;

		CampaignEntity campaignEntity = new CampaignEntity();
		campaignEntity.setId(id);
		campaignEntity.setDeid(deid);
		campaignEntity.setName(CAMPAIGN_NAME);
		campaignEntity.setDescription(CAMPAIGN_DESCRIPTION);
		return campaignEntity;
	}

	private SchedulingPeriodEntity createSchedulingPeriodEntity(Long number, Long campaignID) {
		final String SCHEDULING_PERIOD_NAME = "Scheduling Period: " + number;
		final String SCHEDULING_PERIOD_DESCRIPTION = "This is Scheduling Period ; " + number;

		SchedulingPeriodEntity schedulingPeriodEntity = new SchedulingPeriodEntity();
		schedulingPeriodEntity.setId(number);
//		schedulingPeriodEntity.setCampaignDeid(campaignID);
		schedulingPeriodEntity.setName(SCHEDULING_PERIOD_NAME);
		schedulingPeriodEntity.setDescription(SCHEDULING_PERIOD_DESCRIPTION);
		return schedulingPeriodEntity;
	}

	private Long getNewID() {
		Long toReturn = schedulingPeriodId;
		schedulingPeriodId++;
		return toReturn;
	}

}
