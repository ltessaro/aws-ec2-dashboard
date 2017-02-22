package cc.cloudjourney.ec2dashboard.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.ListRolesResult;

import cc.cloudjourney.ec2dashboard.model.Role;

public class RoleApi {
	private static final Map<String, Role> iamRoles = new HashMap<String, Role>();

	public static List<Role> list(String q) {
		iamRoles.clear();
		ListRolesResult rolesResults = AmazonIdentityManagementClientBuilder.defaultClient().listRoles();
		List<com.amazonaws.services.identitymanagement.model.Role> roles = rolesResults.getRoles();
		for (com.amazonaws.services.identitymanagement.model.Role rl : roles) {
			String arn = rl.getArn();
			String name = rl.getRoleName();
			String id = rl.getRoleId();
			if (rl.getAssumeRolePolicyDocument().contains("ec2.amazonaws.com")) {
				iamRoles.put(rl.getArn(), new Role(id, arn, name));
			}
		}
		
		Collection<Role> resultRoles = iamRoles.values();

		if (q != null && q.length() > 2) {
			List<Role> result = new ArrayList<Role>();
			for (Role tmp : resultRoles) { 
				if (tmp.getName().contains(q)) {
					result.add(tmp);
				}
			}
			return result;
		}
		return new ArrayList<Role>(resultRoles);
	}
}
