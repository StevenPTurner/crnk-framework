package io.crnk.example.springboot.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Campaign")
@Table(name = "campaign")
@JsonApiResource(type = "campaign")
public class CampaignEntity implements Serializable{

	@Id
	@JsonApiId
	private Long id;

	private Long deid;

	private String name;

	private String description;
	@OneToMany(
			mappedBy = "campaign",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JsonApiRelation
	@JsonManagedReference
	private List<SchedulingPeriodEntity> schedulingPeriodEntities = new ArrayList<>();

	public Long getDeid() {
		return deid;
	}

	public void setDeid(Long deid) {
		this.deid = deid;
	}

	public void addSchedulingPeriod(SchedulingPeriodEntity schedulingPeriodEntity) {
		schedulingPeriodEntities.add(schedulingPeriodEntity);
		schedulingPeriodEntity.setCampaign(this);
	}

	public void removeSchedulingPeriodEntity (SchedulingPeriodEntity schedulingPeriodEntity) {
		schedulingPeriodEntities.remove(schedulingPeriodEntity);
		schedulingPeriodEntity.setCampaign(null);
	}

	public List<SchedulingPeriodEntity> getSchedulingPeriods() {
		return schedulingPeriodEntities;
	}

	public void setSchedulingPeriodEntities(List<SchedulingPeriodEntity> schedulingPeriodEntities) {
		this.schedulingPeriodEntities = schedulingPeriodEntities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
