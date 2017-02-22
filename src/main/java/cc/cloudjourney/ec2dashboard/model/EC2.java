package cc.cloudjourney.ec2dashboard.model;

import java.util.List;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Tag;

public class EC2 {
	private String name;
	private String instanceID;
	private Role instanceProfile; //one role for while.
	
	public EC2(String name, String instanceID, Role profile) {
		this.name = name;
		this.instanceID = instanceID;
		this.instanceProfile = profile;
	}
	
	public String getName() {
		return name;
	}
	public String getInstanceID() {
		return instanceID;
	}
	public Role getInstanceProfile() {
		return instanceProfile;
	}
	
	public static String getInstanceName(Instance i) {
		List<Tag> tags = i.getTags();
		for(Tag t : tags) {
			if (t.getKey().equalsIgnoreCase("name")) {
				return t.getValue();
			}
		}
		return "";
	}
}
