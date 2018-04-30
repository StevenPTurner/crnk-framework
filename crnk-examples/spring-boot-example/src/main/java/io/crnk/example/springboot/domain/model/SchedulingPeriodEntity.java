package io.crnk.example.springboot.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.SerializeType;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "SchedulingPeriod")
@Table(name = "scheduling_period")
@JsonApiResource(type = "schedulingPeriod")
public class SchedulingPeriodEntity implements Serializable{

	@Id
	@JsonApiId
	private Long id;
//	private Long campaignDeid;

	private String name;

	private String description;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(
			name = "campaign_deid", referencedColumnName = "deid")
	@JsonApiRelation(serialize = SerializeType.LAZY)
	@JsonBackReference
	private CampaignEntity campaign;

//	public Long getCampaignDeid() {
//		return campaignDeid;
//	}

//	public void setCampaignDeid(Long campaignDeid) {
//		this.campaignDeid = campaignDeid;
//	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SchedulingPeriodEntity )) return false;
		return id != null && id.equals(((SchedulingPeriodEntity) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	public CampaignEntity getCampaign() {
		return campaign;
	}

	public void setCampaign(CampaignEntity campaign) {
		this.campaign = campaign;
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
