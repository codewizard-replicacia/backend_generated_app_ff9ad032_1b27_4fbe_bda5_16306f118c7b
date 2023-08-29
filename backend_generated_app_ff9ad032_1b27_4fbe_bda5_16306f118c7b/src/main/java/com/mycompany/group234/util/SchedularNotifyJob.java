package com.mycompany.group234.util;

import java.util.List;
import java.util.Optional;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.mycompany.group234.model.SchedularInfo;
import com.mycompany.group234.model.VaccineScheduler;
import com.mycompany.group234.model.VisitScheduler;
import com.mycompany.group234.repository.SchedularInfoRepository;
import com.mycompany.group234.repository.VaccineSchedulerRepository;
import com.mycompany.group234.repository.VisitSchedulerRepository;

public class SchedularNotifyJob implements Job {

	private final Logger log = LoggerFactory.getLogger(SchedularNotifyJob.class);

	@Autowired
	VisitSchedulerRepository visitSchedulerRepository;
	
	@Autowired
	VaccineSchedulerRepository vaccineSchedulerRepository;

	@Autowired
	SchedularInfoRepository infoRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		NotificationClient client = new NotificationClient();
		List<SchedularInfo> list = getSchedularInfoList();
		
		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(v -> {
				try {
					Optional<String> message = Optional.empty();
					Optional<String> phone = Optional.empty();

					if (v.getType().equalsIgnoreCase(SchedularInfoType.VISIT.toString())) {
						VisitScheduler visitScheduler = visitSchedulerRepository.getByVisitId(v.getTypeId());
						message = Optional.of("You have visit schedule today.");
						phone = Optional.ofNullable(visitScheduler.getAlertPhonenum().toString());
					}
					if (v.getType().equalsIgnoreCase(SchedularInfoType.VACCINE.toString())) {
						VaccineScheduler vaccineScheduler = vaccineSchedulerRepository.getByVaccineId(v.getTypeId());
						message = Optional.of("You have vaccine schedule today.");
						phone = Optional.ofNullable(vaccineScheduler.getAlertPhonenum().toString());
					}
					if (phone.isPresent()) {
						Optional<String> result = Optional.ofNullable(client.sendSms(phone.get(), message.get()));
						if (result.isPresent()) {
							if (result.get().equalsIgnoreCase("success")) {
								updateSchedularInfo(v.getId());
							} else {
								log.error("Unable to Send Message To: " + phone.get());
							}
						}
					}

				} catch (Exception e) {
					log.error("Error in Job: ", e);
				}

			});
		}
		log.info("Job Completed!!!!!");
	}

	private void updateSchedularInfo(int id) {
		SchedularInfo schedularInfo = infoRepository.getById(id);
		schedularInfo.setNextNotifiedDate(
				DateUtils.getNextDateFromDate(schedularInfo.getNextNotifiedDate(), schedularInfo.getRecurranceType()));
		schedularInfo.setRemainingRecurrance(schedularInfo.getRemainingRecurrance() - 1);
		infoRepository.save(schedularInfo);
	}

	private List<SchedularInfo> getSchedularInfoList() {
		return infoRepository.findByDate();
	}
	
	
}
