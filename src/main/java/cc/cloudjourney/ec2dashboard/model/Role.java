package cc.cloudjourney.ec2dashboard.model;

import com.amazonaws.services.ec2.model.IamInstanceProfile;

public class Role {
	private String id;
	private String name;
	private String arn;
	
	public Role(IamInstanceProfile profile) {
		this(profile.getId(), profile.getArn(), null);
	}
	
	public Role(String id, String arn, String name) {
		this.id = id;
		this.name = name;
		if (arn != null) this.arn = arn.substring(arn.indexOf("/")+1);
			
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getArn() {
		return arn;
	}
}
